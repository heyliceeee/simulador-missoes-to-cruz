package org.example.api.implementation.utils;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.interfaces.*;
import org.example.api.implementation.models.InimigoImpl;
import org.example.api.implementation.models.ItemImpl;
import org.example.api.implementation.models.MissaoImpl;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class ImportJsonImpl implements ImportJson {
    private static final Logger logger = LoggerFactory.getLogger(ImportJsonImpl.class);
    private final Mapa mapa;

    /**
     * Construtor que inicializa o mapa.
     *
     * @param mapa O mapa que será utilizado na importação.
     */
    public ImportJsonImpl(Mapa mapa) {
        this.mapa = mapa;
    }

    @Override
    public Missao carregarMissao(String jsonPath) throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
    
            validarEstrutura(jsonObject);
    
            String codMissao = validarString(jsonObject.get("cod-missao"), "cod-missao");
            int versao = validarInt(jsonObject.get("versao"), "versao");
    
            Missao missao = new MissaoImpl(codMissao, versao, mapa);
    
            carregarMapa(jsonPath); // Este método lança DivisionNotFoundException
    
            return missao;
        } catch (IOException | ParseException e) {
            logger.error("Erro ao carregar missão: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }
    

    @Override
    public void carregarMapa(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            validarEstrutura(jsonObject);

            // Processar divisões
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            //logger.info("Carregando divisões...");
            for (Object element : edificioArray) {
                String divisaoNome = validarString(element, "edificio");
                mapa.adicionarDivisao(divisaoNome);
                //logger.debug("Divisão adicionada: {}", divisaoNome);
            }

            // Processar conexões
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            //logger.info("Carregando conexões...");
            for (Object element : ligacoesArray) {
                JSONArray ligacao = (JSONArray) element;
                if (ligacao.size() != 2) {
                    throw new InvalidJsonStructureException("Cada conexão deve conter exatamente dois valores.");
                }
                String origem = validarString(ligacao.get(0), "ligacao[0]");
                String destino = validarString(ligacao.get(1), "ligacao[1]");

                if (mapa.getDivisaoPorNome(origem) == null) {
                    throw new DivisionNotFoundException("Divisão de origem não encontrada: " + origem);
                }
                if (mapa.getDivisaoPorNome(destino) == null) {
                    throw new DivisionNotFoundException("Divisão de destino não encontrada: " + destino);
                }

                mapa.adicionarLigacao(origem, destino);
                //logger.debug("Ligação adicionada entre '{}' e '{}'", origem, destino);
            }

            // Processar entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            logger.info("Carregando entradas e saídas...");
            for (Object element : entradasSaidasArray) {
                String nomeDivisao = validarString(element, "entradas-saidas");
                mapa.adicionarEntradaSaida(nomeDivisao);
                //logger.debug("Entrada/Saída adicionada: {}", nomeDivisao);
            }

            // Processar alvo
            JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
            //logger.info("Carregando alvo...");
            String alvoDivisao = validarString(alvoObj.get("divisao"), "alvo.divisao");
            String alvoTipo = validarString(alvoObj.get("tipo"), "alvo.tipo");
            mapa.definirAlvo(alvoDivisao, alvoTipo);
            //logger.debug("Alvo definido: Divisão '{}', Tipo '{}'", alvoDivisao, alvoTipo);

            // Processar inimigos
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            //logger.info("Carregando inimigos...");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                Divisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    Inimigo inimigo = new InimigoImpl(nome, poder);
                    mapa.adicionarInimigo(divisaoNome, inimigo);
                    //logger.debug("Inimigo '{}' adicionado à divisão '{}'", nome, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisão para inimigo não encontrada: " + divisaoNome);
                }
            }

            // Processar itens
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            //logger.info("Carregando itens...");
            for (Object element : itensArray) {
                JSONObject itemObj = (JSONObject) element;
                String tipo = validarString(itemObj.get("tipo"), "item.tipo");
                int pontos = itemObj.containsKey("pontos-recuperados")
                        ? validarInt(itemObj.get("pontos-recuperados"), "item.pontos-recuperados")
                        : validarInt(itemObj.get("pontos-extra"), "item.pontos-extra");
                String divisaoNome = validarString(itemObj.get("divisao"), "item.divisao");

                Divisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    Item item = new ItemImpl(tipo, pontos);
                    mapa.adicionarItem(divisaoNome, item);
                    //logger.debug("Item '{}' com pontos '{}' adicionado à divisão '{}'", tipo, pontos, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisão para item não encontrada: " + divisaoNome);
                }
            }

            logger.info("Mapa carregado com sucesso!");
        } catch (IOException | ParseException e) {
            logger.error("Erro ao processar o arquivo JSON: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }

    private void validarEstrutura(JSONObject jsonObject) throws InvalidJsonStructureException {
        if (!jsonObject.containsKey("edificio") || !(jsonObject.get("edificio") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'edificio' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("ligacoes") || !(jsonObject.get("ligacoes") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'ligacoes' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("entradas-saidas") || !(jsonObject.get("entradas-saidas") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'entradas-saídas' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("alvo") || !(jsonObject.get("alvo") instanceof JSONObject)) {
            throw new InvalidJsonStructureException("Campo 'alvo' é obrigatório e deve ser um objeto.");
        }
        if (!jsonObject.containsKey("inimigos") || !(jsonObject.get("inimigos") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'inimigos' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("itens") || !(jsonObject.get("itens") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'itens' é obrigatório e deve ser uma lista.");
        }
    }

    private String validarString(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof String)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser uma string válida.");
        }
        return ((String) value).trim();
    }

    private int validarInt(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof Number)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser um número inteiro válido.");
        }
        return ((Number) value).intValue();
    }


}
