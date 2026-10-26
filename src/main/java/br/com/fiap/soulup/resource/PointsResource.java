package br.com.fiap.soulup.resource;

import br.com.fiap.soulup.bo.PointsBO;
import br.com.fiap.soulup.dto.PointsRequest;
import br.com.fiap.soulup.model.Points;
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
@RequestMapping("/api/pontos")
public class PointsResource {

    private final PointsBO pointsBO;

    public PointsResource(PointsBO pointsBO) {
        this.pointsBO = pointsBO;
    }

    @GetMapping
    public List<Points> listar() {
        return pointsBO.listar();
    }

    @GetMapping("/{id}")
    public Points buscar(@PathVariable Long id) {
        return pointsBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Points> cadastrar(@RequestBody PointsRequest dados) {
        Points criado = pointsBO.cadastrar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public Points atualizar(@PathVariable Long id, @RequestBody PointsRequest dados) {
        return pointsBO.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        pointsBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
