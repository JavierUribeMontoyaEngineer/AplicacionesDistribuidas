package controlador;

import java.io.File;


import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Remote;

import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;
import modelo.Usuario;


@Remote
public interface ControladorEJBRemote {
	public boolean login(String usuario, String clave);

	public Usuario registrarUsuario(String usuario, String clave, String email, String nombre, String nif);

	public void updateUsuario(Usuario usuarioActual, String nombreNuevo, String claveNuevo, String emailNuevo);

	public Usuario buscarUsuario(String nombreUsuario);

	// En el controlador actualizo la logica y delego en el DAO para
	// persistirlo.
	public void addCatalogo(Usuario usuario, Catalogo catalogo);

	public Catalogo registrarCatalogo(String nombre, Date fecha, String web, String url, Usuario usuario);

	public void cargarItemsEnCatalogo(String nombreArchivo, Catalogo catalogo);

	public boolean borrarCatalogo(Catalogo catalogo);

	public Item buscarItem(String nombreItem);
	public boolean usuarioExiste(String usuario);

	public List<Item> buscarItemPorCriterio(TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion,
			Catalogo catalogo, String campo);

	public List<Item> buscarItemPorCatalogo(Catalogo catalogo);

	public boolean borrarItem(Item item);

	public void borrarItems(List<Item> items);

	public void updateItem(Item itemActual, String urlProducto, String nombre, String urlImagen, String nombreCompleto,
			String precioRebajado, String precioOriginal);


	public Catalogo buscarCatalogo(String nombreCatalogo);

	public List<Catalogo> buscarCatalogosUsuario(Usuario usuario);

	public Categoria registrarCategoria(String nombreCategoria);

	public Categoria buscarCategoria(String nombreCategoria);

	public void asignarCategoria(Categoria categoria, Catalogo catalogo);

	public void asignarCategorias(List<Categoria> categorias, Catalogo catalogo);

	public void desasignarCategoria(Categoria categoria, Catalogo catalogo);

	public List<Usuario> recuperarUsuarios();

	public List<Categoria> recuperarCategorias();

	public List<Categoria> buscarCategoriasPorCatalogo(Catalogo catalogo);

	public boolean borrarCategoria(Categoria categoriaSeleccionada);

	public boolean borrarUsuario(Usuario usuarioSeleccionado);

}
