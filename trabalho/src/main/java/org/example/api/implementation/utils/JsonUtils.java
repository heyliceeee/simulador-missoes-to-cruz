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
            logger.info("Arquivo JSON parseado com sucesso.");

            // Validar estrutura do JSON
            validarEstrutura(jsonObject);
            logger.info("Estrutura do JSON validada com sucesso.");

            // Ler divisões
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            logger.info("Carregando divisões:");
            for (Object element : edificioArray) {
                String divisaoNome = validarString(element, "edificio");
                mapa.adicionarDivisao(divisaoNome);
                logger.debug("Divisão adicionada: {}", divisaoNome);
            }

            // Verificar se as divisões foram carregadas corretamente
            logger.info("\nDivisões carregadas no grafo:");
            mapa.getDivisoes().forEach(divisao -> logger.info(divisao.getNomeDivisao()));

            // Ler conexões
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            logger.info("\nCarregando ligações:");
            for (Object element : ligacoesArray) {
                JSONArray ligacao = (JSONArray) element;
                if (ligacao.size() != 2) {
                    throw new InvalidJsonStructureException("Cada conexão deve conter exatamente dois valores.");
                }
                String origem = validarString(ligacao.get(0), "ligacao[0]");
                String destino = validarString(ligacao.get(1), "ligacao[1]");

                // Verifique se as divisões existem antes de conectar
                if (mapa.getDivisaoPorNome(origem) == null) {
                    throw new DivisionNotFoundException("Divisão de origem não encontrada: " + origem);
                }
                if (mapa.getDivisaoPorNome(destino) == null) {
                    throw new DivisionNotFoundException("Divisão de destino não encontrada: " + destino);
                }

                mapa.adicionarLigacao(origem, destino);
                logger.debug("Ligação adicionada entre '{}' e '{}'", origem, destino);
            }

            logger.info("\nMapa carregado com sucesso!");

            // Processar inimigos
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            logger.info("\nCarregando inimigos:");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                Divisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    Inimigo inimigo = new Inimigo(nome, poder);
                    mapa.adicionarInimigo(divisaoNome, inimigo);
                    logger.debug("Inimigo '{}' adicionado à divisão '{}'", nome, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisão para inimigo não encontrada: " + divisaoNome);
                }
            }

            // Processar itens
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            logger.info("\nCarregando itens:");
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
                        logger.debug("Item '{}' com pontos recuperados '{}' adicionado à divisão '{}'", tipo, pontosRecuperados, divisaoNome);
                    } else {
                        item = new Item(tipo, pontosExtra);
                        logger.debug("Item '{}' com pontos extra '{}' adicionado à divisão '{}'", tipo, pontosExtra, divisaoNome);
                    }
                    mapa.adicionarItem(divisaoNome, item);
                } else {
                    throw new DivisionNotFoundException("Divisão para item não encontrada: " + divisaoNome);
                }
            }

            // Processar entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            logger.info("\nCarregando entradas e saídas:");
            for (Object element : entradasSaidasArray) {
                String nomeDivisao = validarString(element, "entradas-saidas");
                mapa.adicionarEntradaSaida(nomeDivisao);
                logger.debug("Entrada/Saída adicionada: {}", nomeDivisao);
            }

            // Processar alvo
            JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
            String alvoDivisao = validarString(alvoObj.get("divisao"), "alvo.divisao");
            String alvoTipo = validarString(alvoObj.get("tipo"), "alvo.tipo");
            mapa.definirAlvo(alvoDivisao, alvoTipo);
            logger.debug("Alvo definido: Divisão '{}', Tipo '{}'", alvoDivisao, alvoTipo);

        } catch (IOException e) {
            logger.error("Erro de IO ao ler o arquivo JSON: {}", e.getMessage());
        } catch (ParseException e) {
            logger.error("Erro ao analisar o arquivo JSON: {}", e.getMessage());
        } catch (InvalidJsonStructureException | InvalidFieldException | DivisionNotFoundException e) {
            logger.error("Erro ao processar o JSON: {}", e.getMessage());
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
            throw new InvalidFieldException("Campo '" + campo + "' deve ser uma string válida.");
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
            throw new InvalidFieldException("Campo '" + campo + "' deve ser um número inteiro válido.");
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
            throw new InvalidJsonStructureException("Campo 'edificio' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("ligacoes") || !(jsonObject.get("ligacoes") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'ligacoes' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("entradas-saidas") || !(jsonObject.get("entradas-saidas") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'entradas-saidas' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("alvo") || !(jsonObject.get("alvo") instanceof JSONObject)) {
            throw new InvalidJsonStructureException("Campo 'alvo' é obrigatório e deve ser um objeto.");
        }
        if (!jsonObject.containsKey("itens") || !(jsonObject.get("itens") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'itens' é obrigatório e deve ser uma lista.");
        }
        if (!jsonObject.containsKey("inimigos") || !(jsonObject.get("inimigos") instanceof JSONArray)) {
            throw new InvalidJsonStructureException("Campo 'inimigos' é obrigatório e deve ser uma lista.");
        }
    }
}
