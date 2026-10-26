package br.com.fiap.soulup.resource;

import br.com.fiap.soulup.bo.CO2EconomiaBO;
import br.com.fiap.soulup.bo.PointsBO;
import br.com.fiap.soulup.bo.TicketBO;
import br.com.fiap.soulup.bo.UsuarioBO;
import br.com.fiap.soulup.dto.SeloResponse;
import br.com.fiap.soulup.dto.UsuarioRequest;
import br.com.fiap.soulup.dto.UsuarioResponse;
import br.com.fiap.soulup.model.CO2Economia;
import br.com.fiap.soulup.model.Points;
import br.com.fiap.soulup.model.Ticket;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioResource {

    private final UsuarioBO usuarioBO;
    private final PointsBO pointsBO;
    private final TicketBO ticketBO;
    private final CO2EconomiaBO co2EconomiaBO;

    public UsuarioResource(UsuarioBO usuarioBO, PointsBO pointsBO, TicketBO ticketBO, CO2EconomiaBO co2EconomiaBO) {
        this.usuarioBO = usuarioBO;
        this.pointsBO = pointsBO;
        this.ticketBO = ticketBO;
        this.co2EconomiaBO = co2EconomiaBO;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioBO.listar();
    }

    @GetMapping("/{id}")
    public UsuarioResponse buscar(@PathVariable Long id) {
        return usuarioBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@RequestBody UsuarioRequest dados) {
        UsuarioResponse criado = usuarioBO.cadastrar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.id()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public UsuarioResponse atualizar(@PathVariable Long id, @RequestBody UsuarioRequest dados) {
        return usuarioBO.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/pontos")
    public Points pontos(@PathVariable Long id) {
        return pointsBO.buscarPorUsuario(id);
    }

    @GetMapping("/{id}/tickets")
    public List<Ticket> tickets(@PathVariable Long id) {
        return ticketBO.listarPorUsuario(id);
    }

    @GetMapping("/{id}/co2")
    public List<CO2Economia> co2(@PathVariable Long id) {
        return co2EconomiaBO.listarPorUsuario(id);
    }

    @GetMapping("/{id}/selo")
    public SeloResponse selo(@PathVariable Long id) {
        return co2EconomiaBO.seloDoUsuario(id);
    }
}
