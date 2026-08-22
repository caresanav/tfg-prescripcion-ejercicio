package com.tfg.repository;

import com.tfg.entity.Ejercicio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EjercicioRepository extends JpaRepository<Ejercicio, Long> {

    // Todos los ejercicio que tengan activo = True
    List<Ejercicio> findByActivoTrue();
}