package com.tfg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Recursos públicos
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/login").permitAll()

                // Enlaces públicos del paciente
                .requestMatchers("/respuesta/**").permitAll()
                .requestMatchers(
                        "/entrenamiento/resumen/**")
                .permitAll()

                // Páginas principales de cada perfil
                .requestMatchers(
                        "/medico",
                        "/start")
                .hasAnyRole("MEDICO", "ADMIN")

                .requestMatchers("/fisio1")
                .hasAnyRole("FISIO1", "ADMIN")

                .requestMatchers("/fisio2")
                .hasAnyRole("FISIO2", "ADMIN")

                // Apertura de tareas desde el área médica
                .requestMatchers(
                        HttpMethod.GET,
                        "/medico/tasks/**")
                .hasRole("MEDICO")

                // Apertura de tareas desde el área de Fisio 1
                .requestMatchers(
                        HttpMethod.GET,
                        "/fisio1/tasks/**")
                .hasRole("FISIO1")

                // Apertura de tareas desde el área de Fisio 2
                .requestMatchers(
                        HttpMethod.GET,
                        "/fisio2/tasks/**")
                .hasRole("FISIO2")

                // Apertura de tareas desde administración
                .requestMatchers(
                        HttpMethod.GET,
                        "/admin/tasks/**")
                .hasRole("ADMIN")

                // Operaciones realizadas por el médico
                .requestMatchers(
                        HttpMethod.POST,
                        "/tasks/*/medica",
                        "/tasks/*/seguimiento-medico")
                .hasRole("MEDICO")

                // Operaciones realizadas por Fisio 1
                .requestMatchers(
                        HttpMethod.POST,
                        "/tasks/*/fisica",
                        "/tasks/*/prescripcion",
                        "/tasks/*/seguimiento-fisio")
                .hasRole("FISIO1")

                // Operaciones realizadas por Fisio 2
                .requestMatchers(
                        HttpMethod.POST,
                        "/tasks/*/entrenamiento",
                        "/tasks/*/subir-entrenamiento")
                .hasRole("FISIO2")

                // Listado general de tareas
                .requestMatchers(
                        "/tasks",
                        "/tasks/**")
                .hasAnyRole(
                        "MEDICO",
                        "FISIO1",
                        "FISIO2",
                        "ADMIN")

                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(
            PasswordEncoder passwordEncoder) {

        UserDetails medico = User.builder()
                .username("medico")
                .password(passwordEncoder.encode("1234"))
                .roles("MEDICO")
                .build();

        UserDetails fisio1 = User.builder()
                .username("fisio1")
                .password(passwordEncoder.encode("1234"))
                .roles("FISIO1")
                .build();

        UserDetails fisio2 = User.builder()
                .username("fisio2")
                .password(passwordEncoder.encode("1234"))
                .roles("FISIO2")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("1234"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(
                medico,
                fisio1,
                fisio2,
                admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}