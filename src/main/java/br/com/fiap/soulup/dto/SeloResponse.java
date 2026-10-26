package br.com.fiap.soulup.dto;

/**
 * Selo sustentável do usuário com o total de CO2 economizado (em gramas).
 */
public record SeloResponse(Long usuarioId, double totalCo2Economizado, String selo) {
}
