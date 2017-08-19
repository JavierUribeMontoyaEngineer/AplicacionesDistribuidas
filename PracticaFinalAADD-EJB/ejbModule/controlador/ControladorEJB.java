package controlador;

import java.io.File;
import java.io.FileReader;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import modelo.Catalogo;
import modelo.Categoria;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;
import modelo.Item;
import modelo.Usuario;
import modelo.dao.CatalogoDAO;
import modelo.dao.CategoriaDAO;
import modelo.dao.ExceptionDAO;
import modelo.dao.FactoriaDAO;
import modelo.dao.FactoriaLocalDAO;
import modelo.dao.ItemDAO;
import modelo.dao.UsuarioDAO;

@Stateful(mappedName = "ControladorEJB")
public class ControladorEJB implements ControladorEJBRemote {

	@EJB(beanName = "Contador")
	private Contador contador;

	@EJB(beanName = "Factoria")
	private FactoriaLocalDAO factoria;

	@PostConstruct
	public void configurarFactoria() {
		try {
			// Configurar la instancia de DAOFactoria
			factoria.setFactoriaDAO(FactoriaDAO.JPA);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
	}

	public FactoriaLocalDAO getFactoria() {
		return factoria;
	}

	public boolean usuarioExiste(String usuario) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = factoria.getUsuarioDAO();
			Usuario usu = usuarioDAO.findByNombre(usuario);
			if (usu == null)
				return false;
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean login(String usuario, String clave) {
		UsuarioDAO usuarioDAO;

		try {
			usuarioDAO = factoria.getUsuarioDAO();
			Usuario usu = usuarioDAO.findByNombre(usuario);
			if (usu == null)
				return false;
			return usu.getClave().equals(clave);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return false;
	}

	public Usuario registrarUsuario(String usuario, String clave, String email, String nombre, String nif) {
		UsuarioDAO usuarioDAO;
		usuarioDAO = factoria.getUsuarioDAO();
		try {
			return usuarioDAO.create(nif, nombre, usuario, clave, email);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updateUsuario(Usuario usuarioActual, String nombreNuevo, String claveNuevo, String emailNuevo) {
		usuarioActual.update(claveNuevo, emailNuevo, nombreNuevo);

		UsuarioDAO usuarioDAO;

		usuarioDAO = factoria.getUsuarioDAO();
		try {
			usuarioDAO.update(usuarioActual);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}

	}

	public Usuario buscarUsuario(String nombreUsuario) {
		UsuarioDAO usuarioDAO;
		Usuario usuario = null;
		try {
			usuarioDAO = factoria.getUsuarioDAO();
			usuario = usuarioDAO.findByNombre(nombreUsuario);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return usuario;
	}

	// En el controlador actualizo la logica y delego en el DAO para
	// persistirlo.
	public void addCatalogo(Usuario usuario, Catalogo catalogo) {
		usuario.getCatalogos().add(catalogo);
		UsuarioDAO usuarioDAO;
		usuarioDAO = factoria.getUsuarioDAO();
		try {
			usuarioDAO.update(usuario);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
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
		CatalogoDAO catalogoDAO = factoria.getCatalogoDAO();
		Catalogo catalogo = null;
		try {
			catalogo = catalogoDAO.create(nombre, fecha, web, url, usuario);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return catalogo;

	}

	/**
	 * Carga los items dado del fichero csv pasado por parametro y los carga en
	 * el catalogo
	 * 
	 * @param nombreArchivo
	 * @param catalogo
	 */
	public void cargarItemsEnCatalogo(String nombreArchivo, Catalogo catalogo) {
		Iterable<CSVRecord> records;
		try {

			Reader in = new FileReader(nombreArchivo);

			records = CSVFormat.RFC4180.parse(in);

			String urlProducto = ""; // 1
			String nombre = ""; // 3
			String urlImagen = ""; // 4
			String nombreCompleto = ""; // 5
			String marcaItem = ""; // 6
			String precioRebajado = ""; // 11
			String precioOriginal = ""; // 12

			ItemDAO itemDAO = factoria.getItemDAO();
			for (CSVRecord record : records) {
				for (int i = 0; i < record.size(); i++) {
					String column = record.get(i);
					switch (i) {
					case 1:
						urlProducto = column;
						break;
					case 3:
						nombre = column;
						break;
					case 4:
						urlImagen = column;
						break;
					case 5:
						nombreCompleto = column;
						break;
					case 6:
						marcaItem = column;
						break;
					case 11:
						precioRebajado = column;
						break;
					case 12:
						precioOriginal = column;
						break;
					}
				}
				// La MarcaItem la inserto como esta en el Enumerado, sin
				// espacios, y si despues la quiero recuperar con el nombre real
				// con espacios
				// uso .getDisplayName().
				Item item = itemDAO.create(urlProducto, nombre, urlImagen, nombreCompleto, MarcaItem.getEnum(marcaItem),
						precioRebajado, precioOriginal, catalogo);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}

		System.out.println("finalizada la carga");

	}

	public boolean borrarCatalogo(Catalogo catalogo) {
		// System.out.println("Items catalogo"+catalogo.getItems().size());
		// catalogo.removeItems();
		System.out.println("********************************************");

		CatalogoDAO catalogoDAO;
		try {
			catalogoDAO = factoria.getCatalogoDAO();
			catalogoDAO.remove(catalogo);
		} catch (ExceptionDAO e) {
			return false;
		}
		return true;
	}

	public Item buscarItem(String nombreItem) {
		ItemDAO itemDAO;
		Item item = null;
		try {
			itemDAO = factoria.getItemDAO();
			item = itemDAO.findByNombre(nombreItem);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return item;

	}

	public List<Item> buscarItemPorCriterio(TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion,
			Catalogo catalogo, String campo) {
		ItemDAO itemDAO;
		Item item = null;
		List<Item> resultados = new LinkedList<Item>();
		try {
			itemDAO = factoria.getItemDAO();
			resultados = itemDAO.findByCriterio(criterio, orden, campoOrdenacion, catalogo, campo);
			// switch (criterio) {
			// case NOMBRE_COMPLETO: {
			// switch (orden) {
			// case ASCENDENTE:
			// resultados = itemDAO.findbyNombreCompletoAsc(campo);
			//
			// break;
			// case DESCENDENTE:
			// resultados = itemDAO.findbyNombreCompletoAsc(campo);
			//
			// break;
			// default:
			// break;
			// }
			// }
			// case CODIGO: {
			// switch (orden) {
			// case ASCENDENTE:
			// resultados = itemDAO.findbyCodigoAsc(campo);
			//
			// break;
			// case DESCENDENTE:
			// resultados = itemDAO.findbyOrdenCodigoDesc(campo);
			//
			// break;
			// default:
			// break;
			// }
			// }
			//
			// }
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return resultados;

	}

	public List<Item> buscarItemPorCatalogo(Catalogo catalogo) {
		ItemDAO itemDAO;
		List<Item> resultados = new LinkedList<Item>();
		try {
			itemDAO = factoria.getItemDAO();
			resultados = itemDAO.findByCatalogo(catalogo);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return resultados;

	}

	/**
	 * Borra el item pasado por parametro, si no lo encuentra devuelve false
	 * 
	 * @param item
	 * @return
	 */
	public boolean borrarItem(Item item) {
		ItemDAO itemDAO;
		try {
			itemDAO = factoria.getItemDAO();
			itemDAO.remove(item);
			// Si no ha encontrado el item para borrarlo y salta excepcion se
			// devuelve falso
		} catch (ExceptionDAO e) {
			System.out.println("ERROR");
			return false;
		}
		System.out.println("Item " + item.getCodigo() + " borrado");
		return true;
	}

	public void borrarItems(List<Item> items) {
		System.out.println(".................. " + items.size());
		for (Item item : items) {
			borrarItem(item);
		}
	}

	public void updateItem(Item itemActual, String urlProducto, String nombre, String urlImagen, String nombreCompleto,
			String precioRebajado, String precioOriginal) {
		itemActual.update(urlProducto, nombre, urlImagen, nombreCompleto, precioRebajado, precioOriginal);

		ItemDAO itemDAO;

		itemDAO = factoria.getItemDAO();
		try {
			itemDAO.update(itemActual);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}

	}

	public void pruebaImportacion(String nombreArchivo) {
		Iterable<CSVRecord> records;
		try {

			CatalogoDAO catalogoDAO = factoria.getCatalogoDAO();

			Catalogo catalogo = catalogoDAO.create("Palas Padel", new Date(), "PadelMania", "https://padelmania.com/",
					null);
			System.out.println(new File(".").getAbsoluteFile());

			Reader in = new FileReader(nombreArchivo);

			records = CSVFormat.RFC4180.parse(in);

			String urlProducto = ""; // 1
			String nombre = ""; // 3
			String urlImagen = ""; // 4
			String nombreCompleto = ""; // 5
			String marcaItem = ""; // 6
			String precioRebajado = ""; // 11
			String precioOriginal = ""; // 12

			ItemDAO itemDAO = factoria.getItemDAO();
			for (CSVRecord record : records) {
				for (int i = 0; i < record.size(); i++) {
					String column = record.get(i);
					switch (i) {
					case 1:
						urlProducto = column;
						break;
					case 3:
						nombre = column;
						break;
					case 4:
						urlImagen = column;
						break;
					case 5:
						nombreCompleto = column;
						break;
					case 6:
						marcaItem = column;
						break;
					case 11:
						precioRebajado = column;
						break;
					case 12:
						precioOriginal = column;
						break;
					}
				}
				// La MarcaItem la inserto como esta en el Enumerado, sin
				// espacios, y si despues la quiero recuperar con el nombre real
				// con espacios
				// uso .getDisplayName().
				itemDAO.create(urlProducto, nombre, urlImagen, nombreCompleto, MarcaItem.getEnum(marcaItem),
						precioRebajado, precioOriginal, null);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}

		System.out.println("finalizada la carga");

	}

	public Catalogo buscarCatalogo(String nombreCatalogo) {
		CatalogoDAO catalogoDAO;
		Catalogo catalogo = null;
		try {
			catalogoDAO = factoria.getCatalogoDAO();
			catalogo = catalogoDAO.findByNombre(nombreCatalogo);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return catalogo;
	}

	public List<Catalogo> buscarCatalogosUsuario(Usuario usuario) {
		CatalogoDAO catalogoDAO;
		List<Catalogo> catalogos = null;
		try {
			catalogoDAO = factoria.getCatalogoDAO();
			catalogos = catalogoDAO.findByUsuario(usuario);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return catalogos;
	}

	public Categoria registrarCategoria(String nombreCategoria) {
		CategoriaDAO categoriaDAO;
		Categoria categoria = null;
		try {
			categoriaDAO = factoria.getCategoriaDAO();
			categoria = categoriaDAO.create(nombreCategoria);
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return categoria;
	}

	public Categoria buscarCategoria(String nombreCategoria) {
		CategoriaDAO categoriaDAO;
		Categoria categoria = null;
		try {
			categoriaDAO = factoria.getCategoriaDAO();
			categoria = categoriaDAO.findByNombre(nombreCategoria);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return categoria;
	}

	public void asignarCategoria(Categoria categoria, Catalogo catalogo) {
		CatalogoDAO catalogoDAO;
		CategoriaDAO categoriaDAO;

		try {
			catalogoDAO = factoria.getCatalogoDAO();
			categoriaDAO = factoria.getCategoriaDAO();
			if (!catalogoDAO.existeCategoria(categoria, catalogo.getNombre())) {
				catalogo.addCategoria(categoria);
				categoria.addCatalogo(catalogo);
				catalogoDAO.update(catalogo);
				categoriaDAO.update(categoria);
			}
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
	}

	public void asignarCategorias(List<Categoria> categorias, Catalogo catalogo) {
		for (Categoria categoria : categorias) {
			asignarCategoria(categoria, catalogo);
		}
	}

	public void desasignarCategoria(Categoria categoria, Catalogo catalogo) {
		CatalogoDAO catalogoDAO;
		CategoriaDAO categoriaDAO;

		try {
			catalogoDAO = factoria.getCatalogoDAO();
			categoriaDAO = factoria.getCategoriaDAO();
			if (catalogoDAO.existeCategoria(categoria, catalogo.getNombre())) {
				System.out.println("************************************************");
				catalogo.removeCategoria(categoria);
				categoria.removeCatalogo(catalogo);
				System.out.println("Antes update BD");
				for (Categoria cat : buscarCategoriasPorCatalogo(catalogo)) {
					System.out.println(cat);
				}
				catalogoDAO.update(catalogo);
				categoriaDAO.update(categoria);

			}
		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
	}

	public List<Usuario> recuperarUsuarios() {
		UsuarioDAO usuarioDAO;
		List<Usuario> usuarios = null;
		try {
			usuarioDAO = factoria.getUsuarioDAO();
			usuarios = usuarioDAO.recuperarUsuarios();

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return usuarios;
	}

	public List<Categoria> recuperarCategorias() {
		CategoriaDAO categoriaDAO;
		List<Categoria> categorias = null;
		try {
			categoriaDAO = factoria.getCategoriaDAO();
			categorias = categoriaDAO.recuperarCategorias();

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return categorias;
	}

	public List<Categoria> buscarCategoriasPorCatalogo(Catalogo catalogo) {
		CategoriaDAO categoriaDAO;
		List<Categoria> resultados = new LinkedList<Categoria>();
		try {
			categoriaDAO = factoria.getCategoriaDAO();

			resultados = categoriaDAO.findByCatalogo(catalogo);

		} catch (ExceptionDAO e) {
			e.printStackTrace();
		}
		return resultados;
	}

	public boolean borrarCategoria(Categoria categoriaSeleccionada) {
		CategoriaDAO categoriaDAO;

		try {
			categoriaDAO = factoria.getCategoriaDAO();
			categoriaDAO.remove(categoriaSeleccionada);

		} catch (ExceptionDAO e) {
			return false;
		}
		return true;
	}

	public boolean borrarUsuario(Usuario usuarioSeleccionado) {
		UsuarioDAO usuarioDAO;
		try {
			usuarioDAO = factoria.getUsuarioDAO();
			usuarioDAO.remove(usuarioSeleccionado);

		} catch (ExceptionDAO e) {
			return false;
		}
		return true;

	}

}
