package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import controlador.Controlador;

@ManagedBean(name = "registrarCategoriaBean")
@SessionScoped
public class RegistrarCategoriaBean {

	private String nombreCategoria;

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
	}
	
	public void registrar() {
		Controlador.getUnicaInstancia().registrarCategoria(nombreCategoria);
	}
	
	public void clear() {
		nombreCategoria = "";
	}

	
	
}
