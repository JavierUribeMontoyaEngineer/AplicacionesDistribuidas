package modelo.dao;

import java.util.List;

import modelo.Catalogo;
import modelo.Item;
import modelo.MarcaItem;
import modelo.OrdenCriterio;
import modelo.TipoCriterio;

public interface ItemDAO {

	public Item create (String urlProducto, String titulo, String urlImagen, String nombre, MarcaItem marcaItem, String precioRebajado, String precioOriginal, Catalogo catalogo) throws ExceptionDAO;

	public void update (Item item) throws ExceptionDAO;

	public Item findByNombre (String nombreItem) throws ExceptionDAO;
	
	public List<Item> findByCriterio (TipoCriterio criterio, OrdenCriterio orden, TipoCriterio campoOrdenacion, Catalogo catalogo, String campoBuscado) throws ExceptionDAO;
	
	public List<Item> findByCatalogo (Catalogo catalogo) throws ExceptionDAO;
	
	public void remove(Item item)throws ExceptionDAO;


}
