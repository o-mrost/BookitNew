package bookit;

import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

@ManagedBean(name = "mb")
@SessionScoped

public class LoginBean {


	private String kennung = "";
	private String pw = "";
	private boolean loggedIn = false;

	/*--------------------------------------------------------------------------*/

	public LoginBean() {
		System.out.println("Mb.<init>...");
		System.out.println((new Date()).toString());
	}

	/*--------------------------------------------------------------------------*/

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

	public String actLogin() {

		String sOutcome = null;
		System.out.println("actLogin()...");

		kennung = kennung.trim();
		pw = pw.trim();

		if (kennung.equalsIgnoreCase("user") && pw.equals("user")) {
			sOutcome = "user";
			loggedIn = true;
		} else if (kennung.equalsIgnoreCase("admin") && pw.equals("admin")) {
			sOutcome = "admin";
			loggedIn = true;
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler",
					"Kennung oder PW falsch (user/user oder admin/admin)"));

		return sOutcome;
	}

	/*--------------------------------------------------------------------------*/

	public void aclLogout(ActionEvent ae) {
		loggedIn = false;
	}

}
