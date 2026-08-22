package com.tfg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Escanea los paquetes que empiezan por com.tfg
// Busca los @Controller, @Service...

@SpringBootApplication(scanBasePackages = "com.tfg")
public class AppWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(AppWebApplication.class, args);
	}

}
