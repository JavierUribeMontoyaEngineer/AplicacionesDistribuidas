package modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Item;
import modelo.MarcaItem;
import modelo.Usuario;

public class JDBCUsuarioDAO implements UsuarioDAO {

	private DataSource ds;
	private FactoriaDAO factoria;

	public JDBCUsuarioDAO(DataSource ds) {
		this.ds = ds;
	}

	@Override
	public Usuario create(String nif, String nombre, String usuario, String clave, String email) throws ExceptionDAO {
		Connection con = null;
		try {
			Usuario usuarioExistente = findByNombre(usuario);
			if (usuarioExistente == null) {
				con = ds.getConnection();
				PreparedStatement stmt = con
						.prepareStatement("INSERT into USUARIO (usuario,clave,email,nif,nombre) " + "values (?,?,?,?,?)");
				stmt.setString(1, usuario);
				stmt.setString(2, clave);
				stmt.setString(3, email);
				stmt.setString(4, nif);
				stmt.setString(5, nombre);
				stmt.executeUpdate();
	
				stmt.close();
				con.close();
	
				Usuario c = new Usuario(nif, nombre, usuario, clave, email);
				return c;
			}
			return usuarioExistente;
			
		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}


	// Actualizo todos los valores del usuario, que ya tendra el nuevo catalogo
	// en su modelo
	@Override
	public void update(Usuario usuario) throws ExceptionDAO {
		Connection con = null;
		try {
			con = ds.getConnection();

			// Actualizo en la tabla Usuario el usuario
			PreparedStatement stmt = con
					.prepareStatement("UPDATE usuario SET clave=?, email=?, nif=?, nombre=? WHERE usuario = ?");
			stmt.setString(1, usuario.getClave());
			stmt.setString(2, usuario.getEmail());
			stmt.setString(3, usuario.getNif());
			stmt.setString(4, usuario.getNombre());
			stmt.setString(5, usuario.getUsuario());
			stmt.executeUpdate();

			stmt.close();
			con.close();

		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}

	@Override
	public Usuario findByNombre(String nombreUsuario) throws ExceptionDAO {
		Connection con = null;
		List<Catalogo> listaCatalogos = new LinkedList<Catalogo>();

		try {
			con = ds.getConnection();
			PreparedStatement stmt = con
					.prepareStatement("SELECT usuario,clave,email,nif,nombre FROM usuario WHERE usuario = ?");
			stmt.setString(1, nombreUsuario);
			ResultSet rs = stmt.executeQuery();

			Usuario usu = null;
			// En lugar de devolver los 5 datos primitivos, los aglutino y
			// devuelvo el objeto Usuario con esos valores.
			if (rs.next()) {
				usu = new Usuario(rs.getString("nif"), rs.getString("nombre"), rs.getString("usuario"),
						rs.getString("clave"), rs.getString("email"));
				CatalogoDAO catalogoDAO;
				catalogoDAO = Controlador.getUnicaInstancia().getFactoria().getCatalogoDAO();
				listaCatalogos = catalogoDAO.findByUsuario(usu);
				usu.setCatalogos(listaCatalogos);
			}

			stmt.close();
			con.close();

			// Si lo encuentra devuelve el usuario y si no, null.
			return usu;

		} catch (SQLException e) {
			throw new ExceptionDAO(e.getMessage());
		}
	}

	@Override
	public List<Usuario> recuperarUsuarios() throws ExceptionDAO {
		return null;
	}

	@Override
	public void remove(Usuario usuarioSeleccionado) throws ExceptionDAO {
		
	}

	

}
