package org.example.api.implementation.utils;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;
import org.example.api.implementation.models.Divisao;
import org.example.api.implementation.models.Inimigo;
import org.example.api.implementation.models.Item;
import org.example.api.implementation.models.Mapa;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private final Mapa mapa;

    public JsonUtils(Mapa mapa) {
        this.mapa = mapa;
    }

    /**
     * Carrega o mapa do edifício a partir de um arquivo JSON.
     *
     * @param jsonPath Caminho do arquivo JSON.
     * @throws InvalidJsonStructureException Se a estrutura básica do JSON estiver incorreta.
     * @throws InvalidFieldException         Se um campo específico do JSON for inválido.
     * @throws DivisionNotFoundException     Se uma divisão referenciada não for encontrada no mapa.
     */
    public void carregarMapa(String jsonPath) throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            // Parse JSON
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            logger.info("JSON parseado com sucesso.");

            // Validar estrutura do JSON
            validarEstrutura(jsonObject);
            logger.info("Estrutura do JSON validada com sucesso.");

            // Ler divisões
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            logger.info("Carregar divisoes:");
            for (Object element : edificioArray) {
                String divisaoNome = validarString(element, "edificio");
                mapa.adicionarDivisao(divisaoNome);
                logger.debug("Divisao adicionada: {}", divisaoNome);
            }

            // Verificar se as divisões foram carregadas corretamente
            logger.info("\nDivisoes carregadas no grafo:");
            mapa.getDivisoes().forEach(divisao -> logger.info(divisao.getNomeDivisao()));

            // Ler conexões
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            logger.info("\nCarregando ligacoes:");
            for (Object element : ligacoesArray) {
                JSONArray ligacao = (JSONArray) element;
                if (ligacao.size() != 2) {
                    throw new InvalidJsonStructureException("Cada conexao deve conter exatamente dois valores.");
                }
                String origem = validarString(ligacao.get(0), "ligacao[0]");
                String destino = validarString(ligacao.get(1), "ligacao[1]");

                // Verifique se as divisões existem antes de conectar
                if (mapa.getDivisaoPorNome(origem) == null) {
                    throw new DivisionNotFoundException("Divisao de origem nao encontrada: " + origem);
                }
                if (mapa.getDivisaoPorNome(destino) == null) {
                    throw new DivisionNotFoundException("Divisao de destino nao encontrada: " + destino);
                }

                mapa.adicionarLigacao(origem, destino);
                logger.debug("Ligacao adicionada entre '{}' e '{}'", origem, destino);
            }

            logger.info("\nMapa carregado com sucesso!");

            // Processar inimigos
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            logger.info("\nCarregar inimigos:");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                Divisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    Inimigo inimigo = new Inimigo(nome, poder);
                    mapa.adicionarInimigo(divisaoNome, inimigo);
                    logger.debug("Inimigo '{}' adicionado a divisao '{}'", nome, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisao para inimigo nao encontrada: " + divisaoNome);
                }
            }

            // Processar itens
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            logger.info("\nCarregar itens:");
            for (Object element : itensArray) {
                JSONObject itemObj = (JSONObject) element;
                String divisaoNome = validarString(itemObj.get("divisao"), "item.divisao");
                String tipo = validarString(itemObj.get("tipo"), "item.tipo");
                int pontosRecuperados = itemObj.containsKey("pontos-recuperados") ? validarInt(itemObj.get("pontos-recuperados"), "item.pontos-recuperados") : 0;
                int pontosExtra = itemObj.containsKey("pontos-extra") ? validarInt(itemObj.get("pontos-extra"), "item.pontos-extra") : 0;

                Divisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    Item item;
                    if (pontosRecuperados > 0) {
                        item = new Item(tipo, pontosRecuperados);
                        logger.debug("Item '{}' com pontos recuperados '{}' adicionado a divisao '{}'", tipo, pontosRecuperados, divisaoNome);
                    } else {
                        item = new Item(tipo, pontosExtra);
                        logger.debug("Item '{}' com pontos extra '{}' adicionado a divisao '{}'", tipo, pontosExtra, divisaoNome);
                    }
                    mapa.adicionarItem(divisaoNome, item);
                } else {
                    throw new DivisionNotFoundException("Divisao para item nao encontrada: " + divisaoNome);
                }
            }

            // Processar entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            logger.info("\nCarregar entradas e saidas:");
            for (Object element : entradasSaidasArray) {
                String nomeDivisao = validarString(element, "entradas-saidas");
                mapa.adicionarEntradaSaida(nomeDivisao);
                logger.debug("Entrada/Saida adicionada: {}", nomeDivisao);
            }

            // Processar alvo
            JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
            String alvoDivisao = validarString(alvoObj.get("divisao"), "alvo.divisao");
            String alvoTipo = validarString(alvoObj.get("tipo"), "alvo.tipo");
            mapa.definirAlvo(alvoDivisao, alvoTipo);
            logger.debug("Alvo definido: Divisao '{}', Tipo '{}'", alvoDivisao, alvoTipo);

        } catch (IOException e) {
            logger.error("Erro de IO ao ler o JSON: {}", e.getMessage());
        } catch (ParseException e) {
            logger.error("Erro ao analisar o JSON: {}", e.getMessage());
        } catch (InvalidJsonStructureException | InvalidFieldException | DivisionNotFoundException e) {
            logger.error("Erro ao processar o JSON: {}", e.getMessage());
            throw e; // Repropague a exceção
        }
    }

    /**
     * Valida se um valor é uma string.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return Valor como string, se válido.
     * @throws InvalidFieldException Se o valor não for uma string válida.
     */
    private String validarString(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof String)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser uma string valida.");
        }
        return ((String) value).trim(); // Remove espaços extras
    }

    /**
     * Valida se um valor é um número inteiro.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return Valor como inteiro, se válido.
     * @throws InvalidFieldException Se o valor não for um número inteiro válido.
     */
    private int validarInt(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof Number)) {
            logger.error("Campo '{}' invalido: valor atual '{}'", campo, value);
            throw new InvalidFieldException("Campo '" + campo + "' deve ser um numero inteiro valido.");
        }
        return ((Number) value).intValue();
    }


    /**
     * Valida se a estrutura básica do JSON está presente.
     *
     * @param jsonObject Objeto JSON a ser validado.
     * @throws InvalidJsonStructureException Se a estrutura básica estiver incorreta.
     */
    private void validarEstrutura(JSONObject jsonObject) throws InvalidJsonStructureException {
        if (!jsonObject.containsKey("edificio") || !(jsonObject.get("edificio") instanceof JSONArray)) {
            logger.error("Campo 'edificio' esta ausente ou nao e uma lista.");
            throw new InvalidJsonStructureException("Campo 'edificio' obrigatorio e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("ligacoes") || !(jsonObject.get("ligacoes") instanceof JSONArray)) {
            logger.error("Campo 'ligacoes' esta ausente ou nao e uma lista.");
            throw new InvalidJsonStructureException("Campo 'ligacoes' obrigatorio e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("entradas-saidas") || !(jsonObject.get("entradas-saidas") instanceof JSONArray)) {
            logger.error("Campo 'entradas-saidas' esta ausente ou nao e uma lista.");
            throw new InvalidJsonStructureException("Campo 'entradas-saidas' obrigatorio e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("alvo") || !(jsonObject.get("alvo") instanceof JSONObject)) {
            logger.error("Campo 'alvo' esta ausente ou nao e um objeto.");
            throw new InvalidJsonStructureException("Campo 'alvo' obrigatorio e deve ser um objeto.");
        }
        if (!jsonObject.containsKey("itens") || !(jsonObject.get("itens") instanceof JSONArray)) {
            logger.error("Campo 'itens' esta ausente ou nao e uma lista.");
            throw new InvalidJsonStructureException("Campo 'itens' obrigatorio e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("inimigos") || !(jsonObject.get("inimigos") instanceof JSONArray)) {
            logger.error("Campo 'inimigos' esta ausente ou nao e uma lista.");
            throw new InvalidJsonStructureException("Campo 'inimigos' obrigatorio e deve ser uma lista.");
        }
    }
}
