package br.com.fiap.soulup.dto;

/**
 * Redefinição de senha: o usuário confirma CPF e nome cadastrados.
 */
public record RecuperarSenhaRequest(String cpf, String name, String newPassword) {
}
