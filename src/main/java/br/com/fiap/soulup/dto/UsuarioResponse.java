package br.com.fiap.soulup.dto;

import br.com.fiap.soulup.model.Usuario;

/**
 * Usuário devolvido pela API - nunca inclui a senha.
 */
public record UsuarioResponse(Long id, String name, String userName, String cpf, int viewMinutes) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getName(), usuario.getUserName(),
                usuario.getCpf(), usuario.getViewMinutes());
    }
}
