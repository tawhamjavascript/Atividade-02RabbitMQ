package com.tawham.ifpb.sistema.distribuida;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class Produtor {
    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_");
        String NOME_FILA = "task_queue";
        try (
            Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel()) {
            boolean duravel = true;
            channel.queueDeclare(NOME_FILA, duravel, false, false, null);
            String mensagem = String.join("", args);
            int middleMensagem = mensagem.length() / 2;
            mensagem = mensagem.substring(0, middleMensagem) + ".Taw-Ham Almeida Balbino de Paula" + mensagem.substring(middleMensagem);

            channel.basicPublish("", NOME_FILA, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes());
            System.out.println("[x] Enviado '" + mensagem + "'");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
