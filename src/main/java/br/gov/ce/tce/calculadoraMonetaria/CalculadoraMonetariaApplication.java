package br.gov.ce.tce.calculadoraMonetaria;

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

    public double calculaFatorAcumulado(LocalDate data) {

        LocalDate dataEstabelecida = LocalDate.of(2015, 10, 1);

        if (data.isEqual(dataEstabelecida)) {
            return 1.380045;
        }

        String periodo = calculaPeriodo(data);

        return calculaFatorAcumulado(data.minusMonths(1)) * (1 + Double.parseDouble(
            String.valueOf(apiServiceClient.getInpcData(periodo))));
    }

    public double calculaValorAtualizado(double valorInicial) {
        valorInicial = requisicao.getValorNominal();

        LocalDate dataInicial = requisicao.getDataInicial();
        LocalDate dataAtual = LocalDate.now();

        double fatorAcumuladoInicial = calculaFatorAcumulado(dataInicial);

        double fatorAcumuladoAtualizado = calculaFatorAcumulado(dataAtual);

        double fatorDeAtualizacao = fatorAcumuladoAtualizado / fatorAcumuladoInicial;

        double valorAtualizado = valorInicial * fatorDeAtualizacao;

        return valorAtualizado;

    }

    public double calculaValorFinal(double valorAtualizado, double taxaDeJuros,
        LocalDate dataInicial) {

        dataInicial = requisicao.getDataInicial();

        long mesesCorridos = requisicao.calculaMesesDeAtraso(dataInicial);

        double valorComJuros = valorAtualizado * taxaDeJuros * mesesCorridos;

        double valorFinal = valorAtualizado + valorComJuros;

        return valorFinal;
    }

    public String calculaPeriodo(LocalDate data) {
        return data.minusMonths(1)
            .format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
}
