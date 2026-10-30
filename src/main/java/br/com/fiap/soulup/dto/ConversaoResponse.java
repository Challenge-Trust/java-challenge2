package br.com.fiap.soulup.dto;

/**
 * Resultado de uma conversão: o que foi ganho agora e como ficaram os saldos do usuário.
 */
public record ConversaoResponse(Long usuarioId, int pontosGanhos, int ticketsEmitidos,
                                int viewMinutes, int earnedPoints) {
}
