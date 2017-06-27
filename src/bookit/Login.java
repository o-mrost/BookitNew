package bookit;

import static java.lang.System.out;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.Locale;

@ManagedBean(name = "lb")
@RequestScoped
public class Login {

	private Util util = new Util();
	private Connection con = null;
	private Statement stm = null;
	private ResultSet rs = null;

	private String username = "";
	private String password = "";
	private boolean userLoggedIn = false;
	private boolean adminLoggedIn = false;
	private boolean anybodyLoggedIn = false;
	private String name = "";
	private String sqlUsername = "";
	private String sqlPassword = "";
	private boolean adminLogin = false;

	/*---------------------------------------------------------------------------------*/

	// constructor login
	public Login() {
		System.out.println("MyBean.<init>...");
		System.out.println("User logged in: " + userLoggedIn);
		System.out.println("Admin logged in: " + adminLoggedIn);
		System.out.println((new Date()).toString());
	}

	/*---------------------------------------------------------------------------------*/

	// getter and setters for input and output fields

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

	public boolean isAnybodyLoggedIn() {
		return anybodyLoggedIn;
	}

	public void setAnybodyLoggedIn(boolean anybodyLoggedIn) {
		this.anybodyLoggedIn = anybodyLoggedIn;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPw(String s) {
		password = s;
	}

	public String getPw() {
		return password;
	}

	public Date getDate() {
		return new Date();
	}

	public boolean isUserLoggedIn() {
		return userLoggedIn;
	}

	public boolean isAdminLoggedIn() {
		return adminLoggedIn;
	}

	/*---------------------------------------------------------------------------------*/

	// define what happens when login button gets clicked

	public String actLogin(ActionEvent ae) {

		String sOutcome = null;
		System.out.println("actLogin()...");
		System.out.println("admin login " + adminLogin);

		username = username.trim();
		password = password.trim();

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
				ps.setString(1, username);
				System.out.println("prepared statement: " + ps.toString());
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

		password = util.cryptpw(null, password);

		System.out.println("++++++++++++++++++++++++++++++++");
		System.out.println("Username: " + username);
		System.out.println("Password: " + password);
		System.out.println("++++++++++++++++++++++++++++++++");
		System.out.println("sqlusername: " + sqlUsername + ", sql password: " + sqlPassword);
		System.out.println("++++++++++++++++++++++++++++++++");

		if (adminLogin) {
			sOutcome = "admin";
			adminLoggedIn = true;
			userLoggedIn = false;
		} else {
			sOutcome = "user";
			userLoggedIn = true;
			adminLoggedIn = false;

		}
		adminLoggedIn = adminLogin;

		if (username.equalsIgnoreCase(sqlUsername) && password.equals(sqlPassword)) {

			System.out.println("login successful");

			System.out.println("#########    outcome: " + sOutcome);

			// sOutcome = "user";
			// userLoggedIn = true;
			// adminLoggedIn = false;
			// System.out.println("USER EINGELOGGT: " + userLoggedIn);
			// System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
			anybodyLoggedIn = true;
		}

		// else if (username.equalsIgnoreCase("admin") &&
		// password.equals("admin"))
		//
		// {
		// sOutcome = "admin";
		// adminLoggedIn = true;
		// userLoggedIn = false;
		// System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
		// System.out.println("USER EINGELOGGT: " + userLoggedIn);
		// anybodyLoggedIn = true;
		// }

		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch(user/user oder admin/admin)"));

		return sOutcome;
	}

	private void showData(boolean admin) throws SQLException {

		if (admin) {
			setName(rs.getString("company_name"));
			setSqlPassword(rs.getString("company_passwort"));
			setSqlUsername(rs.getString("company_login"));

		} else {

			setName(rs.getString("customer_firstname") + " " + rs.getString("customer_lastname"));
			setSqlPassword(rs.getString("customer_passwort"));
			setSqlUsername(rs.getString("customer_login"));
		}

		System.out.println("logged as: " + name + ", pass: " + sqlPassword + ", name: " + sqlUsername);

	}

	/*---------------------------------------------------------------------------------*/

	// listen to button and change boolean if clicked
	public void aclLogout(ActionEvent ae) {

		anybodyLoggedIn = false;
		userLoggedIn = false;
		adminLoggedIn = false;
		System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
		System.out.println("USER EINGELOGGT: " + userLoggedIn);
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
