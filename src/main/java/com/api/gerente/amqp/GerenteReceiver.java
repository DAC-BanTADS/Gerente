package com.api.gerente.amqp;

import com.api.gerente.controllers.GerenteController;
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
    private GerenteController gerenteController;

    @RabbitHandler
    public GerenteTransfer receive(@Payload GerenteTransfer gerenteTransfer) {
        if (gerenteTransfer.getAction().equals("save-gerente")) {
            if (Objects.isNull(gerenteTransfer.getGerenteDto())) {
                gerenteTransfer.setAction("failed-gerente");
                return gerenteTransfer;
            }

            ResponseEntity<Object> response = gerenteController.saveGerente(gerenteTransfer.getGerenteDto());

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                gerenteTransfer.setAction("success-gerente");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            return gerenteTransfer;
        }

        return null;
    }
}
