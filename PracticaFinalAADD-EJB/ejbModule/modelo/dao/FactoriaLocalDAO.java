package modelo.dao;

import javax.ejb.Local;

@Local
public interface FactoriaLocalDAO {

	UsuarioDAO getUsuarioDAO();

	CategoriaDAO getCategoriaDAO();

	CatalogoDAO getCatalogoDAO();

	ItemDAO getItemDAO();

	void setFactoriaDAO(int tipo) throws ExceptionDAO;
}
