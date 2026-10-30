package br.com.fiap.soulup.dto;

import br.com.fiap.soulup.model.Usuario;

/**
 * Usuário devolvido pela API - nunca inclui a senha e o CPF sai mascarado
 * (ex.: ***.982.247-**), para não expor o dado pessoal completo (LGPD).
 */
public record UsuarioResponse(Long id, String name, String userName, String cpf, int viewMinutes) {

    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(usuario.getId(), usuario.getName(), usuario.getUserName(),
                mascararCpf(usuario.getCpf()), usuario.getViewMinutes());
    }

    /** Mostra só os 6 dígitos do meio: 52998224725 -> ***.982.247-** */
    static String mascararCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return "***.***.***-**";
        }
        return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
    }
}
