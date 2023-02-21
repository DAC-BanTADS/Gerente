package com.api.gerente.services;

import com.api.gerente.models.GerenteModel;
import com.api.gerente.repositories.GerenteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class GerenteService {
    final GerenteRepository gerenteRepository;

    public GerenteService(GerenteRepository gerenteRepository) {
        this.gerenteRepository = gerenteRepository;
    }

    @Transactional
    public GerenteModel save(GerenteModel gerenteModel) {
        return gerenteRepository.save(gerenteModel);
    }

    public Page<GerenteModel> findAll(Pageable pageable) {
        return gerenteRepository.findAll(pageable);
    }

    public Optional<GerenteModel> findById(UUID id) {
        return gerenteRepository.findById(id);
    }

    public Optional<GerenteModel> findByEmail(String email) {
        return gerenteRepository.findByEmail(email);
    }

    public Optional<GerenteModel> findByNumeroClientesMin() { return gerenteRepository.findByNumeroClientesMin(); }

    public Optional<GerenteModel> findByNumeroClientesMax() { return gerenteRepository.findByNumeroClientesMax(); }

    @Transactional
    public void delete(GerenteModel gerenteModel) {
        gerenteRepository.delete(gerenteModel);
    }

    public boolean existsByCpf(String cpf) {
        return gerenteRepository.existsByCpf(cpf);
    }

    public boolean existsByEmail(String email) {
        return gerenteRepository.existsByEmail(email);
    }

    public boolean existsByTelefone(String telefone) {
        return gerenteRepository.existsByTelefone(telefone);
    }
}