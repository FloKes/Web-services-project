package services;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class InternalCommunation {





	//sending part
	private final static String SENDING_QUEUE_NAME = "hello";

	public static void send(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		try (Connection connection = factory.newConnection();
		     Channel channel = connection.createChannel()) {
			channel.queueDeclare(SENDING_QUEUE_NAME, false, false, false, null);
			String message = "hello    D World!";
			channel.basicPublish("", SENDING_QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
			System.out.println(" [x] Sent '" + message + "'");
		}
	}


	//Receiving part
	private final static String RECEIVING_QUEUE_NAME = "hello";

	public static void recv(String[] argv) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(RECEIVING_QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		DeliverCallback deliverCallback = (consumerTag, delivery) -> {
			String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
			System.out.println(" [x] Received '" + message + "'");
		};
		channel.basicConsume(RECEIVING_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	}


}
