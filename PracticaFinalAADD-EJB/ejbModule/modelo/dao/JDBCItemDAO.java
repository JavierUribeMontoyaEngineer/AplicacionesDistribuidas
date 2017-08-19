package modelo.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.sql.DataSource;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.Usuario;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;

public class JDBCItemDAO implements ItemDAO {

	private DataSource ds;

	public JDBCItemDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Item create(String urlProducto, String nombre, String urlImagen, String nombreCompleto, MarcaItem marcaItem,
			String precioRebajado, String precioOriginal, Catalogo catalogo) throws ExceptionDAO {

		Connection con = null;
		try {
			con = ds.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"INSERT into ITEM (urlProducto,nombre,urlImagen,nombreCompleto,marcaItem,precioRebajado,precioOriginal,catalogo_nombre) "
							+ "values (?,?,?,?,?,?,?,?)");
			stmt.setString(1, urlProducto);
			stmt.setString(2, nombre);
			stmt.setString(3, urlImagen);
			stmt.setString(4, nombreCompleto);
			// Insertamos la marca sin espacios, tal cual aparece en el
			// enumerado
			stmt.setString(5, marcaItem.toString());
			stmt.setString(6, precioRebajado);
			stmt.setString(7, precioOriginal);
			stmt.setString(8, catalogo.getNombre());
			stmt.executeUpdate();

			

			Item itemJDBC = new Item(urlProducto, nombre, urlImagen, nombreCompleto, marcaItem, precioRebajado,
					precioOriginal, catalogo);
			System.out.println("***" + catalogo.getNombre());
			if (catalogo != null)
				catalogo.getItems().add(itemJDBC);
			
			stmt = con.prepareStatement("SELECT codigo FROM item WHERE nombreCompleto = ?");
		    stmt.setString(1, nombreCompleto);
		    ResultSet rs = stmt.executeQuery();
		    if (rs.next())
		    	itemJDBC.setCodigo(rs.getInt("codigo"));

	    	stmt.close();
			con.close();
			return itemJDBC;

		} catch (SQLException e) {
			e.printStackTrace();
		} /*
			 * catch (ClassNotFoundException e) { e.printStackTrace(); }
			 */
		return null;
	}

	@Override
	public void update(Item item) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();

			// Actualizo en la tabla Item el item
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE item SET urlProducto=?,nombre=?,urlImagen=?,nombreCompleto=?,precioRebajado=?,precioOriginal=? WHERE codigo=?");
			stmt.setString(1, item.getUrlProducto());
			stmt.setString(2, item.getNombre());
			stmt.setString(3, item.getUrlImagen());
			stmt.setString(4, item.getNombreCompleto());
			stmt.setString(5, item.getPrecioRebajado());
			stmt.setString(6, item.getPrecioOriginal());
			stmt.setInt(7, item.getCodigo());

			stmt.executeUpdate();

			stmt.close();
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}

	// Buscamos de la BD un Item que tenga en la columna "nombre" nombreItem
	@Override
	public Item findByNombre(String nombreItem) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT codigo,urlProducto,nombre,urlImagen,nombreCompleto,marcaItem,precioRebajado,precioOriginal,catalogo_nombre FROM item WHERE nombre = ?");
			stmt.setString(1, nombreItem);
			ResultSet rs = stmt.executeQuery();
			Item item = null;
			// En lugar de devolver los 5 datos primitivos, los aglutino y
			// devuelvo el objeto Item con esos valores.
			if (rs.next()) {
				item = new Item(rs.getString("urlProducto"), rs.getString("nombre"), rs.getString("urlImagen"),
						rs.getString("nombreCompleto"), MarcaItem.valueOf(rs.getString("marcaItem")),
						rs.getString("precioRebajado"), rs.getString("precioOriginal"), null);
				item.setCodigo(rs.getInt("codigo"));

				String nombreCatalogo = rs.getString("catalogo_nombre");
				CatalogoDAO catalogoDAO;
				catalogoDAO = Controlador.getUnicaInstancia().getFactoria().getCatalogoDAO();
				Catalogo cat = catalogoDAO.findByNombre(nombreCatalogo);
				item.setCatalogo(cat);
			}

			stmt.close();
			con.close();

			// Si lo encuentra devuelve el item y si no, null.
			return item;

		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		} /*
			 * catch (ClassNotFoundException e) { e.printStackTrace(); return
			 * null; }
			 */
	}

	@Override
	public List<Item> findByCatalogo(Catalogo catalogo) throws ExceptionDAO {
		Connection con = null;
		List<Item> lista = new LinkedList<Item>();
		try {
			con = ds.getConnection();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT codigo,urlProducto,nombre,urlImagen,nombreCompleto,marcaItem,precioRebajado,precioOriginal FROM item WHERE catalogo_nombre = ?");
			stmt.setString(1, catalogo.getNombre());
			ResultSet rs = stmt.executeQuery();
			Item item = null;				
			

			while (rs.next()) {
				item = new Item(rs.getString("urlProducto"), rs.getString("nombre"), rs.getString("urlImagen"),
						rs.getString("nombreCompleto"), MarcaItem.valueOf(rs.getString("marcaItem")),
						rs.getString("precioRebajado"), rs.getString("precioOriginal"), catalogo);
				item.setCodigo(rs.getInt("codigo"));
				lista.add(item);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}

		return lista;
	}


	@Override
	public List<Item> findByCriterio(TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion,
			Catalogo catalogo, String campo) throws ExceptionDAO {
		// Si pasan una cadena vacia devolvemos lista vacia
		if (campo == "")
			return new LinkedList<Item>();

		Connection con = null;
		List<Item> lista = new LinkedList<Item>();
		Item item = null;
		try {
			con = ds.getConnection();

			String ordenacion;
			// Si quiero ordenar por cadenas numericas (codigo o precios), hago
			// un casting a unsigned para
			// que la BD me lo reconozca como un valor numerico y lo ordene
			// bien.
			if (campoOrdenacion == TipoCriterio.PRECIOORIGINAL || campoOrdenacion == TipoCriterio.PRECIOREBAJADO
					|| campoOrdenacion == TipoCriterio.CODIGO)
				ordenacion = "cast(item." + campoOrdenacion.getDisplayName() + " as unsigned)";
			else
				ordenacion = "item." + campoOrdenacion.getDisplayName();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT codigo,urlProducto,nombre,urlImagen,nombreCompleto,marcaItem,precioRebajado,precioOriginal,catalogo_nombre FROM Item WHERE "
							+ criterio.getDisplayName() + " = ? ORDER BY " + ordenacion + " " + orden.getDisplayName());

			switch (criterio) {
			// Caso 1: Comprobar si es enumerado pasamos al setParameter el tipo
			// MarcaItem
			case MARCAITEM: {

				MarcaItem marcaEnum = MarcaItem.getEnum(campo);
				if (marcaEnum != null)
					stmt.setString(1, campo);
				// Ponen en criterio MarcaItem pero no pasan un tipo MarcaItem
				else
					return null;
				break;
			}
			// Caso 2: Es un dato tipo entero, pasamos al setParameter
			// un entero
			case CODIGO: {
				try {
					stmt.setInt(1, Integer.parseInt(campo));
				} catch (NumberFormatException e) {
					// Ponen en criterio codigo pero no es un numero el campo
					// devolvemos null
					return null;
				}
				break;
			}
			// Caso 3: Es un String, pasamos al setParameter el campo
			default: {
				stmt.setString(1, campo);
				break;

			}
			}

			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				item = new Item(rs.getString("urlProducto"), rs.getString("nombre"), rs.getString("urlImagen"),
						rs.getString("nombreCompleto"), MarcaItem.valueOf(rs.getString("marcaItem")),
						rs.getString("precioRebajado"), rs.getString("precioOriginal"), null);
				item.setCodigo(rs.getInt("codigo"));
				
				String nombreCatalogo = rs.getString("catalogo_nombre");
				CatalogoDAO catalogoDAO;
				catalogoDAO = Controlador.getUnicaInstancia().getFactoria().getCatalogoDAO();
				Catalogo cat = catalogoDAO.findByNombre(nombreCatalogo);
				item.setCatalogo(cat);
				
				lista.add(item);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
		return lista;

	}
	
	@Override
	public void remove(Item item) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			System.out.println(item.getNombre());
			PreparedStatement stmt = con.prepareStatement("DELETE FROM item WHERE nombre = ?");
			stmt.setString(1, item.getNombre());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}

}
