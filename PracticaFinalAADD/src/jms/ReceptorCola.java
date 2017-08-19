package jms;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ReceptorCola {
	public static String recibir(long tiempoEspera) throws NamingException, JMSException {
		InitialContext iniCtx = new InitialContext();
		Object tmp = iniCtx.lookup("ConnectionFactory");
		QueueConnectionFactory qcf = (QueueConnectionFactory) tmp;
		QueueConnection conn = qcf.createQueueConnection();
		Queue queue = (Queue) iniCtx.lookup("queue/adCola");
		QueueSession session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		QueueReceiver queueReceiver = session.createReceiver(queue);
		conn.start();
		TextMessage message = (TextMessage) queueReceiver.receive(tiempoEspera);
		conn.close();
		if (message != null) {
			return message.getText();
		} else {
			return null;
		}
	}
}