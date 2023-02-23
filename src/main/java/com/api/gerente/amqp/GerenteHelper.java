package com.api.gerente.amqp;

import com.api.gerente.dtos.GerenteDto;
import com.api.gerente.models.GerenteModel;
import com.api.gerente.services.GerenteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class GerenteHelper {
    final GerenteService gerenteService;

    public GerenteHelper(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    public ResponseEntity<String> saveGerente(@Valid GerenteDto gerenteDto){
        if(gerenteService.existsByCpf(gerenteDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: CPF já está sendo utilizado!");
        }
        if(gerenteService.existsByEmail(gerenteDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: E-mail já está sendo utilizado!");
        }
        if(gerenteService.existsByTelefone(gerenteDto.getTelefone())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: O telefone já está sendo utilizada!");
        }

        var gerenteModel = new GerenteModel();
        BeanUtils.copyProperties(gerenteDto, gerenteModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteService.save(gerenteModel).getId().toString());
    }


    public ResponseEntity<GerenteDto> deleteGerente(UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GerenteDto());
        }

        GerenteDto gerenteDto = new GerenteDto();
        BeanUtils.copyProperties(gerenteModelOptional.get(), gerenteDto);

        gerenteService.delete(gerenteModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body(gerenteDto);
    }

    public ResponseEntity<String> updateGerente(UUID id, @Valid GerenteDto gerenteDto){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        var gerenteModel = new GerenteModel();
        BeanUtils.copyProperties(gerenteDto, gerenteModel);
        gerenteModel.setId(gerenteModelOptional.get().getId());

        gerenteService.save(gerenteModel);
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get().getEmail());
    }

    public ResponseEntity<Object> addOneClienteToGerente(UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }

        gerenteModelOptional.get().setNumeroClientes(
                gerenteModelOptional.get().getNumeroClientes() + 1
        );

        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.save(gerenteModelOptional.get()));
    }

    public ResponseEntity<Object> addClienteToGerente(UUID idGerenteAntigo, UUID idGerenteAtual){
        Optional<GerenteModel> gerenteModelAtualOptional = gerenteService.findById(idGerenteAtual);
        Optional<GerenteModel> gerenteModelAntigoOptional = gerenteService.findById(idGerenteAntigo);

        if (gerenteModelAtualOptional.isEmpty() || gerenteModelAntigoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }

        gerenteModelAtualOptional.get().setNumeroClientes(
                gerenteModelAtualOptional.get().getNumeroClientes() + gerenteModelAntigoOptional.get().getNumeroClientes()
        );

        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.save(gerenteModelAtualOptional.get()));
    }

    public ResponseEntity<Object> subOneClienteToGerente(UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }

        gerenteModelOptional.get().setNumeroClientes(
                gerenteModelOptional.get().getNumeroClientes() - 1
        );

        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.save(gerenteModelOptional.get()));
    }

    public ResponseEntity<String> getGerenteByNumeroClientesMin(){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findByNumeroClientesMin();
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get().getId().toString());
    }

    public ResponseEntity<String> getGerenteByNumeroClientesMax(){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findByNumeroClientesMax();
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get().getId().toString());
    }

    public ResponseEntity<GerenteDto> getGerenteById(UUID id) {
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new GerenteDto());
        }

        var gerenteDto = new GerenteDto();
        BeanUtils.copyProperties(gerenteModelOptional.get(), gerenteDto);

        return ResponseEntity.status(HttpStatus.OK).body(gerenteDto);
    }

    public ResponseEntity<Object> getGerenteNumber() {
        List<GerenteModel> gerenteModelList = gerenteService.findAllSaga();
        if (gerenteModelList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não há gerentes");
        }

        int qtd = 0;
        for (GerenteModel gerenteModel : gerenteModelList) {
            qtd++;
        }

        return ResponseEntity.status(HttpStatus.OK).body(qtd);
    }
}
