package com.api.gerente.amqp;

import com.api.gerente.dtos.GerenteDto;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Objects;
import java.util.UUID;

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
            if (Objects.isNull(gerenteTransfer.getGerenteDto().getCpf())) {
                gerenteTransfer.setAction("failed-gerente");
                gerenteTransfer.setMessage(("Nenhum dado de Gerente foi passado."));
                return gerenteTransfer;
            }

            ResponseEntity<String> response = gerenteHelper.saveGerente(gerenteTransfer.getGerenteDto());

            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setMessage(response.getBody());
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("update-gerente")) {
            if (Objects.isNull(gerenteTransfer.getGerenteDto().getCpf())) {
                gerenteTransfer.setAction("failed-gerente");
                gerenteTransfer.setMessage(("Nenhum dado de Gerente foi passado."));
                return gerenteTransfer;
            }

            UUID idUpdate = UUID.fromString(gerenteTransfer.getMessage());

            ResponseEntity<String> response = gerenteHelper.updateGerente(idUpdate, gerenteTransfer.getGerenteDto());

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setMessage(response.getBody());
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("delete-gerente")) {
            UUID idDelete = UUID.fromString(gerenteTransfer.getMessage());

            ResponseEntity<GerenteDto> response = gerenteHelper.deleteGerente(idDelete);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setGerenteDto(response.getBody());
                gerenteTransfer.setMessage("Gerente deletado com sucesso.");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage("Gerente não encontrado.");
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("min-gerente")) {
            ResponseEntity<String> response = gerenteHelper.getGerenteByNumeroClientesMin();

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setMessage(response.getBody());
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("max-gerente")) {
            ResponseEntity<String> response = gerenteHelper.getGerenteByNumeroClientesMax();

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setMessage(response.getBody());
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()));
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("add-one-cliente")) {
            UUID idUpdate = UUID.fromString(gerenteTransfer.getMessage());

            ResponseEntity<Object> response = gerenteHelper.addOneClienteToGerente(idUpdate);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()).toString());
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("add-cliente")) {
            String[] substrings = gerenteTransfer.getMessage().split("\\+");

            UUID idGerenteAntigo = UUID.fromString(substrings[0]);
            UUID idGerenteAtual = UUID.fromString(substrings[1]);

            ResponseEntity<Object> response = gerenteHelper.addClienteToGerente(idGerenteAntigo, idGerenteAtual);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()).toString());
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("sub-one-cliente")) {
            UUID idUpdate = UUID.fromString(gerenteTransfer.getMessage());

            ResponseEntity<Object> response = gerenteHelper.subOneClienteToGerente(idUpdate);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()).toString());
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("get-gerente")) {
            UUID id = UUID.fromString(gerenteTransfer.getMessage());

            ResponseEntity<GerenteDto> response = gerenteHelper.getGerenteById(id);

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setGerenteDto(response.getBody());
                return gerenteTransfer;
            }

            gerenteTransfer.setAction("failed-gerente");
            gerenteTransfer.setMessage("Gerente não encontrado.");
            return gerenteTransfer;
        }

        if (gerenteTransfer.getAction().equals("get-number-gerente")) {
            ResponseEntity<Object> response = gerenteHelper.getGerenteNumber();

            if (response.getStatusCode().equals(HttpStatus.OK)) {
                gerenteTransfer.setAction("success-gerente");
                gerenteTransfer.setMessage(Objects.requireNonNull(response.getBody()).toString());
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
