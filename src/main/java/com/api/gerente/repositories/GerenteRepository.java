package com.api.gerente.repositories;

import com.api.gerente.models.GerenteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GerenteRepository extends JpaRepository<GerenteModel, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
