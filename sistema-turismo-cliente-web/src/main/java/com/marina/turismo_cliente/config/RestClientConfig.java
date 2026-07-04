package com.marina.turismo_cliente.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // Endereço do projeto base sistema-turismo, que expõe a API REST de clientes
    @Value("${api.sistema-turismo.base-url}")
    private String apiBaseUrl;

    @Bean
    RestClient restClient() {
        return RestClient.builder()
                .baseUrl(apiBaseUrl)
                .build();
    }
}
