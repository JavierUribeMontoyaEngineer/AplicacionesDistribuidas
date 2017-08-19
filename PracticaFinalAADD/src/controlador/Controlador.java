package controlador;

import java.io.File;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.sun.glass.ui.GestureSupport;

import modelo.Catalogo;
import modelo.Categoria;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;
import modelo.Item;
import modelo.Usuario;

public class Controlador {
	ControladorEJBRemote controladorEJB;

	private static Controlador unicaInstancia;

	private Controlador() {
		try {
			controladorEJB = (ControladorEJBRemote) new InitialContext()
					.lookup("java:global/PracticaFinalAADD-EJB/ControladorEJB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	public static Controlador getUnicaInstancia() {
		if (unicaInstancia == null) {
			unicaInstancia = new Controlador();
		}
		return unicaInstancia;
	}

	public boolean usuarioExiste(String usuario) {
		return controladorEJB.usuarioExiste(usuario);
	}

	public boolean login(String usuario, String clave) {
		return controladorEJB.login(usuario, clave);
	}

	public Usuario registrarUsuario(String usuario, String clave, String email, String nombre, String nif) {
		return controladorEJB.registrarUsuario(usuario, clave, email, nombre, nif);
	}

	public void updateUsuario(Usuario usuarioActual, String nombreNuevo, String claveNuevo, String emailNuevo) {
		controladorEJB.updateUsuario(usuarioActual, nombreNuevo, claveNuevo, emailNuevo);
	}

	public Usuario buscarUsuario(String nombreUsuario) {
		return controladorEJB.buscarUsuario(nombreUsuario);
	}

	// En el controlador actualizo la logica y delego en el DAO para
	// persistirlo.
	public void addCatalogo(Usuario usuario, Catalogo catalogo) {
		controladorEJB.addCatalogo(usuario, catalogo);
	}

	/**
	 * Registra un catalogo vacio
	 * 
	 * @param nombre
	 * @param fecha
	 * @param web
	 * @param url
	 * @param usuario
	 */
	public Catalogo registrarCatalogo(String nombre, Date fecha, String web, String url, Usuario usuario) {
		return controladorEJB.registrarCatalogo(nombre, fecha, web, url, usuario);

	}

	/**
	 * Carga los items dado del fichero csv pasado por parametro y los carga en
	 * el catalogo
	 * 
	 * @param nombreArchivo
	 * @param catalogo
	 */
	public void cargarItemsEnCatalogo(String nombreArchivo, Catalogo catalogo) {
		controladorEJB.cargarItemsEnCatalogo(nombreArchivo, catalogo);

	}

	public boolean borrarCatalogo(Catalogo catalogo) {
		return controladorEJB.borrarCatalogo(catalogo);
	}

	public Item buscarItem(String nombreItem) {
		return controladorEJB.buscarItem(nombreItem);

	}

	public List<Item> buscarItemPorCriterio(TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion,
			Catalogo catalogo, String campo) {
		return controladorEJB.buscarItemPorCriterio(criterio, orden, campoOrdenacion, catalogo, campo);

	}

	public List<Item> buscarItemPorCatalogo(Catalogo catalogo) {
		return controladorEJB.buscarItemPorCatalogo(catalogo);

	}

	/**
	 * Borra el item pasado por parametro, si no lo encuentra devuelve false
	 * 
	 * @param item
	 * @return
	 */
	public boolean borrarItem(Item item) {
		return controladorEJB.borrarItem(item);
	}

	public void borrarItems(List<Item> items) {
		controladorEJB.borrarItems(items);

	}

	public void updateItem(Item itemActual, String urlProducto, String nombre, String urlImagen, String nombreCompleto,
			String precioRebajado, String precioOriginal) {
		controladorEJB.updateItem(itemActual, urlProducto, nombre, urlImagen, nombreCompleto, precioRebajado,
				precioOriginal);

	}

	public Catalogo buscarCatalogo(String nombreCatalogo) {
		return controladorEJB.buscarCatalogo(nombreCatalogo);
	}

	public List<Catalogo> buscarCatalogosUsuario(Usuario usuario) {
		return controladorEJB.buscarCatalogosUsuario(usuario);
	}

	public Categoria registrarCategoria(String nombreCategoria) {
		return controladorEJB.registrarCategoria(nombreCategoria);
	}

	public Categoria buscarCategoria(String nombreCategoria) {
		return controladorEJB.buscarCategoria(nombreCategoria);
	}

	public void asignarCategoria(Categoria categoria, Catalogo catalogo) {
		controladorEJB.asignarCategoria(categoria, catalogo);
	}

	public void asignarCategorias(List<Categoria> categorias, Catalogo catalogo) {
		controladorEJB.asignarCategorias(categorias, catalogo);
	}

	public void desasignarCategoria(Categoria categoria, Catalogo catalogo) {
		controladorEJB.desasignarCategoria(categoria, catalogo);
	}

	public List<Usuario> recuperarUsuarios() {
		return controladorEJB.recuperarUsuarios();
	}

	public List<Categoria> recuperarCategorias() {
		return controladorEJB.recuperarCategorias();
	}

	public List<Categoria> buscarCategoriasPorCatalogo(Catalogo catalogo) {
		return controladorEJB.buscarCategoriasPorCatalogo(catalogo);
	}

	public boolean borrarCategoria(Categoria categoriaSeleccionada) {
		return controladorEJB.borrarCategoria(categoriaSeleccionada);
	}

	public boolean borrarUsuario(Usuario usuarioSeleccionado) {
		return controladorEJB.borrarUsuario(usuarioSeleccionado);
	}

}
