package br.gov.ce.tce.calculadoraMonetaria.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class InpcResponse {
    private String id;
    private String variavel;
    private String unidade;
    private List<Resultado> resultados;

    @Data
    @AllArgsConstructor
    public static class Resultado {
        private List<Classificacao> classificacoes;
        private List<Serie> series;
    }

    @Data
    @AllArgsConstructor
    public static class Classificacao {
        private String id;
        private String nome;
    }

    @Data
    @AllArgsConstructor
    public static class Serie {
        private Localidade localidade;
        private java.util.Map<String, String> serie;

        @Data
        @AllArgsConstructor
        public static class Localidade {
            private String id;
            private Nivel nivel;
            private String nome;
        }

        @Data
        @AllArgsConstructor
        public static class Nivel {
            private String id;
            private String nome;
        }
    }
}

