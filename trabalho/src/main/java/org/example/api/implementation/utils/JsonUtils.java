package org.example.api.implementation.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.api.implementation.models.Divisao;
import org.example.api.implementation.models.Item;
import org.example.api.implementation.models.Mapa;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lida com a leitura e parsing do ficheiro JSON
 */
public class JsonUtils {
    private Mapa mapa;

    public JsonUtils(Mapa mapa) {
        this.mapa = mapa;
    }

    /**
     * le o JSON e constroi o objeto Mapa
     * @param ficheiro
     */
    public void carregarMapa(String ficheiro) {
        try (FileReader reader = new FileReader(ficheiro)) {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);


            // Lê as divisões
            JsonArray divisaoArray = jsonObject.getAsJsonArray("divisoes");
            for (JsonElement divisaoElement : divisaoArray) {
                String divisao = divisaoElement.getAsString();
                mapa.adicionarDivisao(divisao);
            }


            // Lê conexões
            JsonArray conexoesArray = jsonObject.getAsJsonArray("conexoes");
            for (JsonElement conexaoElement : conexoesArray) {
                JsonArray conexao = conexaoElement.getAsJsonArray();
                mapa.adicionarLigacao(conexao.get(0).getAsString(), conexao.get(1).getAsString());
            }


            // Lê entradas e saídas
            JsonArray entradasSaidasArray = jsonObject.getAsJsonArray("entradas-saidas");
            for (JsonElement entradaSaida : entradasSaidasArray) {
                mapa.adicionarEntradaSaida(entradaSaida.getAsString());
            }


            // Lê alvo
            JsonObject alvoJson = jsonObject.getAsJsonObject("alvo");
            String nomeDivisao = alvoJson.get("divisao").getAsString();
            String tipoAlvo = alvoJson.get("tipo").getAsString();

            // Converte o nome da divisão em um objeto Divisao
            Divisao divisaoAlvo = mapa.getDivisaoPorNome(nomeDivisao);

            // Define o alvo usando o objeto Divisao
            mapa.definirAlvo(divisaoAlvo, tipoAlvo);


            // Lê itens
            JsonArray itensArray = jsonObject.getAsJsonArray("itens");
            for (JsonElement itemElement : itensArray) {
                JsonObject itemObj = itemElement.getAsJsonObject();
                Item item = new Item(
                        itemObj.get("tipo").getAsString(),
                        itemObj.get("pontos").getAsInt()
                );
                mapa.adicionarItem(itemObj.get("divisao").getAsString(), item);
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar o mapa: " + e.getMessage());
        }
    }
}
