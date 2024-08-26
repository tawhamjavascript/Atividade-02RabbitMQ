package com.tawham.ifpb.sistema.distribuida;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Consumidor {
    public static void main(String[] args) throws Exception {
        String TASK_QUEUE_NAME = "task_queue";
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");
        Connection conexao = connectionFactory.newConnection();
        Channel canal = conexao.createChannel();
        int prefetchCount = 1;
        canal.basicQos(prefetchCount);
        boolean duravel = true;
        canal.queueDeclare(TASK_QUEUE_NAME, duravel, false, false, null);
        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] Recebido '" + mensagem + "'");
            try {
                doWork(mensagem);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                System.out.println("[x] Feito");
                canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };
        boolean autoAck = false;
        canal.basicConsume(TASK_QUEUE_NAME, autoAck, callback, consumerTag -> {});
    }
    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
