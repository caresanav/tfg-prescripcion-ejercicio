package com.tfg.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.server.api.model.instance.TaskInstance;
import org.kie.server.api.model.instance.TaskSummary;
import org.kie.server.client.ProcessServicesClient;
import org.kie.server.client.QueryServicesClient;
import org.kie.server.client.UserTaskServicesClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tfg.client.ResumenApiClient;
import com.tfg.client.dto.CrearResumenRequest;
import com.tfg.client.dto.ResumenResponse;
import com.tfg.entity.EmailEnvio;
import com.tfg.model.Entrenamiento;
import com.tfg.model.EvaluacionFisica;

// Creada por Carmen Esau

import com.tfg.model.EvaluacionMedica;
import com.tfg.model.EventoEmail;
import com.tfg.model.PrescripcionFisica;
import com.tfg.model.SeguimientoFisio;
import com.tfg.model.SeguimientoMedico;
import com.tfg.model.TaskView;
import com.tfg.repository.EjercicioRepository;
import com.tfg.service.EmailEnvioService;
import com.tfg.service.EmailKafkaProducer;
import com.tfg.service.EmailService;

/**
 * CONTROLADOR WEB
 * Rutas HTTP que conectan la interfaz (HTML/Thymeleaf) con el motor de procesos
 * jBPM
 * a través del KIE Server
 */

@Controller
public class UiController {

    // Producer Kafka
    private final EmailKafkaProducer emailKafkaProducer;

    // Clientes para hablar con jBPM
    // Arrancar / Corregir procesos
    private final ProcessServicesClient processes;
    // Gestionar tareas humanas
    private final UserTaskServicesClient tasks;

    // Para convertir datos
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Para enviar mail
    private final EmailEnvioService emailEnvioService;

    // Para ejercicios en BBDD
    private final EjercicioRepository ejercicioRepository;

    // Cliente para comunicarse con resumen-api
    private final ResumenApiClient resumenApiClient;

    public UiController(ProcessServicesClient p, UserTaskServicesClient t, QueryServicesClient q,
            EmailService emailService, EmailKafkaProducer emailKafkaProducer, EmailEnvioService emailEnvioService, 
            EjercicioRepository ejercicioRepository, ResumenApiClient resumenApiClient) {
        this.processes = p;
        this.tasks = t;
        this.emailKafkaProducer = emailKafkaProducer;
        this.emailEnvioService = emailEnvioService;
        this.ejercicioRepository = ejercicioRepository;
        this.resumenApiClient = resumenApiClient;
    }

    // Identifican donde y qué proceso/tareas vamos a tocar (para comunicar con
    // jBPM)
    // Salen de application.properties
    // Contenedor donde vive el proceso
    @Value("${kieserver.container}")
    String containerId;
    // id del proceso BPMN a iniciar
    @Value("${process.id}")
    String processId;

    ////////////////////////////// LOGIN ///////////////////////////////////
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    ////////////////////////////// INICIO///////////////////////////////////
    @GetMapping("/")
    public String home(Authentication authentication) {
        return switch (authentication.getName()) {
            case "admin" -> "index";
            case "medico" -> "redirect:/medico";
            case "fisio1" -> "redirect:/fisio1";
            case "fisio2" -> "redirect:/fisio2";
            default -> "redirect:/login";
        };
    }

    @GetMapping("/medico")
    public String areaMedico() {
        return "medico";
    }

    @GetMapping("/fisio1")
    public String areaFisio1() {
        return "fisio";
    }

    @GetMapping("/fisio2")
    public String areaFisio2() {
        return "fisio2";
    }

    ////////////////////////////// INICIA PROCESO///////////////////////////////////
    // Inicia una instancia en KIE server
    @PostMapping("/start")
    public String startProcess(Authentication authentication) {
        // Crea nueva instancia
        processes.startProcess(containerId, processId, new HashMap<>());

        // Después de iniciar, va a la lista de tareas del usuario logueado
        return "redirect:/tasks";
    }

    ////////////////////////////// LISTA TAREAS///////////////////////////////////
    // Lista tareas para un actor
    @GetMapping("/tasks")
    public String listTasks(Authentication authentication, Model model) {
        // Actor que ha iniciado sesion
        String actor = authentication.getName();

        List<TaskSummary> list =
            tasks.findTasksAssignedAsPotentialOwner(actor, 0, 50);

        // Si entramos con rol específico, solo se muestran tareas correspondientes
        if ("medico".equals(actor)) {
            list = list.stream()
                    .filter(t ->
                            "Evaluación médica".equals(t.getName())
                            || "Seguimiento médico".equals(t.getName()))
                    .toList();
        }

        if ("fisio1".equals(actor)) {
            list = list.stream()
                    .filter(t -> {
                        if ("Prescripción física".equals(t.getName())) {
                            return true;
                        }

                        if ("Seguimiento fisioterapéutico".equals(t.getName())) {
                            return true;
                        }

                        if ("Evaluación física".equals(t.getName())) {
                            return pacienteHaConfirmadoRevisionFisicaParaTarea(
                                    t.getId());
                        }

                        return false;
                    })
                    .toList();
        }

        if ("fisio2".equals(actor)) {
            list = list.stream()
                    .filter(t ->
                            "Diseño de entrenamiento".equals(t.getName())
                            || "Subir entrenamiento".equals(t.getName()))
                    .toList();
        }
        

        // Convertimos TaskSummary en TaskView para poder añadir nombre y apellidos del paciente
        List<TaskView> tareasView = list.stream()
                .map(this::convertirATaskView)
                .toList();

        // Listas separadas para pintar mejor la pantalla de medico
        List<TaskView> evaluacionesMedicas = tareasView.stream()
                .filter(t -> "Evaluación médica".equals(t.getName()))
                .toList();

        List<TaskView> seguimientosMedicos = tareasView.stream()
                .filter(t -> "Seguimiento médico".equals(t.getName()))
                .toList();

        // Listas separadas para pintar mejor la pantalla de fisio1
        List<TaskView> evaluacionesFisicas = tareasView.stream()
                .filter(t -> "Evaluación física".equals(t.getName()))
                .toList();

        List<TaskView> prescripcionesFisicas = tareasView.stream()
                .filter(t -> "Prescripción física".equals(t.getName()))
                .toList();

        List<TaskView> seguimientosFisio = tareasView.stream()
        .filter(t -> "Seguimiento fisioterapéutico".equals(t.getName()))
        .toList();

        // Listas separadas para fisio2
        List<TaskView> disenosEntrenamiento = tareasView.stream()
                .filter(t -> "Diseño de entrenamiento".equals(t.getName()))
                .toList();

        List<TaskView> publicacionesEntrenamiento = tareasView.stream()
                .filter(t -> "Subir entrenamiento".equals(t.getName()))
                .toList();

        model.addAttribute("tareas", tareasView);
        model.addAttribute("evaluacionesMedicas", evaluacionesMedicas);
        model.addAttribute("seguimientosMedicos", seguimientosMedicos);
        model.addAttribute("evaluacionesFisicas", evaluacionesFisicas);
        model.addAttribute("prescripcionesFisicas", prescripcionesFisicas);
        model.addAttribute("actor", actor);
        model.addAttribute("seguimientosFisio", seguimientosFisio);
        model.addAttribute("disenosEntrenamiento", disenosEntrenamiento);
        model.addAttribute("publicacionesEntrenamiento", publicacionesEntrenamiento);

        return "tasks";
    }

    ////////////////////////////// RECLAMA E INICIA
    ////////////////////////////// TAREA///////////////////////////////////
    // Reclama + Inicia la tarea y muestra tu formulario
    @GetMapping({
            "/medico/tasks/{id}",
            "/fisio1/tasks/{id}",
            "/fisio2/tasks/{id}",
            "/admin/tasks/{id}"
    })
    public String showTask(@PathVariable Long id, Authentication authentication, Model model) {
        // Actor real: usuario que ha iniciado sesión
        String actor = authentication.getName();

        // Primero averiguamos qué tarea es, antes de reclamarla o iniciarla
        String taskName = tasks.findTaskById(id).getName();

        // Si es evaluación física, solo dejamos entrar si el paciente confirmó por email
        if ("Evaluación física".equals(taskName)
                && !pacienteHaConfirmadoRevisionFisicaParaTarea(id)) {
            return "redirect:/tasks?pendienteConfirmacion";
        }

        // Ahora sí reclamamos e iniciamos la tarea
        try {
            tasks.claimTask(containerId, id, actor);
        } catch (Exception ignore) {
        }
        try {
            tasks.startTask(containerId, id, actor);
        } catch (Exception ignore) {
        }

        model.addAttribute("id", id);
        model.addAttribute("actor", actor);

        // Tarea: Evaluación física
        if ("Evaluación física".equals(taskName)) {
            // 1) sacar processInstanceId desde TaskSummary
            var taskInstance = tasks.getTaskInstance(containerId, id);
            Long processInstanceId = taskInstance.getProcessInstanceId();

            // 2) leer variables del proceso
            Map<String, Object> vars = processes.getProcessInstanceVariables(containerId, processInstanceId);

            // 3) recuperar evaluacionMedica
            Object raw = vars.get("evaluacionMedica");
            EvaluacionMedica em;

            if (raw == null) {
                em = new EvaluacionMedica();
            } else if (raw instanceof EvaluacionMedica) {
                em = (EvaluacionMedica) raw;
            } else {
                em = objectMapper.convertValue(raw, EvaluacionMedica.class);
            }

            // 4) mandarla al modelo para mostrarla en el formulario del fisio
            model.addAttribute("evaluacionMedica", em);

            // el fisio sigue rellenando su objeto
            model.addAttribute("evaluacionFisica", new EvaluacionFisica());
            return "evaluacion_fisica";
        }

        // Tarea: Prescripción física
        if ("Prescripción física".equals(taskName)) {
            // 1) sacar processInstanceId
            var taskInstance = tasks.getTaskInstance(containerId, id);
            Long processInstanceId = taskInstance.getProcessInstanceId();

            // 2) leer variables del proceso
            Map<String, Object> vars = processes.getProcessInstanceVariables(containerId, processInstanceId);

            // 3) recuperar evaluacionMedica
            Object rawMed = vars.get("evaluacionMedica");
            EvaluacionMedica em;

            if (rawMed == null) {
                em = new EvaluacionMedica();
            } else if (rawMed instanceof EvaluacionMedica) {
                em = (EvaluacionMedica) rawMed;
            } else {
                em = objectMapper.convertValue(rawMed, EvaluacionMedica.class);
            }

            // 4) recuperar evaluacionFisica
            Object rawFis = vars.get("evaluacionFisica");
            EvaluacionFisica ef;

            if (rawFis == null) {
                ef = new EvaluacionFisica();
            } else if (rawFis instanceof EvaluacionFisica) {
                ef = (EvaluacionFisica) rawFis;
            } else {
                ef = objectMapper.convertValue(rawFis, EvaluacionFisica.class);
            }

            // 5) mandar todo al modelo
            model.addAttribute("evaluacionMedica", em);
            model.addAttribute("evaluacionFisica", ef);
            model.addAttribute("prescripcionFisica", new PrescripcionFisica());

            return "prescripcion_fisica";
        }
        
        // Tarea: Diseño de entrenamiento
        if ("Diseño de entrenamiento".equals(taskName)) {
            var taskInstance = tasks.getTaskInstance(containerId, id);
            Long processInstanceId = taskInstance.getProcessInstanceId();

            Map<String, Object> vars = processes.getProcessInstanceVariables(containerId, processInstanceId);

            // Recupera la evaluacionFisica
            Object rawFis = vars.get("evaluacionFisica");
            EvaluacionFisica ef;

            if (rawFis == null) {
                ef = new EvaluacionFisica();
            } else if (rawFis instanceof EvaluacionFisica) {
                ef = (EvaluacionFisica) rawFis;
            } else {
                ef = objectMapper.convertValue(rawFis, EvaluacionFisica.class);
            }

            // Recupera la prescripcionFisica
            Object rawPres = vars.get("prescripcionFisica");
            PrescripcionFisica pf;

            // si no existe la crea para que no falle
            if (rawPres == null) {
                pf = new PrescripcionFisica();
                // Si existe como objeto la usa
            } else if (rawPres instanceof PrescripcionFisica) {
                pf = (PrescripcionFisica) rawPres;
                // si existen como jSON la convierte a objeto
            } else {
                pf = objectMapper.convertValue(rawPres, PrescripcionFisica.class);
            }

            model.addAttribute("evaluacionFisica", ef);
            model.addAttribute("prescripcionFisica", pf);
            model.addAttribute("entrenamiento", new Entrenamiento());
            model.addAttribute("ejercicios", ejercicioRepository.findByActivoTrue());

            return "entrenamiento";
        }

        // Tarea: subir entrenamiento
        if ("Subir entrenamiento".equals(taskName)) {
            TaskInstance taskInstance =
                    tasks.getTaskInstance(containerId, id);

            Long processInstanceId =
                    taskInstance.getProcessInstanceId();

            Map<String, Object> vars =
                    processes.getProcessInstanceVariables(
                            containerId,
                            processInstanceId);

            // Recuperar entrenamiento
            Object rawEntrenamiento = vars.get("entrenamiento");
            Entrenamiento entrenamiento;

            if (rawEntrenamiento == null) {
                entrenamiento = new Entrenamiento();
            } else if (rawEntrenamiento instanceof Entrenamiento) {
                entrenamiento = (Entrenamiento) rawEntrenamiento;
            } else {
                entrenamiento = objectMapper.convertValue(
                        rawEntrenamiento,
                        Entrenamiento.class);
            }

            // Recuperar prescripción
            Object rawPrescripcion = vars.get("prescripcionFisica");
            PrescripcionFisica prescripcionFisica;

            if (rawPrescripcion == null) {
                throw new IllegalStateException(
                        "No existe una prescripción para este proceso");
            } else if (rawPrescripcion instanceof PrescripcionFisica) {
                prescripcionFisica =
                        (PrescripcionFisica) rawPrescripcion;
            } else {
                prescripcionFisica = objectMapper.convertValue(
                        rawPrescripcion,
                        PrescripcionFisica.class);
            }

            // Enviar ambos objetos a la pantalla
            model.addAttribute("entrenamiento", entrenamiento);
            model.addAttribute(
                    "prescripcionFisica",
                    prescripcionFisica);

            return "subir_entrenamiento";
        }

        // Tarea seguimiento fisio
        if ("Seguimiento fisioterapéutico".equals(taskName)) {
            TaskInstance taskInstance =
                    tasks.getTaskInstance(containerId, id);

            Long processInstanceId =
                    taskInstance.getProcessInstanceId();

            Map<String, Object> vars =
                    processes.getProcessInstanceVariables(
                            containerId,
                            processInstanceId);

            // Recuperar entrenamiento
            Object rawEntrenamiento = vars.get("entrenamiento");
            Entrenamiento entrenamiento;

            if (rawEntrenamiento == null) {
                entrenamiento = new Entrenamiento();
            } else if (rawEntrenamiento instanceof Entrenamiento) {
                entrenamiento =
                        (Entrenamiento) rawEntrenamiento;
            } else {
                entrenamiento = objectMapper.convertValue(
                        rawEntrenamiento,
                        Entrenamiento.class);
            }

            // Recuperar prescripción
            Object rawPrescripcion = vars.get("prescripcionFisica");
            PrescripcionFisica prescripcionFisica;

            if (rawPrescripcion == null) {
                throw new IllegalStateException(
                        "No existe una prescripción para este proceso");
            } else if (rawPrescripcion instanceof PrescripcionFisica) {
                prescripcionFisica =
                        (PrescripcionFisica) rawPrescripcion;
            } else {
                prescripcionFisica = objectMapper.convertValue(
                        rawPrescripcion,
                        PrescripcionFisica.class);
            }

            // Enviar los objetos a la pantalla
            model.addAttribute("entrenamiento", entrenamiento);
            model.addAttribute(
                    "prescripcionFisica",
                    prescripcionFisica);
            model.addAttribute(
                    "seguimientoFisio",
                    new SeguimientoFisio());

            return "seguimiento_fisio";
        }

        if ("Seguimiento médico".equals(taskName)) {
            TaskInstance taskInstance =
                    tasks.getTaskInstance(containerId, id);

            Long processInstanceId =
                    taskInstance.getProcessInstanceId();

            Map<String, Object> vars =
                    processes.getProcessInstanceVariables(
                            containerId,
                            processInstanceId);

            Object rawSeguimiento = vars.get("seguimientoFisio");
            SeguimientoFisio seguimientoFisio;

            if (rawSeguimiento == null) {
                seguimientoFisio = new SeguimientoFisio();
            } else if (rawSeguimiento instanceof SeguimientoFisio) {
                seguimientoFisio = (SeguimientoFisio) rawSeguimiento;
            } else {
                seguimientoFisio = objectMapper.convertValue(
                        rawSeguimiento,
                        SeguimientoFisio.class);
            }

            Object rawMedica = vars.get("evaluacionMedica");
            EvaluacionMedica evaluacionMedica;

            if (rawMedica == null) {
                evaluacionMedica = new EvaluacionMedica();
            } else if (rawMedica instanceof EvaluacionMedica) {
                evaluacionMedica = (EvaluacionMedica) rawMedica;
            } else {
                evaluacionMedica = objectMapper.convertValue(
                        rawMedica,
                        EvaluacionMedica.class);
            }

            model.addAttribute("evaluacionMedica", evaluacionMedica);
            model.addAttribute("seguimientoFisio", seguimientoFisio);
            model.addAttribute(
                    "seguimientoMedico",
                    new SeguimientoMedico());

            return "seguimiento_medico";
        }

        if ("Evaluación médica".equals(taskName)) {
            model.addAttribute(
                    "evaluacion",
                    new EvaluacionMedica());

            return "evaluacion";
        }

        // Si no encuentra ninguna de las anteriores -> error
        return "redirect:/tasks?taskNoSoportada";
    }

    ////////////////////////////// COMPLETA TAREA MEDICA
    ////////////////////////////// ///////////////////////////////////
    // Completa la tarea con los datos de la evaluación
    @PostMapping("/tasks/{id}/medica")
    public String completeMedica(@PathVariable Long id, Authentication authentication,
            @ModelAttribute EvaluacionMedica evaluacion) {

        // Actor real: usuario que ha iniciado sesión
        String actor = authentication.getName();

        // Necesitamos el processInstanceId antes de completar la tarea
        TaskInstance taskInstance = tasks.findTaskById(id);
        Long processInstanceId = taskInstance.getProcessInstanceId();

        // Montamos el mapa
        Map<String, Object> out = new HashMap<>();
        // Completamos la tarea en jBPM y se guarda en la instancia del proceso la
        // variable evaluacionMedica
        out.put("evaluacionMedica", evaluacion);    // Guarda evaluacionMEdica como var del proceso
        tasks.completeTask(containerId, id, actor, out);

        // Ahora jBPM ejecuta el gateway

        // Si necesita enviar correo o no
        if (Boolean.TRUE.equals(evaluacion.getNecesitaRevisionFisioterapeuta())) {

            // 1. Guardamos primero el registro en BD con estado PENDIENTE
            EmailEnvio emailGuardado = emailEnvioService.guardarPendiente(
                    processInstanceId,
                    evaluacion.getNombre(),
                    evaluacion.getApellidos(),
                    evaluacion.getEmail());

            // 2. Enviamos a Kafka el evento con el id de la fila guardada
            EventoEmail event = new EventoEmail();
            event.setEmailEnvioId(emailGuardado.getId());
            event.setNombre(evaluacion.getNombre());
            event.setApellidos(evaluacion.getApellidos());
            event.setEmail(evaluacion.getEmail());
            event.setTokenRespuesta(emailGuardado.getTokenRespuesta());

            emailKafkaProducer.enviarEmail(event);
        }

        return "redirect:/tasks?done";
    }

    ////////////////////////////// COMPLETA TAREA FISICA
    ////////////////////////////// ///////////////////////////////////
    @PostMapping("/tasks/{id}/fisica")
    public String completeFisica(@PathVariable Long id, Authentication authentication,
            @ModelAttribute EvaluacionFisica evaluacionFisica) {

        // Actor real: usuario que ha iniciado sesión
        String actor = authentication.getName();

        Map<String, Object> out = new HashMap<>();
        out.put("evaluacionFisica", evaluacionFisica);
        tasks.completeTask(containerId, id, actor, out);

        return "redirect:/tasks?done";
    }

    @PostMapping("/tasks/{id}/prescripcion")
    public String completePrescripcion(@PathVariable Long id, Authentication authentication,
            @ModelAttribute PrescripcionFisica prescripcionFisica) {

        // Actor real: usuario que ha iniciado sesión
        String actor = authentication.getName();

        Map<String, Object> out = new HashMap<>();
        out.put("prescripcionFisica", prescripcionFisica);
        tasks.completeTask(containerId, id, actor, out);

        return "redirect:/tasks?done";
    }

    // Guardar el entrenamiento diseñado
    @PostMapping("/tasks/{id}/entrenamiento")
    public String completeEntrenamiento(
            @PathVariable Long id,
            Authentication authentication,
            @RequestParam(required = false) List<Long> ejercicioIds,
            @ModelAttribute Entrenamiento entrenamiento) {

        String actor = authentication.getName();

        if (ejercicioIds != null && !ejercicioIds.isEmpty()) {
            List<String> nombresEjercicios = ejercicioRepository
                    .findAllById(ejercicioIds)
                    .stream()
                    .map(e -> e.getNombre())
                    .toList();

            entrenamiento.setEjerciciosSeleccionados(
                    String.join("\n", nombresEjercicios));
        }

        //Coger duracion y frecuencia de prescripcion


        Map<String, Object> out = new HashMap<>();
        out.put("entrenamiento", entrenamiento);

        tasks.completeTask(containerId, id, actor, out);

        return "redirect:/tasks?done";
    }

    @GetMapping("/entrenamiento/resumen/{token}")
    public String verResumenEntrenamiento(
            @PathVariable String token,
            Model model) {

        try {
            ResumenResponse resumen =
                    resumenApiClient.buscarPorToken(token);

            model.addAttribute("resumen", resumen);

            return "resumen_entrenamiento";

        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "No existe ningún entrenamiento asociado al token indicado");
        }
    }

    @PostMapping("/tasks/{id}/subir-entrenamiento")
    public String completeSubirEntrenamiento(
            @PathVariable Long id,
            Authentication authentication) {

        String actor = authentication.getName();

        TaskInstance taskInstance = tasks.findTaskById(id);
        Long processInstanceId = taskInstance.getProcessInstanceId();

        Map<String, Object> vars =
                processes.getProcessInstanceVariables(
                        containerId,
                        processInstanceId);

        Object rawMed = vars.get("evaluacionMedica");
        EvaluacionMedica evaluacionMedica;

        if (rawMed instanceof EvaluacionMedica) {
            evaluacionMedica = (EvaluacionMedica) rawMed;
        } else {
            evaluacionMedica =
                    objectMapper.convertValue(
                            rawMed,
                            EvaluacionMedica.class);
        }

        Object rawEntrenamiento = vars.get("entrenamiento");
        Entrenamiento entrenamiento;

        if (rawEntrenamiento instanceof Entrenamiento) {
            entrenamiento = (Entrenamiento) rawEntrenamiento;
        } else {
            entrenamiento =
                    objectMapper.convertValue(
                            rawEntrenamiento,
                            Entrenamiento.class);
        }

        Object rawPrescripcion =
                vars.get("prescripcionFisica");

        PrescripcionFisica prescripcionFisica;

        if (rawPrescripcion == null) {
            throw new IllegalStateException(
                    "No existe una prescripción para este proceso");
        } else if (rawPrescripcion instanceof PrescripcionFisica) {
            prescripcionFisica =
                    (PrescripcionFisica) rawPrescripcion;
        } else {
            prescripcionFisica =
                    objectMapper.convertValue(
                            rawPrescripcion,
                            PrescripcionFisica.class);
        }

        CrearResumenRequest request =
                new CrearResumenRequest(
                        processInstanceId,
                        evaluacionMedica.getNombre(),
                        evaluacionMedica.getApellidos(),
                        evaluacionMedica.getEmail(),
                        entrenamiento.getNombrePlan(),
                        prescripcionFisica.getDuracionSemanas(),
                        prescripcionFisica.getFrecuenciaSemanal(),
                        entrenamiento.getIndicacionesGenerales(),
                        entrenamiento.getObservacionesEntrenador(),
                        entrenamiento.getEjerciciosSeleccionados()
                );

        ResumenResponse resumen =
                resumenApiClient.crear(request);

        tasks.completeTask(containerId, id, actor, new HashMap<>());

        return "redirect:/entrenamiento/resumen/"
                + resumen.tokenAcceso();
    }


    @PostMapping("/tasks/{id}/seguimiento-fisio")
    public String completeSeguimientoFisio(
            @PathVariable Long id,
            Authentication authentication,
            @ModelAttribute SeguimientoFisio seguimientoFisio) {

        String actor = authentication.getName();

        boolean requiereSeguimientoMedico =
                Boolean.TRUE.equals(
                        seguimientoFisio.getRequiereSeguimientoMedico());

        boolean cambiaPrescripcion =
                !requiereSeguimientoMedico
                && Boolean.TRUE.equals(
                        seguimientoFisio.getCambiaPrescripcion());

        // Mantener consistentes los datos del objeto
        seguimientoFisio.setCambiaPrescripcion(
                cambiaPrescripcion);

        Map<String, Object> out = new HashMap<>();

        out.put("seguimientoFisio", seguimientoFisio);
        out.put("cambiaPrescripcion", cambiaPrescripcion);

        tasks.completeTask(containerId, id, actor, out);

        return "redirect:/tasks?done";
    }


    @PostMapping("/tasks/{id}/seguimiento-medico")
    public String completeSeguimientoMedico(
            @PathVariable Long id,
            Authentication authentication,
            @ModelAttribute SeguimientoMedico seguimientoMedico) {

        String actor = authentication.getName();

        Map<String, Object> out = new HashMap<>();

        out.put("seguimientoMedico", seguimientoMedico);

        out.put(
                "cambiaPrescripcion",
                Boolean.TRUE.equals(
                        seguimientoMedico.getCambiaPrescripcion()));

        tasks.completeTask(containerId, id, actor, out);

        return "redirect:/tasks?done";
    }

    ////////////////// NOMBRES EN TAREAS ///////////////////////////

    private TaskView convertirATaskView(TaskSummary taskSummary) {
        String nombrePaciente = "Paciente";
        String apellidosPaciente = "sin identificar";

        try {
            TaskInstance taskInstance = tasks.getTaskInstance(containerId, taskSummary.getId());
            Long processInstanceId = taskInstance.getProcessInstanceId();

            Map<String, Object> vars = processes.getProcessInstanceVariables(containerId, processInstanceId);

            Object rawMed = vars.get("evaluacionMedica");

            if (rawMed != null) {
                EvaluacionMedica evaluacionMedica;

                if (rawMed instanceof EvaluacionMedica) {
                    evaluacionMedica = (EvaluacionMedica) rawMed;
                } else {
                    evaluacionMedica = objectMapper.convertValue(rawMed, EvaluacionMedica.class);
                }

                if (evaluacionMedica.getNombre() != null && !evaluacionMedica.getNombre().isBlank()) {
                    nombrePaciente = evaluacionMedica.getNombre();
                }

                if (evaluacionMedica.getApellidos() != null && !evaluacionMedica.getApellidos().isBlank()) {
                    apellidosPaciente = evaluacionMedica.getApellidos();
                }
            }

            return new TaskView(
                    taskSummary.getId(),
                    taskSummary.getName(),
                    taskSummary.getStatus(),
                    processInstanceId,
                    nombrePaciente,
                    apellidosPaciente
            );

        } catch (Exception e) {
            return new TaskView(
                    taskSummary.getId(),
                    taskSummary.getName(),
                    taskSummary.getStatus(),
                    null,
                    nombrePaciente,
                    apellidosPaciente
            );
        }
    }

    // Comprobar que paciente ha confirmado revision, permisos vista
    private boolean pacienteHaConfirmadoRevisionFisicaParaTarea(Long taskId) {
        try {
            TaskInstance taskInstance = tasks.getTaskInstance(containerId, taskId);
            Long processInstanceId = taskInstance.getProcessInstanceId();

            return emailEnvioService.pacienteHaConfirmadoRevisionFisica(processInstanceId);

        } catch (Exception e) {
            return false;
        }
    }
}
