package jms;

import javax.jms.JMSException;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SuscriptorApartado {
	private static TopicSubscriber topicSubscriber = null;
	private static OyenteSuscripcion oyenteSuscripcion = null;

	public static void registrarApartado() throws NamingException, JMSException {
		if (topicSubscriber == null) {
			InitialContext iniCtx = new InitialContext();
			Object tmp = iniCtx.lookup("TopicConnectionFactory");
			TopicConnectionFactory qcf = (TopicConnectionFactory) tmp;
			TopicConnection conn = qcf.createTopicConnection();
			Topic topic = (Topic) iniCtx.lookup("topic/adApartado");
			TopicSession session = conn.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
			conn.start();
			topicSubscriber = session.createSubscriber(topic);
			oyenteSuscripcion = new OyenteSuscripcion();
			System.out.println("Activamos listener");
			topicSubscriber.setMessageListener(oyenteSuscripcion);
		}
	}
}