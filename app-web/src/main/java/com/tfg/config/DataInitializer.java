package com.tfg.config;

import com.tfg.entity.Ejercicio;
import com.tfg.entity.TipoEjercicio;
import com.tfg.repository.EjercicioRepository;
import com.tfg.repository.TipoEjercicioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

// Para insertar datos en BBDD sin hacerlo manualmente
// Si ya existen datos no los crea

@Component
public class DataInitializer implements CommandLineRunner {

    private final TipoEjercicioRepository tipoEjercicioRepository;
    private final EjercicioRepository ejercicioRepository;

    public DataInitializer(TipoEjercicioRepository tipoEjercicioRepository,
                           EjercicioRepository ejercicioRepository) {
        this.tipoEjercicioRepository = tipoEjercicioRepository;
        this.ejercicioRepository = ejercicioRepository;
    }

    @Override
    public void run(String... args) {
        if (tipoEjercicioRepository.count() > 0 || ejercicioRepository.count() > 0) {
            return;
        }

        TipoEjercicio movilidad = tipoEjercicioRepository.save(
                new TipoEjercicio("Movilidad", "Ejercicios para mejorar rango de movimiento y control articular")
        );

        TipoEjercicio fuerza = tipoEjercicioRepository.save(
                new TipoEjercicio("Fuerza", "Ejercicios orientados a mejorar fuerza muscular")
        );

        TipoEjercicio equilibrio = tipoEjercicioRepository.save(
                new TipoEjercicio("Equilibrio", "Ejercicios para mejorar estabilidad y control postural")
        );

        TipoEjercicio cardio = tipoEjercicioRepository.save(
                new TipoEjercicio("Cardio", "Ejercicios de resistencia cardiovascular de baja o media intensidad")
        );

        ejercicioRepository.save(new Ejercicio(
                "Movilidad de cadera",
                "Movimientos suaves de apertura y cierre de cadera.",
                "Básico",
                "Sin material",
                true,
                movilidad
        ));

        ejercicioRepository.save(new Ejercicio(
                "Rotaciones torácicas",
                "Rotaciones controladas del tronco para mejorar movilidad dorsal.",
                "Básico",
                "Esterilla",
                true,
                movilidad
        ));

        ejercicioRepository.save(new Ejercicio(
                "Sentadilla asistida",
                "Sentadilla parcial con apoyo para controlar la técnica.",
                "Básico",
                "Silla o apoyo",
                true,
                fuerza
        ));

        ejercicioRepository.save(new Ejercicio(
                "Puente de glúteo",
                "Elevación de pelvis en decúbito supino para activar glúteos.",
                "Básico",
                "Esterilla",
                true,
                fuerza
        ));

        ejercicioRepository.save(new Ejercicio(
                "Apoyo monopodal asistido",
                "Mantener apoyo sobre una pierna con soporte cercano.",
                "Básico",
                "Pared o silla",
                true,
                equilibrio
        ));

        ejercicioRepository.save(new Ejercicio(
                "Marcha en línea",
                "Caminar colocando un pie delante del otro para trabajar equilibrio.",
                "Intermedio",
                "Sin material",
                true,
                equilibrio
        ));

        ejercicioRepository.save(new Ejercicio(
                "Caminata suave",
                "Marcha continua a ritmo cómodo.",
                "Básico",
                "Sin material",
                true,
                cardio
        ));

        ejercicioRepository.save(new Ejercicio(
                "Bicicleta estática",
                "Trabajo cardiovascular controlado en bicicleta estática.",
                "Básico",
                "Bicicleta estática",
                true,
                cardio
        ));
    }
}