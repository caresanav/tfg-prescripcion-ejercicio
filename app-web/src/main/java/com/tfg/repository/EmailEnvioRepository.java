package com.tfg.repository;

import com.tfg.entity.EmailEnvio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailEnvioRepository extends JpaRepository<EmailEnvio, Long> {

    Optional<EmailEnvio> findByTokenRespuesta(String tokenRespuesta);

    Optional<EmailEnvio> findFirstByProcessInstanceIdOrderByFechaCreacionDesc(Long processInstanceId);
}