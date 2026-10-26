package br.com.fiap.soulup.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Verificação de saúde usada pelo Render/Railway e para "acordar" a API no plano gratuito.
 */
@RestController
public class HealthResource {

    @GetMapping("/health")
    public Map<String, Object> health() {
        return Map.of("status", "UP", "timestamp", LocalDateTime.now());
    }
}
