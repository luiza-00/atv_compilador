# Static Checker Ali2026-2

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)
![Repo Size](https://img.shields.io/github/repo-size/luiza-00/atv_compilador)
![I/O](https://img.shields.io/badge/I%2FO-.262%20%E2%86%92%20.LEX%20%2F%20.TAB-informational)
![Status](https://img.shields.io/badge/Status-Em%20Andamento-yellow)

## 📌 Índice
- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Funcionalidades](#funcionalidades)
  - [Entrada e Controle](#entrada-e-controle)
  - [Análise Léxica & Filtros](#analise-lexica)
  - [Tratamento de Codificação & Filtro de 1º Nível](#tratamento-de-codificacao)
  - [Estruturas de Dados e Pacotes Java](#estruturas-de-dados-e-pacotes-java)
- [Relatórios de Saída (.LEX e .TAB)](#relatorios-de-saida)
- [Como Compilar e Executar](#como-compilar-e-executar)
  - [PowerShell (Windows)](#powershell-windows)
  - [Prompt de Comando (CMD - Windows)](#prompt-de-comando-cmd---windows)
  - [Terminal (Linux / macOS)](#terminal-linux--macos)

<a id="sobre-o-projeto"></a>
## 📌 Sobre o Projeto
O projeto presente neste repositório trata-se de um **Static Checker** para a linguagem **Ali2026-2**, desenvolvido em Java para fins avaliativos da disciplina de Compiladores do curso de Engenharia de Software da Universidade Católica do Salvador (UCSal). 

A linguagem **Ali2026-2** é baseada em parte da especificação da linguagem C. O Static Checker realiza o processamento do código-fonte (arquivos `.262`), executando as tarefas essenciais da análise léxica (filtragem de caracteres inválidos, normalização, tokenização e gerenciamento da Tabela de Símbolos) e estruturando o acompanhamento de contexto para apoio às etapas do compilador.

<a id="tecnologias"></a>
## ✔️ Tecnologias Utilizadas
- **Linguagem:** Java 17 ou superior (compatível com Java 11+);
- **Ambiente de Execução:** Java Virtual Machine (JVM) / CLI (Command Line Interface);
- **Ferramentas de Compilação:** `javac` e `jar` (Standard JDK Tools).

<a id="estrutura-de-pastas"></a>
## 📁 Estrutura de Pastas do Projeto

```text
.
├── src/
│   ├── principal/       # Classe de entrada (Main.java) e interface CLI
│   ├── leitura/         # Leitura de arquivos .262 com controle de codificação
│   ├── lexico/          # Filtro de 1º nível, tokenização e tipos de átomos
│   ├── tabelas/         # Gerenciamento da Tabela de Símbolos e palavras reservadas
│   ├── sintatico/       # Estruturas de controle de contexto e apoio à análise
│   └── relatorio/       # Exportação dos relatórios físicos de saída (.LEX e .TAB)
├── bin/                 # Bytecodes compilados (.class) - gerado na compilação
├── StaticChecker.jar    # Arquivo executável final - gerado ao empacotar
└── README.md            # Documentação principal do repositório
```

<a id="funcionalidades"></a>
## 🔨 Funcionalidades

<a id="entrada-e-controle"></a>
### 1. Entrada e Controle
- **Leitura de arquivo fonte:** Aceita um único arquivo de texto por execução, exigindo a extensão `.262`;
- **Parâmetro flexível:** Recebe o nome do arquivo (ou caminho completo) com ou sem a extensão `.262`, localizando-o automaticamente no sistema de arquivos;
- **Operação syntax-driven:** Atua como programa principal, inicializando as estruturas de dados, coordenando as chamadas ao analisador léxico e acompanhando o contexto dos identificadores.

<a id="analise-lexica"></a>
### 2. Análise Léxica & Filtros
- **Tokenização unitária e case-insensitive:** Lê o código por caractere, forma um átomo por chamada e converte todos os lexemas fora de literais para caixa alta antes do reconhecimento e armazenamento;
- **Limpeza de espaços e comentários:** Descarta espaços em branco, tabulações e quebras de linha (filtrando repetições), além de ignorar comentários de linha (`// ...`) e de bloco (`/* ... */`) mantendo a contagem exata das linhas do arquivo original;
- **Máximo comprimento e truncagem:** Aplica o critério do maior comprimento possível e limita cada átomo a no máximo 30 caracteres válidos, descartando o excedente até o próximo delimitador e garantindo a validade do token truncado.

<a id="tratamento-de-codificacao"></a>
### 3. Tratamento de Codificação & Filtro de 1º Nível
- **Leitura Resiliente:** O leitor realiza a decodificação dos arquivos fontes preservando a integridade do fluxo de entrada sem interromper a execução por incompatibilidade de formato;
- **Filtro de 1º Nível:** Conforme a especificação da linguagem, caracteres inválidos para o alfabeto da linguagem são desconsiderados na formação dos átomos sem gerar erro de execução e sem atuar como delimitador, permitindo que a montagem do token continue.

<a id="estruturas-de-dados-e-pacotes-java"></a>
### 4. Estruturas de Dados e Pacotes Java

| Pacote | Responsabilidade |
| :--- | :--- |
| `principal` | Ponto de entrada (`Main.java`), recepção de parâmetros CLI e coordenação do fluxo de análise. |
| `leitura` | Leitura em buffer dos arquivos `.262` e controle do fluxo de caracteres de entrada. |
| `lexico` | Aplicação do Filtro de 1º Nível, reconhecimento dos átomos e categorização de tokens. |
| `tabelas` | Tabela fixa de Palavras/Símbolos Reservados e manutenção da Tabela de Símbolos unificada. |
| `sintatico` | Estruturas de suporte ao fluxo de átomos, acompanhamento de contexto e atribuição de tipos. |
| `relatorio` | Formatação e gravação dos arquivos físicos de saída (`.LEX` e `.TAB`). |

- **Tabela de Palavras e Símbolos Reservados:** Estrutura fixa pré-carregada com as palavras-chave (`A01`–`A26`) e os símbolos reservados (`B01`–`B22`) da linguagem.
- **Tabela de Símbolos (`.TAB`):** Registra exclusivamente identificadores e constantes (`C01`–`C07`) com endereço/índice único por símbolo, mantendo atributos atualizados:
  - Número da entrada, código do átomo e lexeme (limitado aos 30 primeiros caracteres válidos);
  - Quantidade de caracteres válidos antes e depois da truncagem;
  - Tipo do símbolo em sigla de 2 caracteres (`FP`, `IN`, `ST`, `CH`, `BL`, `VD`, `AF`, `AI`, `AS`, `AC` ou `AB`);
  - Registro das 5 primeiras linhas de ocorrência (contabilizando linhas de comentários na numeração).

<a id="relatorios-de-saida"></a>
## 📄 Relatórios de Saída (.LEX e .TAB)

Ao processar um arquivo fonte `.262` (exemplo: `programa.262`), o analisador gera dois arquivos no mesmo diretório do arquivo original:

1. **`programa.LEX`**: Registra a sequência ordenada de todos os átomos encontrados, exibindo o lexema, o código da categoria (`Axx`, `Bxx`, `Cxx`), o índice na Tabela de Símbolos (quando aplicável) e a linha de ocorrência.
2. **`programa.TAB`**: Apresenta a Tabela de Símbolos final após a análise, listando todos os identificadores/constantes armazenados e seus 7 atributos detalhados.

<a id="como-compilar-e-executar"></a>
## 🚀 Como Compilar e Executar

### Pré-requisitos
- JDK 11 ou superior instalado e configurado na variável de ambiente `PATH`.

<a id="powershell-windows"></a>
### 🛠️ Execução via PowerShell (Windows)

#### 1. Compilar os arquivos fontes
```powershell
New-Item -ItemType Directory -Force -Path bin
javac -encoding UTF-8 -d bin -sourcepath src (Get-ChildItem -Recurse src/*.java).FullName
```

#### 2. Gerar o arquivo executável (`.jar`)
```powershell
Set-Content -Path MANIFEST.MF -Value "Main-Class: principal.Main`n" -NoNewline
jar cvfm StaticChecker.jar MANIFEST.MF -C bin .
Remove-Item MANIFEST.MF
```

#### 3. Executar o Static Checker
```powershell
# Execução utilizando o arquivo JAR (informando o nome com ou sem extensão)
java -jar StaticChecker.jar caminho/para/seu_arquivo.262

# Ou execução direta a partir dos arquivos compilados
java -cp bin principal.Main caminho/para/seu_arquivo
```

<a id="prompt-de-comando-cmd---windows"></a>
### 🛠️ Execução via Prompt de Comando (CMD - Windows)

#### 1. Compilar os arquivos fontes
```cmd
if not exist bin mkdir bin
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d bin @sources.txt
del sources.txt
```

#### 2. Gerar o arquivo executável (`.jar`)
```cmd
echo Main-Class: principal.Main> MANIFEST.MF
jar cvfm StaticChecker.jar MANIFEST.MF -C bin .
del MANIFEST.MF
```

#### 3. Executar o Static Checker
```cmd
java -jar StaticChecker.jar caminho\para\seu_arquivo.262
```

<a id="terminal-linux--macos"></a>
### 🛠️ Execução via Terminal (Linux / macOS)

#### 1. Compilar os arquivos fontes
```bash
mkdir -p bin
javac -encoding UTF-8 -d bin -sourcepath src $(find src -name "*.java")
```

#### 2. Gerar o arquivo executável (`.jar`)
```bash
echo "Main-Class: principal.Main" > MANIFEST.MF
jar cvfm StaticChecker.jar MANIFEST.MF -C bin .
rm MANIFEST.MF
```

#### 3. Executar o Static Checker
```bash
java -jar StaticChecker.jar caminho/para/seu_arquivo.262
```
