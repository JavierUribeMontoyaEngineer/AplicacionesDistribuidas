package modelo.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAFactoriaDAO extends FactoriaDAO {

	private EntityManagerFactory emf;
	
	public JPAFactoriaDAO() {
		// Creamos la Unidad de Persistencia con nombre PracticaFinalAADD (esta en el fichero persistence.xml de META-INF)
		emf = Persistence.createEntityManagerFactory ("PracticaFinalAADD");

	}
	
	@Override
	public UsuarioDAO getUsuarioDAO() {
		return new JPAUsuarioDAO(emf);
	}

	@Override
	public CategoriaDAO getCategoriaDAO() {
		return new JPACategoriaDAO(emf);
	}

	@Override
	public CatalogoDAO getCatalogoDAO() {
		return new JPACatalogoDAO(emf);
	}

	@Override
	public ItemDAO getItemDAO() {
		return new JPAItemDAO(emf);
	}

}
