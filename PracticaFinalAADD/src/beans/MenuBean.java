package beans;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

@ManagedBean(name = "menuBean")
@SessionScoped
public class MenuBean {

	private String plantilla;
	
	public String getPlantilla() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String usuarioActual = (String) request.getSession().getAttribute("usuario_actual");
		System.out.println("******************* " + usuarioActual);
		if (usuarioActual.equals("admin"))
			plantilla="WEB-INF/templates/homeAdminTemplate.xhtml";
		else
			plantilla="WEB-INF/templates/homeTemplate.xhtml";

		return plantilla;
	}
	
	public void setPlantilla(String plantilla) {
		this.plantilla = plantilla;
	}
	
	public void cerrarSesion() {
	//	FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
		request.getSession().setAttribute("usuario_actual", null);
		
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");

		} catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
