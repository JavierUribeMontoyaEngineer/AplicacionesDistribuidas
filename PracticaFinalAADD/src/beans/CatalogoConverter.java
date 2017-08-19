package beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import controlador.Controlador;
import modelo.Catalogo;

@ManagedBean(name = "catalogoConverterBean")
@SessionScoped
public class CatalogoConverter implements Converter {

	
	/**
	 * Nos viene del getAsString como un String el nombre del catalogo y llamamos al controlador
	 * para obtener el objeto catalogo
	 */
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String nombreCatalogo) {
		System.out.println("nombre:" + nombreCatalogo);
		if (nombreCatalogo != null) {
			Catalogo catalogo = Controlador.getUnicaInstancia().buscarCatalogo(nombreCatalogo);
			return catalogo;
		} else
			return null;
	}

	/**
	 * Los items seleccionados en el menu son catalogo.nombre(porque viene del #{listarCatalogosBean.catalogos}
	 * por tanto "value" sera un string con el nombre del catalogo, 
	 * asi que lo convertimos a string para que el getAsObject lo pueda convertir a objeto
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object value) {
		return String.valueOf(value);
	}

}
