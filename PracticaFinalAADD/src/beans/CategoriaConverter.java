package beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import controlador.Controlador;
import modelo.Categoria;

@ManagedBean(name = "categoriaConverterBean")
@SessionScoped
public class CategoriaConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String nombreCategoria) {
		if (nombreCategoria != null) {
			Categoria categoria = Controlador.getUnicaInstancia().buscarCategoria(nombreCategoria);
			return categoria;
		} else
			return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		return String.valueOf(value);
	}

}
