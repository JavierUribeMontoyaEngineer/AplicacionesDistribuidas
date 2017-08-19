package modelo.dao;

import java.sql.Connection;

import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class JDBCFactoriaDAO extends FactoriaDAO {

	private DataSource ds = null;

	public JDBCFactoriaDAO() throws ExceptionDAO, ClassNotFoundException, SQLException {
		try {
			InitialContext contexto = new InitialContext();
			/* Obtengo el DataSource del recurso ubicado en AADD_JDBC (este recurso esta en server.xml).
			 * Tambien tengo que tener este recurso declarado en web.xml de WEB-INF.
			 * Este DataSource ya tendra la informacion de la url, user y password de la BD.
			 */
			ds = (DataSource) contexto.lookup("java:/comp/env/jdbc/AADD_JDBC");
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public UsuarioDAO getUsuarioDAO() {
		return (UsuarioDAO) new JDBCUsuarioDAO(ds);
	}

	@Override
	public CategoriaDAO getCategoriaDAO() {
		return (CategoriaDAO) new JDBCCategoriaDAO(ds);
	}

	@Override
	public CatalogoDAO getCatalogoDAO() {
		return (CatalogoDAO) new JDBCCatalogoDAO(ds);
	}

	@Override
	public ItemDAO getItemDAO() {
		return (ItemDAO) new JDBCItemDAO(ds);
	}

}
