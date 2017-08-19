package modelo.dao;

import javax.ejb.Stateless;

@Stateless(name = "Factoria")
public class FactoriaDAO implements FactoriaLocalDAO {
	protected FactoriaDAO factoria = null;

	// Metodos factoria
	public UsuarioDAO getUsuarioDAO() {
		return factoria.getUsuarioDAO();
	};

	public CategoriaDAO getCategoriaDAO() {
		return factoria.getCategoriaDAO();
	}

	public CatalogoDAO getCatalogoDAO() {
		return factoria.getCatalogoDAO();
	}

	public ItemDAO getItemDAO() {
		return factoria.getItemDAO();
	}

	// Declaracion como constantes de los tipos de factoria
	public final static int JDBC = 1;
	public final static int JPA = 2;

	public void setFactoriaDAO(int tipo) throws ExceptionDAO {
		switch (tipo) {
		case JDBC: {
			try {
				factoria = new JDBCFactoriaDAO();
			} catch (Exception e) {
				throw new ExceptionDAO(e.getMessage());
			}
		}
		case JPA: {
			try {
				factoria = new JPAFactoriaDAO();
			} catch (Exception e) {
				throw new ExceptionDAO(e.getMessage());
			}
		}
		}
	}
}