package modelo.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import modelo.Catalogo;
import modelo.Usuario;

public class JPAUsuarioDAO implements UsuarioDAO {

	private EntityManager em;
	
	public JPAUsuarioDAO(EntityManagerFactory emf) {
		// Creamos un EntityManager del EntityManagerFactory
		em = emf.createEntityManager();
	}

	@Override
	public Usuario create(String nif, String nombre, String usuario, String clave, String email) throws ExceptionDAO {
		Usuario usuarioExistente = em.find(Usuario.class, usuario);
	    // Si no existe lo creo
		if (usuarioExistente == null) {
			em.getTransaction().begin();
			Usuario usuJPA = new Usuario(nif, nombre, usuario, clave, email);
			em.persist(usuJPA);
			em.getTransaction().commit();
			return usuJPA;
		}
		else
			return usuarioExistente;
	}

	@Override
	public void update(Usuario usuario) throws ExceptionDAO {
		em.getTransaction().begin();
		em.merge(usuario);
		em.getTransaction().commit();

	}
	
	

	@Override
	public Usuario findByNombre(String usuario) throws ExceptionDAO {
		// Buscamos en la entidad JPA Usuario el objeto con clave primaria "usuario".
		// El find devuelve el objeto que le ponga en el primer parametro.
		Usuario usuJPA = em.find(Usuario.class, usuario);
		return usuJPA;
	}

	@Override
	public List<Usuario> recuperarUsuarios() throws ExceptionDAO {
		String jpql = "SELECT usuario FROM Usuario usuario";
	    Query query = em.createQuery(jpql);
	    
	    return (List<Usuario>)query.getResultList();
	}
	
	@Override
	public void remove(Usuario usuarioSeleccionado) throws ExceptionDAO {
		em.getTransaction().begin();
		Usuario usuarioDAO = em.merge(usuarioSeleccionado);
		em.remove(usuarioDAO);
		em.getTransaction().commit();

	}



}
