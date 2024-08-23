package br.gov.ce.tce.calculadoraMonetaria.service.dto;

import java.util.List;

public record Resultados(
    List<Object> classificacoes,
    List<Serie> series
) {

}
