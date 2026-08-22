Sistema de prescripción y seguimiento de ejercicio físico

Prototipo desarrollado como Trabajo de Fin de Grado para gestionar un proceso multidisciplinar de evaluación, prescripción y seguimiento de ejercicio físico.

Componentes
- app-web: aplicación principal desarrollada con Spring Boot y Thymeleaf.
- resumen-api: servicio REST para almacenar y consultar los resúmenes de entrenamiento.
- proceso-bpmn: proceso asistencial modelado mediante BPMN.
- docker-compose.yml: configuración de PostgreSQL, Apache Kafka y jBPM.

Tecnologías utilizadas
Java 17, Spring Boot, Thymeleaf, Spring Security, jBPM, KIE Server, PostgreSQL, Apache Kafka, Docker y Maven.

Requisitos
- Java 17.
- Maven.
- Docker Desktop.
- Git.

Configuración
Antes de ejecutar el proyecto se debe copiar .env.example como .env y completar las variables con las credenciales locales. El archivo .env no debe subirse al repositorio.

Ejecución
La infraestructura se inicia ejecutando docker compose up -d.
La primera vez es necesario importar el proyecto de la carpeta proceso-bpmn en Business Central y desplegarlo en KIE Server.
La API se inicia con mvn -f resumen-api/pom.xml spring-boot:run.
La aplicación web se inicia con mvn -f app-web/pom.xml spring-boot:run.
La aplicación estará disponible en http://localhost:9090 y el servicio REST en http://localhost:9091.

Estado del proyecto
Este proyecto es un prototipo académico ejecutado en un entorno local. No está preparado para utilizar datos sanitarios reales ni para desplegarse directamente en producción.