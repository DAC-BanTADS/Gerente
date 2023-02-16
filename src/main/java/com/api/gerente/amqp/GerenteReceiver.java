package com.api.gerente.amqp;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Objects;

@RabbitListener(queues = "gerente")
public class GerenteReceiver {
    @Autowired
    private RabbitTemplate template;
    @Autowired
    private GerenteProducer gerenteProducer;
    @Autowired
    private GerenteHelper gerenteHelper;

    @RabbitHandler
    public GerenteTransfer receive(@Payload GerenteTransfer gerenteTransfer) {
        if (gerenteTransfer.getAction().equals("save-gerente")) {
            if (Objects.isNull(gerenteTransfer.getGerenteDto())) {
                gerenteTransfer.setAction("failed-gerente");
                gerenteTransfer.setMessage(("Nenhum dado de Gerente foi passado."));
                return gerenteTransfer;
            }

            ResponseEntity<Object> response = gerenteHelper.saveGerente(gerenteTransfer.getGerenteDto());

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                gerenteTransfer.setAction("success-gerente");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()).toString());
            return gerenteTransfer;
        }

        gerenteTransfer.setAction("failed-gerente");
        gerenteTransfer.setMessage("Ação informada não existe.");
        return gerenteTransfer;
    }
}
