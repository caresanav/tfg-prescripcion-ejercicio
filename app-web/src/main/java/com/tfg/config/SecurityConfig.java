package com.tfg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Para no tener que tocar ahora todos tus formularios Thymeleaf con CSRF.
            // En una versión más avanzada se podría activar y añadir el token a cada formulario.
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth

                // Recursos públicos
                .requestMatchers("/css/**").permitAll()
                .requestMatchers("/login").permitAll()

                // Enlaces públicos del paciente
                .requestMatchers("/respuesta/**").permitAll()
                .requestMatchers("/entrenamiento/resumen/**").permitAll()

                // Médico
                .requestMatchers("/medico", "/start").hasAnyRole("MEDICO", "ADMIN")

                // Fisio 1
                .requestMatchers("/fisio1").hasAnyRole("FISIO1", "ADMIN")

                // Fisio 2
                .requestMatchers("/fisio2").hasAnyRole("FISIO2", "ADMIN")

                // Tareas: las dejamos para usuarios autenticados.
                // Luego el actor real se saca del login, no de la URL.
                .requestMatchers("/tasks", "/tasks/**").hasAnyRole("MEDICO", "FISIO1", "FISIO2", "ADMIN")

                // Todo lo demás requiere login
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
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {

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

        return new InMemoryUserDetailsManager(medico, fisio1, fisio2, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}