package com.api.gerente.repositories;

import com.api.gerente.models.GerenteModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GerenteRepository extends JpaRepository<GerenteModel, UUID> {
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    Optional<GerenteModel> findByEmail(String email);

    boolean existsByTelefone(String telefone);

    @Query(
            value = "select * from gerente order by numero_clientes asc limit 1",
            nativeQuery = true
    )
    Optional<GerenteModel> findByNumeroClientesMin();

    @Query(
            value = "select * from gerente where numero_clientes > 0 order by numero_clientes desc limit 1",
            nativeQuery = true
    )
    Optional<GerenteModel> findByNumeroClientesMax();
}
