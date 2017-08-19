package modelo.dao;

import java.util.List;

import modelo.Catalogo;
import modelo.Usuario;

public interface UsuarioDAO {

	public Usuario create (String nif, String nombre, String usuario, String clave, String email) throws ExceptionDAO;
	//public Usuario addCatalogo (Usuario usuario, Catalogo catalogo) throws DAOException;
	public void update (Usuario usuario) throws ExceptionDAO;

	public Usuario findByNombre (String usuario) throws ExceptionDAO;
	public List<Usuario> recuperarUsuarios() throws ExceptionDAO;
	public void remove(Usuario usuarioSeleccionado) throws ExceptionDAO;
	
}
