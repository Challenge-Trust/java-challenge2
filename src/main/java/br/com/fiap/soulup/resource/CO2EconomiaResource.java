package br.com.fiap.soulup.resource;

import br.com.fiap.soulup.bo.CO2EconomiaBO;
import br.com.fiap.soulup.dto.CO2EconomiaRequest;
import br.com.fiap.soulup.model.CO2Economia;
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
@RequestMapping("/api/co2")
public class CO2EconomiaResource {

    private final CO2EconomiaBO co2EconomiaBO;

    public CO2EconomiaResource(CO2EconomiaBO co2EconomiaBO) {
        this.co2EconomiaBO = co2EconomiaBO;
    }

    @GetMapping
    public List<CO2Economia> listar() {
        return co2EconomiaBO.listar();
    }

    @GetMapping("/{id}")
    public CO2Economia buscar(@PathVariable Long id) {
        return co2EconomiaBO.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<CO2Economia> cadastrar(@RequestBody CO2EconomiaRequest dados) {
        CO2Economia criado = co2EconomiaBO.cadastrar(dados);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(criado.getId()).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @PutMapping("/{id}")
    public CO2Economia atualizar(@PathVariable Long id, @RequestBody CO2EconomiaRequest dados) {
        return co2EconomiaBO.atualizar(id, dados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        co2EconomiaBO.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
