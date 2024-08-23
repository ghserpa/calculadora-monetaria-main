package br.gov.ce.tce.calculadoraMonetaria.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@Component
public class Requisicao {
    private LocalDate dataInicial;
    private String mesDeRecolhimento;
    private double valorNominal;

    public long calculaMesesDeAtraso(LocalDate dataInicial) {
       LocalDate dataAtual = LocalDate.now();

        return ChronoUnit.MONTHS.between(dataInicial, dataAtual);
    }

}
