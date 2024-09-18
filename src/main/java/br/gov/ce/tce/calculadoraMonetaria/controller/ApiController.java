package br.gov.ce.tce.calculadoraMonetaria.controller;

import br.gov.ce.tce.calculadoraMonetaria.CalculadoraMonetariaApplication;
import br.gov.ce.tce.calculadoraMonetaria.model.Requisicao;
import br.gov.ce.tce.calculadoraMonetaria.service.ApiServiceClient;
import br.gov.ce.tce.calculadoraMonetaria.service.InpcService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class ApiController {

    private final ApiServiceClient apiServiceClient;

    @Autowired
    private Requisicao requisicao;

    @Autowired
    private CalculadoraMonetariaApplication calculadoraMonetariaApplication;

    @Autowired
    private InpcService inpcService;

    @PostMapping("/calcular")
    public String calculaValorCorrigido(Requisicao requisicao, Model model) {

        // Obtendo os dados de entrada da Requisicao
        LocalDate dataInicial = requisicao.getDataInicial().withDayOfMonth(1);

        LocalDate dataDeRecolhimento = requisicao.getMesRecolhimento();
        if (dataDeRecolhimento.getMonth() == Month.FEBRUARY) {
            dataDeRecolhimento = dataDeRecolhimento.withDayOfMonth(28);
        } else {
            dataDeRecolhimento = dataDeRecolhimento.withDayOfMonth(30);
        }

        double valorInicial = requisicao.getValorNominal();

        // Verificando se os dados estão presentes
        if (dataInicial == null || dataDeRecolhimento == null) {
            model.addAttribute("error", "Data Inicial e Mês de Recolhimento não podem estar vazios.");
            return "index";
        }

        if (dataInicial.isAfter(dataDeRecolhimento)) {
            model.addAttribute("error", "Data Inicial não pode ser posterior ao Mês de Recolhimento.");
            return "index";
        }

        LocalDate dataEstabelecida = LocalDate.of(2015, 10, 1);
        if (dataInicial.isBefore(dataEstabelecida) || dataDeRecolhimento.isBefore(dataEstabelecida)) {
            model.addAttribute("error", "Data inválida. Por favor, insira uma data a partir de 1 de outubro de 2015.");
            return "index";
        }

        LocalDate dataAtual = LocalDate.now().plusMonths(1);
        if (dataInicial.isAfter(dataAtual) || dataDeRecolhimento.isAfter(dataAtual)) {
            model.addAttribute("error", "Não foi possível encontrar o índice para a data selecionada.");
            return "index";
        }

        try {
            // Obtenção do periodo para o cálculo do fator acumulado
            String periodoInicial = calculadoraMonetariaApplication.calculaPeriodo(dataInicial);
            String periodoRecolhimento = calculadoraMonetariaApplication.calculaPeriodo(dataDeRecolhimento);

            // Obtendo os valores do INPC usando o metodo GET
            Double inpcInicial = inpcService.getInpc(periodoInicial);
            Double inpcRecolhimento = inpcService.getInpc(periodoRecolhimento);


            // Verificando se foi necessário usar o período anterior
            if (inpcInicial == null) {
                String periodoAnterior = calculadoraMonetariaApplication.calculaPeriodo(dataInicial.minusMonths(1));
                inpcInicial = inpcService.getInpc(periodoAnterior);
                dataInicial = dataInicial.minusMonths(1);
                model.addAttribute("warning", "Índice atual ainda não disponível. Cálculo feito com o índice do mês anterior.");
            }

            if (inpcRecolhimento == null) {
                String periodoAnterior = calculadoraMonetariaApplication.calculaPeriodo(dataDeRecolhimento.minusMonths(1));
                inpcRecolhimento = inpcService.getInpc(periodoAnterior);
                dataDeRecolhimento = dataDeRecolhimento.minusMonths(1);
                model.addAttribute("warning", "Índice atual ainda não disponível. Cálculo feito com o índice do mês anterior.");
            }


            // Obtendo os fatores acumulados
            double fatorAcumuladoInicial = calculadoraMonetariaApplication.calculaFatorAcumulado(dataInicial, inpcInicial);
            double fatorAcumuladoAtualizado = calculadoraMonetariaApplication.calculaFatorAcumulado(dataDeRecolhimento.plusMonths(1), inpcRecolhimento);

            // Calculando o valor atualizado
            double valorAtualizado = calculadoraMonetariaApplication.calculaValorAtualizado(fatorAcumuladoInicial, fatorAcumuladoAtualizado, valorInicial);

            // Calculando os juros mensais
            double jurosMes = calculadoraMonetariaApplication.calculaJuros(valorAtualizado, calculadoraMonetariaApplication.getTaxaDeJuros(), dataInicial, dataDeRecolhimento);

            // Calculando o valor final
            double valorFinal = calculadoraMonetariaApplication.calculaValorFinal(valorAtualizado, jurosMes);

            // Adicionando os resultados ao modelo
            model.addAttribute("valorInicial", requisicao.getValorNominal());
            model.addAttribute("dataInicial", requisicao.getDataInicial()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            model.addAttribute("valorAtualizado", valorAtualizado);
            model.addAttribute("jurosMes", jurosMes);
            model.addAttribute("valorFinal", valorFinal);
            model.addAttribute("mesRecolhimento", requisicao.getMesRecolhimento()
                    .format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        } catch (Exception e) {
            model.addAttribute("error", "Ocorreu um erro ao calcular os valores: " + e.getMessage());
            return "index";
        }

        return "index";
    }

}




