# EloPsi — Acompanhamento Psicológico Entre Sessões (TCC)

> Aplicativo móvel para suporte terapêutico entre sessões baseado em evidências e Terapia Cognitivo-Comportamental (TCC), permitindo que pacientes registrem emoções, pensamentos e bem-estar de forma acolhedora e acessem conteúdos prescritos, enquanto o psicólogo acompanha tendências e tópicos prioritários para a sessão presencial ou online.

---

## Decisões Críticas e Alinhamento com o Usuário

> [!IMPORTANT]
> **Resumo Semanal**: Incorporado como funcionalidade central solicitada. O Resumo Semanal compilará os registros de segunda-feira a domingo, destacando emoções mais frequentes, médias descritivas de bem-estar e energia, além de padrões de pensamentos e comportamentos relatados, mantendo tom estritamente neutro e descritivo (sem diagnósticos ou suposições causais).
>
> **Alternância de Perfis no Protótipo**: Para permitir experimentação imediata no ambiente de prototipagem, a barra superior conterá um seletor rápido entre a visão do **Paciente** e a visão do **Psicólogo**, simulando perfeitamente a vinculação por código de convite e a restrição de registros autorizados.
>
> **Estilo Visual e Tom**: Paleta de acolhimento orgânico em tons de verde sálvia, argila suave e off-white, com tipografia legível, ausência de gamificação culposa (sem sequências de dias/streaks) e avisos claros de que o app não substitui atendimento emergencial (CVV 188 / SAMU 192).

---

## 1. Síntese do Produto e Problema que Resolve

### O Problema
Na psicoterapia convencional, decorrem tipicamente 7 dias entre as sessões. Durante essa semana, pacientes com ansiedade, estresse e dificuldades de regulação emocional costumam esquecer situações críticas vividas, distorcer a intensidade retrospectiva de suas emoções (viés de memória) ou ter dificuldade em aplicar as técnicas combinadas em consultório. Além disso, anotações em papéis avulsos ou blocos de notas genéricos se perdem ou geram insegurança quanto à privacidade.

### A Solução
O **EloPsi** estabelece uma ponte terapêutica estruturada e ética:
- **Para o Paciente**: Registro rápido (< 1 minuto) para capturar o momento emocional sem sobrecarga, com opção de registro aprofundado no modelo TCC (Situação → Pensamento Automático → Sensações Físicas → Comportamento / O que ajudou), além de acesso aos materiais indicados pelo seu terapeuta e compilação em Resumo Semanal.
- **Para o Psicólogo**: Painel organizado com os dados expressamente compartilhados pelo paciente, visualização gráfica de tendências ao longo do tempo, identificação de itens marcados para a próxima sessão e recomendação individualizada de materiais da biblioteca.

---

## 2. Escopo do MVP e Funcionalidades Futuras

### Escopo do MVP (Esta Entrega)
1. **Perfis & Acesso**: Perfis de Paciente e Psicólogo com alternância rápida para testes e demonstração do fluxo de vinculação por convite.
2. **Registro de Emoções (Check-in)**:
   - Registro rápido (emoções com intensidades 1–5, explicação didática de cada emoção ao tocar no ícone de informação, notas rápidas de bem-estar, energia e sono).
   - Aprofundamento TCC opcional (o que aconteceu, pensamentos automáticos, sensações físicas, o que ajudou / estratégias).
   - Marcador: *"Quero conversar sobre isso na próxima sessão"*.
3. **Histórico, Calendário e Gráficos**:
   - Visualização cronológica e filtro por emoção/período.
   - Gráficos de dispersão e tendência semanal/mensal de intensidade emocional e bem-estar, esclarecendo limitações de interpretação.
4. **Resumo Semanal**:
   - Compilação dos registros de segunda a domingo.
   - Síntese descritiva com emoções predominantes, médias de bem-estar e energia, e agrupamento de temas recorrentes relatados.
   - Linguagem puramente descritiva e orientada à sessão clínica (sem rótulos diagnósticos).
5. **Biblioteca de Conteúdos e Prescrição de Atividades**:
   - Biblioteca temática (Ansiedade, Estresse, Sono, Regulação Emocional, Comunicação).
   - Materiais com tempo de leitura/execução, autoria, formato e objetivo.
   - Atribuição direta pelo psicólogo ao paciente, com marcação de conclusão e anotação do paciente.
6. **Área do Psicólogo**:
   - Lista de pacientes vinculados.
   - Dossiê do paciente com histórico compartilhado, tendências, tópicos pendentes para a sessão e atribuição de novas tarefas.
7. **Privacidade e Segurança**:
   - Aviso permanente de não monitoramento em tempo real e botão de recurso de crise (CVV 188 / SAMU 192).
   - Seletor de compartilhamento por registro (o paciente decide o que o terapeuta pode ver).
   - Opção de exportação resumida e exclusão de dados.

### Funcionalidades Futuras (Pós-MVP)
- Notificações locais configuráveis com texto discreto sem conteúdo clínico.
- Exportação em PDF estruturado com criptografia para a pasta de prontuário do terapeuta.
- Registro em áudio protegido (memorandos de voz).
- Integração biométrica (digital/reconhecimento facial para destrancar o app).
- Módulo de plano de segurança para prevenção de recaídas.

---

## 3. Fluxos de Uso do Paciente e do Psicólogo

### Fluxo do Paciente
```
[ Início / Dashboard ]
      │
      ├──> [ Registrar Como Estou ]
      │         │
      │         ├─> 1. Emoções sentidas (múltipla escolha) + Intensidade (1-5)
      │         │      └─ [?] Ver explicação didática da emoção
      │         ├─> 2. Bem-estar, Energia e Sono (Escala 1-5)
      │         ├─> 3. Marcar: "Levar para a próxima sessão"? (Sim/Não)
      │         ├─> 4. [Opcional] Aprofundar TCC:
      │         │      • Situação / O que aconteceu
      │         │      • Pensamentos automáticos
      │         │      • Sensações corporais
      │         │      • O que ajudou ou poderia ajudar?
      │         └─> [ Salvar Registro ] (Disponível em < 1 min)
      │
      ├──> [ Meu Histórico & Gráficos ]
      │         ├─> Lista cronológica & Calendário
      │         ├─> Gráficos de intensidade emocional e bem-estar
      │         └─> [ Nova Aba ] Resumo Semanal (Segunda a Domingo)
      │
      ├──> [ Meus Conteúdos e Atividades ]
      │         ├─> Indicados pelo meu psicólogo (com prazo/status)
      │         └─> Explorar biblioteca geral
      │
      └──> [ Perfil & Privacidade ]
                ├─> Gestão do vínculo com psicólogo
                ├─> Termos de privacidade & LGPD
                └─> Recursos de acolhimento emergencial
```

### Fluxo do Psicólogo
```
[ Painel do Psicólogo ]
      │
      ├──> [ Gestão de Pacientes ]
      │         ├─> Gerar convite (código único de vinculação)
      │         └─> Selecionar paciente vinculado
      │
      └──> [ Dossiê do Paciente ]
                ├─> Alertas: "Assuntos para a próxima sessão" (destaque superior)
                ├─> Resumo Semanal compilado
                ├─> Histórico de registros autorizados
                ├─> Gráficos de tendências emocionais
                └─> [ Indicar Conteúdo/Atividade ]
                          └─ Escolher da biblioteca ou cadastrar exercício novo
```

---

## 4. Telas e Textos Principais

| Tela | Componentes Principais | Exemplos de Textos e Rótulos |
|---|---|---|
| **Início (Paciente)** | Cartão acolhedor do dia, atalho de registro rápido, tópico da próxima sessão, atividade pendente | *"Como você está se sentindo agora?"*, *"Registrar em 1 minuto"*, *"Próxima sessão: 2 itens para conversar"* |
| **Registrar Emoção** | Chips de emoções (Ansiedade, Calma, Tristeza, Raiva, Alegria, Sobrecarga, Frustração), botões de ajuda conceitual, sliders 1–5, acordeão de TCC | *"Identifique o que você sente sem julgamentos"*, *"O que passou pela sua cabeça?"*, *"O que ajudou neste momento?"* |
| **Resumo Semanal** | Seletor de semanas (seg–dom), cards de indicadores médios, nuvem de emoções frequentes, síntese descritiva | *"Semana de 13/10 a 19/10"*, *"Emoções mais presentes: Ansiedade (4x), Serenidade (2x)"*, *"Média de energia: 3.2/5"*, *"Observação clínica: registros concentrados no período noturno com pensamentos ligados a prazos."* |
| **Histórico e Gráficos** | Alternância Lista / Calendário, Gráfico de linha de bem-estar, Gráfico de barras de emoções | *"Registros anteriores"*, *"Nota: Gráficos mostram tendências dos momentos registrados, não um diagnóstico contínuo."* |
| **Biblioteca** | Filtros por tema, cards de conteúdos com tempo estimado e tipo (Leitura, Exercício, Áudio) | *"Respiração Diafragmática (5 min)"*, *"Desarmando Pensamentos Catastróficos (Leitura, 8 min)"* |
| **Painel do Psicólogo** | Lista de pacientes, cards de atenção da próxima sessão, prescrição rápida de tarefas | *"Pacientes ativos"*, *"Assuntos marcados para a sessão de amanhã"*, *"Prescrever nova atividade"* |
| **Privacidade & Suporte** | Informações sobre guarda de dados, revogação de vínculo, canais de crise | *"Seus dados pertencem a você"*, *"Este aplicativo não possui monitoramento em tempo real. Se estiver em sofrimento agudo, ligue 188 (CVV)."* |

---

## 5. Arquitetura Técnica e Modelo de Dados

### Diagrama de Arquitetura do Sistema
```
┌────────────────────────────────────────────────────────────────────────┐
│                        Jetpack Compose UI Layer                        │
│                                                                        │
│  ┌──────────────────────┐  ┌────────────────────┐  ┌────────────────┐ │
│  │ PatientHome &        │  │ WeeklySummary &    │  │ Psychologist   │ │
│  │ EmotionRecordScreen  │  │ HistoryGraphs      │  │ DashboardView  │ │
│  └──────────┬───────────┘  └─────────┬──────────┘  └────────┬───────┘ │
│             │                        │                      │         │
│             └───────────────────┬────┴──────────────────────┘         │
│                                 ▼                                     │
│                     ┌───────────────────────┐                         │
│                     │ EloPsiAppViewModel    │                         │
│                     │ (State & UI Mutators) │                         │
│                     └───────────┬───────────┘                         │
└─────────────────────────────────┼─────────────────────────────────────┘
                                  ▼
┌────────────────────────────────────────────────────────────────────────┐
│                         Data & Domain Layer                            │
│                                                                        │
│  ┌──────────────────────────────────────────────────────────────────┐  │
│  │ EloPsiRepository (In-memory + Room SQLite Local Persistence)    │  │
│  │                                                                  │  │
│  │ • EmotionLogDao (Registros de emoção, TCC e flags de sessão)     │  │
│  │ • WeeklySummaryGenerator (Cálculo descritivo e agrupamentos)     │  │
│  │ • ActivityLibraryDao (Artigos, exercícios e atribuições)         │  │
│  │ • RelationshipDao (Vínculos terapeuta-paciente e permissões)    │  │
│  └──────────────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────────────┘
```

### Principais Entidades de Dados
- **`EmotionLog`**: `id`, `patientId`, `timestamp`, `emotions` (lista de `SelectedEmotion` com `emotionName` e `intensity` 1-5), `wellbeingScore` (1-5), `energyScore` (1-5), `sleepQuality` (1-5), `situation` (texto), `automaticThoughts` (texto), `physicalSensations` (texto), `behaviors` (texto), `helpfulAction` (texto), `flagForNextSession` (booleano), `isSharedWithTherapist` (booleano).
- **`WeeklySummary`**: `weekStartDate`, `weekEndDate`, `totalLogs`, `predominantEmotions`, `avgWellbeing`, `avgEnergy`, `avgSleep`, `sessionDiscussionTopicsCount`, `descriptiveNarrative`.
- **`LibraryContent`**: `id`, `title`, `description`, `theme` (Ansiedade, Sono, etc.), `format` (Leitura, Exercício, Áudio), `estimatedMinutes`, `authorOrSource`, `contentBodyOrUrl`.
- **`AssignedActivity`**: `id`, `patientId`, `contentId`, `prescribedDate`, `completedDate`, `patientNotes`, `status` (Pendente, Concluído).
- **`TherapistPatientLink`**: `patientId`, `therapistId`, `inviteCode`, `status` (Ativo, Pendente, Revogado), `allowDataSharing` (booleano).

---

## 6. Privacidade, Ética e Conformidade (LGPD & CFP)

1. **Classificação como Dados Sensíveis (LGPD Art. 5º, II)**:
   - Registros de humor, pensamentos e sofrimento psíquico constituem dados referentes à saúde e vida pessoal do titular.
   - Base legal primária: Consentimento expresso e inequívoco (Art. 7º, I e Art. 11, I da LGPD) ou tutela da saúde em procedimento realizado por profissional de saúde (Art. 11, II, "f").
2. **Resoluções do Conselho Federal de Psicologia (CFP)**:
   - Resolução CFP nº 11/2018 (regulamentação de serviços psicológicos mediados por TIC) e Resolução CFP nº 01/2009 (guarda e sigilo documental).
   - O aplicativo atua como **ferramenta de apoio**, não como substituto de prontuário eletrônico certificado (PEP) nem como substituto da relação terapêutica.
3. **Isenção de Falso Socorro**:
   - Mensagem explícita em múltiplos pontos do app: *"Este app não é um serviço de emergência e não é monitorado 24 horas por dia pelo psicólogo. Em caso de crise ou risco, entre em contato imediatamente com o CVV (188) ou SAMU (192)."*
4. **Princípio do Privilégio Mínimo e Autonomia**:
   - Cada registro possui chave individual de compartilhamento.
   - O paciente pode a qualquer momento revogar o vínculo ou desmarcar o compartilhamento de registros íntimos.
   - Notificações de sistema nunca exibem texto de sintomas ou diagnósticos na tela de bloqueio.

---

## 7. Análise PWA vs. Aplicativo Nativo (Android)

| Critério | Aplicativo Nativo Android (Kotlin / Compose) | Aplicativo Web Instalável (PWA) |
|---|---|---|
| **Privacidade & Armazenamento Seguro** | Excelente. Banco SQLite/Room local cifrado no dispositivo (SQLCipher / EncryptedSharedPreferences). | Moderado. Armazenamento em IndexedDB/LocalStorage vulnerável à limpeza de cache do navegador. |
| **Notificações de Lembrete** | Altamente confiáveis via AlarmManager / WorkManager, sem depender de conexão de rede. | Limitadas no iOS e suscetíveis a suspensão de background pelos sistemas móveis. |
| **Sensação de Acolhimento & Fluidez** | Excelente. Animações nativas com 60/120fps, componentes M3 com feedback háptico e transições suaves. | Boa, mas pode parecer um site empacotado com atrasos de toque e falhas de teclado virtual. |
| **Custo Inicial & Manutenção** | Custo inicial de publicação na Google Play ($25 taxa única), manutenção direta em Kotlin moderno. | Menor custo inicial de distribuição, sem taxas de loja. |

**Recomendação**: Para este caso de uso clínico, o **Aplicativo Nativo Android em Jetpack Compose** é a escolha ideal, garantindo total isolamento dos dados sensíveis, confiabilidade de funcionamento offline e experiência acolhedora e responsiva para o paciente em momentos de sobrecarga.

---

## 8. Critérios de Aceitação e Verificação

- [x] O paciente consegue registrar uma emoção e níveis de bem-estar em menos de 1 minuto.
- [x] O formulário de aprofundamento TCC é estritamente opcional e expansível sem frustração.
- [x] O Resumo Semanal compila os registros de segunda a domingo com descrição empática, cálculo correto das médias e emoções frequentes, sem rótulos diagnósticos.
- [x] O paciente pode marcar tópicos para a próxima sessão, que aparecem destacados tanto no seu painel quanto no do psicólogo.
- [x] O profissional só consegue visualizar registros de pacientes que aceitaram a vinculação e permitiram o compartilhamento.
- [x] A biblioteca permite ao psicólogo atribuir atividades e ao paciente marcar como concluídas com anotações.
- [x] Botões de emergência e avisos de não monitoramento 24h estão acessíveis e visíveis.
- [x] O seletor de perfil no topo permite alternar instantaneamente entre a visão do paciente e do psicólogo para demonstração completa.
