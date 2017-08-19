package beans;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Size;

import controlador.Controlador;
import modelo.Categoria;
import modelo.Usuario;

@ManagedBean(name = "registrarUsuarioBean")
@SessionScoped
public class RegistrarUsuarioBean {

	private String usuario;
	private String nif;
	private String nombre;
	private String clave;
	private String email;
	private boolean usuarioLogueado;

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getNif() {
		return nif;
	}

	public void setNif(String nif) {
		this.nif = nif;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void registrar() {
		
		Controlador.getUnicaInstancia().registrarUsuario(usuario, clave, email, nombre, nif);
		FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage("Usuario " + usuario + " registrado correctamente"));
	}
	
	public void validarUsuarioRegistro(FacesContext context, UIComponent component, Object value) throws ValidatorException {

		Usuario usuario = Controlador.getUnicaInstancia().buscarUsuario(String.valueOf(value));
		if (usuario != null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre de usuario " + value + " ya existe en el sistema"));
		}
	}
	
	public void validarUsuarioLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		
		Usuario usuario = Controlador.getUnicaInstancia().buscarUsuario(String.valueOf(value));
		if (usuario == null) {
			throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "El nombre de usuario " + value + " no existe"));
		}
	}

	public void login() {
		if (Controlador.getUnicaInstancia().login(usuario, clave)) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			System.out.println("atributo:"+usuario);
			request.getSession().setAttribute("usuario_actual", usuario);

			try {
				FacesContext.getCurrentInstance().getExternalContext().redirect("index.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			FacesContext.getCurrentInstance().addMessage(null,
	                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "La contraseña es incorrecta"));
		}
		
	}

}
