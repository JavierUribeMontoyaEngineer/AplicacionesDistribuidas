package modelo.dao;

import java.util.List;

import modelo.Catalogo;
import modelo.Categoria;

public interface CategoriaDAO {

	public Categoria create (String nombre) throws ExceptionDAO;
	public Categoria findByNombre (String nombre) throws ExceptionDAO;
	public List<Categoria> findByCatalogo (Catalogo catalogo) throws ExceptionDAO;
	public void update(Categoria categoria) throws ExceptionDAO;
	public List<Categoria> recuperarCategorias() throws ExceptionDAO;
	public void remove(Categoria categoriaSeleccionada) throws ExceptionDAO;

	
	
}
