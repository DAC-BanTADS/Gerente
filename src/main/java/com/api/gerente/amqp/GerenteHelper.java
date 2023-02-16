package com.api.gerente.amqp;

import com.api.gerente.dtos.GerenteDto;
import com.api.gerente.models.GerenteModel;
import com.api.gerente.services.GerenteService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class GerenteHelper {
    final GerenteService gerenteService;

    public GerenteHelper(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    public ResponseEntity<Object> saveGerente(@Valid GerenteDto gerenteDto){
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
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteService.save(gerenteModel));
    }


    public ResponseEntity<Object> deleteGerente(UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        gerenteService.delete(gerenteModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Gerente deletado com sucesso.");
    }

    public ResponseEntity<Object> updateGerente(UUID id, @Valid GerenteDto gerenteDto){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        var gerenteModel = new GerenteModel();
        BeanUtils.copyProperties(gerenteDto, gerenteModel);
        gerenteModel.setId(gerenteModelOptional.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.save(gerenteModel));
    }
}
