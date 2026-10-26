package br.com.fiap.soulup.dto;

/**
 * Dados de uma viagem de ônibus. O co2Economizado é calculado pela API.
 */
public record CO2EconomiaRequest(Long usuarioId, Double distanciaKm, Integer passageiroOnibus) {
}
