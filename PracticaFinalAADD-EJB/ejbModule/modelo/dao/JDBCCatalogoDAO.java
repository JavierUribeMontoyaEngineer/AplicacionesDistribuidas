package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;


import controlador.Controlador;
import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.MarcaItem;
import modelo.Usuario;

public class JDBCCatalogoDAO implements CatalogoDAO {
	private DataSource ds;
	private FactoriaDAO factoria;

	public JDBCCatalogoDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Catalogo create(String nombre, Date fecha, String web, String url, Usuario usuario) throws ExceptionDAO {
		Connection con = null;
		try {
			Catalogo catalogoExistente = findByNombre(nombre);
			if (catalogoExistente == null) {
				con = ds.getConnection();
				PreparedStatement stmt = con.prepareStatement(
						"INSERT into CATALOGO (nombre,fecha,web,url,usuario) " + "values (?,?,?,?,?)");
				stmt.setString(1, nombre);
				stmt.setTimestamp(2, new Timestamp(fecha.getTime()));
				stmt.setString(3, web);
				stmt.setString(4, url);
				stmt.setString(5, usuario.getUsuario());
				stmt.executeUpdate();
				stmt.close();
				con.close();
				Catalogo catalogo = new Catalogo(nombre, fecha, web, url, usuario);
				if (usuario != null)
					usuario.getCatalogos().add(catalogo);
				return catalogo;
			}
			return catalogoExistente;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Catalogo findByNombre(String nombre) throws ExceptionDAO {
		Connection con = null;
		List<Item> listaItems = new LinkedList<Item>();
		List<Categoria> listaCategorias = new LinkedList<Categoria>();

		try {
			con = ds.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT nombre,fecha,web,url,usuario FROM Catalogo WHERE nombre = ?");
			stmt.setString(1, nombre);
			ResultSet rs = stmt.executeQuery();
			Catalogo catalogo = null;

			if (rs.next()) {
				catalogo = new Catalogo(rs.getString("nombre"), rs.getTimestamp("fecha"), rs.getString("web"),
						rs.getString("url"), null);

				// Añadimos los items al catalogo
				ItemDAO itemDAO;
				itemDAO = Controlador.getUnicaInstancia().getFactoria().getItemDAO();
				listaItems = itemDAO.findByCatalogo(catalogo);
				catalogo.setItems(listaItems);

				// Añadimos el usuario al catalogo
				String nombreUsuario = rs.getString("usuario");
				UsuarioDAO usuarioDAO;
				usuarioDAO = Controlador.getUnicaInstancia().getFactoria().getUsuarioDAO();
				Usuario usu = usuarioDAO.findByNombre(nombreUsuario);
				catalogo.setUsuario(usu);
				
				// Añadimos las categorias al catalogo
				CategoriaDAO categoriaDAO;
				categoriaDAO = Controlador.getUnicaInstancia().getFactoria().getCategoriaDAO();
				listaCategorias = categoriaDAO.findByCatalogo(catalogo);
				catalogo.setCategorias(listaCategorias);
			}

			stmt.close();
			con.close();
			return catalogo;

		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}

	@Override
	public List<Catalogo> findByUsuario(Usuario usuario) throws ExceptionDAO {
		Connection con = null;
		List<Catalogo> listaCatalogos = new LinkedList<Catalogo>();
		List<Item> listaItems = new LinkedList<Item>();
		List<Categoria> listaCategorias = new LinkedList<Categoria>();

		try {
			con = ds.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT nombre,fecha,web,url,usuario FROM Catalogo WHERE usuario = ?");
			stmt.setString(1, usuario.getUsuario());
			ResultSet rs = stmt.executeQuery();
			Catalogo catalogo = null;

			while (rs.next()) {
				catalogo = new Catalogo(rs.getString("nombre"), rs.getTimestamp("fecha"), rs.getString("web"),
						rs.getString("url"), usuario);

				ItemDAO itemDAO;
				itemDAO = Controlador.getUnicaInstancia().getFactoria().getItemDAO();
				listaItems = itemDAO.findByCatalogo(catalogo);
				catalogo.setItems(listaItems);

				CategoriaDAO categoriaDAO;
				categoriaDAO = Controlador.getUnicaInstancia().getFactoria().getCategoriaDAO();
				listaCategorias = categoriaDAO.findByCatalogo(catalogo);
				catalogo.setCategorias(listaCategorias);
				
				listaCatalogos.add(catalogo);
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
		return listaCatalogos;
	}

	// No dejamos modificar el usuario del catalogo, no tiene sentido
	@Override
	public void update(Catalogo catalogo) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE catalogo SET fecha=?, web=?, url=? WHERE nombre =?");
			
			stmt.setTimestamp(1, new Timestamp(catalogo.getFecha().getTime()));
			stmt.setString(2, catalogo.getWeb());
			stmt.setString(3, catalogo.getUrl());
			stmt.setString(4, catalogo.getNombre());
			stmt.executeUpdate();
			stmt.close();
					
			
			stmt = con.prepareStatement("DELETE FROM CATEGORIA_CATALOGO WHERE nom_catalogo=?");
			stmt.setString(1, catalogo.getNombre());
			stmt.executeUpdate();
			stmt.close();
			for (Categoria categoria : catalogo.getCategorias()) {
				System.out.println("Insert en categoria_catalogo "+categoria.getCodigo()+","+catalogo.getNombre());
				stmt = con.prepareStatement(
						"INSERT INTO categoria_catalogo(cod_categoria, nom_catalogo) VALUES(?,?) ");
				stmt.setInt(1, categoria.getCodigo());
				stmt.setString(2, catalogo.getNombre());
			}
			stmt.executeUpdate();
			stmt.close();
			
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}

	@Override
	  public boolean existeCategoria(Categoria categoria, String nombreCatalogo) {
	    try {
	      Catalogo catalogo = findByNombre(nombreCatalogo);
	      for (Categoria categoriaIterable : catalogo.getCategorias()) {
	        if (categoriaIterable.getCodigo() == categoria.getCodigo())
	          return true;
	      }
	    } catch (ExceptionDAO e) {
	      e.printStackTrace();
	    }

	    return false;
	  }

	@Override
	public List<Catalogo> findByCategoria(Categoria categoria) throws ExceptionDAO {
		Connection con = null;
		List<Catalogo> listaCatalogos = new LinkedList<Catalogo>();
		List<Item> listaItems = new LinkedList<Item>();
		try {
			con = ds.getConnection();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT cod_categoria, nom_catalogo FROM categoria_catalogo WHERE cod_categoria = ?");
			stmt.setInt(1, categoria.getCodigo());
			ResultSet rs = stmt.executeQuery();
			Catalogo catalogo = null;				
			
			// Para cada categoria_catalogo encontrado obtengo la info de su catalogo con otro SELECT, lo creo y lo añado a la lista 
			while (rs.next()) {
				String nombreCatalogo = rs.getString("nom_catalogo");

					catalogo = findByNombre(nombreCatalogo);
					listaCatalogos.add(catalogo);
				//}
				//stmt1.close();
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}

		return listaCatalogos;	
	}

	@Override
	public void remove(Catalogo catalogo) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			System.out.println(catalogo.getNombre());
			PreparedStatement stmt = con.prepareStatement("DELETE FROM catalogo WHERE nombre = ?");
			stmt.setString(1, catalogo.getNombre());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
		
	}

}
