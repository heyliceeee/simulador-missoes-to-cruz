# **Simulador de Missões - To Cruz**

## **Descrição do Projeto**
O **Simulador de Missões - To Cruz** é uma aplicação desenvolvida em Java que simula missões baseadas em mapas com diversas divisões interconectadas. O jogador controla o personagem **To Cruz**, que deve explorar o mapa, derrotar inimigos, coletar itens e resgatar o alvo designado, retornando com sucesso a uma divisão de saída.

O projeto inclui:
- **Simulação Manual**: O jogador toma decisões, controlando os movimentos e ações do To Cruz.
- **Simulação Automática**: O sistema calcula e executa o melhor caminho com base nos recursos disponíveis.

---

## **Objetivos**
- Aplicar conceitos de **estruturas de dados**, **grafos** e **algoritmos de busca**.
- Simular missões com cenários reais de exploração, combate e estratégia.
- Implementar soluções para movimentação, combate automático, inventário e interação com o mapa.

---

## **Funcionalidades Principais**

### **1. Simulação Manual**
- Permite ao jogador tomar decisões sobre os movimentos e ações do personagem To Cruz.
- Comandos disponíveis:
  - **Mover**: Escolher uma divisão adjacente para se mover.
  - **Atacar**: Enfrentar os inimigos na divisão atual.
  - **Usar**: Utilizar kits de vida disponíveis no inventário.
  - **Sair**: Encerrar a simulação manual.

### **2. Simulação Automática**
- O sistema calcula o **melhor caminho** para o objetivo e saída usando **algoritmos de busca em grafos**.
- Garante que os inimigos sejam derrotados e os itens sejam coletados para maximizar a sobrevivência.

### **3. Inventário e Itens**
- Sistema de inventário para armazenar itens coletados (kits de vida, coletes).
- Utilização estratégica de itens para recuperação de vida durante a simulação.

### **4. Combate**
- Sistema de combate que envolve inimigos com diferentes níveis de poder.
- Tó Cruz ataca primeiro, e o sistema gerencia os danos causados.

### **5. Mapas Dinâmicos**
- Os mapas são carregados a partir de ficheiros **JSON** configuráveis.
- O jogador pode interagir com divisões, conexões, inimigos e itens.

---

## **Tecnologias Utilizadas**
- **Java SE**: Linguagem de programação principal.
- **Gradle**: Ferramenta de build e gerenciamento de dependências.
- **JUnit 5**: Framework de testes unitários.
- **SLF4J**: Logging.
- **JSON Simple**: Processamento de ficheiros JSON.
- **Estruturas de Dados**: Implementação de listas, filas, pilhas e grafos.

---

## **Requisitos**
- **Java Development Kit (JDK)** 11 ou superior.
- **Gradle** 6.x ou superior (pode ser utilizado o wrapper incluído no projeto).

---

## **Como Executar**

### 1. **Clonar o Repositório**
```bash
git clone https://github.com/teu-usuario/simulador-missoes-to-cruz.git
cd simulador-missoes-to-cruz
```

### 2. **Compilar e Buildar o Projeto**
Utiliza o Gradle Wrapper para compilar o projeto:
```bash
./gradlew build
```
Para sistemas Windows:
```bash
gradlew.bat build
```

### 3. **Executar a Simulação Manual**
```bash
./gradlew runManual
```

### 4. **Executar a Simulação Automática**
```bash
./gradlew runAuto
```

### 5. **Executar os Testes**
Executa os testes com JUnit 5:
```bash
./gradlew test
```

---

## **Estrutura do Projeto**

```
src/
│── main/
│   ├── java/
│   │   ├── org/example/api/       # Interfaces e exceções
│   │   ├── org/example/impl/      # Implementações principais
│   │   ├── org/example/models/    # Classes de modelo (ToCruz, Inimigos, etc.)
│   │   ├── org/example/simulation/ # Simulação manual e automática
│   │   └── org/example/utils/     # Utilitários para JSON e exportação
│   └── resources/
│       ├── test_map.json          # Ficheiro JSON de exemplo
│       ├── invalid_structure.json # Estrutura inválida para testes
│       ├── missing_division.json  # JSON com divisão inexistente
│       └── invalid_field.json     # JSON com campo inválido
│
└── test/
    ├── java/
    │   ├── org/example/test/      # Testes unitários JUnit
    └── resources/                 # Ficheiros JSON para testes
```

---

## **Exemplo de Ficheiro JSON**
```json
{
  "cod-missao": "pata de coelho",
  "versao": 1,
  "edificio": ["Entrada", "Objetivo", "Saida"],
  "ligacoes": [["Entrada", "Objetivo"], ["Objetivo", "Saida"]],
  "inimigos": [{"nome": "Inimigo1", "poder": 30, "divisao": "Objetivo"}],
  "entradas-saidas": ["Entrada", "Saida"],
  "alvo": {"divisao": "Objetivo", "tipo": "quimico"},
  "itens": [{"divisao": "Objetivo", "pontos-recuperados": 20, "tipo": "kit de vida"}]
}
```

---

## **Testes**
Os testes unitários abrangem os seguintes casos:
- Validação do carregamento de mapas a partir de ficheiros JSON.
- Comportamento de **simulação manual** e **automática**.
- Detecção de cenários de erro, como divisões ou campos inválidos no ficheiro JSON.

Para executar os testes:
```bash
./gradlew test
```

---
## **Contribuições**
Made with ☕ and 💜:

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/heyliceeee">
        <img src="https://github.com/heyliceeee.png" width="100px;" alt="Foto da Alice Dias no GitHub"/><br>
        <sub>
          <b>Alice Dias</b>
        </sub>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/sancabenicio">
        <img src="https://github.com/sancabenicio.png" width="100px;" alt="Foto do Benicio Sanca no GitHub"/><br>
        <sub>
          <b>Benicio Sanca</b>
        </sub>
      </a>
    </td>
  </tr>
</table>
