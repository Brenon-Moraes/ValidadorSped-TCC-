package com.brenon.validador_sped.controller;

import com.brenon.validador_sped.dto.ErroTraducaoDTO;
import com.brenon.validador_sped.dto.ResultadoAnaliseDTO;
import com.brenon.validador_sped.service.SpedProcessadorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Controlador REST para operações de validação e tradução de erros do SPED.
 *
 * <p>Expõe dois endpoints:</p>
 * <ul>
 *   <li>{@code POST /api/sped/upload} — Upload e análise do arquivo SPED.</li>
 *   <li>{@code GET /api/sped/pesquisar} — Pesquisa manual por código ou termo.</li>
 * </ul>
 *
 * <p>Toda a lógica de negócio é delegada ao {@link SpedProcessadorService},
 * seguindo o Princípio da Responsabilidade Única (SRP). O tratamento de exceções
 * é centralizado no {@code GlobalExceptionHandler}.</p>
 *
 * @author Brenon
 */
@RestController
@RequestMapping("/api/sped")
public class SpedController {

    private static final Logger logger = LoggerFactory.getLogger(SpedController.class);

    private final SpedProcessadorService service;

    /**
     * Injeção de dependência via construtor (prática recomendada pelo Spring).
     */
    public SpedController(SpedProcessadorService service) {
        this.service = service;
    }

    /**
     * Endpoint para upload e processamento do arquivo SPED.
     *
     * <p>Recebe o arquivo via multipart, processa linha a linha e retorna
     * a lista de erros encontrados com suas traduções e orientações.</p>
     *
     * @param file arquivo SPED (.txt) enviado pelo frontend
     * @return lista de {@link ResultadoAnaliseDTO} com os erros traduzidos
     */
    @PostMapping("/upload")
    public ResponseEntity<List<ResultadoAnaliseDTO>> analisarArquivoSped(
            @RequestParam("file") MultipartFile file) {

        logger.info("Recebido arquivo para análise: '{}' ({} bytes)",
                file.getOriginalFilename(), file.getSize());

        List<ResultadoAnaliseDTO> resultados = service.processarArquivo(file);

        if (resultados.isEmpty()) {
            logger.info("Nenhum erro mapeado encontrado no arquivo: '{}'",
                    file.getOriginalFilename());
        }

        return ResponseEntity.ok(resultados);
    }

    /**
     * Endpoint para pesquisa manual de erros por código, campo ou mensagem PVA.
     *
     * @param codigo termo de busca informado pelo usuário
     * @return lista de {@link ErroTraducaoDTO} com os resultados encontrados
     */
    @GetMapping("/pesquisar")
    public ResponseEntity<List<ErroTraducaoDTO>> pesquisarPorTermo(
            @RequestParam("codigo") String codigo) {

        List<ErroTraducaoDTO> erros = service.pesquisarPorTermo(codigo);
        return ResponseEntity.ok(erros);
    }
}
