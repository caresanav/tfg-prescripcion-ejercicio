package com.tfg.config;

//Creada por Carmen Esau
// Puente entre Spring Boot y KIE Server

import org.kie.server.client.*;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

//Cuando necesites hablar con jBPM, usa esta URL, este usuario y esta contraseña

// Definimos configuraciones
// Spring lee las clases Configuration al arrancar y crea objetos
// a partir de los métodos marcados como Bean
@Configuration
public class KieClientConfig {
    // Lee de application.properties
    @Value("${kieserver.url}")  //Donde está KIE Server
    String url;
    @Value("${kieserver.user}")
    String user;
    @Value("${kieserver.pwd}")
    String pwd;

    // Creamos 'herramientas' para que el resto del código pueda hacer
    // cosas con JBPM

    // Creamos el cliente ppal
    @Bean
    public KieServicesClient kieClient() {
        KieServicesConfiguration conf = KieServicesFactory.newRestConfiguration(url, user, pwd);
        conf.setMarshallingFormat(MarshallingFormat.JSON);
        return KieServicesFactory.newKieServicesClient(conf);
    }

    // Clientes especificos

    // Iniciar procesos
    @Bean
    public ProcessServicesClient processes(KieServicesClient c) {
        return c.getServicesClient(ProcessServicesClient.class);
    }

    // Reclamar / Iniciar / Completar tareas
    @Bean
    public UserTaskServicesClient tasks(KieServicesClient c) {
        return c.getServicesClient(UserTaskServicesClient.class);
    }

    // Listar tareas del usuario
    @Bean
    public QueryServicesClient queries(KieServicesClient c) {
        return c.getServicesClient(QueryServicesClient.class);
    }
}
