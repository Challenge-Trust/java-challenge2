package br.com.fiap.soulup.dto;

import java.time.LocalDateTime;

/**
 * Corpo padrão de erro devolvido por toda a API.
 */
public record ErroResponse(int status, String erro, String mensagem, LocalDateTime timestamp) {

    public static ErroResponse of(int status, String erro, String mensagem) {
        return new ErroResponse(status, erro, mensagem, LocalDateTime.now());
    }
}
