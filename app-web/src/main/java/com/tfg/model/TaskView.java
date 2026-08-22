package com.tfg.model;

public class TaskView {

    private Long id;
    private String name;
    private String status;
    private Long processInstanceId;

    private String nombrePaciente;
    private String apellidosPaciente;

    public TaskView() {
    }

    public TaskView(Long id, String name, String status, Long processInstanceId,
                    String nombrePaciente, String apellidosPaciente) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.processInstanceId = processInstanceId;
        this.nombrePaciente = nombrePaciente;
        this.apellidosPaciente = apellidosPaciente;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Long getProcessInstanceId() {
        return processInstanceId;
    }

    public String getNombrePaciente() {
        return nombrePaciente;
    }

    public String getApellidosPaciente() {
        return apellidosPaciente;
    }
}