# Static Checker Ali2026-2

![Java](https://img.shields.io/badge/Java-17%2B-ED8B00?logo=openjdk&logoColor=white)
![Language](https://img.shields.io/badge/Spec-Ali2026--2-blue)
![I/O](https://img.shields.io/badge/I%2FO-.262%20%E2%86%92%20.LEX%20%2F%20.TAB-informational)
![Status](https://img.shields.io/badge/Status-Em%20Andamento-yellow)

## 📌 Índice
- [Sobre o Projeto](#sobre-o-projeto)
- [Tecnologias Utilizadas](#tecnologias)
- [Estrutura de Pastas](#estrutura-de-pastas)
- [Funcionalidades](#funcionalidades)
  - [Entrada e Controle](#entrada-e-controle)
  - [Análise Léxica](#analise-lexica)
  - [Tratamento de Encoding](#tratamento-de-encoding)
  - [Estruturas de Dados e Pacotes Java](#estruturas-de-dados-e-pacotes-java)
- [Relatórios de Saída (.LEX e .TAB)](#relatorios-de-saida)
- [Como Compilar e Executar](#como-compilar-e-executar)
  - [PowerShell (Windows)](#powershell-windows)
  - [Prompt de Comando (CMD - Windows)](#prompt-de-comando-cmd---windows)
  - [Terminal (Linux / macOS)](#terminal-linux--macos)

<a id="sobre-o-projeto"></a>
## 📌 Sobre o Projeto
O projeto presente neste repositório trata-se de um **Static Checker** para a linguagem **Ali2026-2**, desenvolvido em Java para fins avaliativos da disciplina de Compiladores do curso de Engenharia de Software da Universidade Católica do Salvador (UCSal). 

A linguagem **Ali2026-2** é baseada em parte da especificação da linguagem C. O Static Checker executa o processamento do código-fonte (arquivos `.262`), realizando tarefas essenciais da análise léxica (filtragem, normalização, tokenização e gerenciamento da Tabela de Símbolos) e preparando as estruturas para a análise sintática.

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
│   ├── leitura/         # Leitura de arquivos .262 e tratamento de encoding
│   ├── lexico/          # Filtros de 1º e 2º nível (analisador léxico)
│   ├── tabelas/         # Gerenciamento da Tabela de Símbolos
│   ├── sintatico/       # Estruturas e interfaces para fases sintáticas
│   └── relatorio/       # Exportação dos arquivos de saída .LEX e .TAB
├── bin/                 # Bytecodes compilados (.class) - gerado na compilação
├── staticChecker.jar    # Arquivo executável final - gerado ao empacotar
└── README.md            # Documentação principal do repositório
```

<a id="funcionalidades"></a>
## 🔨 Funcionalidades

<a id="entrada-e-controle"></a>
### 1. Entrada e Controle
- **Leitura de arquivo fonte:** Aceita arquivos texto formatados em padrão ASCII/ANSI por execução, exigindo a extensão `.262`;
- **Parâmetro flexível:** Recebe o nome do arquivo (ou caminho completo) com ou sem a extensão `.262`, localizando-o automaticamente;
- **Operação syntax-driven:** Atua como programa principal, inicializando as estruturas de dados, coordenando as chamadas ao analisador léxico e preparando o controle de escopo.

<a id="analise-lexica"></a>
### 2. Análise Léxica & Filtros
- **Tokenização unitária e case-insensitive:** Lê o código por caractere, forma um átomo por chamada e converte todos os lexemas fora de literais para caixa alta antes do reconhecimento e armazenamento;
- **Filtro de caracteres inválidos (1º Nível):** Desconsidera caracteres fora do alfabeto da linguagem sem interromper a execução nem a montagem do token atual;
- **Limpeza de espaços e comentários:** Descarta espaços em branco, tabulações e quebras de linha (filtrando repetições), além de ignorar comentários de linha (`// ...`) e de bloco (`/* ... */`) enquanto mantém a contagem exata das linhas do arquivo original;
- **Máximo comprimento e truncagem:** Aplica o critério do maior comprimento possível e limita cada átomo a no máximo 30 caracteres válidos, descartando o excedente até o próximo delimitador e garantindo a validade do token truncado (como forçar o fechamento de strings e preservar o formato numérico).

<a id="tratamento-de-encoding"></a>
### 3. Tratamento de Encoding (`MalformedInputException`)
- O leitor de arquivos no pacote `leitura` possui tratamento de *fallback* para decodificação em `ISO-8859-1` / `Windows-1252`;
- Previne exceções do tipo `java.nio.charset.MalformedInputException` durante a leitura de arquivos `.262` que contenham caracteres acentuados ou cedilha em comentários e textos.

<a id="estruturas-de-dados-e-pacotes-java"></a>
### 4. Estruturas de Dados e Pacotes Java

| Pacote | Responsabilidade |
| :--- | :--- |
| `principal` | Ponto de entrada (`Main.java`), recepção de parâmetros via linha de comando e orquestração do fluxo. |
| `leitura` | Leitura em buffer dos arquivos `.262` e resolução de encodings de texto. |
| `lexico` | Filtro de 1º Nível (limpeza e normalização) e Filtro de 2º Nível (reconhecimento léxico). |
| `tabelas` | Manutenção da Tabela de Símbolos unificada e gerenciamento dos átomos `C01`–`C07`. |
| `sintatico` | Interfaces e estruturas base para checagens sintáticas e estáticas. |
| `relatorio` | Formatação e gravação dos arquivos físicos de saída (`.LEX` e `.TAB`). |

- **Tabela de Palavras e Símbolos Reservados:** Estrutura fixa pré-carregada com as palavras-chave (`A01`–`A26`) e os símbolos reservados (`B01`–`B22`) da linguagem.
- **Tabela de Símbolos (`.TAB`):** Registra exclusivamente identificadores e constantes (`C01`–`C07`) com endereço/índice único por símbolo, mantendo atributos atualizados:
  - Número da entrada, código do átomo e lexeme (limitado aos 30 primeiros caracteres válidos);
  - Quantidade de caracteres válidos antes e depois da truncagem;
  - Tipo do símbolo em sigla de 2 caracteres (`FP`, `IN`, `ST`, `CH`, `BL`, `VD`, `AF`, `AI`, `AS`, `AC` ou `AB`);
  - Registro das 5 primeiras linhas de ocorrência (contabilizando comentários na numeração das linhas).

<a id="relatorios-de-saida"></a>
## 📄 Relatórios de Saída (.LEX e .TAB)

Ao processar um arquivo fonte `.262` (exemplo: `programa.262`), o analisador gera dois arquivos na mesma pasta do arquivo original:

1. **`programa.LEX`**: Registra a sequência ordenada de todos os átomos encontrados, exibindo o lexema, o código da categoria (`Axx`, `Bxx`, `Cxx`), o índice na Tabela de Símbolos (quando aplicável às categorias `C`) e a linha de ocorrência.
2. **`programa.TAB`**: Apresenta o retrato consolidado da Tabela de Símbolos ao final da execução, listando cada identificador/literal único armazenado e seus respectivos 7 atributos detalhados.

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
jar cvfm staticChecker.jar MANIFEST.MF -C bin .
Remove-Item MANIFEST.MF
```

#### 3. Executar o Static Checker
```powershell
# Execução utilizando o arquivo JAR (informando o nome com ou sem extensão)
java -jar staticChecker.jar caminho/para/seu_arquivo.262

# Ou execução direta a partir dos arquivos compilados
java -cp bin principal.Main caminho/para/seu_arquivo
```

<a id="prompt-de-comando-cmd---windows"></a>
### 🛠️ Execução via Prompt de Comando (CMD - Windows)

#### 1. Compilar os arquivos fontes
```cmd
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\principal\Main.java
```

#### 2. Gerar o arquivo executável (`.jar`)
```cmd
echo Main-Class: principal.Main> MANIFEST.MF
jar cvfm staticChecker.jar MANIFEST.MF -C bin .
del MANIFEST.MF
```

#### 3. Executar o Static Checker
```cmd
java -jar staticChecker.jar caminho\para\seu_arquivo.262
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
jar cvfm staticChecker.jar MANIFEST.MF -C bin .
rm MANIFEST.MF
```

#### 3. Executar o Static Checker
```bash
java -jar staticChecker.jar caminho/para/seu_arquivo.262
```
