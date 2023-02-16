package com.api.gerente.controllers;

import com.api.gerente.models.GerenteModel;
import com.api.gerente.services.GerenteService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<Page<GerenteModel>> getAllGerentes(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(gerenteService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getGerenteById(@PathVariable(value = "id") UUID id){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findById(id);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Object> getGerenteByEmail(@PathVariable(value = "email") String email){
        Optional<GerenteModel> gerenteModelOptional = gerenteService.findByEmail(email);
        if (!gerenteModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Gerente não encontrado.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(gerenteModelOptional.get());
    }
}
