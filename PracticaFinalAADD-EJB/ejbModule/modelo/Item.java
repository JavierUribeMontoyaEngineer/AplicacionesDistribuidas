package modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class Item implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	// Que el codigo sea autoincrementado.
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int codigo;
	
	private String urlProducto;			// 1
	private String nombre;				// 3
	private String urlImagen;			// 4
	private String nombreCompleto;		// 5
	// Ponemos el tipo del enumerado a String para que no coja valores numericos sino el nombre real.
	@Enumerated(EnumType.STRING)
	private MarcaItem marcaItem;		// 6
	
	// La marca como String no se guarda en BD
	@Transient
	private String marcaString;

	private String precioRebajado;		//11
	private String precioOriginal;		//12
	
	// El Item tiene 1 Catalogo, y el Catalogo tiene muchos Items
	@ManyToOne
	private Catalogo catalogo;
	
	public Item(){};
	public Item(String urlProducto, String nombre, String urlImagen, String nombreCompleto,
			MarcaItem marcaItem, String precioRebajado, String precioOriginal, Catalogo catalogo) {
		super();
		this.urlProducto = urlProducto;
		this.nombre = nombre;
		this.urlImagen = urlImagen;
		this.nombreCompleto = nombreCompleto;
		this.marcaItem = marcaItem;
		this.precioRebajado = precioRebajado;
		this.precioOriginal = precioOriginal;
		this.catalogo = catalogo;
	}
	
	
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	public String getUrlProducto() {
		return urlProducto;
	}
	public void setUrlProducto(String urlProducto) {
		this.urlProducto = urlProducto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getUrlImagen() {
		return urlImagen;
	}
	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}
	public String getNombreCompleto() {
		return nombreCompleto;
	}
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}
	public MarcaItem getMarcaItem() {
		return marcaItem;
	}
	public void setMarcaItem(MarcaItem marcaItem) {
		this.marcaItem = marcaItem;
	}
	public String getMarcaString() {
		return this.marcaItem.getDisplayName();
	}
	public void setMarcaString(String marcaString) {
		this.marcaString = marcaString;
	}
	public String getPrecioRebajado() {
		return precioRebajado;
	}
	public void setPrecioRebajado(String precioRebajado) {
		this.precioRebajado = precioRebajado;
	}
	public String getPrecioOriginal() {
		return precioOriginal;
	}
	public void setPrecioOriginal(String precioOriginal) {
		this.precioOriginal = precioOriginal;
	}
	public Catalogo getCatalogo() {
		return catalogo;
	}
	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}
//	@Override
//	public String toString() {
//		return "Item [codigo=" + codigo + ", urlProducto=" + urlProducto + ", nombre=" + nombre + ", urlImagen="
//				+ urlImagen + ", nombreCompleto=" + nombreCompleto + ", marcaItem=" + marcaItem + ", precioRebajado="
//				+ precioRebajado + ", precioOriginal=" + precioOriginal + ", catalogo=" + catalogo.getNombre() + ", dueño catalogo: " + catalogo.getUsuario().getUsuario()/*catalogo */+ "]";
//	}
	public void update(String urlProducto, String nombre, String urlImagen, String nombreCompleto,
			String precioRebajado, String precioOriginal) {
		this.urlProducto = urlProducto;
		this.nombre = nombre;
		this.urlImagen = urlImagen;
		this.nombreCompleto = nombreCompleto;
		this.precioRebajado = precioRebajado;
		this.precioOriginal = precioOriginal;
	}
	
	
	
	
	
	
	

	
}
