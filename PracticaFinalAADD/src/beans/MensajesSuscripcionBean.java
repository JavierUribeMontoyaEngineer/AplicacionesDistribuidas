package beans;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.jms.JMSException;
import javax.naming.NamingException;

import controlador.Controlador;
import jms.PublicadorApartado;
import jms.SuscriptorApartado;
import modelo.Catalogo;

@ManagedBean(name = "mensajesSuscripcionBean")
@SessionScoped
public class MensajesSuscripcionBean {
	private List<String> mensajesRecibidos = new LinkedList<String>();
	private List<String> mensajesCatalogo = new LinkedList<String>();

	public void enviarTexto(String nombreCatalogo) {
		try {
			PublicadorApartado.enviar(nombreCatalogo);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void recibirTodosTexto() {
		try {
			SuscriptorApartado.registrarApartado();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	// public String getTipo() {
	// return tipo;
	// }
	//
	// public void setTipo(String tipo) {
	// this.tipo = tipo;
	// }

	public List<String> getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<String> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}

	public List<String> getMensajesCatalogo() {
		mensajesCatalogo.clear();
		for (String nombreCatalogo : mensajesRecibidos) {
			Catalogo catalogo = Controlador.getUnicaInstancia().buscarCatalogo(nombreCatalogo);
			String catalogoMensaje = "Nombre: " + nombreCatalogo + " URL: " + catalogo.getUrl() + " Web: "
					+ catalogo.getWeb() + " Fecha: " + catalogo.getFecha() + " Categorias:" + catalogo.getCategorias();
			System.out.println("dfdf");
			mensajesCatalogo.add(catalogoMensaje);
		}
		return mensajesCatalogo;
	}

	public void setMensajesCatalogo(List<String> mensajesCatalogo) {
		this.mensajesCatalogo = mensajesCatalogo;
	}
}