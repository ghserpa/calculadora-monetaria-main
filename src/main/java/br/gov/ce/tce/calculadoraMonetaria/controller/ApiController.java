package br.gov.ce.tce.calculadoraMonetaria.controller;

import br.gov.ce.tce.calculadoraMonetaria.CalculadoraMonetariaApplication;
import br.gov.ce.tce.calculadoraMonetaria.model.Requisicao;
import br.gov.ce.tce.calculadoraMonetaria.service.ApiServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Month;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class ApiController {

    private final ApiServiceClient apiServiceClient;

    @Autowired
    private Requisicao requisicao;

    @Autowired
    private CalculadoraMonetariaApplication calculadoraMonetariaApplication;

    @GetMapping("/inpc")
    public double getInpc(@RequestParam String periodo) {
        try {
            // Chamando o serviço e obtendo os dados do INPC para o período solicitado
            return Double.parseDouble(apiServiceClient.getInpcData(periodo).getFirst()
                    .resultados().getFirst()
                    .series().getFirst()
                    .serie().get(periodo));
        } catch (Exception e) {
            throw new RuntimeException("Erro ao buscar o INPC para o período: " + periodo, e);
        }
    }

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

        try {
            // Obtenção do INPC para o cálculo do fator acumulado
            String periodoInicial = calculadoraMonetariaApplication.calculaPeriodo(dataInicial);
            String periodoRecolhimento = calculadoraMonetariaApplication.calculaPeriodo(dataDeRecolhimento);

            // Obtendo os valores do INPC usando o metodo GET
            double inpcInicial = getInpc(periodoInicial);

            double inpcRecolhimento = getInpc(periodoRecolhimento);

            // Obtendo os fatores acumulados
            double fatorAcumuladoInicial = calculaFatorAcumulado(dataInicial, inpcInicial);

            double fatorAcumuladoAtualizado = calculaFatorAcumulado(dataDeRecolhimento.plusMonths(1), inpcRecolhimento);

            // Calculando o valor atualizado
            double valorAtualizado = calculadoraMonetariaApplication.calculaValorAtualizado(fatorAcumuladoInicial, fatorAcumuladoAtualizado, dataInicial, dataDeRecolhimento, valorInicial);

            // Calculando os juros mensais
            double jurosMes = calculadoraMonetariaApplication.calculaJuros(valorAtualizado, calculadoraMonetariaApplication.getTaxaDeJuros(), dataInicial, dataDeRecolhimento);

            // Calculando o valor final
            double valorFinal = calculadoraMonetariaApplication.calculaValorFinal(valorAtualizado, jurosMes);

            // Adicionando os resultados ao modelo
            model.addAttribute("valorAtualizado", valorAtualizado);
            model.addAttribute("jurosMes", jurosMes);
            model.addAttribute("valorFinal", valorFinal);

        } catch (Exception e) {
            model.addAttribute("error", "Ocorreu um erro ao calcular os valores: " + e.getMessage());
            return "index";
        }

        return "index";
    }

    // Calculando Fator Acumulado
    public double calculaFatorAcumulado(LocalDate data, double inpc) {

        LocalDate dataEstabelecida = LocalDate.of(2015, 11, 1);

        if (data.isBefore(dataEstabelecida)) {
            return 1.380045;
        }

        String periodo = calculadoraMonetariaApplication.calculaPeriodo(data.minusMonths(1));

        return calculaFatorAcumulado(data.minusMonths(1), getInpc(periodo)) * (1 + inpc/100);
    }

}




