package com.tfg.repository;

import com.tfg.entity.TipoEjercicio;
import org.springframework.data.jpa.repository.JpaRepository;

// Permite consultar/guardar en tabla TipoEjercicio

public interface TipoEjercicioRepository extends JpaRepository<TipoEjercicio, Long> {
}