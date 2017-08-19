package modelo.dao;

import java.util.Date;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;


import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.Usuario;

public class JPACatalogoDAO implements CatalogoDAO {

	private EntityManager em;

	public JPACatalogoDAO(EntityManagerFactory emf) {
		em = emf.createEntityManager();
	}

	@Override
	public Catalogo create(String nombre, Date fecha, String web, String url, Usuario usuario) throws ExceptionDAO {
		Catalogo catalogoExistente = em.find(Catalogo.class, nombre);
		// Si no existe lo creo
		if (catalogoExistente == null) {
			em.getTransaction().begin();

			Catalogo catalogo = new Catalogo(nombre, fecha, web, url, usuario);
			em.persist(catalogo);

			if (usuario != null)
				usuario.getCatalogos().add(catalogo);

			em.getTransaction().commit();

			return catalogo;
		}
		// Si ya existia lo devuelvo
		else
			return catalogoExistente;

	}

	@Override
	public Catalogo findByNombre(String nombre) throws ExceptionDAO {
		Catalogo catalogoJPA = em.find(Catalogo.class, nombre);
		return catalogoJPA;

	}

	@Override
	public List<Catalogo> findByUsuario(Usuario usuario) throws ExceptionDAO {
		String jpql = "SELECT catalogo FROM Catalogo catalogo WHERE catalogo.usuario = :usuario";
		Query query = em.createQuery(jpql);
		query.setParameter("usuario", usuario);
		List<Catalogo> resultados = query.getResultList();
		return resultados;
	}

	@Override
	public void update(Catalogo catalogo) {
		em.getTransaction().begin();
		em.merge(catalogo);
		em.getTransaction().commit();
	}

	@Override
	public boolean existeCategoria(Categoria categoria, String nombreCatalogo) {
		Catalogo catalogoJPA = em.find(Catalogo.class, nombreCatalogo);

		for (Categoria categoriaIterable : catalogoJPA.getCategorias()) {
			if (categoriaIterable.getCodigo() == categoria.getCodigo())
				return true;
		}

		return false;
	}

	@Override
	public List<Catalogo> findByCategoria(Categoria categoria) throws ExceptionDAO {
		Categoria  categoriaJPA = em.find(Categoria.class, categoria.getNombre());
		return categoriaJPA.getCatalogos();
	}

	@Override
	  public void remove(Catalogo catalogo) throws ExceptionDAO {
	    em.getTransaction().begin();
	    Catalogo catalogoDAO = em.merge(catalogo);
	    em.remove(catalogoDAO);
	    em.getTransaction().commit();

	  }

}
