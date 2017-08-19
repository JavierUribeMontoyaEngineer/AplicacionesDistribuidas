package beans;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Categoria;
import modelo.Usuario;

@ManagedBean(name="registrarCatalogosBean")
@SessionScoped
public class RegistrarCatalogoBean {
	private MensajesSuscripcionBean beanMensajes;
	private String nombre;
	private String web;
	private String url;
	private Usuario usuario;
	//private List<String> categoriasSeleccionadas;
	private List<Categoria> categoriasSeleccionadas;

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getWeb() {
		return web;
	}
	public void setWeb(String web) {
		this.web = web;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void registrar(ActionEvent evento) {
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("usuario_actual");
		usuario = (Usuario) parametro.getValue();

		Catalogo catalogo = Controlador.getUnicaInstancia().registrarCatalogo(nombre, new Date(), web, url, usuario);
		Controlador.getUnicaInstancia().asignarCategorias(categoriasSeleccionadas, catalogo);

		System.out.println("Catalogo registrado");
		FacesMessage msg = new FacesMessage("Registro de catálogo", nombre);
		FacesContext.getCurrentInstance().addMessage(null, msg);
		
		//Mandar notificacion
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		beanMensajes = (MensajesSuscripcionBean) session.get("mensajesSuscripcionBean");
		beanMensajes.enviarTexto(catalogo.getNombre());
	}
	
	public void clear() {
		nombre = "";
		web = "";
		url = "";
		usuario = null;
		categoriasSeleccionadas = null;
	}
	
	public List<Categoria> getCategoriasSeleccionadas() {
		return categoriasSeleccionadas;
	}

	public void setCategoriasSeleccionadas(List<Categoria> categoriasSeleccionadas) {
		this.categoriasSeleccionadas = categoriasSeleccionadas;
	}
	
	
}
