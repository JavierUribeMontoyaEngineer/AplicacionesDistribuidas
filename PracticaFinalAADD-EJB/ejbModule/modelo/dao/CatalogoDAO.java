package modelo.dao;

import java.util.Date;
import java.util.List;

import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.Usuario;

public interface CatalogoDAO {

	public Catalogo create (String nombre, Date fecha, String web, String url, Usuario usuario) throws ExceptionDAO;
	public Catalogo findByNombre (String nombre) throws ExceptionDAO;
	public List<Catalogo> findByUsuario (Usuario usuario) throws ExceptionDAO;
	public void update(Catalogo catalogo) throws ExceptionDAO;
	public boolean existeCategoria(Categoria categoria,String nombreCatalogo) throws ExceptionDAO;
	public List<Catalogo> findByCategoria(Categoria categoria) throws ExceptionDAO;
	public void remove(Catalogo catalogo) throws ExceptionDAO;
}
