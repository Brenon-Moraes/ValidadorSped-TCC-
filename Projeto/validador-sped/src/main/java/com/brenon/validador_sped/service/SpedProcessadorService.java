package com.brenon.validador_sped.service;

import com.brenon.validador_sped.dto.ErroTraducaoDTO;
import com.brenon.validador_sped.dto.ResultadoAnaliseDTO;
import com.brenon.validador_sped.exception.ArquivoInvalidoException;
import com.brenon.validador_sped.model.ErroTraduzido;
import com.brenon.validador_sped.repository.ErroTraduzidoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Serviço responsável pela lógica de negócio do processamento de arquivos SPED.
 *
 * <p>Contém duas funcionalidades principais:</p>
 * <ul>
 *   <li><b>Processamento de arquivo:</b> Lê o arquivo SPED linha a linha,
 *       faz o parsing dos campos separados por pipe ({@code |}) e cruza com
 *       a base de erros conhecidos em memória.</li>
 *   <li><b>Pesquisa manual:</b> Busca por termo nos campos código de erro,
 *       campo e mensagem PVA.</li>
 * </ul>
 *
 * @author Brenon
 */
@Service
public class SpedProcessadorService {

    private static final Logger logger = LoggerFactory.getLogger(SpedProcessadorService.class);

    private final ErroTraduzidoRepository repository;

    /**
     * Injeção de dependência via construtor (prática recomendada pelo Spring).
     */
    public SpedProcessadorService(ErroTraduzidoRepository repository) {
        this.repository = repository;
    }

    /**
     * Processa o arquivo SPED enviado pelo usuário.
     *
     * <p>Fluxo de execução:</p>
     * <ol>
     *   <li>Valida o arquivo (formato, extensão).</li>
     *   <li>Carrega todos os erros do banco em memória (1 query — evita problema N+1).</li>
     *   <li>Lê o arquivo linha a linha em ISO-8859-1 (encoding padrão SPED).</li>
     *   <li>Para cada linha, extrai o código do registro e cruza com a base de erros.</li>
     *   <li>Retorna a lista de erros traduzidos encontrados como DTOs.</li>
     * </ol>
     *
     * @param file arquivo SPED (.txt) enviado via multipart
     * @return lista de {@link ResultadoAnaliseDTO} com os erros encontrados
     * @throws ArquivoInvalidoException se o arquivo for inválido ou houver erro de I/O
     */
    public List<ResultadoAnaliseDTO> processarArquivo(MultipartFile file) {
        validarArquivo(file);

        logger.info("Iniciando processamento do arquivo SPED: '{}' ({} bytes)",
                file.getOriginalFilename(), file.getSize());

        // Carrega todos os erros em memória uma única vez (otimização N+1)
        List<ErroTraduzido> todosErros = repository.findAll();
        List<ResultadoAnaliseDTO> resultados = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.ISO_8859_1))) {

            String linha;
            int numeroLinha = 0;

            while ((linha = reader.readLine()) != null) {
                numeroLinha++;
                if (linha.trim().isEmpty()) continue;

                // O parâmetro -1 garante que campos vazios não sejam descartados pelo split
                String[] campos = linha.split("\\|", -1);

                // No formato SPED, o código do registro fica no índice 1 (ex: |0000|, |C100|)
                if (campos.length > 1) {
                    String codigoRegistro = campos[1].trim();
                    if (codigoRegistro.isEmpty()) continue;

                    // Busca em memória por erros cujo codigoErro contenha o código do registro
                    String codigoUpper = codigoRegistro.toUpperCase();

                    for (ErroTraduzido erro : todosErros) {
                        if (erro.getCodigoErro() != null
                                && erro.getCodigoErro().toUpperCase().contains(codigoUpper)) {
                            resultados.add(new ResultadoAnaliseDTO(
                                    numeroLinha,
                                    codigoRegistro,
                                    erro.getCodigoErro(),
                                    erro.getCampo(),
                                    erro.getMensagemPva(),
                                    erro.getMensagemTraduzida(),
                                    erro.getOrientacaoCorrecao()
                            ));
                        }
                    }
                }
            }

            logger.info("Arquivo '{}' processado com sucesso: {} linhas lidas, {} erros mapeados.",
                    file.getOriginalFilename(), numeroLinha, resultados.size());

        } catch (IOException e) {
            logger.error("Erro de I/O ao processar arquivo SPED: '{}'", file.getOriginalFilename(), e);
            throw new ArquivoInvalidoException(
                    "Erro ao ler o arquivo. Verifique se o formato é válido.", e);
        }

        return resultados;
    }

    /**
     * Pesquisa erros na base de dados por termo (busca abrangente).
     *
     * <p>O termo é pesquisado simultaneamente nos campos:
     * {@code codigoErro}, {@code campo} e {@code mensagemPva} (case-insensitive).</p>
     *
     * @param termo código, campo ou mensagem do PVA para buscar
     * @return lista de {@link ErroTraducaoDTO} com os resultados
     * @throws ArquivoInvalidoException se o termo for vazio ou nulo
     */
    public List<ErroTraducaoDTO> pesquisarPorTermo(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            throw new ArquivoInvalidoException("Informe um código ou termo válido para busca.");
        }

        String termoBusca = termo.trim();
        logger.info("Pesquisa manual por termo: '{}'", termoBusca);

        List<ErroTraduzido> erros = repository
                .findByCodigoErroContainingIgnoreCaseOrCampoContainingIgnoreCaseOrMensagemPvaContainingIgnoreCase(
                        termoBusca, termoBusca, termoBusca);

        logger.info("Pesquisa por '{}': {} resultado(s) encontrado(s).", termoBusca, erros.size());

        return erros.stream()
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

    /**
     * Valida formato e integridade do arquivo SPED antes do processamento.
     *
     * @param file arquivo enviado pelo usuário
     * @throws ArquivoInvalidoException se o arquivo estiver vazio ou com extensão inválida
     */
    private void validarArquivo(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ArquivoInvalidoException("Por favor, selecione um arquivo SPED válido.");
        }

        String nomeArquivo = file.getOriginalFilename();
        if (nomeArquivo == null || !nomeArquivo.toLowerCase().endsWith(".txt")) {
            throw new ArquivoInvalidoException(
                    "Formato de arquivo inválido. Apenas arquivos .txt são aceitos.");
        }
    }
}
