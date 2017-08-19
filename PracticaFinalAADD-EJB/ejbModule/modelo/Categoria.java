package modelo;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class Categoria implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	// Que el codigo sea autoincrementado.
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int codigo;
	
	@Column(unique=true)
	private String nombre;
	
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Catalogo> catalogos;

	
	
	// Necesario para el ORM de JPA
	public Categoria() {
	}

	public Categoria(String nombre) {
		super();
		this.nombre = nombre;
		this.catalogos = new LinkedList<Catalogo>();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public List<Catalogo> getCatalogos() {
		return catalogos;
	}
	
	public void setCatalogos(List<Catalogo> catalogos) {
		this.catalogos = catalogos;
	}
	
	public void addCatalogo(Catalogo catalogo){
		catalogos.add(catalogo);
	}

	@Override
	public String toString() {
		return "Categoria [codigo=" + codigo + ", nombre=" + nombre + ", catalogos=" + catalogos.size() + "]";
	}

	public void removeCatalogo(Catalogo catalogo) {
		System.out.println("Categoria: " + catalogos.remove(catalogo));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + codigo;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Categoria other = (Categoria) obj;
		if (codigo != other.codigo)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	

	
	
	

}
