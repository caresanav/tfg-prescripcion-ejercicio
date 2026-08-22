package com.tfg.client;

import com.tfg.client.dto.CrearResumenRequest;
import com.tfg.client.dto.ResumenResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ResumenApiClient {

    private final RestClient restClient;

    public ResumenApiClient(
            RestClient.Builder restClientBuilder,
            @Value("${resumen-api.url}") String resumenApiUrl) {

        this.restClient = restClientBuilder
                .baseUrl(resumenApiUrl)
                .build();
    }

    /*
     * Envía un POST a resumen-api para guardar el resumen.
     */
    public ResumenResponse crear(
            CrearResumenRequest request) {

        return restClient
                .post()
                .uri("/api/resumenes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(ResumenResponse.class);
    }

    /*
     * Envía un GET a resumen-api para consultar por token.
     */
    public ResumenResponse buscarPorToken(String token) {

        return restClient
                .get()
                .uri("/api/resumenes/{token}", token)
                .retrieve()
                .body(ResumenResponse.class);
    }
}