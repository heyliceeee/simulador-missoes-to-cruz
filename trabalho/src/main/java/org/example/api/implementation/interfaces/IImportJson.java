package org.example.api.implementation.interfaces;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;

/**
 * Interface para importacao de dados em formato JSON.
 *
 * <p>
 * Essa interface define metodos para carregar mapas e missoes a partir
 * de arquivos JSON, garantindo que as estruturas sejam validas e os campos
 * obrigatorios estejam presentes.
 * </p>
 */
public interface IImportJson {
        /**
         * Carrega e processa um mapa a partir de um arquivo JSON.
         *
         * @param jsonPath O caminho para o arquivo JSON contendo os dados do mapa.
         * @throws InvalidJsonStructureException Se a estrutura do JSON nao for valida
         *                                       ou nao
         *                                       corresponder ao esperado.
         * @throws InvalidFieldException         Se algum campo necessario estiver
         *                                       ausente
         *                                       ou tiver um valor invalido.
         * @throws DivisionNotFoundException     Se alguma divisao referenciada no JSON
         *                                       nao for encontrada ou nao existir no
         *                                       contexto atual.
         */
        void carregarMapa(String jsonPath)
                        throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;

        /**
         * Carrega e processa uma missao a partir de um arquivo JSON.
         *
         * @param jsonPath O caminho para o arquivo JSON contendo os dados da missao.
         * @return Um objeto {@link IMissao} representando a missao carregada.
         * @throws InvalidJsonStructureException Se a estrutura do JSON nao for valida
         *                                       ou nao
         *                                       corresponder ao esperado.
         * @throws InvalidFieldException         Se algum campo necessario estiver
         *                                       ausente
         *                                       ou tiver um valor invalido.
         * @throws DivisionNotFoundException     Se alguma divisao referenciada no JSON
         *                                       nao for encontrada ou nao existir no
         *                                       contexto atual.
         */
        IMissao carregarMissao(String jsonPath)
                        throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;
}
