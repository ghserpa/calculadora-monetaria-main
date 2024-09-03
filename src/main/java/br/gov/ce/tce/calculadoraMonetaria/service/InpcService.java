package br.gov.ce.tce.calculadoraMonetaria.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class InpcService {

    @Autowired
    private InpcLoader inpcLoader;

    @Autowired
    private ApiServiceClient apiServiceClient;

    public Double getInpc(String periodo) {
        Double inpc = inpcLoader.getInpc(periodo);

        if (inpc == null) {
            inpc = fetchInpcFromApi(periodo);

            if (inpc != null) {
                inpcLoader.addInpc(periodo, inpc);
            } /*else {
                    periodo = getPeriodoAnterior(periodo);

                    if (periodo == null) {
                        throw new RuntimeException("Erro: Não foi possível encontrar o índice do período.");
                    }
                }*/
        }
        return inpc;
    }

    private Double fetchInpcFromApi(String periodo) {
        try {
            return Double.parseDouble(apiServiceClient.getInpcData(periodo).getFirst()
                    .resultados().getFirst()
                    .series().getFirst()
                    .serie().get(periodo));

        } catch (Exception e) {
            return null;
        }
    }

    /*private String getPeriodoAnterior(String periodo) {

        int ano = Integer.parseInt(periodo.substring(0, 4));
        int mes = Integer.parseInt(periodo.substring(4, 6));

        if (mes == 1) {
            mes = 12;
            ano -= 1;
        } else {
            mes -= 1;
        }

        return String.format("%04d%02d", ano, mes);
    }*/

}

