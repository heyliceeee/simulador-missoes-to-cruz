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

public class ImportJsonImpl implements IImportJson {
    private static final Logger logger = LoggerFactory.getLogger(ImportJsonImpl.class);
    private final IMapa mapa;

    /**
     * Construtor que inicializa o mapa.
     *
     * @param mapa O mapa que será utilizado na importação.
     */
    public ImportJsonImpl(IMapa mapa) {
        this.mapa = mapa;
    }

    @Override
    public IMissao carregarMissao(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            logger.info("JSON parseado com sucesso.");

            // Validar estrutura do JSON
            validarEstrutura(jsonObject);

            String codMissao = validarString(jsonObject.get("cod-missao"), "cod-missao");
            int versao = validarInt(jsonObject.get("versao"), "versao");

            IMissao missao = new MissaoImpl(codMissao, versao, mapa);

            carregarMapa(jsonPath); // Este método lança DivisionNotFoundException

            return missao;
        } catch (IOException | ParseException e) {
            logger.error("Erro ao carregar missão: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Carrega o mapa do edifício a partir de um arquivo JSON.
     *
     * @param jsonPath Caminho do arquivo JSON.
     * @throws InvalidJsonStructureException Se a estrutura básica do JSON estiver
     *                                       incorreta.
     * @throws InvalidFieldException         Se um campo específico do JSON for
     *                                       inválido.
     * @throws DivisionNotFoundException     Se uma divisão referenciada não for
     *                                       encontrada no mapa.
     */
    @Override
    public void carregarMapa(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            validarEstrutura(jsonObject);

            // Processar divisões
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

                if (mapa.getDivisaoPorNome(origem) == null) {
                    throw new DivisionNotFoundException("Divisao de origem nao encontrada: " + origem);
                }
                if (mapa.getDivisaoPorNome(destino) == null) {
                    throw new DivisionNotFoundException("Divisao de destino nao encontrada: " + destino);
                }

                mapa.adicionarLigacao(origem, destino);
                logger.debug("Ligacao adicionada entre '{}' e '{}'", origem, destino);
            }

            // Processar entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            logger.info("Carregando entradas e saídas...");
            for (Object element : entradasSaidasArray) {
                String nomeDivisao = validarString(element, "entradas-saidas");
                mapa.adicionarEntradaSaida(nomeDivisao);
                // logger.debug("Entrada/Saída adicionada: {}", nomeDivisao);
            }

            // Processar alvo
            JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
            // logger.info("Carregando alvo...");
            String alvoDivisao = validarString(alvoObj.get("divisao"), "alvo.divisao");
            String alvoTipo = validarString(alvoObj.get("tipo"), "alvo.tipo");
            mapa.definirAlvo(alvoDivisao, alvoTipo);
            // logger.debug("Alvo definido: Divisão '{}', Tipo '{}'", alvoDivisao,
            // alvoTipo);

            // Processar inimigos
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            logger.info("\nCarregar inimigos:");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                IDivisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    IInimigo inimigo = new InimigoImpl(nome, poder);
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
                String tipo = validarString(itemObj.get("tipo"), "item.tipo");
                int pontosRecuperados = itemObj.containsKey("pontos-recuperados")
                        ? validarInt(itemObj.get("pontos-recuperados"), "item.pontos-recuperados")
                        : 0;
                int pontosExtra = itemObj.containsKey("pontos-extra")
                        ? validarInt(itemObj.get("pontos-extra"), "item.pontos-extra")
                        : 0;
                String divisaoNome = validarString(itemObj.get("divisao"), "item.divisao");

                IDivisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    IItem item;
                    if (pontosRecuperados > 0) {
                        item = new ItemImpl(tipo, pontosRecuperados);
                        logger.debug("Item '{}' com pontos recuperados '{}' adicionado a divisao '{}'", tipo,
                                pontosRecuperados, divisaoNome);
                    } else {
                        item = new ItemImpl(tipo, pontosExtra);
                        logger.debug("Item '{}' com pontos extra '{}' adicionado a divisao '{}'", tipo, pontosExtra,
                                divisaoNome);
                    }
                    mapa.adicionarItem(divisaoNome, item);
                    // logger.debug("Item '{}' com pontos '{}' adicionado à divisão '{}'", tipo,
                    // pontos, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisao para item nao encontrada: " + divisaoNome);
                }
            }
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
     * @throws InvalidJsonStructureException Se a estrutura básica estiver
     *                                       incorreta.
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
