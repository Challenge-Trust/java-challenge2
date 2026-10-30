package br.com.fiap.soulup.dto;

/**
 * Dados para cadastrar (POST) ou atualizar (PUT) um usuário.
 * No PUT o CPF é opcional: se vier vazio, o CPF atual é mantido.
 * No PUT a senha é opcional: se vier vazia, a senha atual é mantida.
 */
public record UsuarioRequest(String name, String userName, String cpf, String password, Integer viewMinutes) {
}
