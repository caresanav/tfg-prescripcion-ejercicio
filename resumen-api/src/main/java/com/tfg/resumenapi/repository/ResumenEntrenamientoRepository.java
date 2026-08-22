package com.tfg.resumenapi.repository;

import com.tfg.resumenapi.entity.ResumenEntrenamiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumenEntrenamientoRepository
        extends JpaRepository<ResumenEntrenamiento, Long> {

    Optional<ResumenEntrenamiento> findByTokenAcceso(
            String tokenAcceso);

    Optional<ResumenEntrenamiento>
            findFirstByProcessInstanceIdOrderByFechaCreacionDesc(
                    Long processInstanceId);
}