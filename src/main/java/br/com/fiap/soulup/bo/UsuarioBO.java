package br.com.fiap.soulup.bo;

import br.com.fiap.soulup.dao.UsuarioDAO;
import br.com.fiap.soulup.dto.LoginRequest;
import br.com.fiap.soulup.dto.RecuperarSenhaRequest;
import br.com.fiap.soulup.dto.UsuarioRequest;
import br.com.fiap.soulup.dto.UsuarioResponse;
import br.com.fiap.soulup.exception.ConflitoException;
import br.com.fiap.soulup.exception.CredenciaisInvalidasException;
import br.com.fiap.soulup.exception.RecursoNaoEncontradoException;
import br.com.fiap.soulup.exception.RegraNegocioException;
import br.com.fiap.soulup.model.Usuario;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Regras de negócio e validações de usuário: cadastro, edição, login e recuperação de senha.
 */
@Component
public class UsuarioBO {

    private static final int NOME_MAX = 100;
    private static final int USERNAME_MAX = 30;
    private static final int SENHA_MIN = 6;
    private static final int SENHA_MAX = 30;

    private final UsuarioDAO usuarioDAO;
    private final ConversaoBO conversaoBO;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UsuarioBO(UsuarioDAO usuarioDAO, ConversaoBO conversaoBO) {
        this.usuarioDAO = usuarioDAO;
        this.conversaoBO = conversaoBO;
    }

    public List<UsuarioResponse> listar() {
        return usuarioDAO.findAll().stream().map(UsuarioResponse::from).toList();
    }

    public UsuarioResponse buscarPorId(Long id) {
        return UsuarioResponse.from(buscarEntidade(id));
    }

    /** Usado pelos outros BOs para garantir que o usuário existe. */
    public Usuario buscarEntidade(Long id) {
        if (id == null) {
            throw new RegraNegocioException("Informe o usuarioId.");
        }
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário " + id + " não encontrado."));
    }

    public UsuarioResponse cadastrar(UsuarioRequest dados) {
        validar(dados, true);
        String cpf = somenteDigitos(dados.cpf());
        if (usuarioDAO.findByCpf(cpf).isPresent()) {
            throw new ConflitoException("Já existe um usuário cadastrado com esse CPF.");
        }

        Usuario usuario = new Usuario(dados.name().trim(), textoOuNulo(dados.userName()),
                passwordEncoder.encode(dados.password()), cpf, minutos(dados));
        usuarioDAO.insert(usuario);
        conversaoBO.converter(usuario);
        return UsuarioResponse.from(usuario);
    }

    public UsuarioResponse atualizar(Long id, UsuarioRequest dados) {
        Usuario usuario = buscarEntidade(id);
        validar(dados, false);
        String cpf = somenteDigitos(dados.cpf());
        usuarioDAO.findByCpf(cpf)
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new ConflitoException("Já existe outro usuário cadastrado com esse CPF.");
                });

        usuario.setName(dados.name().trim());
        usuario.setUserName(textoOuNulo(dados.userName()));
        usuario.setCpf(cpf);
        usuario.setViewMinutes(minutos(dados));
        if (!vazio(dados.password())) {
            usuario.setPassword(passwordEncoder.encode(dados.password()));
        }
        usuarioDAO.update(usuario);
        conversaoBO.converter(usuario);
        return UsuarioResponse.from(usuario);
    }

    public void excluir(Long id) {
        if (!usuarioDAO.delete(id)) {
            throw new RecursoNaoEncontradoException("Usuário " + id + " não encontrado.");
        }
    }

    public UsuarioResponse autenticar(LoginRequest login) {
        if (login == null || vazio(login.cpf()) || vazio(login.password())) {
            throw new RegraNegocioException("Informe CPF e senha.");
        }
        return usuarioDAO.findByCpf(somenteDigitos(login.cpf()))
                .filter(usuario -> passwordEncoder.matches(login.password(), usuario.getPassword()))
                .map(UsuarioResponse::from)
                .orElseThrow(CredenciaisInvalidasException::new);
    }

    public void redefinirSenha(RecuperarSenhaRequest dados) {
        if (dados == null || vazio(dados.cpf()) || vazio(dados.name())) {
            throw new RegraNegocioException("Informe o CPF e o nome cadastrados.");
        }
        validarSenha(dados.newPassword());

        Usuario usuario = usuarioDAO.findByCpf(somenteDigitos(dados.cpf()))
                .filter(u -> u.getName().trim().equalsIgnoreCase(dados.name().trim()))
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Nenhum usuário encontrado com esse CPF e nome."));
        usuario.setPassword(passwordEncoder.encode(dados.newPassword()));
        usuarioDAO.update(usuario);
    }

    private void validar(UsuarioRequest dados, boolean senhaObrigatoria) {
        if (dados == null) {
            throw new RegraNegocioException("Corpo da requisição vazio.");
        }
        if (vazio(dados.name())) {
            throw new RegraNegocioException("O nome é obrigatório.");
        }
        if (dados.name().trim().length() > NOME_MAX) {
            throw new RegraNegocioException("O nome deve ter no máximo " + NOME_MAX + " caracteres.");
        }
        if (dados.userName() != null && dados.userName().trim().length() > USERNAME_MAX) {
            throw new RegraNegocioException("O nome de usuário deve ter no máximo " + USERNAME_MAX + " caracteres.");
        }
        if (!cpfValido(dados.cpf())) {
            throw new RegraNegocioException("CPF inválido.");
        }
        if (senhaObrigatoria || !vazio(dados.password())) {
            validarSenha(dados.password());
        }
        if (dados.viewMinutes() != null && dados.viewMinutes() < 0) {
            throw new RegraNegocioException("Os minutos assistidos não podem ser negativos.");
        }
    }

    private void validarSenha(String senha) {
        if (vazio(senha) || senha.length() < SENHA_MIN || senha.length() > SENHA_MAX) {
            throw new RegraNegocioException(
                    "A senha deve ter entre " + SENHA_MIN + " e " + SENHA_MAX + " caracteres.");
        }
    }

    /** CPF com 11 dígitos, não repetidos, e dígitos verificadores corretos. */
    static boolean cpfValido(String valor) {
        String cpf = somenteDigitos(valor);
        if (cpf.length() != 11 || cpf.chars().distinct().count() == 1) {
            return false;
        }
        return digitoVerificador(cpf, 9) == cpf.charAt(9) - '0'
                && digitoVerificador(cpf, 10) == cpf.charAt(10) - '0';
    }

    private static int digitoVerificador(String cpf, int tamanho) {
        int soma = 0;
        for (int i = 0; i < tamanho; i++) {
            soma += (cpf.charAt(i) - '0') * (tamanho + 1 - i);
        }
        int resto = (soma * 10) % 11;
        return resto == 10 ? 0 : resto;
    }

    private static String somenteDigitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    private static int minutos(UsuarioRequest dados) {
        return dados.viewMinutes() == null ? 0 : dados.viewMinutes();
    }

    private static String textoOuNulo(String valor) {
        return vazio(valor) ? null : valor.trim();
    }

    private static boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
