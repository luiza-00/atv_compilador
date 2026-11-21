# Static Checker Ali2026-2 | EQ01

# Índice
* [Sobre o Projeto](#sobre-o-projeto)
  * [Tecnologias Utilizadas](#tecnologias)
  * [Funcionalidades](#funcionalidades)
    * [Entrada e Controle](#entrada)
    * [Análise Léxica](#lex)
    * [Estruturas de Dados](#estrutura)
    * [Saída](#saida)

# 📌 Sobre o projeto
O projeto presente neste repositório trata-se de um Static Checker sobre a linguagem Ali2026-2 desenvolvido na linguagem Java, criado para fins avaliativos da disciplina de Compiladores, pertencente ao curso de Engenharia de Software da Universidade Católica do Salvador. A linguagem Ali2026-2 é baseada em parte da
especificação da linguagem de programação C, tendo o Static Checker executando as tarefas de análise léxica e parte da análise sintática.

<div id='sobre-o-projeto'/>

## ✔️ Tecnologias Utilizadas
- Java 17 ou superior

<div id='tecnologias'/>

## 🔨 Funcionalidades
<div id='funcionalidades'/>

### Entrada e Controle
- `Leitura de arquivo fonte`: Aceita um único arquivo texto em ASCII por execução, exigindo a extensão `.262`.
- `Parâmetro`: Recebe apenas o nome (ou caminho completo + nome) do arquivo sem a extensão, localizando o `.262` automaticamente.
- `Operação syntax-driven`: Atua como programa principal, inicializando as estruturas de dados, coordenando as chamadas ao analisador léxico e realizando o controle de escopo.

<div id='entrada'/>

### Análise Léxica
- `Tokenização unitária e case-insensitive`: Lê o código caractere a caractere, forma um átomo por chamada e converte todos os lexemes para caixa alta antes do reconhecimento e armazenamento.
- `Filtro de caracteres inválidos (1º nível)`: Desconsidera caracteres inválidos para a linguagem sem gerar erros de execução e sem interromper a montagem do átomo atual.
- `Limpeza de espaços e comentários`: Trata espaços em branco, tabulações e quebras de linha como delimitadores (filtrando repetições) e descarta comentários de linha `//` e de bloco `/* ... */`.
- `Máximo comprimento e truncagem`: Aplica o critério do maior comprimento possível e limita cada átomo a no máximo 30 caracteres válidos, descartando o excedente até o próximo delimitador e garantindo que o token truncado permaneça válido (como forçar fechamento de cadeias e manter formatos numéricos coerentes).

<div id='lex'/>

### Estruturas de Dados
- `Tabela de Palavras e Símbolos Reservados`: Estrutura fixa pré-carregada com as palavras-chave `A01–A26` e os símbolos reservados `B01–B22` da linguagem.
- `Tabela de Símbolos`: Registra exclusivamente identificadores e constantes `C01–C07` com endereço único por símbolo, mantendo 7 atributos atualizados:
  - Número da entrada, código do átomo e lexeme (limitado aos 30 primeiros caracteres válidos);
  - Quantidade de caracteres válidos antes e depois da truncagem;
  - Tipo do símbolo em sigla de 2 caracteres (FP, IN, ST, CH, BL, VD, AF, AI, AS, AC ou AB);
  - Registro das 5 primeiras linhas de ocorrência (contabilizando linhas de comentários na numeração).
 
<div id='estrutura'/>

### Saída
- `.LEX`: Relatório sequencial de todos os átomos encontrados no texto fonte, exibindo lexeme, código do átomo, índice na tabela de símbolos (quando aplicável) e linha.
- `.TAB`: Retrato final da tabela de símbolos após a análise, listando todos os identificadores armazenados e seus respectivos atributos. 

<div id='saida'/>
