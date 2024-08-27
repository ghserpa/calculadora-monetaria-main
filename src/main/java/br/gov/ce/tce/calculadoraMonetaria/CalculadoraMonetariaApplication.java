package br.gov.ce.tce.calculadoraMonetaria;

import br.gov.ce.tce.calculadoraMonetaria.controller.ApiController;
import br.gov.ce.tce.calculadoraMonetaria.model.Requisicao;
import br.gov.ce.tce.calculadoraMonetaria.service.ApiServiceClient;
import java.time.format.DateTimeFormatter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
@Getter
public class CalculadoraMonetariaApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalculadoraMonetariaApplication.class, args);
    }

    private final double taxaDeJuros = 0.01;

    private Requisicao requisicao = new Requisicao();

    @Autowired
    private ApiServiceClient apiServiceClient;

    public double calculaValorAtualizado(double fatorAcumuladoInicial, double fatorAcumuladoAtualizado, LocalDate dataInicial, LocalDate dataDeRecolhimento, double valorInicial) {

        double fatorDeAtualizacao = fatorAcumuladoAtualizado / fatorAcumuladoInicial;

        return valorInicial * fatorDeAtualizacao;

    }

    public double calculaJuros(double valorAtualizado, double taxaDeJuros, LocalDate dataInicial, LocalDate dataDeRecolhimento) {

        long mesesCorridos = requisicao.calculaMesesDeAtraso(dataInicial, dataDeRecolhimento);

        return valorAtualizado * taxaDeJuros * mesesCorridos;
    }

    public double calculaValorFinal(double valorAtualizado, double juros) {

        return valorAtualizado + juros;
    }

    public String calculaPeriodo(LocalDate data) {
        return data.minusMonths(1)
            .format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
