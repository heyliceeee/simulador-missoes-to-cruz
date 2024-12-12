package org.example.api.implementation.interfaces;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;

/**
 * Interface para importação de dados em formato JSON.
 * 
 * <p>
 * Essa interface define métodos para carregar mapas e missões a partir
 * de arquivos JSON, garantindo que as estruturas sejam válidas e os campos
 * obrigatórios estejam presentes.
 * </p>
 */
public interface ImportJson {

    /**
     * Carrega e processa um mapa a partir de um arquivo JSON.
     *
     * @param jsonPath O caminho para o arquivo JSON contendo os dados do mapa.
     * @throws InvalidJsonStructureException Se a estrutura do JSON não for válida
     *                                       ou não
     *                                       corresponder ao esperado.
     * @throws InvalidFieldException         Se algum campo necessário estiver
     *                                       ausente
     *                                       ou tiver um valor inválido.
     * @throws DivisionNotFoundException     Se alguma divisão referenciada no JSON
     *                                       não for encontrada ou não existir no
     *                                       contexto atual.
     */
    void carregarMapa(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;

    /**
     * Carrega e processa uma missão a partir de um arquivo JSON.
     *
     * @param jsonPath O caminho para o arquivo JSON contendo os dados da missão.
     * @return Um objeto {@link Missao} representando a missão carregada.
     * @throws InvalidJsonStructureException Se a estrutura do JSON não for válida
     *                                       ou não
     *                                       corresponder ao esperado.
     * @throws InvalidFieldException         Se algum campo necessário estiver
     *                                       ausente
     *                                       ou tiver um valor inválido.
     * @throws DivisionNotFoundException     Se alguma divisão referenciada no JSON
     *                                       não for encontrada ou não existir no
     *                                       contexto atual.
     */
    Missao carregarMissao(String jsonPath)
            throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;
}
