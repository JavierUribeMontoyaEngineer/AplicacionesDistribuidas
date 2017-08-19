package modelo.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;

public class JPACategoriaDAO implements CategoriaDAO {
	private EntityManager em;

	public JPACategoriaDAO(EntityManagerFactory emf) {
		em = emf.createEntityManager();
	}

	@Override
	public Categoria create(String nombre) throws ExceptionDAO {
		Categoria categoriaExistente = findByNombre(nombre);
		if (categoriaExistente == null) {
			em.getTransaction().begin();
			Categoria categoria = new Categoria(nombre);
			em.persist(categoria);
			em.getTransaction().commit();
			return categoria;
		} else
			return categoriaExistente;
	}

	@Override
	public Categoria findByNombre(String nombre) throws ExceptionDAO {
		try {
		String jpql = "SELECT categoria FROM Categoria categoria WHERE categoria.nombre = :nombre";
		Query query = em.createQuery(jpql);
		query.setParameter("nombre", nombre);
		Categoria categoria = (Categoria) query.getSingleResult();
		
		return categoria;
		//Si no encontramos resultados en select devolvemos null
		} catch (NoResultException e){
			return null;
		}
	}

	@Override
	public List<Categoria> findByCatalogo(Catalogo catalogo) throws ExceptionDAO {
//		String jpql = "SELECT categoria FROM Categoria categoria WHERE categoria.catalogo = :catalogo";
//		Query query = em.createQuery(jpql);
//		query.setParameter("catalogo", catalogo);
//		List<Categoria> resultados = query.getResultList();
//		return resultados;
		Catalogo catalogoJPA = em.find(Catalogo.class, catalogo.getNombre());
		//for (Categoria cate : catalogoJPA.getCategorias()) {
		//	System.out.println(cate.toString());
		//}
		return catalogoJPA.getCategorias();
	}

	@Override
	public void update(Categoria categoria) {
		em.getTransaction().begin();
		em.merge(categoria);
		em.getTransaction().commit();
		
	}

	@Override
	public List<Categoria> recuperarCategorias() throws ExceptionDAO {
		String jpql = "SELECT categoria FROM Categoria categoria";
		Query query = em.createQuery(jpql);
		return (List<Categoria>) query.getResultList();
	}

	@Override
	public void remove(Categoria categoria) throws ExceptionDAO {
		em.getTransaction().begin();
		Categoria categoriaDAO = em.merge(categoria);
		em.remove(categoriaDAO);
		em.getTransaction().commit();
		
	}

}
