package com.marina.turismo_cliente.service;

import com.marina.turismo_cliente.dto.ClienteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Encapsula todas as chamadas HTTP para a API REST de clientes do projeto sistema-turismo
// Repassa as operações via RestClient
@Service
public class ClienteApiService {

    private final RestClient restClient;

    public ClienteApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public List<ClienteDTO> listarTodos() {
        return restClient.get()
                .uri("/clientes")
                .retrieve()
                .body(new org.springframework.core.ParameterizedTypeReference<List<ClienteDTO>>() {});
    }

    public Optional<ClienteDTO> buscarPorId(Long id) {
        try {
            return Optional.ofNullable(
                    restClient.get()
                            .uri("/clientes/{id}", id)
                            .retrieve()
                            .body(ClienteDTO.class));
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            throw traduzirErro(e);
        }
    }

    public ClienteDTO salvar(ClienteDTO cliente) {
        try {
            return restClient.post()
                    .uri("/clientes")
                    .body(cliente)
                    .retrieve()
                    .body(ClienteDTO.class);
        } catch (RestClientResponseException e) {
            throw traduzirErro(e);
        }
    }

    public ClienteDTO atualizar(Long id, ClienteDTO cliente) {
        try {
            return restClient.put()
                    .uri("/clientes/{id}", id)
                    .body(cliente)
                    .retrieve()
                    .body(ClienteDTO.class);
        } catch (RestClientResponseException e) {
            throw traduzirErro(e);
        }
    }

    public void excluir(Long id) {
        try {
            restClient.delete()
                    .uri("/clientes/{id}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientResponseException e) {
            throw traduzirErro(e);
        }
    }

    // Extrai a mensagem de erro amigável enviada pelo GlobalExceptionHandler da API 
    private IllegalArgumentException traduzirErro(RestClientResponseException e) {
        try {
            Map<?, ?> corpo = e.getResponseBodyAs(Map.class);
            Object mensagem = corpo != null ? corpo.get("mensagem") : null;
            if (mensagem != null) {
                return new IllegalArgumentException(mensagem.toString());
            }
        } catch (Exception ignorada) {
        }
        return new IllegalArgumentException("Não foi possível completar a operação na API.");
    }
}
