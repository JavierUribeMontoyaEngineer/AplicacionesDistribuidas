package modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
//import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Usuario implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String usuario;
	private String nif;
	private String nombre;
	private String clave;
	private String email;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
	private List<Catalogo> catalogos;

	// Necesario para el ORM de JPA: me va a crear el objeto vacio y despues
	// añadira los atributos con set (la lista la añade con set pero vacia).
	public Usuario() {
	}

	public Usuario(String nif, String nombre, String usuario, String clave, String email) {
		super();
		this.nif = nif;
		this.nombre = nombre;
		this.usuario = usuario;
		this.clave = clave;
		this.email = email;
		this.catalogos = new LinkedList<Catalogo>();
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

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	public List<Catalogo> getCatalogos() {
		return catalogos;
	}

	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}

	public void update(String claveNuevo, String emailNuevo, String nombreNuevo) {
		setClave(claveNuevo);
		setEmail(emailNuevo);
		setNombre(nombreNuevo);

	}

	@Override
	public String toString() {
		return "Usuario [usuario=" + usuario + ", nif=" + nif + ", nombre=" + nombre + ", clave=" + clave + ", email="
				+ email + ", catalogos=" + catalogos + "]";
	}
	
	

}
