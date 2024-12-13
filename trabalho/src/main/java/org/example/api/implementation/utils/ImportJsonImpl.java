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

/**
 * Classe responsável pela importação de dados de missões e mapas a partir de arquivos JSON.
 * <p>
 * Esta classe implementa a interface {@code IImportJson} e oferece funcionalidades para
 * carregar informações de mapas, divisões, conexões, inimigos e itens, além de configurar
 * as missões associadas.
 * </p>
 */
public class ImportJsonImpl implements IImportJson {

    private static final Logger logger = LoggerFactory.getLogger(ImportJsonImpl.class);
    private final IMapa mapa;

    /**
     * Construtor que inicializa o mapa utilizado para importação.
     *
     * @param mapa O mapa no qual os dados serão carregados.
     */
    public ImportJsonImpl(IMapa mapa) {
        this.mapa = mapa;
    }

    /**
     * Carrega uma missão a partir de um arquivo JSON.
     *
     * @param jsonPath Caminho do arquivo JSON contendo os dados da missão.
     * @return A missão configurada com base no JSON.
     * @throws InvalidJsonStructureException Se a estrutura básica do JSON for inválida.
     * @throws InvalidFieldException         Se um campo específico do JSON for inválido.
     * @throws DivisionNotFoundException     Se uma divisão referenciada não for encontrada no mapa.
     */
    @Override
    public IMissao carregarMissao(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // Validação e criação da missão
            String codMissao = validarString(jsonObject.get("cod-missao"), "cod-missao");
            int versao = validarInt(jsonObject.get("versao"), "versao");
            IMissao missao = new MissaoImpl(codMissao, versao, mapa);

            // Carrega o mapa associado à missão
            carregarMapa(jsonPath);

            return missao;
        } catch (IOException | ParseException e) {
            logger.error("Erro ao carregar missão: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao processar o arquivo JSON: " + e.getMessage());
        }
    }

    /**
     * Carrega as divisões, conexões, inimigos, itens e outras informações do mapa
     * a partir de um arquivo JSON.
     *
     * @param jsonPath Caminho do arquivo JSON contendo os dados do mapa.
     * @throws InvalidJsonStructureException Se a estrutura básica do JSON for inválida.
     * @throws InvalidFieldException         Se um campo específico do JSON for inválido.
     * @throws DivisionNotFoundException     Se uma divisão referenciada não for encontrada no mapa.
     */
    @Override
    public void carregarMapa(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException {
        try (FileReader reader = new FileReader(jsonPath)) {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(reader);

            // Processar divisões
            JSONArray edificioArray = (JSONArray) jsonObject.get("edificio");
            for (Object element : edificioArray) {
                String divisaoNome = validarString(element, "edificio");
                mapa.adicionarDivisao(divisaoNome);
            }

            // Processar conexões
            JSONArray ligacoesArray = (JSONArray) jsonObject.get("ligacoes");
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
            }

            // Processar entradas e saídas
            JSONArray entradasSaidasArray = (JSONArray) jsonObject.get("entradas-saidas");
            for (Object element : entradasSaidasArray) {
                String nomeDivisao = validarString(element, "entradas-saidas");
                mapa.adicionarEntradaSaida(nomeDivisao);
            }

            // Processar alvo
            JSONObject alvoObj = (JSONObject) jsonObject.get("alvo");
            String alvoDivisao = validarString(alvoObj.get("divisao"), "alvo.divisao");
            String alvoTipo = validarString(alvoObj.get("tipo"), "alvo.tipo");
            mapa.definirAlvo(alvoDivisao, alvoTipo);

            // Processar inimigos
            JSONArray inimigosArray = (JSONArray) jsonObject.get("inimigos");
            for (Object element : inimigosArray) {
                JSONObject inimigoObj = (JSONObject) element;
                String nome = validarString(inimigoObj.get("nome"), "inimigo.nome");
                int poder = validarInt(inimigoObj.get("poder"), "inimigo.poder");
                String divisaoNome = validarString(inimigoObj.get("divisao"), "inimigo.divisao");

                IDivisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    IInimigo inimigo = new InimigoImpl(nome, poder);
                    mapa.adicionarInimigo(divisaoNome, inimigo);
                } else {
                    throw new DivisionNotFoundException("Divisão para inimigo não encontrada: " + divisaoNome);
                }
            }

            // Processar itens
            JSONArray itensArray = (JSONArray) jsonObject.get("itens");
            for (Object element : itensArray) {
                JSONObject itemObj = (JSONObject) element;
                String tipo = validarString(itemObj.get("tipo"), "item.tipo");
                int pontos = itemObj.containsKey("pontos")
                        ? validarInt(itemObj.get("pontos"), "item.pontos")
                        : 0;
                String divisaoNome = validarString(itemObj.get("divisao"), "item.divisao");

                IDivisao divisao = mapa.getDivisaoPorNome(divisaoNome);
                if (divisao != null) {
                    IItem item = new ItemImpl(tipo, pontos);
                    mapa.adicionarItem(divisaoNome, item);
                } else {
                    throw new DivisionNotFoundException("Divisão para item não encontrada: " + divisaoNome);
                }
            }
        } catch (IOException e) {
            logger.error("Erro de IO ao ler o JSON: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro de IO: " + e.getMessage());
        } catch (ParseException e) {
            logger.error("Erro ao analisar o JSON: {}", e.getMessage());
            throw new InvalidJsonStructureException("Erro ao analisar o JSON: " + e.getMessage());
        }
    }

    /**
     * Valida se um valor é uma string válida.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return O valor como string.
     * @throws InvalidFieldException Se o valor não for uma string válida.
     */
    private String validarString(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof String)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser uma string válida.");
        }
        return ((String) value).trim();
    }

    /**
     * Valida se um valor é um número inteiro válido.
     *
     * @param value Valor a ser validado.
     * @param campo Nome do campo para mensagens de erro.
     * @return O valor como inteiro.
     * @throws InvalidFieldException Se o valor não for um número inteiro válido.
     */
    private int validarInt(Object value, String campo) throws InvalidFieldException {
        if (value == null || !(value instanceof Number)) {
            throw new InvalidFieldException("Campo '" + campo + "' deve ser um número inteiro válido.");
        }
        return ((Number) value).intValue();
    }
}
