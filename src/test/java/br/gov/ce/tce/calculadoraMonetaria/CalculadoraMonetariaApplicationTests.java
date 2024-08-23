package br.gov.ce.tce.calculadoraMonetaria;

/*import br.gov.ce.tce.calculadoraMonetaria.model.Requisicao;
import br.gov.ce.tce.calculadoraMonetaria.CalculadoraMonetariaApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CalculadoraMonetariaApplication.class)
@ExtendWith(SpringExtension.class)
public class CalculadoraMonetariaApplicationTests {

	@Autowired
	private CalculadoraMonetariaApplication calculadora;

	private Requisicao requisicao = new Requisicao();

	@Autowired
	private ApiService apiService;

	@Test
	public void testCalculaFatorAcumulado() {
		// Configurando a data para o teste
		LocalDate data = LocalDate.of(2015, 12, 1);

		double fatorAcumulado = calculadora.calculaFatorAcumulado(data);

		// Ajuste a expectativa conforme o comportamento real do ApiService
		assertEquals(2.19, fatorAcumulado, 0.001);
	}

	@Test
	public void testCalculaValorAtualizado() {
		// Configurando o valor inicial e as datas
		double valorInicial = 1.0;
		LocalDate dataInicial = LocalDate.of(2022, 1, 1);

		// Configurando o Requisicao com a data e o valor
		requisicao.setDataInicial(dataInicial);
		requisicao.setValorNominal(valorInicial);

		// Executando o cálculo do valor atualizado
		double valorAtualizado = calculadora.calculaValorAtualizado(valorInicial);

		// Ajuste a expectativa conforme o comportamento real do ApiService
		assertEquals(1.13, valorAtualizado, 0.001);
	}

	@Test
	public void testCalculaValorFinal() {
		// Configurando os parâmetros para o cálculo do valor final
		double valorAtualizado = 1.13;
		double taxaDeJuros = 0.01;
		LocalDate dataInicial = LocalDate.of(2022, 1, 1);

		// Configurando o Requisicao com a data e o valor
		requisicao.setDataInicial(dataInicial);

		// Executando o cálculo do valor final
		double valorFinal = calculadora.calculaValorFinal(valorAtualizado, taxaDeJuros, dataInicial);

		// Ajuste a expectativa conforme a lógica do cálculo
		assertEquals(1.482, valorFinal, 0.001);
	}
}*/
