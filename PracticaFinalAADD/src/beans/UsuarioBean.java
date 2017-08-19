package beans;

import java.io.Serializable;
import java.util.LinkedList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import controlador.Controlador;
import modelo.Usuario;

@ManagedBean(name = "usuarioBean")
@SessionScoped
public class UsuarioBean {
	private Usuario usuarioActual;

	@PostConstruct
	public void init() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String usuarioLogueado = (String) request.getSession().getAttribute("usuario_actual");
		Usuario usuarioRecuperado = Controlador.getUnicaInstancia().buscarUsuario(usuarioLogueado);
		this.usuarioActual = usuarioRecuperado;
	}

	public Usuario getUsuarioActual() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String usuarioLogueado = (String) request.getSession().getAttribute("usuario_actual");

		if (!usuarioActual.getUsuario().equals(usuarioLogueado))
			usuarioActual = Controlador.getUnicaInstancia().buscarUsuario(usuarioLogueado);

		return usuarioActual;

	}

	public void setUsuarioActual(Usuario usuarioActual) {
		this.usuarioActual = usuarioActual;
	}

	public void onRowEdit(RowEditEvent event) {
		Usuario usuarioEditado = (Usuario) event.getObject();
		System.out.println("ID:" + event.getObject().toString());
		FacesMessage msg = new FacesMessage("Usuario editado:", usuarioEditado.getUsuario());
		System.out.println(usuarioEditado.toString());
		Controlador.getUnicaInstancia().updateUsuario(usuarioActual, usuarioEditado.getNombre(),
				usuarioEditado.getClave(), usuarioEditado.getEmail());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Edicion cancelada:", ((Usuario) event.getObject()).getUsuario());
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

}
