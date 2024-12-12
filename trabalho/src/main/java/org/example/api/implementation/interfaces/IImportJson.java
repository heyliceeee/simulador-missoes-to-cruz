package org.example.api.implementation.interfaces;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;

public interface IImportJson {
        void carregarMapa(String jsonPath)
                        throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;

        IMissao carregarMissao(String jsonPath)
                        throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;
}
