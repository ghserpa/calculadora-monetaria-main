package br.gov.ce.tce.calculadoraMonetaria.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class InpcLoader {
    private Map<String, Double> inpcMap = new HashMap<>();

    public InpcLoader() {
        loadInpcData();
    }

    private void loadInpcData() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("indices_inpc.csv");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                inpcMap.put(parts[0], Double.parseDouble(parts[1]));
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao carregar dados de INPC do arquivo CSV.");
        }
    }

    public Double getInpc(String periodo) {
        return inpcMap.get(periodo);
    }

    public void addInpc(String periodo, Double inpc) {
        inpcMap.put(periodo, inpc);
        saveInpcData(periodo, inpc);
    }

    private void saveInpcData(String periodo, Double inpc) {

        try (FileWriter fw = new FileWriter("src/main/resources/indices_inpc.csv", true)) {
            fw.append(periodo).append(",").append(inpc.toString()).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao salvar dados de INPC no arquivo CSV.");
        }
    }
}
