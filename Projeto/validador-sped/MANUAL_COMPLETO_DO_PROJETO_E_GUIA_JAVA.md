# 📘 MANUAL COMPLETO DO PROJETO & GUIA DIDÁTICO DE JAVA E SPRING BOOT
> **Projeto:** Validador SPED — Tradutor e Assistente de Erros Fiscais  
> **Autor do TCC:** Brenon  
> **Objetivo deste Manual:** Explicar 100% da arquitetura, cada arquivo, cada linha fundamental e ensinar Java/Spring Boot do zero para que você domine o projeto e consiga fazer qualquer alteração com total segurança e autonomia.

---

## 🧭 Sumário Geral
1. [Mensagem Inicial & Guia de Leitura](#1-mensagem-inicial--guia-de-leitura)
2. [O Negócio: O que é o SPED e por que este software existe?](#2-o-negócio-o-que-é-o-sped-e-por-que-este-software-existe)
3. [Curso Relâmpago de Java e Spring Boot (Para quem não sabe nada)](#3-curso-relâmpago-de-java-e-spring-boot-para-quem-não-sabe-nada)
   - 3.1 O que é o Java e como ele funciona (JVM, Bytecode, JDK)
   - 3.2 O que é uma Classe, Objeto, Atributo e Método
   - 3.3 O que é o Maven e o arquivo `pom.xml`
   - 3.4 O que é o Spring Boot e por que o usamos
   - 3.5 O Conceito de Ouro: Injeção de Dependências & Inversão de Controle
   - 3.6 O que são as Anotações (`@Annotation`)
   - 3.7 A Arquitetura em Camadas (Controller -> Service -> Repository -> Model)
4. [Raio-X de Cada Arquivo do Projeto (Explicação Linha por Linha)](#4-raio-x-de-cada-arquivo-do-projeto)
   - 4.1 Configurações & Infraestrutura: `pom.xml`, `application.properties`, `data.sql`
   - 4.2 Inicialização da Aplicação: `ValidadorSpedApplication.java`
   - 4.3 Camada de Domínio / Banco de Dados: `model/ErroTraduzido.java`
   - 4.4 Camada de Acesso a Dados: `repository/ErroTraduzidoRepository.java`
   - 4.5 Camada de Transferência: `dto/ErroTraducaoDTO.java` e `dto/ResultadoAnaliseDTO.java`
   - 4.6 Camada de Tratamento de Exceções: `exception/ArquivoInvalidoException.java` e `exception/GlobalExceptionHandler.java`
   - 4.7 Camada de Regras de Negócio: `service/SpedProcessadorService.java`
   - 4.8 Camada de Controle / API REST: `controller/SpedController.java`
   - 4.9 Camada de Apresentação / Frontend: `src/main/resources/static/index.html`
   - 4.10 Testes Automatizados: `ValidadorSpedApplicationTests.java`
5. [O Ciclo de Vida de uma Operação (Passo a Passo Visual)](#5-o-ciclo-de-vida-de-uma-operação)
   - 5.1 Fluxo de Upload e Análise de Arquivo
   - 5.2 Fluxo de Busca Manual de Código
   - 5.3 O que acontece quando ocorre um erro
6. [Como Executar e Testar o Projeto](#6-como-executar-e-testar-o-projeto)
   - 6.1 Rodando pelo Terminal
   - 6.2 Rodando no VS Code
   - 6.3 Acessando a Aplicação e o Console do Banco de Dados H2
7. [Guia Prático: Como Fazer Modificações Sozinho (Receitas Prontas)](#7-guia-prático-como-fazer-modificações-sozinho)
   - 7.1 Como adicionar um novo erro fiscal no catálogo
   - 7.2 Como adicionar um novo campo em todo o sistema (ex: Gravidade do Erro)
   - 7.3 Como criar um novo endpoint na API REST
   - 7.4 Como alterar o visual da tela (index.html)
8. [Glossário de Termos Essenciais](#8-glossário-de-termos-essenciais)

---

## 1. Mensagem Inicial & Guia de Leitura

Primeiro de tudo: **respire fundo e não se preocupe**. 
É perfeitamente normal usar Inteligência Artificial para acelerar o desenvolvimento e depois sentir que perdeu o controle sobre o código. A boa notícia é que o seu projeto foi estruturado com uma arquitetura **limpa, moderna e muito elegante**, seguindo as melhores práticas do ecossistema Java mundial.

Este manual foi escrito com linguagem direta, didática e sem rodeios. Você não precisa ter medo de termos técnicos, pois cada um deles será explicado com analogias do dia a dia.

Ao final da leitura deste documento, você entenderá perfeitamente:
1. O papel de cada pasta e cada arquivo.
2. Como os dados viajam da tela (navegador) até o banco de dados e voltam traduzidos.
3. Como abrir qualquer arquivo do projeto, alterar uma linha e saber exatamente o que aquilo vai causar no sistema.

---

## 2. O Negócio: O que é o SPED e por que este software existe?

Para entender o código, você precisa entender o **problema real** que o seu TCC resolve.

### 2.1 O que é o SPED Fiscal?
No Brasil, toda empresa precisa enviar para a Receita Federal e para as Secretarias de Fazenda estaduais (SEFAZ) um arquivo contendo todas as notas fiscais que emitiu, todas as notas que recebeu, os produtos vendidos, estoques e impostos calculados (ICMS, IPI, PIS, COFINS). 
Esse arquivo é o **SPED (Sistema Público de Escrituração Digital)**. Ele é um arquivo de texto simples (`.txt`), onde cada linha começa e termina com uma barra vertical (o caractere chamado *pipe* `|`).

Exemplo real de uma linha do SPED:
```text
|C100|0|1|FORN01|55|00|1|123456|15012026|15012026|1500,00|1|0,00|0,00|1500,00|0|0,00|0,00|0,00|1500,00|270,00|...|
```
Cada número entre as barras representa um dado específico (número da nota, data, valor, imposto).

### 2.2 O Problema do PVA (Programa Validador e Assinador)
Antes de transmitir o arquivo para a Receita, a empresa deve passá-lo por um programa oficial chamado **PVA**.
O PVA analisa o arquivo e aponta erros. Só que as mensagens do PVA são extremamente **técnicas, frias e confusas** para os usuários de empresas e analistas de suporte.

Por exemplo, o PVA exibe:
> *"Erro no registro C100, campo COD_CTA: Código da Conta Analítica Contábil não cadastrado no Plano de Contas (Registro 0500)."*

Para um operador comum de suporte ou faturista, isso parece grego. Ele não sabe o que fazer no ERP da empresa (por exemplo, no **ControlGás** ou no sistema contábil **FISCAL3c**).

### 2.3 A Solução: Seu Validador SPED (Tradutor de Erros)
O seu software é um **assistente inteligente e tradutor de erros fiscais**.
Ele possui uma base de dados de erros conhecidos com orientações claras de como consertar o problema no sistema do cliente.
O sistema oferece duas funções:
1. **Importação do Arquivo SPED (.txt):** O usuário envia o arquivo do SPED. O sistema lê as linhas, identifica os registros com erro, busca as orientações e entrega um relatório mastigado de como corrigir.
2. **Pesquisa Manual:** Se o operador já estiver olhando para o relatório do PVA e vir uma sigla como `COD_CTA`, `UNID`, ou `CHV_NFE`, ele simplesmente digita na barra de pesquisa e recebe imediatamente o passo a passo da correção.

---

## 3. Curso Relâmpago de Java e Spring Boot (Para quem não sabe nada)

Se você nunca programou em Java, esta seção é o seu ponto de partida fundamental.

### 3.1 O que é o Java e como ele funciona?
Em linguagens como C ou C++, o programa é compilado diretamente para a máquina onde vai rodar (Windows, Linux, Mac). 
No **Java**, funciona diferente:
- Você escreve o código em arquivos `.java`.
- O compilador do Java (`javac`) transforma isso em um formato intermediário chamado **Bytecode** (arquivos `.class`).
- Esse Bytecode é executado pela **JVM (Java Virtual Machine)**. 
- Graças à JVM, o mesmo código Java roda identicamente em qualquer computador com Windows, Linux ou Mac.

### 3.2 O que é uma Classe, Objeto, Atributo e Método?
Pense no Java como uma fábrica de carros:
- **Classe (`class`):** É a planta baixa / o projeto do carro. Não é o carro real, é apenas o modelo.
- **Objeto (`object` ou instância):** É o carro real fabricado a partir da planta. Você pode fabricar 10 carros a partir da mesma planta.
- **Atributos (`fields` / variáveis):** São as características do carro (cor, modelo, ano, velocidade). No nosso projeto, um erro tem atributos como `codigoErro`, `campo`, `mensagemPva`.
- **Métodos (`methods` / funções):** São as ações que o carro pode fazer (acelerar, frear). No Java, são blocos de código que realizam uma ação (ex: `processarArquivo()`, `salvar()`).

### 3.3 O que é o Maven e o arquivo `pom.xml`?
Quando você constrói uma casa, você não fabrica os tijolos, a fiação e as janelas do zero; você compra pronto.
Na programação é igual. Para criar uma aplicação web com banco de dados, você usa bibliotecas feitas por outros programadores.
O **Maven** é o gerenciador de pacotes do Java. Ele:
1. Lê o arquivo `pom.xml`.
2. Baixa automaticamente da internet todas as bibliotecas necessárias.
3. Compila seu código e empacota tudo em um arquivo executável `.jar`.

### 3.4 O que é o Spring Boot?
O Java puro exigiria centenas de linhas de código chato apenas para subir um servidor web, conectar no banco de dados e receber um arquivo.
O **Spring Boot** é o framework mais famoso do mundo Java. Ele cuida de todo o trabalho pesado de infraestrutura:
- Ele já vem com um servidor web embutido (o Apache Tomcat), então você não precisa instalar nada além do Java.
- Ele descobre seus arquivos e conecta as peças automaticamente.

### 3.5 O Conceito de Ouro: Injeção de Dependências & Inversão de Controle (IoC)
Essa é a ideia mais importante do Spring Boot.
Imagine que uma classe `SpedController` precisa da classe `SpedProcessadorService` para trabalhar.
- **No Java tradicional:** A classe `SpedController` precisaria dar `new SpedProcessadorService()`. Se você quisesse mudar a classe no futuro, teria que reescrever tudo.
- **No Spring Boot (Injeção de Dependência):** Você não cria nada com `new`. Você simplesmente declara: *"Spring, eu preciso de um serviço aqui"*. Quando a aplicação liga, o Spring cria a instância do serviço e entrega pronta dentro do Controller. Isso se chama **Inversão de Controle**.

### 3.6 O que são as Anotações (`@Annotation`)?
No código Java moderno, você verá muitas palavras que começam com arroba (`@`), como `@RestController`, `@Service`, `@Entity`.
Pense nas anotações como **adesivos ou crachás mágicos**. Elas dizem ao Spring Boot qual é o papel de cada classe:
- `@SpringBootApplication`: "Eu sou a classe que liga o motor da aplicação inteira!"
- `@RestController`: "Eu sou uma porta de entrada para a internet (recebo requisições do navegador)!"
- `@Service`: "Eu guardo as regras de negócio e os cálculos inteligentes!"
- `@Repository`: "Eu converso diretamente com o banco de dados (faço INSERT, SELECT, UPDATE)!"
- `@Entity`: "Eu represento uma tabela do banco de dados!"

### 3.7 A Arquitetura em Camadas (Controller -> Service -> Repository -> Model)
O projeto está dividido de forma profissional em camadas. Cada camada tem uma única responsabilidade:

```
[ NAVEGADOR (index.html) ]
        │  ▲
        │  │ (HTTP / JSON)
        ▼  │
 1. [ CONTROLLER ]  ──> SpedController.java (Recebe o clique do usuário)
        │  ▲
        ▼  │
 2. [ SERVICE ]     ──> SpedProcessadorService.java (Aplica a inteligência/regras)
        │  ▲
        ▼  │
 3. [ REPOSITORY ]  ──> ErroTraduzidoRepository.java (Comando SQL para o banco)
        │  ▲
        ▼  │
 4. [ BANCO DE DADOS H2 ] (Tabela erros_traduzidos)
```

- **DTO (Data Transfer Object):** É uma caixinha de dados usada apenas para transportar informações entre o Java e a tela do navegador com segurança.

---

## 4. Raio-X de Cada Arquivo do Projeto

Agora vamos abrir mentalmente cada arquivo do projeto, entender onde ele fica e o que ele faz.

### 4.1 Configurações & Infraestrutura

#### 📁 `validador-sped/pom.xml`
- **Onde fica:** Na raiz da pasta do projeto Java.
- **O que faz:** É a certidão de nascimento do projeto no Maven.
- **Linhas mais importantes:**
  - `<java.version>25</java.version>`: Informa a versão do Java compatível.
  - `<artifactId>spring-boot-starter-webmvc</artifactId>`: Baixa tudo o que é necessário para criar páginas web e APIs REST.
  - `<artifactId>spring-boot-starter-data-jpa</artifactId>`: Baixa o Hibernate e a camada de banco de dados para evitar escrever SQL na mão.
  - `<artifactId>h2</artifactId>`: O banco de dados relacional embutido. Não precisa instalar MySQL, PostgreSQL ou Oracle; o H2 roda dentro da memória da aplicação!
  - `<artifactId>spring-boot-h2console</artifactId>`: Habilita uma tela no navegador para você ver as tabelas do banco visualmente.

#### 📁 `src/main/resources/application.properties`
- **Onde fica:** Dentro da pasta de recursos (`src/main/resources`).
- **O que faz:** É o painel de configurações gerais do Spring Boot.
- **Explicação linha a linha:**
  ```properties
  spring.datasource.url=jdbc:h2:mem:testdb
  spring.datasource.driverClassName=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=
  ```
  > Configura o banco H2 em memória (`mem:testdb`) com usuário `sa` e sem senha. Toda vez que você liga o sistema, o banco é criado limpinho e pronto para uso.

  ```properties
  spring.h2.console.enabled=true
  spring.h2.console.path=/h2-console
  ```
  > Ativa o painel visual do banco no endereço `http://localhost:8080/h2-console`.

  ```properties
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.defer-datasource-initialization=true
  spring.sql.init.mode=always
  spring.jpa.show-sql=true
  ```
  > - `ddl-auto=update`: O Spring cria ou ajusta as tabelas do banco de dados automaticamente com base nas classes `@Entity`.
  > - `defer-datasource-initialization=true` e `init.mode=always`: Garante que o arquivo `data.sql` seja executado logo após as tabelas serem criadas.
  > - `show-sql=true`: Imprime no terminal todas as consultas SQL que o Spring fizer em tempo real (ótimo para aprender!).

  ```properties
  spring.servlet.multipart.max-file-size=20MB
  spring.servlet.multipart.max-request-size=20MB
  ```
  > Como arquivos SPED reais têm milhares de linhas de notas fiscais, aumentamos o limite de upload do Spring para aceitar arquivos de até **20 Megabytes**.

#### 📁 `src/main/resources/data.sql`
- **Onde fica:** Em `src/main/resources`.
- **O que faz:** É a base de conhecimento inicial do sistema! Quando a aplicação sobe, o Spring lê este arquivo e insere os erros fiscais mais comuns no banco de dados.
- **Campos da Tabela `erros_traduzidos`:**
  - `codigo_erro`: Sigla ou código do erro (ex: `UN`, `IND_OPR`, `CHV_NFE`, `COD_CTA`, `COD_NCM`).
  - `campo`: O nome do campo do SPED que causou o problema (ex: `UNID`, `IND_OPER`, `COD_CTA`).
  - `mensagem_pva`: O texto exato que o validador oficial da Receita exibe.
  - `mensagem_traduzida`: A explicação humana e amigável do que está acontecendo.
  - `orientacao_correcao`: O passo a passo prático de como o analista de suporte/contador deve consertar o sistema (ex: ajustes no ControlGás ou no FISCAL3c).

---

### 4.2 Inicialização da Aplicação

#### 📁 `src/main/java/com/brenon/validador_sped/ValidadorSpedApplication.java`
- **O que faz:** É o ponto de partida de todo o programa.
- **O Código:**
  ```java
  package com.brenon.validador_sped;

  import org.springframework.boot.SpringApplication;
  import org.springframework.boot.autoconfigure.SpringBootApplication;

  @SpringBootApplication
  public class ValidadorSpedApplication {
      public static void main(String[] args) {
          SpringApplication.run(ValidadorSpedApplication.class, args);
      }
  }
  ```
- **O que está acontecendo aqui?**
  1. `public static void main(String[] args)`: É o método universal de inicialização do Java. Todo programa Java começa por um método `main`.
  2. `@SpringBootApplication`: Esta anotação mágica faz 3 coisas:
     - Configura a aplicação como uma aplicação Spring.
     - Liga a auto-configuração (descobre que você tem banco H2 e Web e liga os dois sozinhos).
     - Ativa o **Component Scan**: varre todas as subpastas (`controller`, `service`, `repository`, `model`) procurando classes com as anotações do Spring para gerenciá-las.
  3. `SpringApplication.run(...)`: Dispara a inicialização, sobe o servidor web Tomcat na porta 8080 e deixa a aplicação pronta para receber usuários.

---

### 4.3 Camada de Domínio / Banco de Dados (Model)

#### 📁 `src/main/java/com/brenon/validador_sped/model/ErroTraduzido.java`
- **O que faz:** É uma **Entidade JPA**. Ela é a representação em código Java da tabela `erros_traduzidos` do banco de dados.
- **Anotações Chave:**
  - `@Entity`: Diz ao Hibernate: *"Esta classe representa uma tabela no banco de dados"*.
  - `@Table(name = "erros_traduzidos")`: Especifica o nome exato da tabela.
  - `@Id` e `@GeneratedValue(strategy = GenerationType.IDENTITY)`: Define a chave primária (`id`) e faz o banco de dados auto-incrementar o número (1, 2, 3...) sozinho.
  - `@Column(...)`: Define o nome e tamanho máximo da coluna (ex: `length = 2000`).
- **Encapsulamento (Getters e Setters):**
  - Os atributos são `private` (privados) para que ninguém de fora possa alterá-los indevidamente.
  - Os métodos `getId()`, `getCodigoErro()` (Getters) servem para **ler** os valores.
  - Os métodos `setId(...)`, `setCodigoErro(...)` (Setters) servem para **modificar** os valores.

---

### 4.4 Camada de Acesso a Dados (Repository)

#### 📁 `src/main/java/com/brenon/validador_sped/repository/ErroTraduzidoRepository.java`
- **O que faz:** É o intermediário que executa comandos no banco de dados.
- **O Código:**
  ```java
  package com.brenon.validador_sped.repository;

  import com.brenon.validador_sped.model.ErroTraduzido;
  import org.springframework.data.jpa.repository.JpaRepository;
  import org.springframework.stereotype.Repository;
  import java.util.List;

  @Repository
  public interface ErroTraduzidoRepository extends JpaRepository<ErroTraduzido, Long> {

      List<ErroTraduzido> findByCodigoErroContainingIgnoreCase(String codigoErro);

      List<ErroTraduzido> findByCodigoErroContainingIgnoreCaseOrCampoContainingIgnoreCaseOrMensagemPvaContainingIgnoreCase(
          String codigoErro, String campo, String mensagemPva
      );
  }
  ```
- **Por que isso é incrível no Spring Boot?**
  - Observe que este arquivo é uma `interface`, não uma classe comum, e não tem nenhum código SQL escrito dentro!
  - Ao estender `JpaRepository<ErroTraduzido, Long>`, o Spring cria automaticamente métodos como `findAll()`, `findById()`, `save()`, `delete()`.
  - **Métodos de Consulta Derivados (Derived Queries):** O Spring lê o nome do método em inglês e monta o SQL sozinho!
    - `findByCodigoErroContainingIgnoreCase`: Faz um `SELECT * FROM erros_traduzidos WHERE LOWER(codigo_erro) LIKE %termo%`.
    - `OrCampoContainingIgnoreCaseOrMensagemPvaContainingIgnoreCase`: Faz um `SELECT` procurando se o termo digitado pelo usuário existe no código, no campo OU na mensagem do PVA, ignorando maiúsculas e minúsculas!

---

### 4.5 Camada de Transferência (DTO - Data Transfer Object)

#### 📁 `src/main/java/com/brenon/validador_sped/dto/ErroTraducaoDTO.java`
- **O que faz:** Formato dos dados retornados na **pesquisa manual**.
- **O Código:**
  ```java
  package com.brenon.validador_sped.dto;

  public record ErroTraducaoDTO(
      Long id,
      String codigoErro,
      String campo,
      String mensagemPva,
      String mensagemTraduzida,
      String orientacaoCorrecao
  ) {}
  ```
- **O que é um `record` no Java?**
  - O `record` é um recurso moderno do Java (introduzido recentemente). Em vez de escrever 50 linhas com construtores, getters, `equals()`, `hashCode()` e `toString()`, você escreve apenas a lista de campos em uma linha. O Java gera tudo nos bastidores.
  - Além disso, um `record` é **imutável**: uma vez criado, seus valores não podem ser alterados por engano.

#### 📁 `src/main/java/com/brenon/validador_sped/dto/ResultadoAnaliseDTO.java`
- **O que faz:** Formato dos dados retornados quando o usuário **faz upload de um arquivo SPED**.
- **Campos adicionais em relação ao anterior:**
  - `int numeroLinha`: O número exato da linha do arquivo `.txt` onde o erro foi identificado.
  - `String codigoRegistro`: O bloco ou registro do SPED (ex: `0000`, `C100`, `C170`, `M200`).
  - Além do código, campo, mensagem PVA, tradução e orientação.

---

### 4.6 Camada de Tratamento de Erros e Exceções

#### 📁 `src/main/java/com/brenon/validador_sped/exception/ArquivoInvalidoException.java`
- **O que faz:** É uma **exceção personalizada**.
- **Por que criamos ela?** Se o usuário tentar enviar uma foto `.jpg`, uma planilha `.xlsx` ou um arquivo de texto vazio, nós não deixamos o sistema quebrar feio com erros misteriosos. Nós disparamos essa exceção específica com uma mensagem amigável:
  ```java
  throw new ArquivoInvalidoException("Formato de arquivo inválido. Apenas arquivos .txt são aceitos.");
  ```

#### 📁 `src/main/java/com/brenon/validador_sped/exception/GlobalExceptionHandler.java`
- **O que faz:** É o "bombeiro" da aplicação.
- **Como funciona:**
  - A anotação `@ControllerAdvice` faz esta classe monitorar **todos os Controllers** da aplicação 24 horas por dia.
  - Se algum erro for disparado em qualquer parte do sistema, em vez de mostrar aquela tela branca com erro 500 feio e texto técnico (Stack Trace) para o usuário, o `GlobalExceptionHandler` captura o erro e devolve um JSON padronizado e limpo:
  ```json
  {
    "message": "Por favor, selecione um arquivo SPED válido."
  }
  ```
  - Também trata arquivos que ultrapassem os 20MB com `@ExceptionHandler(MaxUploadSizeExceededException.class)`.

---

### 4.7 Camada de Regras de Negócio (Service)

#### 📁 `src/main/java/com/brenon/validador_sped/service/SpedProcessadorService.java`
- **O que faz:** **É o coração e cérebro de todo o sistema!** Toda a inteligência da leitura do SPED, do processamento do texto e da tradução acontece aqui.
- **Vamos dissecar as partes fundamentais dele:**

1. **Injeção do Repository via Construtor:**
   ```java
   private final ErroTraduzidoRepository repository;

   public SpedProcessadorService(ErroTraduzidoRepository repository) {
       this.repository = repository;
   }
   ```
   > O Spring entrega o repositório pronto para o serviço poder consultar o banco.

2. **O Método `processarArquivo(MultipartFile file)`:**
   - **Passo 1: Validação prévia:** Chama `validarArquivo(file)`. Se for vazio ou não for `.txt`, aborta imediatamente.
   - **Passo 2: Otimização de Performance (Prevenção do problema N+1):**
     ```java
     List<ErroTraduzido> todosErros = repository.findAll();
     ```
     > Se o arquivo SPED tiver 10.000 linhas, fazer 10.000 consultas no banco de dados deixaria a aplicação lenta. Por isso, o código carrega **todos os erros na memória RAM de uma única vez** em uma única consulta!
   - **Passo 3: Leitura com Charset Correto:**
     ```java
     try (BufferedReader reader = new BufferedReader(
             new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1)))
     ```
     > O SPED Fiscal brasileiro usa por padrão a codificação `ISO-8859-1` (também conhecida como Latin-1 ou Windows-1252) para acentos e caracteres em português (`ç`, `ã`, `é`). Usar `BufferedReader` garante que arquivos gigantes não sobrecarreguem a memória, lendo linha por linha.
   - **Passo 4: O Quebrador de Pipes (`split`):**
     ```java
     String[] campos = linha.split("\\|", -1);
     ```
     > No SPED, os dados são separados pelo caractere `|`. O parâmetro `-1` é crucial: ele garante que se houver campos vazios no final da linha (ex: `|campo1|||`), o Java não ignore esses campos.
   - **Passo 5: Identificação do Registro e Cruzamento:**
     No padrão SPED, a primeira barra começa na posição 0, e o código do registro (ex: `0000`, `C100`, `C170`) fica no índice 1 (`campos[1]`). O serviço compara o registro com a base de erros e, se encontrar correspondência, cria um `ResultadoAnaliseDTO` e adiciona na lista de resultados!

3. **O Método `pesquisarPorTermo(String termo)`:**
   - Valida se o usuário não digitou texto em branco.
   - Dispara a consulta abrangente no banco de dados.
   - Converte as entidades encontradas em DTOs (`ErroTraducaoDTO`) e devolve.

---

### 4.8 Camada de Controle / API REST (Controller)

#### 📁 `src/main/java/com/brenon/validador_sped/controller/SpedController.java`
- **O que faz:** É a porta de entrada da API. É quem escuta os pedidos da internet e entrega as respostas.
- **Anotações Chave:**
  - `@RestController`: Informa que as respostas dos métodos serão devolvidas automaticamente em formato **JSON**.
  - `@RequestMapping("/api/sped")`: Todos os links desta classe começam com `/api/sped`.
- **Os Dois Endpoints (Rotas):**

1. **`POST /api/sped/upload`:**
   ```java
   @PostMapping("/upload")
   public ResponseEntity<List<ResultadoAnaliseDTO>> analisarArquivoSped(
           @RequestParam("file") MultipartFile file) {
       List<ResultadoAnaliseDTO> resultados = service.processarArquivo(file);
       return ResponseEntity.ok(resultados);
   }
   ```
   > Recebe o arquivo enviado pelo formulário HTML através de um multipart request, entrega para o `SpedProcessadorService` e devolve a lista de erros encontrados em código HTTP 200 (OK).

2. **`GET /api/sped/pesquisar?codigo=...`:**
   ```java
   @GetMapping("/pesquisar")
   public ResponseEntity<List<ErroTraducaoDTO>> pesquisarPorTermo(
           @RequestParam("codigo") String codigo) {
       List<ErroTraducaoDTO> erros = service.pesquisarPorTermo(codigo);
       return ResponseEntity.ok(erros);
   }
   ```
   > Recebe o parâmetro `codigo` passado na URL (ex: `codigo=COD_CTA`) e devolve a lista de erros encontrados.

---

### 4.9 Camada de Apresentação / Frontend

#### 📁 `src/main/resources/static/index.html`
- **Onde fica:** Na pasta `src/main/resources/static/`. Qualquer arquivo colocado aqui é servido diretamente pelo Spring Boot como página web estática na raiz `http://localhost:8080/`.
- **Tecnologias Utilizadas:**
  - **HTML5 semântico**: Estrutura da página.
  - **Bootstrap 5 (CSS & Icons)**: Estilização moderna, responsiva e profissional sem precisar escrever centenas de linhas de CSS na mão.
  - **JavaScript Puro (Vanilla JS)**: Comunicação assíncrona com o backend via API `fetch` moderna.
- **Principais Componentes da Tela:**
  1. **Navbar com tema azul fiscal**: Título e ícone de documento.
  2. **Abas (Nav-pills)**: Alterna entre a visualização de "Importar Arquivo SPED" e "Pesquisar Erro Manualmente".
  3. **Área de Drag & Drop / Upload**: Caixa estilizada que permite clicar ou arrastar o arquivo `.txt`.
  4. **Função `enviarArquivo()` no JS**:
     - Cria um objeto `FormData`.
     - Anexa o arquivo selecionado.
     - Mostra um indicador de carregamento (*spinner*).
     - Envia via `fetch('/api/sped/upload', { method: 'POST', body: formData })`.
     - Chama `exibirResultados()` para desenhar os cards na tela.
  5. **Função `pesquisarErroManual()` no JS**:
     - Captura o texto digitado (ex: `0000`, `UNID`).
     - Chama `fetch('/api/sped/pesquisar?codigo=' + codigo)`.
     - Desenha os cards explicativos na tela.
  6. **Cards de Erro Formatados**:
     - Exibem distintivos (*badges*) com o número da linha e o registro do SPED.
     - Exibem o Código do Erro, Campo, Mensagem do PVA, Tradução Humanizada e a Orientação de Correção em destaque azul.

---

### 4.10 Testes Automatizados

#### 📁 `src/test/java/com/brenon/validador_sped/ValidadorSpedApplicationTests.java`
- **O que faz:** É a classe de teste padrão criada pelo Spring.
- **O que ela testa:** O método `contextLoads()` verifica se a aplicação consegue subir, carregar as propriedades, inicializar o banco H2 e conectar todas as classes sem dar erro. Se este teste passar, significa que a arquitetura do projeto está íntegra.

---

## 5. O Ciclo de Vida de uma Operação (Passo a Passo Visual)

Para você nunca mais ter dúvidas de como as coisas funcionam juntas, acompanhe a jornada de um arquivo:

### 5.1 O que acontece quando você clica em "Analisar Arquivo SPED":
```text
1. O Usuário seleciona o arquivo "test_sped.txt" e clica no botão azul.
   │
2. [index.html / JavaScript]
   Pega o arquivo, empacota num FormData e faz: POST /api/sped/upload
   │
3. [SpedController.java]
   O Spring recebe a requisição HTTP e extrai o MultipartFile.
   Chama: service.processarArquivo(file)
   │
4. [SpedProcessadorService.java]
   - Valida se o arquivo termina com .txt e não está vazio.
   - Chama repository.findAll() e carrega todos os erros do banco para a memória RAM.
   - Abre o arquivo usando BufferedReader com codificação ISO-8859-1.
   - Lê linha 1: |0000|0|01012026|...
   - Divide com split("\\|") e pega o registro (ex: "0000").
   - Compara com a lista de erros da memória. Se casar, cria o DTO com linha e tradução.
   - Retorna a lista de ResultadoAnaliseDTO.
   │
5. [SpedController.java]
   Devolve a lista dentro de um ResponseEntity.ok(resultados) com código HTTP 200.
   O Spring converte a lista Java automaticamente para um texto JSON.
   │
6. [index.html / JavaScript]
   Recebe o JSON, esconde o spinner de carregamento e cria dinamicamente 
   os cards HTML com as mensagens traduzidas na tela.
```

---

## 6. Como Executar e Testar o Projeto

### 6.1 Rodando pelo Terminal (PowerShell ou Prompt de Comando)
Abra o terminal na pasta `Projeto\validador-sped`:
```powershell
cd "c:\Users\Desktop\OneDrive\Área de Trabalho\TCC - Copia\Projeto\validador-sped"
.\mvnw.cmd spring-boot:run
```
*(No Linux ou Mac seria `./mvnw spring-boot:run`)*

O Spring Boot vai compilar os arquivos e no final mostrará:
```text
Tomcat started on port 8080 (http) with context path '/'
Started ValidadorSpedApplication in 2.345 seconds
```

### 6.2 Acessando no Navegador
1. **A Aplicação:** Abra o navegador e acesse:
   👉 **`http://localhost:8080`**
2. **O Console do Banco de Dados H2:**
   👉 **`http://localhost:8080/h2-console`**
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **User Name:** `sa`
   - **Password:** *(deixe em branco)*
   - Clique em **Connect**. Você poderá ver a tabela `ERROS_TRADUZIDOS` e rodar consultas SQL diretamente!

---

## 7. Guia Prático: Como Fazer Modificações Sozinho (Receitas Prontas)

Aqui está o que você mais precisa: tutoriais do tipo *"Copie e Cole / Faça Você Mesmo"* para editar o sistema com confiança.

### 7.1 Receita 1: Como Adicionar uma Nova Tradução de Erro Fiscal
Se você descobriu um novo erro comum do PVA e quer que o sistema passe a traduzi-lo, você só precisa alterar um único arquivo: **`src/main/resources/data.sql`**.

1. Abra o arquivo `data.sql`.
2. Vá até o final do arquivo e adicione um novo bloco `INSERT`:
```sql
-- Erro: CST de ICMS Incompatível com CFOP
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'CST_ICMS', 
    'CST_ICMS', 
    'CST incompatível com a operação informada no CFOP.', 
    'O código de tributação do ICMS informado no item não pode ser utilizado com este CFOP de operação.', 
    '1. Abra o cadastro do item no ControlGás.\n2. Verifique na tabela de correlação fiscal o CST correto para este CFOP.\n3. Reexporte o arquivo SPED.'
);
```
3. Salve o arquivo e reinicie a aplicação. Pronto! O erro já pode ser pesquisado manualmente e será detectado no upload!

---

### 7.2 Receita 2: Como Criar um Novo Endpoint na API REST
Digamos que você queira criar uma rota que lista **todos** os erros cadastrados na base, para que você possa montar um catálogo completo no futuro.

1. **Abra o arquivo `SpedProcessadorService.java`** e adicione o método:
```java
public List<ErroTraducaoDTO> listarTodos() {
    return repository.findAll().stream()
            .map(e -> new ErroTraducaoDTO(
                    e.getId(),
                    e.getCodigoErro(),
                    e.getCampo(),
                    e.getMensagemPva(),
                    e.getMensagemTraduzida(),
                    e.getOrientacaoCorrecao()
            ))
            .toList();
}
```

2. **Abra o arquivo `SpedController.java`** e adicione a rota pública:
```java
@GetMapping("/todos")
public ResponseEntity<List<ErroTraducaoDTO>> listarTodosOsErros() {
    return ResponseEntity.ok(service.listarTodos());
}
```
3. Salve e teste no navegador: `http://localhost:8080/api/sped/todos`. Ele devolverá todos os erros em formato JSON!

---

### 7.3 Receita 3: Como Alterar as Cores e Textos da Interface
Tudo o que diz respeito ao visual fica dentro de **`src/main/resources/static/index.html`**.

- **Para mudar o título superior:** Procure a linha 48:
  ```html
  Validador SPED &mdash; Tradutor de Erros
  ```
- **Para mudar a cor do cabeçalho:** Procure na tag `<style>` (linha 15):
  ```css
  .navbar {
      background: linear-gradient(135deg, #1e3c72 0%, #2a5298 100%);
  }
  ```
  Você pode substituir `#1e3c72` e `#2a5298` por qualquer cor hexadecimal de sua preferência (ex: tons de verde, grafite ou roxo).
- **Para mudar o texto de instrução:** Procure na linha 59 e edite a mensagem diretamente no HTML.

---

### 7.4 Receita 4: Como Adicionar um Novo Campo no Sistema (Ex: "Gravidade do Erro")
Digamos que a sua banca do TCC peça: *"Brenon, seria legal classificar se o erro é 'CRÍTICO' ou 'ADVERTÊNCIA'"*. 
Veja o passo a passo completo:

1. **No Banco (`data.sql`):** Adicione a coluna nos `INSERTs`:
   ```sql
   INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao, gravidade)
   VALUES ('UN', 'UNID', '...', '...', '...', 'CRÍTICO');
   ```

2. **Na Entidade (`model/ErroTraduzido.java`):**
   - Adicione o atributo:
     ```java
     @Column(name = "gravidade", length = 20)
     private String gravidade;
     ```
   - Crie o Getter e o Setter:
     ```java
     public String getGravidade() { return gravidade; }
     public void setGravidade(String gravidade) { this.gravidade = gravidade; }
     ```

3. **Nos DTOs (`dto/ErroTraducaoDTO.java` e `dto/ResultadoAnaliseDTO.java`):**
   - Adicione `String gravidade` na lista de campos do record.

4. **No Service (`SpedProcessadorService.java`):**
   - Ao instanciar os DTOs, passe `erro.getGravidade()`.

5. **Na Tela (`index.html`):**
   - Na função `exibirResultados`, mostre o campo:
     ```javascript
     <strong>Gravidade:</strong> ${item.gravidade || 'Informativo'} <br>
     ```

---

## 8. Glossário de Termos Essenciais

Para você consultar rapidamente quando estiver lendo código ou se preparando para apresentar o TCC:

| Termo | O que significa de verdade? |
| :--- | :--- |
| **API REST** | Um padrão de comunicação que permite que um frontend (página web) converse com um backend (Java) trocando mensagens em formato de texto JSON. |
| **Bean** | Qualquer objeto que é criado, configurado e gerenciado pelo motor do Spring Boot. |
| **Controller** | A classe Java responsável por escutar requisições vindas da internet (GET, POST) e responder ao usuário. |
| **Service** | A classe Java onde você coloca a lógica e os cálculos do seu programa. |
| **Repository** | A classe ou interface responsável por conversar com o banco de dados. |
| **Entity** | Uma classe Java cujas variáveis viram colunas de uma tabela do banco de dados. |
| **DTO (Data Transfer Object)** | Uma classe simples usada apenas para transportar dados entre diferentes partes do sistema sem expor as tabelas do banco. |
| **H2 Database** | Um banco de dados relacional SQL ultrarrápido feito em Java que roda dentro da memória da aplicação. |
| **PVA** | Programa Validador e Assinador da Receita Federal do Brasil para o SPED. |
| **Pipe (`\|`)** | O caractere de barra vertical usado para separar campos no arquivo texto do SPED Fiscal. |
| **Charset (ISO-8859-1)** | O padrão de caracteres que preserva letras acentuadas da língua portuguesa no SPED. |
| **Maven Wrapper (`mvnw`)** | Um script que baixa a versão exata do Maven necessária para o projeto, sem que você precise instalar nada manualmente no Windows. |

---

> **Dica Final:** Guarde este arquivo como se fosse ouro. Sempre que bater uma dúvida ou você esquecer para que serve uma anotação ou classe, volte a este manual e consulte o sumário!
