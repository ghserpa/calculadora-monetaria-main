package br.gov.ce.tce.calculadoraMonetaria.controller;

import br.gov.ce.tce.calculadoraMonetaria.service.ApiServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ApiController {

    private final ApiServiceClient apiServiceClient;

    @GetMapping("/inpc")
    public String getInpcData(@RequestParam String periodo) {
        // Chama o serviço e obtém os dados do INPC
        String inpc = apiServiceClient.getInpcData(periodo).getFirst()
            .resultados().getFirst()
            .series().getFirst()
            .serie().get(periodo);

        return "inpc = " + inpc; // Retorna o nome do template HTML
    }

    @GetMapping("/") // Mapeia a rota inicial para o formulário
    public String index() {
        return "index"; // Retorna a página inicial
    }
}

