package br.com.fiap.soulup.bo;

import br.com.fiap.soulup.dao.PointsDAO;
import br.com.fiap.soulup.dto.PointsRequest;
import br.com.fiap.soulup.exception.ConflitoException;
import br.com.fiap.soulup.exception.RecursoNaoEncontradoException;
import br.com.fiap.soulup.exception.RegraNegocioException;
import br.com.fiap.soulup.model.Points;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Regras do saldo de pontos (1 registro por usuário, nunca negativo).
 */
@Component
public class PointsBO {

    private final PointsDAO pointsDAO;
    private final UsuarioBO usuarioBO;

    public PointsBO(PointsDAO pointsDAO, UsuarioBO usuarioBO) {
        this.pointsDAO = pointsDAO;
        this.usuarioBO = usuarioBO;
    }

    public List<Points> listar() {
        return pointsDAO.findAll();
    }

    public Points buscarPorId(Long id) {
        return pointsDAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Registro de pontos " + id + " não encontrado."));
    }

    public Points buscarPorUsuario(Long usuarioId) {
        usuarioBO.buscarEntidade(usuarioId);
        return pointsDAO.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "O usuário " + usuarioId + " ainda não possui pontos."));
    }

    public Points cadastrar(PointsRequest dados) {
        validar(dados);
        if (pointsDAO.findByUsuarioId(dados.usuarioId()).isPresent()) {
            throw new ConflitoException("O usuário " + dados.usuarioId() + " já possui um registro de pontos.");
        }
        return pointsDAO.insert(new Points(dados.usuarioId(), dados.earnedPoints()));
    }

    public Points atualizar(Long id, PointsRequest dados) {
        Points pontos = buscarPorId(id);
        validar(dados);
        pointsDAO.findByUsuarioId(dados.usuarioId())
                .filter(outro -> !outro.getId().equals(id))
                .ifPresent(outro -> {
                    throw new ConflitoException("O usuário " + dados.usuarioId() + " já possui um registro de pontos.");
                });

        pontos.setUsuarioId(dados.usuarioId());
        pontos.setEarnedPoints(dados.earnedPoints());
        pontos.setDataAtualizacao(LocalDateTime.now());
        pointsDAO.update(pontos);
        return pontos;
    }

    public void excluir(Long id) {
        if (!pointsDAO.delete(id)) {
            throw new RecursoNaoEncontradoException("Registro de pontos " + id + " não encontrado.");
        }
    }

    private void validar(PointsRequest dados) {
        if (dados == null) {
            throw new RegraNegocioException("Corpo da requisição vazio.");
        }
        if (dados.earnedPoints() == null || dados.earnedPoints() < 0) {
            throw new RegraNegocioException("Informe earnedPoints maior ou igual a 0.");
        }
        usuarioBO.buscarEntidade(dados.usuarioId());
    }
}
