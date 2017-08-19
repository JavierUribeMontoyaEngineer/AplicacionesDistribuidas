package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.jms.JMSException;
import javax.naming.NamingException;

import org.glassfish.jersey.Severity;

import jms.EmisorCola;
import jms.ReceptorCola;
import modelo.Categoria;
import modelo.Mensaje;

@ManagedBean(name = "mensajesBean")
@SessionScoped
public class MensajesBean {
	private String texto;
	private String respuesta;
	private String preguntaRespondida;
	private List<Mensaje> mensajes = new LinkedList<Mensaje>();
	private int tiempoEspera = 10000;
	private List<String> mensajesRecibidos = new LinkedList<String>();

	public List<Mensaje> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensaje> mensajes) {
		this.mensajes = mensajes;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void enviarTexto(ActionEvent event) {
		String resultado = "";
		if (texto == null || texto.equals("")) {
			resultado = "No se puede enviar un mensaje vacio.";
			FacesContext.getCurrentInstance().addMessage("ShippingForm:texto", new FacesMessage(resultado));
		}
		try {
			EmisorCola.enviar(texto);
		} catch (NamingException e) {
			resultado = "Error durante el envio.";
			e.printStackTrace();
		} catch (JMSException e) {
			resultado = "Error durante el envio.";
			e.printStackTrace();
		}
		resultado = "Envio realizado correctamente.";
		FacesContext.getCurrentInstance().addMessage("ShippingForm:texto", new FacesMessage(resultado));
		texto = "";
	}

	public void recibirTexto(ActionEvent event) {
		String resultado = "";
		if (!mensajesRecibidos.isEmpty()) {
			resultado = "No has respondido al mensaje anterior :(";
			FacesContext.getCurrentInstance().addMessage("ReceptionForm:texto", new FacesMessage(FacesMessage.SEVERITY_FATAL,resultado,resultado));
		} else {
			String recibido = "";
			try {
				recibido = ReceptorCola.recibir(tiempoEspera);
				System.out.println("Recibido:" + recibido);
			} catch (NamingException e) {
				resultado = "Error durante el envio.";
				e.printStackTrace();
			} catch (JMSException e) {
				resultado = "Error durante el envio.";
				e.printStackTrace();
			}
			if (recibido != null && !recibido.equals("")) {
				resultado = "Mensaje recibido correctamente.";
				FacesContext.getCurrentInstance().addMessage("ReceptionForm:texto", new FacesMessage(resultado));
				texto = recibido;
				mensajesRecibidos.add(texto);
				System.out.println("texto:" + texto);
			} else {
				if (resultado.equals(""))
					resultado = "No se recibio ningún mensaje.";
				texto = "";
				FacesContext.getCurrentInstance().addMessage("ReceptionForm:texto", new FacesMessage(resultado));
			}
		}
	}

	public void recibirTodosTexto(ActionEvent event) {
		String recibido = "";
		int contadorMensajes = 0;
		while (recibido != null) {
			try {
				recibido = ReceptorCola.recibir(tiempoEspera);
			} catch (NamingException e) {
				e.printStackTrace();
			} catch (JMSException e) {
				e.printStackTrace();
			}
			if (recibido != null) {
				contadorMensajes++;
				mensajesRecibidos.add(recibido);
			}
		}
		if (contadorMensajes > 0)
			FacesContext.getCurrentInstance().addMessage("ReceptionForm:texto",
					new FacesMessage("Mensajes recibidos correctamente"));
	}

	public void responder(ActionEvent evento) {
		UIParameter parametro = (UIParameter) evento.getComponent().findComponent("pregunta");
		preguntaRespondida = (String) parametro.getValue();
		FileWriter fichero = null;
		PrintWriter pw = null;
		try {
			fichero = new FileWriter("mensajes.txt", true);
			pw = new PrintWriter(fichero);
			pw.println(preguntaRespondida);
			pw.println(respuesta);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fichero)
					fichero.close();
				respuesta = "";
				mensajesRecibidos.clear();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void leerFichero() {
		File archivo = null;
		FileReader fr = null;
		BufferedReader br = null;
		mensajes.clear();
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			archivo = new File("mensajes.txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			while ((linea = br.readLine()) != null) {
				Mensaje mensaje = new Mensaje();
				mensaje.setPregunta(linea);
				linea = br.readLine();
				mensaje.setRespuesta(linea);
				mensajes.add(mensaje);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta
			// una excepcion.
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public int getTiempoEspera() {
		return tiempoEspera;
	}

	public void setTiempoEspera(int tiempoEspera) {
		this.tiempoEspera = tiempoEspera;
	}

	public List<String> getMensajesRecibidos() {
		return mensajesRecibidos;
	}

	public void setMensajesRecibidos(List<String> mensajesRecibidos) {
		this.mensajesRecibidos = mensajesRecibidos;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getPreguntaRespondida() {
		return preguntaRespondida;
	}

	public void setPreguntaRespondida(String preguntaRespondida) {
		this.preguntaRespondida = preguntaRespondida;
	}

}