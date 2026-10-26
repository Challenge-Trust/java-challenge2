package br.com.fiap.soulup.bo;

import br.com.fiap.soulup.dao.CO2EconomiaDAO;
import br.com.fiap.soulup.dto.CO2EconomiaRequest;
import br.com.fiap.soulup.dto.SeloResponse;
import br.com.fiap.soulup.exception.RecursoNaoEncontradoException;
import br.com.fiap.soulup.exception.RegraNegocioException;
import br.com.fiap.soulup.model.CO2Economia;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Cálculo do CO2 economizado ao trocar o carro pelo ônibus e selo sustentável do usuário.
 */
@Component
public class CO2EconomiaBO {

    /** Emissão média de um carro, em gramas de CO2 por km. */
    static final double EMISSAO_CARRO_G_KM = 192.0;
    /** Emissão média de um ônibus, em gramas de CO2 por km (dividida entre os passageiros). */
    static final double EMISSAO_ONIBUS_G_KM = 1050.0;

    private final CO2EconomiaDAO co2DAO;
    private final UsuarioBO usuarioBO;

    public CO2EconomiaBO(CO2EconomiaDAO co2DAO, UsuarioBO usuarioBO) {
        this.co2DAO = co2DAO;
        this.usuarioBO = usuarioBO;
    }

    /** Gramas de CO2 que deixam de ser emitidas ao fazer o trajeto de ônibus em vez de carro. */
    public double calcularEconomia(double distanciaKm, int passageirosOnibus) {
        double emissaoPorPassageiro = EMISSAO_ONIBUS_G_KM / passageirosOnibus;
        double economiaPorKm = EMISSAO_CARRO_G_KM - emissaoPorPassageiro;
        return Math.round(economiaPorKm * distanciaKm * 100) / 100.0;
    }

    /** Selo pelo total economizado: Ouro a partir de 100 kg, Prata a partir de 50 kg. */
    public String calcularSelo(double totalGramas) {
        double totalKg = totalGramas / 1000;
        if (totalKg >= 100) {
            return "Ouro";
        } else if (totalKg >= 50) {
            return "Prata";
        }
        return "Bronze";
    }

    public SeloResponse seloDoUsuario(Long usuarioId) {
        double total = listarPorUsuario(usuarioId).stream()
                .mapToDouble(CO2Economia::getCo2Economizado)
                .sum();
        return new SeloResponse(usuarioId, total, calcularSelo(total));
    }

    public List<CO2Economia> listar() {
        return co2DAO.findAll();
    }

    public List<CO2Economia> listarPorUsuario(Long usuarioId) {
        usuarioBO.buscarEntidade(usuarioId);
        return co2DAO.findByUsuarioId(usuarioId);
    }

    public CO2Economia buscarPorId(Long id) {
        return co2DAO.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cálculo de CO2 " + id + " não encontrado."));
    }

    public CO2Economia cadastrar(CO2EconomiaRequest dados) {
        validar(dados);
        double economia = calcularEconomia(dados.distanciaKm(), dados.passageiroOnibus());
        return co2DAO.insert(new CO2Economia(dados.usuarioId(), dados.distanciaKm(), economia,
                dados.passageiroOnibus()));
    }

    public CO2Economia atualizar(Long id, CO2EconomiaRequest dados) {
        CO2Economia co2 = buscarPorId(id);
        validar(dados);
        co2.setUsuarioId(dados.usuarioId());
        co2.setDistanciaKm(dados.distanciaKm());
        co2.setPassageiroOnibus(dados.passageiroOnibus());
        co2.setCo2Economizado(calcularEconomia(dados.distanciaKm(), dados.passageiroOnibus()));
        co2.setDataCalculo(LocalDateTime.now());
        co2DAO.update(co2);
        return co2;
    }

    public void excluir(Long id) {
        if (!co2DAO.delete(id)) {
            throw new RecursoNaoEncontradoException("Cálculo de CO2 " + id + " não encontrado.");
        }
    }

    private void validar(CO2EconomiaRequest dados) {
        if (dados == null) {
            throw new RegraNegocioException("Corpo da requisição vazio.");
        }
        if (dados.distanciaKm() == null || dados.distanciaKm() <= 0) {
            throw new RegraNegocioException("Informe a distância em km (maior que 0).");
        }
        if (dados.passageiroOnibus() == null || dados.passageiroOnibus() <= 0) {
            throw new RegraNegocioException("Quantidade de passageiros do ônibus inválida.");
        }
        usuarioBO.buscarEntidade(dados.usuarioId());
    }
}
