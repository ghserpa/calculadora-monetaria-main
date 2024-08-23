package br.gov.ce.tce.calculadoraMonetaria.service.dto;

import java.util.Map;

public record Serie(
    Localidade localidade,
    Map<String, String> serie
) {

}
