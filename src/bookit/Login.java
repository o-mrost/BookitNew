package bookit;

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

	private String username = "";
	private String password = "";
	private boolean userLoggedIn = false;
	private boolean adminLoggedIn = false;
	private boolean anybodyLoggedIn = false;

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

		username = username.trim();
		password = password.trim();

		if (username.equalsIgnoreCase("user") && password.equals("user")) {
			sOutcome = "user";
			userLoggedIn = true;
			adminLoggedIn = false;
			System.out.println("USER EINGELOGGT: " + userLoggedIn);
			System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
			anybodyLoggedIn = true;
		}

		else if (username.equalsIgnoreCase("admin") && password.equals("admin"))

		{
			sOutcome = "admin";
			adminLoggedIn = true;
			userLoggedIn = false;
			System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
			System.out.println("USER EINGELOGGT: " + userLoggedIn);
			anybodyLoggedIn = true;
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch(user/user oder admin/admin)"));

		System.out.println("Username: " + username);

		return sOutcome;
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
