package bookit;

import static java.lang.System.out;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

@ManagedBean
@ViewScoped
public class ScheduleView implements Serializable {

	private Util util = new Util();

	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;
	// private PreparedStatement ps = null;

	private int id_booking = 0;
	private Date time_to = new Date(0L);
	private Date time_from = new Date(0L);
	private String comment;
	private String companyName = "";
	private String customerName;

	private TimeConvert convert = new TimeConvert();

	final String SQL_SELECT = "SELECT b.ID_Booking booking, b.Time_From timefrom, "
			+ "b.Time_To timeto, b.Comment comments, c.Company_Name compname, "
			+ "cus.Customer_Lastname custname FROM booking b join company c on "
			+ "b.ID_Company=c.ID_Company join customer cus on b.ID_Customer=cus.ID_Customer";

	private ScheduleModel eventModel;
	private ScheduleEvent event = new DefaultScheduleEvent();

	// getters and setters
	
	public ScheduleModel getEventModel() {
		return eventModel;
	}

	public ScheduleEvent getEvent() {
		return event;
	}

	public void setEvent(ScheduleEvent event) {
		this.event = event;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public java.util.Date getTime_from() {

		return time_from;
	}

	public void setTime_from(java.util.Date dt) {
		if (dt != null)
			time_from = new Date(dt.getTime());
		else
			time_from = new Date(0L);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public java.util.Date getTime_to() {
		return time_to;
	}

	public void setTime_to(java.util.Date dt) {
		if (dt != null)
			time_to = new Date(dt.getTime());
		else
			time_to = new Date(0L);
	}

	public int getId_booking() {
		return id_booking;
	}

	public void setId_booking(int id_booking) {
		this.id_booking = id_booking;
	}

	@PostConstruct
	public void init() {
		eventModel = new DefaultScheduleModel();
		connectToDb();
	}

	private void connectToDb() {
		System.out.println("load info from db");

		if (util != null)
			con = util.getCon();
		if (con != null) {
			try {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_SELECT);
				// if (rs.first())
				// showData();
				while (rs.next()) {
					displayAllAppointmentData();
				}

			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Exception", "Keine Verbindung zur Datenbank (Treiber nicht gefunden?)"));
			out.println("Keine Verbingung zur Datenbank");
		}
	}

	/*--------------------------------------------------------------------------*/
	/**
	 * Verbindung zur Datenbank herstellen und disconnect button und browse
	 * buttons freigeben
	 * 
	 * @param ae
	 * 
	 */
	public void connect(ActionEvent ae) {

		out.println("connect()...");

		if (util != null)
			con = util.getCon();
		if (con != null) {
			try {
				stm = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rs = stm.executeQuery(SQL_SELECT);
				if (rs.first())
					displayAllAppointmentData();

			} catch (Exception ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Exception", "Keine Verbindung zur Datenbank (Treiber nicht gefunden?)"));
			out.println("Keine Verbingung zur Datenbank");
		}
	}

	private void displayAllAppointmentData() throws SQLException {

		setId_booking(rs.getInt("booking"));
		setCompanyName(rs.getString("compname"));
		setCustomerName(rs.getString("custname"));
		setTime_to(rs.getTimestamp("timeto"));
		setComment(rs.getString("comments"));
		setTime_from(rs.getTimestamp("timefrom"));
		
		eventModel.addEvent(new DefaultScheduleEvent(comment, time_from, time_to));

	}
	
	public void insert(ActionEvent actionEvent) throws ParseException {

		if (event.getId() == null) {

			String sql_insert = "INSERT INTO booking (ID_Company, ID_Customer, Time_From, Time_To, Comment) VALUES (?, ?, ?, ?, ?)";

			try {

				String timeFrom = convert.convertTime(event.getStartDate().toString());
				String timeTo = convert.convertTime(event.getEndDate().toString());

				// convert java String to sql date
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				java.util.Date date = sdf.parse(timeFrom);
				java.sql.Timestamp sqlStartDate = new java.sql.Timestamp(date.getTime());

				java.util.Date date2 = sdf.parse(timeTo);
				java.sql.Timestamp sqlEndDate = new java.sql.Timestamp(date2.getTime());

				PreparedStatement ps = con.prepareStatement(sql_insert);

				ps.setInt(1, 1);
				ps.setInt(2, 100);
				ps.setTimestamp(3, sqlStartDate);
				ps.setTimestamp(4, sqlEndDate);
				ps.setString(5, event.getTitle());

				int n = ps.executeUpdate();
				if (n == 1) {
					out.println("O.K., Datensatz eingefügt.");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"O. K.", "Ein Datensatz erfolgreich eingefügt."));
				}

				ps.close();

				// rs = stm.executeQuery(sql_insert);

			} catch (SQLException ex) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "SQLException", ex.getLocalizedMessage()));
				out.println("Error: " + ex);
				ex.printStackTrace();
			}

			eventModel.addEvent(event);
		} else
			eventModel.updateEvent(event);

		event = new DefaultScheduleEvent();
	}

	public void onEventSelect(SelectEvent selectEvent) {
		event = (ScheduleEvent) selectEvent.getObject();
	}

	public void onDateSelect(SelectEvent selectEvent) {
		event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
	}

	public void onEventMove(ScheduleEntryMoveEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	public void onEventResize(ScheduleEntryResizeEvent event) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized",
				"Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());

		addMessage(message);
	}

	private void addMessage(FacesMessage message) {
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}