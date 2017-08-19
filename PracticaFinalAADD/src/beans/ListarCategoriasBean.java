package beans;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import controlador.Controlador;
import modelo.Catalogo;
import modelo.Categoria;
import modelo.Item;
import modelo.Usuario;

@ManagedBean(name = "listarCategoriasBean")
@SessionScoped
public class ListarCategoriasBean {

	private List<Categoria> categorias;
	private List<Categoria> categoriasDelCatalogo;
	private Catalogo catalogoActual;
	//private List<String> categoriasSeleccionadas;
	private List<Categoria> categoriasSeleccionadas;
	private Categoria categoriaSeleccionada;

	
	public Categoria getCategoriaSeleccionada() {
		return categoriaSeleccionada;
	}
	public void setCategoriaSeleccionada(Categoria categoriaSeleccionada) {
		this.categoriaSeleccionada = categoriaSeleccionada;
	}
	public List<Categoria> getCategorias() {
		categorias = Controlador.getUnicaInstancia().recuperarCategorias();
		return categorias;
	}

	public Catalogo getCatalogoActual() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String catalogoActualSesion = (String) request.getSession().getAttribute("catalogo_actual");
		catalogoActual = Controlador.getUnicaInstancia().buscarCatalogo(catalogoActualSesion);
		return catalogoActual;
	}

	public void setCatalogoActual(Catalogo catalogoActual) {
		this.catalogoActual = catalogoActual;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> getCategoriasDelCatalogo() {
		//System.out.println("ListarCategoriasBean.getCategoriasDelCatalogo()");
		categoriasDelCatalogo = Controlador.getUnicaInstancia().buscarCategoriasPorCatalogo(getCatalogoActual());
		return categoriasDelCatalogo;
	}

	public void setCategoriasDelCatalogo(List<Categoria> categoriasDelCatalogo) {
		this.categoriasDelCatalogo = categoriasDelCatalogo;
	}

	public void asignar() {
		System.out.println("ASIGNAR");
		/*List<Categoria> categorias = new LinkedList<Categoria>();
		for (String nombreCategoria : getCategoriasSeleccionadas()) {
			Categoria categoria = Controlador.getUnicaInstancia().buscarCategoria(nombreCategoria);
			categorias.add(categoria);
		}*/
		Controlador.getUnicaInstancia().asignarCategorias(categoriasSeleccionadas, getCatalogoActual());
	}
	
	public void desasignarCategoria(ActionEvent evento) {
		System.out.println("ListarCategoriasBean.borrar()");
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("categoria");
		categoriaSeleccionada = (Categoria) parametro.getValue();
		//catalogoActual.removeCategoria(categoriaSeleccionada);
		//categoriasDelCatalogo.remove(categoriaSeleccionada);
		Controlador.getUnicaInstancia().desasignarCategoria(categoriaSeleccionada, catalogoActual);
		System.out.println("Despues update BD");
		for (Categoria cat : Controlador.getUnicaInstancia().buscarCategoriasPorCatalogo(catalogoActual)) {
			System.out.println(cat);
		}
	}

	public List<Categoria> getCategoriasSeleccionadas() {
		return categoriasSeleccionadas;
	}

	public void setCategoriasSeleccionadas(List<Categoria> categoriasSeleccionadas) {
		this.categoriasSeleccionadas = categoriasSeleccionadas;
	}
	
	public void borrar(ActionEvent evento) {
		System.out.println("ListarCatalogosBean.borrar()");
		// Buscamos el parametro con id "catalogo"
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("categoria");
		categoriaSeleccionada = (Categoria) parametro.getValue();
	
	
		Controlador.getUnicaInstancia().borrarCategoria(categoriaSeleccionada);
		
	}
	
	public void clear() {
		categoriasSeleccionadas = null;
	}

}
