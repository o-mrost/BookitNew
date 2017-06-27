package bookit;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "logbean")
@SessionScoped

public class LoginBean {

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

	public LoginBean() {
		System.out.println("Mb.<init>...");
		System.out.println((new Date()).toString());
	}

	/*--------------------------------------------------------------------------*/

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

	/*--------------------------------------------------------------------------*/
	//

	public String actLogin() {

		String sOutcome = null;

		System.out.println("actLogin()...");

		kennung = kennung.trim();
		pw = pw.trim();

		System.out.println("kennung: " + kennung);
		System.out.println("password: " + pw);

		if (kennung.equalsIgnoreCase("user") && pw.equals("user")) {
			sOutcome = "user";
			loggedIn = true;

			
//			userLoggedIn = true;
//			adminLoggedIn = false;

		} else if (kennung.equalsIgnoreCase("admin") && pw.equals("admin")) {
			sOutcome = "admin";
			loggedIn = true;

//			adminLoggedIn = true;
//			userLoggedIn = false;
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch (user/user oder admin/admin)"));

//		System.out.println("USER EINGELOGGT: " + userLoggedIn);
//		System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
		System.out.println("logged In: " + loggedIn);
		System.out.println("sOutcome: " + sOutcome);

		return sOutcome;
	}

	/*--------------------------------------------------------------------------*/

	public void aclLogout(ActionEvent ae) {

		System.out.println("++++++++++++ actLogout");
		loggedIn = false;
		userLoggedIn = false;
		adminLoggedIn = false;

		System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
		System.out.println("USER EINGELOGGT: " + userLoggedIn);
	}

}
