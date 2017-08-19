package modelo.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import modelo.Catalogo;
import modelo.Item;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;

public class JPAItemDAO implements ItemDAO {

	private EntityManager em;

	public JPAItemDAO(EntityManagerFactory emf) {
		em = emf.createEntityManager();
	}

	@Override
	  public Item create(String urlProducto, String nombre, String urlImagen, String nombreCompleto, MarcaItem marcaItem,
	      String precioRebajado, String precioOriginal, Catalogo catalogo) throws ExceptionDAO {

	    em.getTransaction().begin();
	    Item itemJPA = new Item(urlProducto, nombre, urlImagen, nombreCompleto, marcaItem, precioRebajado,
	        precioOriginal, catalogo);
	    em.persist(itemJPA);
	    // Al crear el Item tenemos que añadirlo al catalogo, solo si este
	    // existe.
	    if (catalogo != null)
	      catalogo.getItems().add(itemJPA);
	    em.merge(catalogo);
	    em.getTransaction().commit();
	    return itemJPA;
	  }

	@Override
	public void update(Item item) throws ExceptionDAO {
		//Item itemDAO = em.find(Item.class, item.getCodigo());
		em.getTransaction().begin();
		em.merge(item);
		em.getTransaction().commit();
	}

	// Devolvemos el Item con codigo nombreItem de la BD
	@Override
	public Item findByNombre(String nombre) throws ExceptionDAO {
		String jpql = "SELECT item FROM Item item WHERE item.nombre = :nombre";
		Query query = em.createQuery(jpql);
		query.setParameter("nombre", nombre);
		Item item = (Item) query.getSingleResult();
		return item;
	}

	// Devolvemos todos los Items del Catalogo pasado como parametro
	@Override
	public List<Item> findByCatalogo(Catalogo catalogo) throws ExceptionDAO {

		// Aqui el item.catalogo hace referencia al atributo catalogo, no a la
		// columna de la BD.
		String jpql = "SELECT item FROM Item item WHERE item.catalogo = :catalogo";
		Query query = em.createQuery(jpql);
		// Decimos que la variable "catalogo" se rellene con catalogo pasado por
		// parametro.
		query.setParameter("catalogo", catalogo);
		// Obtenemos la lista de todos los resultados de la consulta y la
		// devolvemos.
		List<Item> resultados = (List<Item>) query.getResultList();
		return resultados;
	}

	@Override
	public List<Item> findByCriterio(TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion, Catalogo catalogo, String campo) throws ExceptionDAO {
		// Si pasan una cadena vacia devolvemos lista vacia
		if (campo == "")
			return new LinkedList<Item>();

		String ordenacion;
		// Si quiero ordenar por cadenas numericas (codigo o precios), hago un casting a unsigned para
		// que la BD me lo reconozca como un valor numerico y lo ordene bien.
		if (campoOrdenacion == TipoCriterio.PRECIOORIGINAL || campoOrdenacion == TipoCriterio.PRECIOREBAJADO
				|| campoOrdenacion == TipoCriterio.CODIGO)
			ordenacion = "cast(item." + campoOrdenacion.getDisplayName() + " as unsigned)";
		else
			ordenacion = "item." + campoOrdenacion.getDisplayName();
			
		String jpql = "SELECT item FROM Item item WHERE item." + criterio.getDisplayName()
				+ " = :campo AND item.catalogo = :catalogo ORDER BY " + ordenacion + " " + orden.getDisplayName();

		System.out.println(jpql);
		Query query = em.createQuery(jpql);
		query.setParameter("catalogo", catalogo);
		switch (criterio) {
		// Caso 1: Comprobar si es enumerado pasamos al setParameter el tipo
		// MarcaItem
		case MARCAITEM: {
			MarcaItem marcaEnum = MarcaItem.getEnum(campo);
			if (marcaEnum != null)
				query.setParameter("campo", marcaEnum);
			// Ponen en criterio MarcaItem pero no pasan un tipo MarcaItem devolvemos lista vacia
			else
				return new LinkedList<Item>();
			break;
		}
		// Caso 2: Es un dato tipo entero, pasamos al setParameter
		// un entero
		case CODIGO: {
			try {
				query.setParameter("campo", Integer.parseInt(campo));
			} catch (NumberFormatException e) {
				// Ponen en criterio codigo pero no es un numero el campo
				// devolvemos lista vacia
				return new LinkedList<Item>();
			}
			break;
		}
		// Caso 3: Es un String, pasamos al setParameter el campo
		default: {
			query.setParameter("campo", campo);
			break;

		}
		}

		List<Item> resultados = query.getResultList();
		return resultados;
	}

	@Override
	public void remove(Item item) throws ExceptionDAO {

		em.getTransaction().begin();
		Item itemDAO = em.merge(item);
		em.remove(itemDAO);
		em.getTransaction().commit();

	}

}
