package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.MarcaItem;

public class JDBCCategoriaDAO implements CategoriaDAO {
	private DataSource ds;
	
	public JDBCCategoriaDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Categoria create(String nombre) throws ExceptionDAO {
		Connection con = null;
		try {
			Categoria categoriaExistente = findByNombre(nombre);
			if (categoriaExistente == null) {
				con = ds.getConnection();
				PreparedStatement stmt = con.prepareStatement(
						"INSERT into CATEGORIA (nombre) " + "values (?)");
				stmt.setString(1, nombre);
				stmt.executeUpdate();
				
				Categoria categoria = new Categoria(nombre);
				
				stmt = con.prepareStatement("SELECT codigo FROM categoria WHERE nombre = ?");
			    stmt.setString(1, nombre);
			    ResultSet rs = stmt.executeQuery();
		    	if (rs.next())
		    		categoria.setCodigo(rs.getInt("codigo"));

				stmt.close();
				con.close();
				return categoria;
			}
			return categoriaExistente;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}

	@Override
	public Categoria findByNombre(String nombre) throws ExceptionDAO {
		Connection con = null;
		List<Catalogo> listaCatalogos = new LinkedList<Catalogo>();

		try {
			con = ds.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"SELECT codigo,nombre FROM categoria WHERE nombre = ?");
			stmt.setString(1, nombre);
			ResultSet rs = stmt.executeQuery();
			Categoria categoria = null;
			if (rs.next()) {
				categoria = new Categoria(nombre);
				categoria.setCodigo(rs.getInt("codigo"));

				CatalogoDAO catalogoDAO;
				catalogoDAO = Controlador.getUnicaInstancia().getFactoria().getCatalogoDAO();
				listaCatalogos = catalogoDAO.findByCategoria(categoria);
				categoria.setCatalogos(listaCatalogos);
			}

			stmt.close();
			con.close();

			return categoria;

		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		} 
	}

	@Override
	public List<Categoria> findByCatalogo(Catalogo catalogo) throws ExceptionDAO {
		Connection con = null;
		List<Categoria> listaCategorias = new LinkedList<Categoria>();
		try {
			con = ds.getConnection();

			PreparedStatement stmt = con.prepareStatement(
					"SELECT cod_categoria, nom_catalogo FROM categoria_catalogo WHERE nom_catalogo = ?");
			stmt.setString(1, catalogo.getNombre());
			ResultSet rs = stmt.executeQuery();
			Categoria categoria = null;				
			
			// Para cada categoria_catalogo encontrado obtengo la info de su categoria con otro SELECT, la creo y la añado a la lista 
			while (rs.next()) {
				int codigoCategoria = rs.getInt("cod_categoria");
				PreparedStatement stmt1 = con.prepareStatement(
						"SELECT codigo, nombre FROM categoria WHERE codigo = ?");
				stmt1.setInt(1, codigoCategoria);
				ResultSet rs1 = stmt1.executeQuery();
				if (rs1.next()) {
					categoria = new Categoria(rs1.getString("nombre"));
					categoria.setCodigo(codigoCategoria);
					categoria.addCatalogo(catalogo);
					listaCategorias.add(categoria);
				}
				stmt1.close();
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}

		return listaCategorias;
	}

	@Override
	public void update(Categoria categoria) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			PreparedStatement stmt = con.prepareStatement(
					"UPDATE categoria SET nombre=? WHERE codigo =?");
			
			stmt.setString(1, categoria.getNombre());
			stmt.setInt(2, categoria.getCodigo());
			stmt.executeUpdate();
			stmt.close();
					
			stmt = con.prepareStatement("DELETE FROM CATEGORIA_CATALOGO WHERE cod_categoria=?");
			stmt.setInt(1, categoria.getCodigo());
			stmt.executeUpdate();
			stmt.close();
			for (Catalogo catalogo : categoria.getCatalogos()) {
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
	public List<Categoria> recuperarCategorias() throws ExceptionDAO {
		return null;
	}

	@Override
	public void remove(Categoria categoria) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();
			System.out.println(categoria.getNombre());
			PreparedStatement stmt = con.prepareStatement("DELETE FROM categoria WHERE nombre = ?");
			stmt.setString(1, categoria.getNombre());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
		
	}

}
