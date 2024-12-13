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
     * @param mapa O mapa que sera utilizado na importacao.
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
            //validarEstrutura(jsonObject);

            String codMissao = validarString(jsonObject.get("cod-missao"), "cod-missao");
            int versao = validarInt(jsonObject.get("versao"), "versao");

            IMissao missao = new MissaoImpl(codMissao, versao, mapa);

            carregarMapa(jsonPath); // Este método lança DivisionNotFoundException

            return missao;
        } catch (IOException | ParseException e) {
            logger.error("Erro ao carregar missao: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Carrega o mapa do edificio a partir de um arquivo JSON.
     *
     * @param jsonPath Caminho do arquivo JSON.
     * @throws InvalidJsonStructureException Se a estrutura basica do JSON estiver
     *                                       incorreta.
     * @throws InvalidFieldException         Se um campo especifico do JSON for
     *                                       invalido.
     * @throws DivisionNotFoundException     Se uma divisao referenciada nao for
     *                                       encontrada no mapa.
     */
    @Override
    public void carregarMapa(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            //validarEstrutura(jsonObject);

            // Processar divisoes
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            // logger.info("Carregando divisões...");
            for (Object element : edificioArray) {
                String divisaoNome = validarString(element, "edificio");
                mapa.adicionarDivisao(divisaoNome);
                // logger.debug("Divisão adicionada: {}", divisaoNome);
            }

            // Verificar se as divisoes foram carregadas corretamente
            logger.info("\nDivisoes carregadas no grafo:");
            mapa.getDivisoes().forEach(divisao -> logger.info(divisao.getNomeDivisao()));

            // Ler conexoes
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
            // logger.info("Carregando conexões...");
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
                // logger.debug("Ligação adicionada entre '{}' e '{}'", origem, destino);
            }

            // Processar entradas e saidas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            logger.info("Carregando entradas e saidas...");
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
            // logger.info("Carregando inimigos...");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                IDivisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    IInimigo inimigo = new InimigoImpl(nome, poder);
                    mapa.adicionarInimigo(divisaoNome, inimigo);
                    // logger.debug("Inimigo '{}' adicionado à divisão '{}'", nome, divisaoNome);
                } else {
                    throw new DivisionNotFoundException("Divisao para inimigo nao encontrada: " + divisaoNome);
                }
            }

            // Processar itens
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            // logger.info("Carregando itens...");
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
            throw e; // Repropague a excecao
        }
    }

    /**
     * Valida se um valor e uma string.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return Valor como string, se valido.
     * @throws InvalidFieldException Se o valor nao for uma string valida.
     */
    private String validarString(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof String)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser uma string valida.");
        }
        return ((String) value).trim(); // Remove espacos extras
    }

    /**
     * Valida se um valor e um numero inteiro.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return Valor como inteiro, se valido.
     * @throws InvalidFieldException Se o valor nao for um numero inteiro valido.
     */
    private int validarInt(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof Number)) {
            logger.error("Campo '{}' invalido: valor atual '{}'", campo, value);
            throw new InvalidFieldException("Campo '" + campo + "' deve ser um numero inteiro valido.");
        }
        return ((Number) value).intValue();
    }

}
