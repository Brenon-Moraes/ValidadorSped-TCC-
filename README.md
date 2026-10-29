# 📊 Validador SPED — Tradutor e Assistente de Erros Fiscais (TCC)

> **Trabalho de Conclusão de Curso (TCC)**  
> **Autor:** Brenon Moraes  
> **Repositório:** [ValidadorSped-TCC-](https://github.com/Brenon-Moraes/ValidadorSped-TCC-)

---

## 🎯 Sobre o Projeto

O **Validador SPED** é uma solução de software desenvolvida para solucionar uma das maiores dificuldades enfrentadas por escritórios contábeis, empresas e analistas de suporte de sistemas ERP: a interpretação e resolução dos erros técnicos apontados pelo **PVA (Programa Validador e Assinador)** da Receita Federal na escrituração do SPED Fiscal.

O sistema atua como uma camada de inteligência e tradução, transformando mensagens crípticas da Receita em orientações de correção claras e práticas, com instruções passo a passo para sistemas ERP como **ControlGás** e sistemas contábeis como **FISCAL3c**.

---

## 🚀 Funcionalidades Principais

1. **Importação e Análise de Arquivos SPED (`.txt`):**
   * Upload do arquivo SPED diretamente pelo navegador via formulário multipart.
   * Leitura e parsing de registros linha a linha com suporte a codificação fiscal `ISO-8859-1`.
   * Cruzamento inteligente em memória dos códigos de registros e erros fiscais.
   * Exibição visual com badges de número de linha e registro afetado.

2. **Pesquisa Manual de Erros:**
   * Busca rápida e abrangente por termo (código de erro, nome do campo ou mensagem do PVA).
   * Consulta instantânea sem necessidade de possuir o arquivo completo em mãos.

3. **Console do Banco de Dados H2 Embutido:**
   * Acesso visual ao banco em memória para auditoria e acompanhamento de consultas SQL.

---

## 🛠️ Tecnologias Utilizadas

* **Linguagem:** Java 25
* **Framework:** Spring Boot 4.1.1
* **Camada de Dados & Persistência:** Spring Data JPA / Hibernate
* **Banco de Dados:** H2 Database Engine (In-Memory com inicialização via `data.sql`)
* **Tratamento de Exceções:** `@ControllerAdvice` global com logs via SLF4J
* **Frontend:** HTML5, CSS3, Bootstrap 5, Bootstrap Icons e JavaScript Moderno (Fetch API assíncrona)
* **Gerenciador de Build:** Apache Maven com Maven Wrapper (`mvnw`)

---

## 📂 Estrutura do Repositório

```text
├── Artigo_TCC_SPED_Brenon.docx            # Artigo científico do TCC
├── PropostaTCCBrenon.docx                 # Proposta original do projeto
├── Proposta_TCC_ABNT_Brenon.docx          # Proposta formatada em normas ABNT
├── MANUAL_COMPLETO_DO_PROJETO_E_GUIA_JAVA.md # Guia didático completo de Java e da arquitetura
├── test_sped.txt                          # Arquivo SPED de exemplo para testes
├── artigos_Referencia/                    # Artigos científicos e bibliografia de apoio
└── Projeto/
    └── validador-sped/                    # Código-fonte completo da aplicação Spring Boot
        ├── pom.xml
        ├── mvnw / mvnw.cmd
        └── src/
            ├── main/
            │   ├── java/com/brenon/validador_sped/
            │   │   ├── ValidadorSpedApplication.java
            │   │   ├── controller/SpedController.java
            │   │   ├── service/SpedProcessadorService.java
            │   │   ├── repository/ErroTraduzidoRepository.java
            │   │   ├── model/ErroTraduzido.java
            │   │   ├── dto/ (ErroTraducaoDTO, ResultadoAnaliseDTO)
            │   │   └── exception/ (GlobalExceptionHandler, ArquivoInvalidoException)
            │   └── resources/
            │       ├── application.properties
            │       ├── data.sql
            │       └── static/index.html
            └── test/
```

---

## 💻 Como Executar o Projeto Localmente

### Pré-requisitos:
* **Java Development Kit (JDK):** Versão 21 ou superior (recomendado JDK 25)
* Conexão com a internet para o primeiro download das dependências pelo Maven Wrapper.

### Passo a passo:

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/Brenon-Moraes/ValidadorSped-TCC-.git
   cd ValidadorSped-TCC-
   ```

2. **Acesse a pasta do projeto Java:**
   ```bash
   cd Projeto/validador-sped
   ```

3. **Inicie a aplicação via Maven Wrapper:**
   * No Windows (PowerShell / CMD):
     ```powershell
     .\mvnw.cmd spring-boot:run
     ```
   * No Linux / macOS:
     ```bash
     ./mvnw spring-boot:run
     ```

4. **Acesse no navegador:**
   * Aplicação: [http://localhost:8080](http://localhost:8080)
   * Console H2 Database: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
     * *JDBC URL:* `jdbc:h2:mem:testdb`
     * *User Name:* `sa`
     * *Password:* *(deixar em branco)*

---

## 📖 Documentação & Aprendizado

Para um estudo aprofundado sobre o funcionamento de cada linha de código, explicações pedagógicas sobre Java/Spring Boot e receitas práticas de como adicionar novos erros ou funcionalidades, consulte o documento:
👉 **[MANUAL_COMPLETO_DO_PROJETO_E_GUIA_JAVA.md](MANUAL_COMPLETO_DO_PROJETO_E_GUIA_JAVA.md)**

---

## 👨‍💻 Autor

Desenvolvido por **Brenon Moraes** como projeto de Trabalho de Conclusão de Curso (TCC).
