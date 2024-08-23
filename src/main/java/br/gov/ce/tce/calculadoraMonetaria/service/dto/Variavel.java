package br.gov.ce.tce.calculadoraMonetaria.service.dto;

import java.util.List;

public record Variavel(
    String id,
    String variavel,
    String unidade,
    List<Resultados> resultados
) {

}
