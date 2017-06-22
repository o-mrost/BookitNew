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
	private String passwort = "";
	private boolean userLoggedIn = false;
	private boolean adminLoggedIn = false;

	/*---------------------------------------------------------------------------------*/

	//constructor login
	public Login() {
		System.out.println("MyBean.<init>...");
		System.out.println("User logged in: " + userLoggedIn);
		System.out.println("Admin logged in: " + adminLoggedIn);
		System.out.println((new Date()).toString());
	}

	/*---------------------------------------------------------------------------------*/
	
	//getter and setters for input and output fields
	public void setKennung(String s) {
		username = s;
	}

	public String getKennung() {
		return username;
	}

	public void setPw(String s) {
		passwort = s;
	}

	public String getPw() {
		return passwort;
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

	//define what happens when login button gets clicked
	public String actLogin(ActionEvent ae) {
		String sOutcome = null;
		System.out.println("actLogin()...");

		username = username.trim();
		passwort = passwort.trim();

		if (username.equalsIgnoreCase("user") && passwort.equals("user")) 
		{
			sOutcome = "user";
			userLoggedIn = true;
			adminLoggedIn = false;
			System.out.println("USER EINGELOGGT: " + userLoggedIn);
			System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
		} 
		
		else if(username.equalsIgnoreCase("admin")&& passwort.equals("admin"))
		
		{
			sOutcome = "admin";
			adminLoggedIn = true;
			userLoggedIn = false;
			System.out.println("ADMIN EINGELOGGT: " + adminLoggedIn);
			System.out.println("USER EINGELOGGT: " + userLoggedIn);
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch(user/user oder admin/admin)"));

		return sOutcome;
	}

	/*---------------------------------------------------------------------------------*/

	//listen to button and change boolean if clicked
	public void aclLogout(ActionEvent ae) {
		userLoggedIn = false;
		adminLoggedIn = false;
	}
	
	public void languageDE(ActionEvent ae) {
		System.out.println("deutsch");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.GERMAN);
	}
	
	public void languageEN(ActionEvent ae) {
		System.out.println("englisch");
		FacesContext.getCurrentInstance().getViewRoot().setLocale(Locale.ENGLISH);
	}
}
