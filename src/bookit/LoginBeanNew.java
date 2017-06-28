package bookit;

import static java.lang.System.out;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "lbn")
@SessionScoped

public class LoginBeanNew {

	String sOutcome = null;

	private Util util = new Util();
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;

	private String username = "";
	private String password = "";
	private boolean userLoggedIn = false;
	private boolean adminLoggedIn = false;
	private String name = "";
	private String sqlUsername = "";
	private String sqlPassword = "";
	private boolean adminLogin = false;

	private String kennung = "";
	private String pw = "";
	private boolean loggedIn = false;

	/*--------------------------------------------------------------------------*/

	public LoginBeanNew() {
		System.out.println("Mb.<init>...");
		System.out.println((new Date()).toString());
	}

	/*--------------------------------------------------------------------------*/

	public boolean isAdminLogin() {
		return adminLogin;
	}

	public void setAdminLogin(boolean adminLogin) {
		this.adminLogin = adminLogin;
	}

	public String getSqlUsername() {
		return sqlUsername;
	}

	public void setSqlUsername(String sqlUsername) {
		this.sqlUsername = sqlUsername;
	}

	public String getSqlPassword() {
		return sqlPassword;
	}

	public void setSqlPassword(String sqlPassword) {
		this.sqlPassword = sqlPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public boolean isAdminLoggedIn() {
		return adminLoggedIn;
	}

	public void setKennung(String s) {
		kennung = s;
	}

	public String getKennung() {
		return kennung;
	}

	public void setPw(String s) {
		pw = s;
	}

	public String getPw() {
		return pw;
	}

	public Date getDate() {
		return new Date();
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	/*---------------------------------------------------------------------------------*/

	// define what happens when login button gets clicked

	public String actLoginNew() {

		System.out.println("actLogin()...");
		System.out.println("_______________________ admin login " + adminLogin);

		kennung = kennung.trim();
		pw = pw.trim();

		System.out.println("kennung: " + kennung);
		System.out.println("passwort: " + pw);

		String select;
		String selectUser = "SELECT Customer_Lastname, Customer_Firstname,"
				+ " Customer_Login, Customer_Passwort FROM customer WHERE customer_login = ?";

		String selectAdmin = "SELECT Company_Name, Company_Login, Company_Passwort FROM company "
				+ "WHERE Company_Login = ?";

		if (adminLogin) {
			select = selectAdmin;
		} else
			select = selectUser;

		if (util != null)
			con = util.getCon();
		if (con != null) {

			try {
				PreparedStatement ps = con.prepareStatement(select);
				ps.setString(1, kennung);
				// System.out.println("prepared statement: " + ps.toString());
				rs = ps.executeQuery();

				if (rs.first())
					showData(adminLogin);

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

		pw = util.cryptpw(null, pw);

		System.out.println("++++++++++++++++++++++++++++++++");
		System.out.println("Username: " + kennung);
		System.out.println("Password: " + pw);
		System.out.println("++++++++++++++++++++++++++++++++");
		System.out.println("sqlusername: " + sqlUsername + ", sql password: " + sqlPassword);
		System.out.println("++++++++++++++++++++++++++++++++");

		if (kennung.equalsIgnoreCase(sqlUsername) && pw.equals(sqlPassword)) {

			System.out.println("login successful");
			loggedIn = true;
		}

		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch (user/user oder admin/admin)"));

		System.out.println("USER EINGELOGGT: " + userLoggedIn);
		System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);

		if (userLoggedIn) {
			sOutcome = "user";
		}

		if (adminLoggedIn) {
			sOutcome = "admin";
		}

		System.out.println("angemeldet als: " + sOutcome);
		System.out.println("++++++++++ " + "logged as: " + name + ", pass: " + sqlPassword + ", name: " + sqlUsername);

		return sOutcome;
	}

	private void showData(boolean admin) throws SQLException {

		if (admin) {
			setName(rs.getString("company_name"));
			setSqlPassword(rs.getString("company_passwort"));
			setSqlUsername(rs.getString("company_login"));

			adminLoggedIn = true;
			userLoggedIn = false;

		} else {

			setName(rs.getString("customer_firstname") + " " + rs.getString("customer_lastname"));
			setSqlPassword(rs.getString("customer_passwort"));
			setSqlUsername(rs.getString("customer_login"));

			userLoggedIn = true;
			adminLoggedIn = false;

		}
	}

	public void aclLogout(ActionEvent ae) {
		loggedIn = false;
		adminLoggedIn = false;
		userLoggedIn = false;
	}
	
	public void languageDE(ActionEvent ae) {
		System.out.println("deutsch");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMAN);
	}

	public void languageEN(ActionEvent ae) {
		System.out.println("english");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
	}

}
