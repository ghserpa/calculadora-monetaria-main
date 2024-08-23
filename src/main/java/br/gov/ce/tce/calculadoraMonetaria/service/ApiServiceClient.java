package br.gov.ce.tce.calculadoraMonetaria.service;

import br.gov.ce.tce.calculadoraMonetaria.service.dto.Variavel;
import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ApiServiceClient {

    @GetExchange("/api/v3/agregados/1736/periodos/{periodo}/variaveis/44?localidades=N1")
    List<Variavel> getInpcData(@PathVariable String periodo);
}