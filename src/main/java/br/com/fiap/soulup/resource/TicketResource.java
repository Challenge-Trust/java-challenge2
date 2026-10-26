package br.com.fiap.soulup.resource;

import br.com.fiap.soulup.bo.TicketBO;
import br.com.fiap.soulup.dto.TicketRequest;
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
@RequestMapping("/api/tickets")
public class TicketResource {

    private final TicketBO ticketBO;

    public TicketResource(TicketBO ticketBO) {
        this.ticketBO = ticketBO;
    }

    @GetMapping
    public List<Ticket> listar() {
        return ticketBO.listar();
    }

    @GetMapping("/{id}")
    public Ticket buscar(@PathVariable Long id) {
        return ticketBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Ticket> cadastrar(@RequestBody TicketRequest dados) {
        Ticket criado = ticketBO.cadastrar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public Ticket atualizar(@PathVariable Long id, @RequestBody TicketRequest dados) {
        return ticketBO.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        ticketBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
