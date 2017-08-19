package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Catalogo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	private String nombre;
	// Obligatorio poner el Temporal en los Date
	@Temporal(TemporalType.TIMESTAMP)
	private Date fecha;
	private String web;
	private String url;

	@ManyToMany(mappedBy = "catalogos", fetch = FetchType.EAGER)
	// Va a estar ordenado por nombre de forma descendente.
	@OrderBy("nombre desc")
	private List<Categoria> categorias;

	@ManyToOne
	private Usuario usuario;

	@Transient
	private List<Item> itemsFiltrados;
	// Lista de items (la tabla Item tendra una FK a la tabla Catalogo
	@OneToMany(mappedBy = "catalogo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<Item> items;

	// Necesario para el ORM de JPA
	public Catalogo() {
	}

	public Catalogo(String nombre, Date fecha, String web, String url, Usuario usuario) {
		super();
		this.nombre = nombre;
		this.fecha = fecha;
		this.web = web;
		this.url = url;
		this.categorias = new LinkedList<Categoria>();
		this.items = new LinkedList<Item>();
		this.usuario = usuario;
	}

	public List<Item> getItemsFiltrados() {
		return itemsFiltrados;
	}

	public void setItemsFiltrados(List<Item> itemsFiltrados) {
		this.itemsFiltrados = itemsFiltrados;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
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

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	// Funciones relacionadas con los items del Catalogo
	public List<Item> getItems() {
		return items;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	public void addItem(Item item) {
		items.add(item);
	}

	public void addCategoria(Categoria categoria) {
		categorias.add(categoria);
	}

	public void removeCategoria(Categoria categoria) {
		System.out.println("Catalogo: " + categorias.remove(categoria));
		System.out.println("Logica");
		System.out.println(categorias);
	}

	@Override
	public String toString() {
		return "Catalogo [nombre=" + nombre + ", fecha=" + fecha + ", web=" + web + ", url=" + url + ", categorias="
				+ categorias.size() + ", usuario=" + usuario.getUsuario() + " nif=" + usuario.getNif() + ", items="
				+ items.size() + "]";
	}

	public void removeItemFiltrado(Item itemSeleccionado) {
		System.out.println("Antes: " + itemsFiltrados);
		itemsFiltrados.remove(itemSeleccionado);
		System.out.println("Despues: " + itemsFiltrados);

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		result = prime * result + ((web == null) ? 0 : web.hashCode());
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
		Catalogo other = (Catalogo) obj;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		if (web == null) {
			if (other.web != null)
				return false;
		} else if (!web.equals(other.web))
			return false;
		return true;
	}

	public void removeItem(Item item) {
		items.remove(item);

	}

	public void removeItems() {
		items.clear();
	}

}
