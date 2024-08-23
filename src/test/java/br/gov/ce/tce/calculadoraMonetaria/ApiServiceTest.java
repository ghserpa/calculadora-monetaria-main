package br.gov.ce.tce.calculadoraMonetaria;

/*import br.gov.ce.tce.calculadoraMonetaria.CalculadoraMonetariaApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CalculadoraMonetariaApplication.class)
@ExtendWith(SpringExtension.class)
public class ApiServiceTest {

    @Autowired
    private ApiService apiService;

    @Test
    public void testGetInpcData() {
        // Configurando o WebClient diretamente no teste
        WebClient.Builder webClientBuilder = WebClient.builder()
                .baseUrl("https://servicodados.ibge.gov.br")
                .defaultHeader("Content-Type", "application/json");

        // Criando uma instância do ApiService com o WebClient configurado
        apiService = new ApiService(webClientBuilder);

        // Definindo o período para o teste
        String periodo = "202301";

        // Executando o metodo getInpcData
        Mono<String> result = apiService.getInpcData(periodo);

        // Verificando se o resultado é um Mono válido (não causará erro)
        StepVerifier.create(result)
                .expectNextMatches(response -> response != null && !response.isEmpty())
                .verifyComplete();
    }

    @Test
    public void testCalculaPeriodo() {

        // Testando o metodo CalculaPeriodo
        LocalDate mesDeRecolhimento = LocalDate.of(2023, 07, 01);
        LocalDate expectedPeriodo = LocalDate.of(2023, 07, 01);

        String result = apiService.CalculaPeriodo(mesDeRecolhimento);

        // Verificando se o período calculado está correto
        assertEquals(expectedPeriodo, result);
    }

}*/



