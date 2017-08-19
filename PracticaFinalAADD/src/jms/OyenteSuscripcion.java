package jms;

import java.util.Map;

import javax.faces.context.FacesContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import beans.MensajesSuscripcionBean;

public class OyenteSuscripcion implements MessageListener {
	private MensajesSuscripcionBean beanMensajes;

	public OyenteSuscripcion() {
		Map<String, Object> session = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		beanMensajes = (MensajesSuscripcionBean) session.get("mensajesSuscripcionBean");
	}

	@Override
	public void onMessage(Message mensaje) {
		if (mensaje instanceof TextMessage) {
			TextMessage mensajeTexto = (TextMessage) mensaje;
			try {
				beanMensajes.getMensajesRecibidos().add(mensajeTexto.getText());
				System.out.println("Mensaje añadido:"+beanMensajes.getMensajesRecibidos());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}