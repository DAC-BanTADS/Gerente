package com.api.gerente.controllers;

import com.api.gerente.dtos.GerenteDto;
import com.api.gerente.models.GerenteModel;
import com.api.gerente.services.GerenteService;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/gerente")
public class GerenteController {

    final GerenteService gerenteService;

    public GerenteController(GerenteService gerenteService) {
        this.gerenteService = gerenteService;
    }

    @PostMapping
    public ResponseEntity<Object> saveGerente(@RequestBody @Valid GerenteDto gerenteDto){
        if(gerenteService.existsByCpf(gerenteDto.getCpf())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: CPF já está sendo utilizado!");
        }
        if(gerenteService.existsByEmail(gerenteDto.getEmail())){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito: E-mail já está sendo utilizado!");
        }

        var gerenteModel = new GerenteModel();
        BeanUtils.copyProperties(gerenteDto, gerenteModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(gerenteService.save(gerenteModel));
    }

    @GetMapping
    public ResponseEntity<Page<GerenteModel>> getAllGerentes(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneGerente(@PathVariable(value = "id") UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteGerente(@PathVariable(value = "id") UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        gerenteService.delete(gerenteModelOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Gerente deletado com sucesso.");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateGerente(@PathVariable(value = "id") UUID id,
                                                    @RequestBody @Valid GerenteDto gerenteDto){
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
