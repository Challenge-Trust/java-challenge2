package br.com.fiap.soulup.resource;

import br.com.fiap.soulup.bo.UsuarioBO;
import br.com.fiap.soulup.dto.LoginRequest;
import br.com.fiap.soulup.dto.MensagemResponse;
import br.com.fiap.soulup.dto.RecuperarSenhaRequest;
import br.com.fiap.soulup.dto.UsuarioResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    private final UsuarioBO usuarioBO;

    public AuthResource(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    /** Login por CPF e senha: devolve os dados do usuário (sem a senha) ou 401. */
    @PostMapping("/login")
    public UsuarioResponse login(@RequestBody LoginRequest login) {
        return usuarioBO.autenticar(login);
    }

    @PostMapping("/recuperar-senha")
    public MensagemResponse recuperarSenha(@RequestBody RecuperarSenhaRequest dados) {
        usuarioBO.redefinirSenha(dados);
        return new MensagemResponse("Senha redefinida com sucesso.");
    }
}
