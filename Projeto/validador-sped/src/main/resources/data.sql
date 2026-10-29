-- Erro: Unidade de Medida
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'UN', 
    'UNID', 
    'Cadastro de Unidade de Medida não encontrado no arquivo.', 
    'A unidade de medida informada nos itens da nota não está cadastrada na tabela de unidades do sistema fiscal.', 
    '1. Verifique no sistema Fiscal se a sigla da unidade de medida está cadastrada igual ao ControlGás (se não existir, crie).\n2. Adicione uma Unidade Tributável no cadastro da unidade (pode ter o mesmo nome/sigla).\n3. Reเกer/reexporte o arquivo SPED.'
);

-- Erro: Indicador de Operação
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'IND_OPR', 
    'IND_OPER', 
    'Indicador de Operação diferente de 1 (Saída).', 
    'Existe lançamento de NFC-e (Modelo 65) ou NF-e de entrada que não é permitido para empresas contribuintes do ICMS.', 
    'Verifique as notas de entrada. Empresas contribuintes do ICMS não podem escriturar NFC-e como entrada de operação regular.'
);

-- Erro: Chave NFe x CNPJ Emitente
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'CHV_NFE', 
    'CHV_NFE', 
    'CNPJ da chave de acesso difere do CNPJ do emitente.', 
    'A chave da NF-e contém um CNPJ diferente do emitente cadastrado. Ocorre frequentemente quando o emitente é Pessoa Física utilizando o app da SEFAZ (onde a chave contém o CNPJ da SEFAZ).', 
    '1. No ControlGás, localize a nota fiscal.\n2. Altere o modelo da nota para "01 - Avulsa".\n3. Recalcule os tributos, salve e reexporte o arquivo SPED.'
);

-- Erro: Série Divergente
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'SER', 
    'SER', 
    'Série do documento diferente da informada na Chave de Acesso.', 
    'A série informada na escrituração do documento não coincide com os dígitos de série gravados na Chave de Acesso de 44 dígitos.', 
    '1. Verifique a série correta informada no campo "VALOR ESPERADO" do relatório de erros do PVA.\n2. Altere a série do lançamento no ControlGás/ERP para corresponder à chave e reexporte.'
);

-- Erro: Cadastro do Participante / Cidade
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'COD_PART', 
    'COD_PART', 
    'Inconsistência ou dados inválidos no cadastro do participante (Cliente/Fornecedor).', 
    'O município ou código IBGE do participante cadastrado no sistema Fiscal está divergente do ControlGás ou é inválido.', 
    '1. Acesse o cadastro de pessoas no FISCAL3c pelo código do participante.\n2. Acerte o município escolhendo exatamente o mesmo cadastrado no ControlGás.\n3. Gere o SPED novamente.'
);

-- Erro: Natureza da Receita (PIS/COFINS)
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'NAT_REC', 
    'NAT_REC', 
    'Código da Natureza da Receita inválido ou não informado para operação isenta/NT/alíquota zero.', 
    'Notas com CST de PIS/COFINS sem tributação exigem a informação da Natureza da Receita correspondente.', 
    '1. Verifique se o produto (ex: água) está com CST incorreto (ex: CST 08 em vez de CST 04).\n2. Informe o código de receita padrão (ex: 416 ou 999).\n3. Caso haja mais de um registro inválido na mesma nota, consolide/some os valores em apenas um registro e exclua o excedente.'
);

-- Erro: Conta Contábil não encontrada
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'COD_CTA', 
    'COD_CTA', 
    'Código da Conta Analítica Contábil não cadastrado no Plano de Contas (Registro 0500).', 
    'A conta contábil vinculada à operação ou ao item não foi localizada no plano de contas escriturado.', 
    '1. Cadastre a conta no Plano de Contas com os seguintes dados:\n   - Data de Inclusão: 01/01/2000\n   - Código: Utilizar o sugerido ou um código livre\n   - Tipo: Analítico | Natureza: 04 | Nível: 04\n2. Vincule a conta criada à Natureza de Operação ou item.'
);

-- Erro: Data do Documento
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'DT_DOC', 
    'DT_DOC', 
    'Data de emissão do documento inválida ou fora do período de escrituração.', 
    'A data do documento fiscal está divergente do período de apuração ou do movimento.', 
    'Ajuste a data da nota para corresponder exatamente à data do movimento (visível na tela/janela do sistema fiscal).'
);

-- Erro: Valor da Operação
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'VL_OPR', 
    'VL_OPR', 
    'Valor da operação não informado ou divergente da soma dos itens.', 
    'O valor do documento fiscal está zerado ou em desacordo com os itens lançados.', 
    'Incatualize o valor total da nota de acordo com o painel de notas (valor destacado em azul). Nota: Geralmente ocorre em conjunto com o erro do campo VL_RED_BC.'
);

-- Erro: Valor da Redução da Base de Cálculo
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'VL_RED_BC', 
    'VL_RED_BC', 
    'Valor da Redução da Base de Cálculo em desacordo com o CST/Tributação.', 
    'O valor de redução de BC foi preenchido incorretamente para uma operação com tributação integral ou isenta.', 
    'Verifique no ControlGás a tributação do CST da nota. Se a tributação estiver zerada/sem redução, zere também o campo VL_RED_BC.'
);

-- Erro: Chave do Documento Eletrônico
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'CHV_DOCE', 
    'CHV_DOCE', 
    'Chave de documento eletrônico informada em documento que não é NF-e/NFC-e.', 
    'Foi preenchida chave de acesso de 44 dígitos em modelo de nota não eletrônico (ex: nota manual modelo 1/1A).', 
    'Remova a chave de acesso do lançamento, pois o campo só deve ser preenchido para NF-e/NFC-e (modelos 55/65).'
);

-- Erro: Código de Ajuste de Tamanho Inválido (C197)
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'COD_AJ', 
    'COD_AJ', 
    'Tamanho do campo do código de ajuste ou observação fiscal inválido.', 
    'O código de ajuste da obrigação acessória/ICMS no registro C197 deve conter exatamente 10 caracteres e está com tamanho diferente.', 
    'Abra o arquivo .txt do SPED em um editor de texto e substitua a tag incorreta. Exemplo: substituir "|C197|RJ801130|" por "|C197|RJ90980000|".'
);

-- Erro: Registro Filho não Encontrado (C170/C190)
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'REG_FILHO', 
    'REG', 
    'Registro filho obrigatório não encontrado para o documento fiscal.', 
    'Falta o detalhamento dos itens (C170) ou o analítico do documento (C190).', 
    '1. Abra a nota no ControlGás e verifique a tributação.\n2. Adicione manualmente o registro filho no PVA com as informações de tributação.\n3. Se der erro de CFOP inválido/não encontrado, utilize o CFOP "060" para operações cobradas por ST.'
);

-- Erro: Inscrição Estadual e Telefone/Fax
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'IE_FONE_FAX', 
    'IE / FONE', 
    'Inscrição Estadual, Telefone ou Fax inválido ou não preenchido.', 
    'Falta de preenchimento ou digitação incorreta nos dados cadastrais do estabelecimento/participante.', 
    '1. Verifique e insira o número de telefone/fax no cadastro do estabelecimento.\n2. Para IE inválida, confirme o número no SINTEGRA/CCC ou informe "ISENTO" quando aplicável.'
);

-- Erro: NCM Inválido
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'COD_NCM', 
    'COD_NCM', 
    'Código NCM inválido ou inexistente na Tabela IBPT/RFB (Registro 0200).', 
    'O NCM informado no produto não existe na tabela oficial ou expirou vigência.', 
    '1. Consulte o NCM correto do item na tabela de NCMs atualizada.\n2. Para produtos genéricos/outros sem NCM específico, utilize o padrão "27111910" (ou o código NCM genérico indicado do sistema).'
);

-- Erro: Sem valor informado - M200 / M600 (PIS/COFINS)
INSERT INTO erros_traduzidos (codigo_erro, campo, mensagem_pva, mensagem_traduzida, orientacao_correcao) 
VALUES (
    'M200_M600', 
    'COD_REC', 
    'Sem valor informado ou Bloco M200 (PIS) / M600 (COFINS) não gerado.', 
    'A apuração de PIS/COFINS do período não consolidou os totais ou falta preencher o Código de Receita de recolhimento (Campo 5 - COD_REC).', 
    '1. Verifique se o código de receita do imposto está preenchido na guia de apuração.\n2. Na maioria dos casos de divergência complexa de apuração do M200/M600, este erro deve ser encaminhado para validação da Contabilidade.'
);