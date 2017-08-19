package beans;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "idiomaBean")
@SessionScoped
public class IdiomaBean {
	private Locale locale;
	private String language;

	public IdiomaBean() {
		// Al crear el Bean por defecto cogemos el idioma del navegador (lo cogemos desde la peticion)
		locale = FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String idioma, String pais) {
		System.out.println(idioma);
		language = idioma;
		// Creamos el idioma elegido con el objeto Locale
		if (pais=="")
			locale = new Locale(idioma);
		else
			locale = new Locale(idioma, pais);
		// Cambiamos el idioma de la vista actual
		FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
	}
}
