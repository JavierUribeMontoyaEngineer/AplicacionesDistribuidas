package modelo;

import java.io.Serializable;

public class Mensaje implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String pregunta;
	private String respuesta;

	public Mensaje() {
		// TODO Auto-generated constructor stub
	}
	
	public Mensaje(String pregunta, String respuesta) {
		this.pregunta = pregunta;
		this.respuesta = respuesta;
	}

	public String getPregunta() {
		return pregunta;
	}

	public void setPregunta(String pregunta) {
		this.pregunta = pregunta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

}
