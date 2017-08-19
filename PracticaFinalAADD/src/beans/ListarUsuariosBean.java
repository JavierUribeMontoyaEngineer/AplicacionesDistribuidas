package beans;

import java.util.LinkedList;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.ActionEvent;

import controlador.Controlador;
import modelo.Categoria;
import modelo.Usuario;

// Declaramos el bean usando Anotaciones (asi se hace en JSF 2)
@ManagedBean(name="listarUsuariosBean")
@SessionScoped
public class ListarUsuariosBean {
	
	private List<Usuario> usuarios;
	private Usuario usuarioSeleccionado;
	
	public List<Usuario> getUsuarios() {
		usuarios = Controlador.getUnicaInstancia().recuperarUsuarios();
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	public Usuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(Usuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
	
	public void seleccionarUsuario(ActionEvent evento) {
		HtmlCommandButton boton = (HtmlCommandButton) evento.getComponent();
		// Buscamos el parametro con id "id_usuario"
		UIParameter parametro = (UIParameter) boton.findComponent("id_usuario");
		usuarioSeleccionado = (Usuario) parametro.getValue();
		
	}
	
	public void borrarUsuario(ActionEvent evento) {
		System.out.println("ListarUsuariosBean.borrarUsuario()");
		
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("usuario");
		usuarioSeleccionado = (Usuario) parametro.getValue();
		Controlador.getUnicaInstancia().borrarUsuario(usuarioSeleccionado);
	}

}
