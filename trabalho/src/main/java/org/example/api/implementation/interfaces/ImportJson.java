package org.example.api.implementation.interfaces;

import org.example.api.exceptions.DivisionNotFoundException;
import org.example.api.exceptions.InvalidFieldException;
import org.example.api.exceptions.InvalidJsonStructureException;

public interface ImportJson {
    void carregarMapa(String jsonPath) throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;

    Missao carregarMissao(String jsonPath) throws InvalidJsonStructureException, InvalidFieldException, DivisionNotFoundException;
}

