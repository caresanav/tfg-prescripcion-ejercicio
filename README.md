# Sistema de prescripción y seguimiento de ejercicio físico

Prototipo desarrollado como Trabajo de Fin de Grado para gestionar un proceso multidisciplinar de evaluación, prescripción y seguimiento de ejercicio físico.

> Este proyecto tiene finalidad académica y se ejecuta en un entorno local. No está preparado para utilizar datos sanitarios reales ni para desplegarse directamente en producción.

## Descripción

La aplicación coordina la participación de diferentes perfiles profesionales mediante un proceso asistencial modelado con BPMN y ejecutado mediante jBPM y KIE Server.

El flujo contempla la evaluación médica, la evaluación física, la prescripción, el diseño y publicación del entrenamiento y su posterior seguimiento. También incorpora notificaciones asíncronas por correo y la consulta del entrenamiento mediante un enlace específico.

## Componentes

- **`app-web`**: aplicación principal desarrollada con Spring Boot y Thymeleaf.
- **`resumen-api`**: servicio REST encargado de almacenar y recuperar los resúmenes de entrenamiento.
- **`proceso-bpmn`**: proyecto KJAR que contiene el proceso asistencial modelado con BPMN.
- **`docker-compose.yml`**: configuración de PostgreSQL, Apache Kafka y jBPM.

## Estructura del repositorio

```text
tfg-prescripcion-ejercicio/
├── app-web/
├── resumen-api/
├── proceso-bpmn/
├── docker-compose.yml
├── .env.example
├── .gitignore
└── README.md
```

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Thymeleaf
- Spring Security
- jBPM y KIE Server
- BPMN
- PostgreSQL
- Spring Data JPA
- Apache Kafka
- SMTP
- Servicios REST
- Docker
- Maven

## Requisitos

Para ejecutar el proyecto se necesita:

- Java 17
- Maven
- Docker Desktop
- Git
- Una cuenta de correo compatible con SMTP

## Configuración

Primero se debe crear el archivo local `.env` a partir de la plantilla:

```bash
cp .env.example .env
```

Después deben completarse las variables de `.env` con las credenciales y valores correspondientes.

El archivo `.env` contiene información privada y está excluido del repositorio mediante `.gitignore`. No debe añadirse manualmente a Git.

## Inicio de la infraestructura

PostgreSQL, Kafka y jBPM se ejecutan mediante Docker Compose:

```bash
docker compose up -d
```

La primera descarga e inicialización de jBPM puede tardar varios minutos.

| Servicio | Dirección o puerto |
|---|---|
| Business Central | `http://localhost:8080/business-central` |
| KIE Server | `http://localhost:8080/kie-server` |
| PostgreSQL | `localhost:5432` |
| Apache Kafka | `localhost:9092` |

## Despliegue del proceso BPMN

La carpeta `proceso-bpmn` contiene el proyecto KJAR con el proceso `PrescripcionEjercicio`.

Durante la primera instalación se debe:

1. Acceder a Business Central.
2. Importar el proyecto contenido en `proceso-bpmn`.
3. Construir y desplegar el proyecto en KIE Server.
4. Comprobar que se ha creado el contenedor `exercise-prescription_1.0.0-SNAPSHOT`.

La aplicación no podrá iniciar nuevas instancias hasta que el proceso esté desplegado.

## Ejecución de resumen-api

En una terminal, desde la raíz del repositorio:

```bash
set -a
source .env
set +a
mvn -f resumen-api/pom.xml spring-boot:run
```

El servicio REST estará disponible en:

```text
http://localhost:9091
```

Sus operaciones principales son:

- `POST /api/resumenes`: almacena un resumen de entrenamiento.
- `GET /api/resumenes/{token}`: recupera un resumen mediante su token.

## Ejecución de app-web

En una segunda terminal, desde la raíz del repositorio:

```bash
set -a
source .env
set +a
mvn -f app-web/pom.xml spring-boot:run
```

La aplicación web estará disponible en:

```text
http://localhost:9090
```

## Usuarios de demostración

| Perfil | Usuario | Contraseña |
|---|---|---|
| Médico | `medico` | `1234` |
| Primer fisioterapeuta | `fisio1` | `1234` |
| Segundo fisioterapeuta | `fisio2` | `1234` |
| Administrador | `admin` | `1234` |

Estas cuentas están definidas en memoria y se utilizan exclusivamente para demostrar el funcionamiento del prototipo.

## Detención de la infraestructura

Para detener los contenedores conservando sus datos:

```bash
docker compose down
```

No debe utilizarse la opción `-v` si se quieren conservar los volúmenes de PostgreSQL y jBPM.

## Consideraciones de seguridad

La solución implementa una autenticación básica mediante Spring Security, pero mantiene varias limitaciones propias de un prototipo:

- Usuarios definidos en memoria.
- Cuenta técnica común para acceder a KIE Server.
- Protección CSRF desactivada.
- Comunicaciones HTTP en el entorno local.
- Tokens públicos sin fecha de caducidad.
- Credenciales gestionadas mediante variables locales.

## Estado del proyecto

El recorrido funcional principal ha sido implementado y probado en un entorno local. Las pruebas se han centrado en la ejecución del proceso, la comunicación entre componentes y la separación básica de tareas por perfil profesional.

## Autora

Carmen Esau — Trabajo de Fin de Grado.
