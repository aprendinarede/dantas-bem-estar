package com.example.data.repository

import com.example.data.model.AssignedActivity
import com.example.data.model.EmotionInfo
import com.example.data.model.EmotionLog
import com.example.data.model.LibraryContent
import com.example.data.model.PatientProfile
import com.example.data.model.SelectedEmotion
import com.example.data.model.TherapistProfile
import com.example.data.model.WeeklySummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class EloPsiRepository {

  private val emotionCatalog = listOf(
    EmotionInfo(
      id = "ansiedade",
      name = "Ansiedade",
      explanation = "Estado de alerta, apreensão ou inquietação mental e física diante de incertezas futuras. Seu papel evolutivo é nos preparar para desafios, mas em excesso pode gerar desgaste.",
      category = "Alerta",
      reflectionPrompt = "Que incerteza ou cenário futuro minha mente está tentando antecipar agora?"
    ),
    EmotionInfo(
      id = "calma",
      name = "Calma",
      explanation = "Sensação de serenidade, equilíbrio interno e ritmo respiratório compassado. Não significa ausência de problemas, mas presença atenta e acolhedora no aqui e agora.",
      category = "Equilíbrio",
      reflectionPrompt = "O que no meu ambiente ou na minha postura está me ajudando a desacelerar?"
    ),
    EmotionInfo(
      id = "sobrecarga",
      name = "Sobrecarga",
      explanation = "Sensação de peso psíquico quando as demandas (reais ou autoimpostas) parecem exceder nosso tempo, energia e recursos disponíveis no momento.",
      category = "Desgaste",
      reflectionPrompt = "O que é realmente urgente hoje e o que pode ser adiado, delegado ou simplificado?"
    ),
    EmotionInfo(
      id = "tristeza",
      name = "Tristeza",
      explanation = "Resposta saudável e natural a perdas, frustrações ou desilusões. Funciona como um sinalizador para desacelerar, buscar conforto e reorganizar significados.",
      category = "Recolhimento",
      reflectionPrompt = "De que tipo de cuidado ou acolhimento preciso neste momento?"
    ),
    EmotionInfo(
      id = "alegria",
      name = "Alegria / Gratidão",
      explanation = "Sentimento de vitalidade, contentamento e conexão genuína com o que faz sentido. Fortalece nossa flexibilidade cognitiva e vínculos afetivos.",
      category = "Vitalidade",
      reflectionPrompt = "O que aconteceu que merece ser reconhecido e saboreado?"
    ),
    EmotionInfo(
      id = "raiva",
      name = "Raiva / Irritação",
      explanation = "Surge quando percebemos que limites foram ultrapassados, que houve injustiça ou que um objetivo importante foi bloqueado. É energia de proteção que precisa de direcionamento assertivo.",
      category = "Defesa",
      reflectionPrompt = "Qual limite importante para mim foi desrespeitado ou negligenciado?"
    ),
    EmotionInfo(
      id = "frustracao",
      name = "Frustração",
      explanation = "Desconforto que surge na distância entre a expectativa criada e o resultado concreto obtido. Oportunidade para flexibilizar planos.",
      category = "Descompasso",
      reflectionPrompt = "Minha expectativa inicial era realista com as condições reais disponíveis?"
    ),
    EmotionInfo(
      id = "culpa",
      name = "Culpa",
      explanation = "Sentimento de desconforto gerado pela percepção de termos transgredido um valor pessoal ou ferido alguém. Importante diferenciar responsabilidade real de autocobrança excessiva.",
      category = "Avaliação",
      reflectionPrompt = "Estou avaliando a situação com fatos ou com cobranças desproporcionais?"
    )
  )

  private val libraryItems = listOf(
    LibraryContent(
      id = "lib-1",
      title = "Técnica da Respiração Diafragmática (4-7-8)",
      description = "Exercício fisiológico para desaceleração do sistema nervoso simpático através do prolongamento da expiração.",
      theme = "Ansiedade",
      format = "Exercício Prático",
      estimatedMinutes = 5,
      authorOrSource = "Protocolos Baseados em Evidências para Regulação Fisiológica",
      clinicalObjective = "Reduzir hiperventilação e sensação de aperto no peito em momentos de sobrecarga emocional.",
      contentSteps = listOf(
        "Sente-se com a coluna ereta e os pés apoiados no chão. Coloque uma mão sobre o peito e outra no abdômen.",
        "Expire todo o ar pela boca suavemente.",
        "Puxe o ar pelo nariz contando mentalmente até 4, percebendo o abdômen expandir suavemente.",
        "Segure o ar confortavelmente por 4 a 7 segundos.",
        "Solte o ar pela boca de forma lenta e compassada contando até 8.",
        "Repita este ciclo por 4 vezes consecutivas sem pressa."
      )
    ),
    LibraryContent(
      id = "lib-2",
      title = "Desarmando Pensamentos Catastróficos (Teste de Evidências)",
      description = "Roteiro guiado de TCC para identificar distorções do tipo 'e se tudo der errado?' e construir pensamentos alternativos realistas.",
      theme = "Ansiedade",
      format = "Leitura & Exercício",
      estimatedMinutes = 7,
      authorOrSource = "Beck Institute / Prática Clínica em TCC",
      clinicalObjective = "Distinguir pensamentos automáticos de fatos concretos comprovados.",
      contentSteps = listOf(
        "Identifique a previsão pessimista que sua mente criou (ex: 'Vou falhar e todos vão me julgar').",
        "Pergunte-se: Quais são os fatos reais que comprovam 100% essa conclusão?",
        "Pergunte-se: Quais evidências reais mostram que o pior pode não acontecer ou que posso lidar com o desafio?",
        "Qual é o cenário mais provável e realista?",
        "Se um bom amigo estivesse na mesma situação, o que você diria a ele de forma sincera e acolhedora?"
      )
    ),
    LibraryContent(
      id = "lib-3",
      title = "Aterramento 5-4-3-2-1 para Picos de Estresse",
      description = "Prática sensorial de ancoragem no presente imediato para interromper ruminações e reorientar a atenção.",
      theme = "Regulação Emocional",
      format = "Exercício Prático",
      estimatedMinutes = 4,
      authorOrSource = "Terapia Focada no Trauma e Regulação Emocional",
      clinicalObjective = "Trazer o foco mental de volta ao corpo e ao espaço físico imediato.",
      contentSteps = listOf(
        "Olhe ao seu redor e nomeie mentalmente 5 coisas que você pode VER.",
        "Toque e sinta 4 texturas ou superfícies diferentes (sua roupa, a mesa, suas mãos).",
        "Preste atenção e identifique 3 sons distintos no ambiente.",
        "Identifique 2 aromas ou cheiros presentes no ar.",
        "Perceba 1 sabor em sua boca ou dê um gole d'água com atenção plena."
      )
    ),
    LibraryContent(
      id = "lib-4",
      title = "Higiene do Sono: Rituais de Desaceleração Mental",
      description = "Orientações comportamentais para criar transição saudável entre as demandas do dia e a fase de repouso noturno.",
      theme = "Sono",
      format = "Leitura Aplicada",
      estimatedMinutes = 6,
      authorOrSource = "Terapia Cognitivo-Comportamental para Insônia (TCC-I)",
      clinicalObjective = "Diminuir a ativação cognitiva na hora de dormir e associar a cama com descanso.",
      contentSteps = listOf(
        "Estabeleça um 'ponto de corte' para telas e e-mails de trabalho pelo menos 45 minutos antes de deitar.",
        "Mantenha um bloco de notas ao lado da cama; se pensamentos sobre tarefas surgirem, anote-os e diga: 'Está guardado para amanhã'.",
        "Se não adormecer em cerca de 20 minutos, saia da cama, faça uma atividade calma com luz baixa e retorne apenas quando o sono vier.",
        "Mantenha o quarto escuro, silencioso e em temperatura amena."
      )
    ),
    LibraryContent(
      id = "lib-5",
      title = "Comunicação Assertiva com a Técnica D.E.S.C.",
      description = "Framework prático para expressar sentimentos, limites e necessidades sem agressividade e sem passividade.",
      theme = "Comunicação",
      format = "Exercício Reflexivo",
      estimatedMinutes = 8,
      authorOrSource = "Treinamento de Habilidades Sociais e Assertividade",
      clinicalObjective = "Posicionar limites saudáveis no trabalho e relações pessoais diminuindo a sobrecarga.",
      contentSteps = listOf(
        "D (Descrever): Descreva os fatos objetivos da situação, sem julgamentos ou generalizações ('Quando a reunião passou de 19h...').",
        "E (Expressar): Expresse como você se sentiu na primeira pessoa ('Eu me senti sobrecarregado...').",
        "S (Solicitar): Proponha claramente uma alternativa viável ('Gostaria que alinhássemos prazos com antecedência').",
        "C (Consequências): Destaque o benefício mútuo dessa mudança ('Assim poderei entregar com mais foco e qualidade')."
      )
    )
  )

  // Seed default patient and therapist
  private var currentPatientProfile = PatientProfile(
    id = "paciente-101",
    name = "Ana Clara Souza",
    email = "ana.clara@exemplo.com",
    inviteCode = "ELOPSI-4921",
    therapistId = "terapeuta-99",
    linkedDate = "15 de Setembro de 2026",
    suggestedFrequency = "1 a 2 vezes ao dia ou após momentos intensos",
    isSharingActive = true
  )

  private val therapistProfile = TherapistProfile(
    id = "terapeuta-99",
    name = "Dr. Eduardo Silva",
    crp = "CRP 06/98214-SP",
    email = "eduardo.psicologia@clinica.com",
    defaultInviteCode = "ELOPSI-4921"
  )

  // In-memory data holders with Flow for reactive UI
  private val _emotionLogsFlow = MutableStateFlow<List<EmotionLog>>(createInitialSeedLogs())
  val emotionLogsFlow: Flow<List<EmotionLog>> = _emotionLogsFlow.asStateFlow()

  private val _assignedActivitiesFlow = MutableStateFlow<List<AssignedActivity>>(createInitialSeedActivities())
  val assignedActivitiesFlow: Flow<List<AssignedActivity>> = _assignedActivitiesFlow.asStateFlow()

  fun getEmotionCatalog(): List<EmotionInfo> = emotionCatalog

  fun getLibraryContents(): List<LibraryContent> = libraryItems

  fun getPatientProfile(): PatientProfile = currentPatientProfile

  fun getTherapistProfile(): TherapistProfile = therapistProfile

  fun updatePatientSharing(active: Boolean) {
    currentPatientProfile = currentPatientProfile.copy(isSharingActive = active)
  }

  fun addEmotionLog(log: EmotionLog) {
    val updated = listOf(log) + _emotionLogsFlow.value
    _emotionLogsFlow.value = updated
  }

  fun updateEmotionLog(log: EmotionLog) {
    _emotionLogsFlow.value = _emotionLogsFlow.value.map {
      if (it.id == log.id) log else it
    }
  }

  fun deleteEmotionLog(logId: String) {
    _emotionLogsFlow.value = _emotionLogsFlow.value.filterNot { it.id == logId }
  }

  fun completeActivity(activityId: String, patientNote: String) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy 'às' HH:mm", Locale("pt", "BR"))
    val now = dateFormat.format(Date())
    _assignedActivitiesFlow.value = _assignedActivitiesFlow.value.map { activity ->
      if (activity.id == activityId) {
        activity.copy(
          isCompleted = true,
          patientNote = patientNote,
          completedDateFormatted = now
        )
      } else activity
    }
  }

  fun assignActivity(contentId: String, patientId: String) {
    val content = libraryItems.find { it.id == contentId } ?: return
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val newActivity = AssignedActivity(
      id = UUID.randomUUID().toString(),
      patientId = patientId,
      contentId = content.id,
      title = content.title,
      theme = content.theme,
      format = content.format,
      estimatedMinutes = content.estimatedMinutes,
      assignedByTherapistName = therapistProfile.name,
      assignedDateFormatted = dateFormat.format(Date()),
      isCompleted = false
    )
    _assignedActivitiesFlow.value = listOf(newActivity) + _assignedActivitiesFlow.value
  }

  fun generateNewInviteCode(): String {
    val randomNum = (1000..9999).random()
    val newCode = "ELOPSI-$randomNum"
    currentPatientProfile = currentPatientProfile.copy(inviteCode = newCode)
    return newCode
  }

  // Generates compiled weekly summaries (Monday through Sunday)
  fun generateWeeklySummaries(logs: List<EmotionLog>): List<WeeklySummary> {
    if (logs.isEmpty()) return emptyList()

    // Group logs by ISO calendar week (Monday to Sunday)
    val calendar = Calendar.getInstance(Locale("pt", "BR"))
    calendar.firstDayOfWeek = Calendar.MONDAY

    val groupedByWeek = logs.groupBy { log ->
      calendar.timeInMillis = log.timestamp
      val week = calendar.get(Calendar.WEEK_OF_YEAR)
      val year = calendar.get(Calendar.YEAR)
      "$year-$week"
    }

    return groupedByWeek.map { (key, weekLogs) ->
      buildWeeklySummaryForGroup(key, weekLogs)
    }.sortedByDescending { it.startDateTimestamp }
  }

  private fun buildWeeklySummaryForGroup(weekKey: String, weekLogs: List<EmotionLog>): WeeklySummary {
    val calendar = Calendar.getInstance(Locale("pt", "BR"))
    calendar.firstDayOfWeek = Calendar.MONDAY

    val sortedLogs = weekLogs.sortedBy { it.timestamp }
    val firstTimestamp = sortedLogs.first().timestamp
    calendar.timeInMillis = firstTimestamp

    // Set to Monday of this week
    calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    val startOfWeekMillis = calendar.timeInMillis
    val startDateStr = SimpleDateFormat("dd/MM", Locale("pt", "BR")).format(Date(startOfWeekMillis))

    // Set to Sunday of this week
    calendar.add(Calendar.DAY_OF_WEEK, 6)
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    val endOfWeekMillis = calendar.timeInMillis
    val endDateStr = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")).format(Date(endOfWeekMillis))

    val weekLabel = "Semana de $startDateStr a $endDateStr"

    // Calculate emotion counts
    val emotionCounts = mutableMapOf<String, Int>()
    weekLogs.forEach { log ->
      log.emotions.forEach { emotion ->
        emotionCounts[emotion.name] = (emotionCounts[emotion.name] ?: 0) + 1
      }
    }
    val predominantEmotions = emotionCounts.toList().sortedByDescending { it.second }.take(4)

    // Calculate averages
    val avgWellbeing = weekLogs.map { it.wellbeingScore }.average().toFloat()
    val avgEnergy = weekLogs.map { it.energyScore }.average().toFloat()
    val avgSleep = weekLogs.map { it.sleepQuality }.average().toFloat()

    // Discussion items flagged for next session
    val discussionLogs = weekLogs.filter { it.flagForNextSession }
    val discussionItems = discussionLogs.mapNotNull {
      when {
        it.situation.isNotBlank() -> it.situation
        it.automaticThoughts.isNotBlank() -> it.automaticThoughts
        it.emotions.isNotEmpty() -> "Momento de ${it.emotions.joinToString { e -> e.name }}"
        else -> null
      }
    }

    // Extract recurring themes from text fields
    val themes = mutableListOf<String>()
    val allText = weekLogs.joinToString(" ") { "${it.situation} ${it.automaticThoughts} ${it.helpfulAction}" }.lowercase()
    if (allText.contains("trabalho") || allText.contains("prazo") || allText.contains("reunião")) {
      themes.add("Demandas profissionais e prazos")
    }
    if (allText.contains("noite") || allText.contains("sono") || allText.contains("dormir")) {
      themes.add("Dificuldade de desaceleração noturna")
    }
    if (allText.contains("cobrança") || allText.contains("insuficiente") || allText.contains("falhar")) {
      themes.add("Padrão de autocrítica e preocupação com desempenho")
    }
    if (allText.contains("respiração") || allText.contains("pausa") || allText.contains("caminhada")) {
      themes.add("Recurso à respiração e pausas estratégicas como alívio")
    }
    if (themes.isEmpty()) {
      themes.add("Variações habituais de rotina e regulação emocional")
    }

    // Compose compassionate descriptive synthesis (strictly non-diagnostic)
    val predominantNames = predominantEmotions.joinToString(", ") { "${it.first} (${it.second}x)" }
    val formattedWellbeing = String.format(Locale("pt", "BR"), "%.1f", avgWellbeing)
    val formattedEnergy = String.format(Locale("pt", "BR"), "%.1f", avgEnergy)
    val formattedSleep = String.format(Locale("pt", "BR"), "%.1f", avgSleep)

    val synthesis = buildString {
      append("Ao longo desta semana (${weekLogs.size} registros realizados), as emoções registradas com maior frequência foram $predominantNames. ")
      append("Os índices médios percebidos foram: bem-estar em $formattedWellbeing/5, nível de energia em $formattedEnergy/5 e percepção da qualidade do sono em $formattedSleep/5. ")
      if (discussionLogs.isNotEmpty()) {
        append("Foram assinalados ${discussionLogs.size} eventos para diálogo na próxima sessão presencial. ")
      }
      append("Observa-se que momentos de maior elevação na ansiedade coincidiram principalmente com situações ligadas a compromissos de rotina, tendo sido relatado benefício em pausas intencionais de respiração.")
    }

    val parts = weekKey.split("-")
    val year = parts.getOrNull(0)?.toIntOrNull() ?: 2026
    val weekNum = parts.getOrNull(1)?.toIntOrNull() ?: 42

    return WeeklySummary(
      weekLabel = weekLabel,
      weekNumber = weekNum,
      year = year,
      startDateTimestamp = startOfWeekMillis,
      endDateTimestamp = endOfWeekMillis,
      totalEntries = weekLogs.size,
      predominantEmotions = predominantEmotions,
      avgWellbeing = avgWellbeing,
      avgEnergy = avgEnergy,
      avgSleep = avgSleep,
      topicsForSessionCount = discussionLogs.size,
      sessionDiscussionItems = discussionItems,
      recurringThemes = themes,
      descriptiveSynthesis = synthesis
    )
  }

  private fun createInitialSeedLogs(): List<EmotionLog> {
    val cal = Calendar.getInstance(Locale("pt", "BR"))
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))

    fun logAt(daysAgo: Int, hour: Int, minute: Int, emotions: List<SelectedEmotion>, wb: Int, en: Int, sl: Int, sit: String, thoughts: String, sensations: String, behavior: String, help: String, flag: Boolean): EmotionLog {
      val c = Calendar.getInstance(Locale("pt", "BR"))
      c.add(Calendar.DAY_OF_YEAR, -daysAgo)
      c.set(Calendar.HOUR_OF_DAY, hour)
      c.set(Calendar.MINUTE, minute)
      val time = c.timeInMillis
      return EmotionLog(
        id = UUID.randomUUID().toString(),
        patientId = "paciente-101",
        timestamp = time,
        dateFormatted = dateFormat.format(Date(time)),
        timeFormatted = timeFormat.format(Date(time)),
        emotions = emotions,
        wellbeingScore = wb,
        energyScore = en,
        sleepQuality = sl,
        situation = sit,
        automaticThoughts = thoughts,
        physicalSensations = sensations,
        behaviors = behavior,
        helpfulAction = help,
        flagForNextSession = flag,
        isSharedWithTherapist = true
      )
    }

    return listOf(
      logAt(
        daysAgo = 0,
        hour = 14,
        minute = 30,
        emotions = listOf(SelectedEmotion("Ansiedade", 4), SelectedEmotion("Sobrecarga", 3)),
        wb = 2,
        en = 2,
        sl = 3,
        sit = "Notificação de adiantamento de prazo da entrega do projeto trimestral no trabalho.",
        thoughts = "Pensamento automático: 'Não vou dar conta de revisar tudo a tempo, vão achar que sou descuidada'.",
        sensations = "Aperto no peito, respiração curta e mãos geladas.",
        behavior = "Parei por 5 minutos antes de responder a mensagem para não agir no impulso.",
        help = "Fiz o exercício da Respiração Diafragmática (4-7-8) indicado na biblioteca do app.",
        flag = true
      ),
      logAt(
        daysAgo = 1,
        hour = 21,
        minute = 15,
        emotions = listOf(SelectedEmotion("Calma", 4), SelectedEmotion("Alegria / Gratidão", 3)),
        wb = 4,
        en = 3,
        sl = 4,
        sit = "Caminhada tranquila no parque no final da tarde e jantar leve com minha irmã.",
        thoughts = "Pensamento: 'Foi bom tirar um tempo para desconectar das telas'.",
        sensations = "Corpo relaxado, músculos dos ombros mais soltos.",
        behavior = "Deixei o celular guardado na bolsa durante a conversa.",
        help = "Focar na presença do momento e apreciar a comida sem pressa.",
        flag = false
      ),
      logAt(
        daysAgo = 2,
        hour = 18,
        minute = 0,
        emotions = listOf(SelectedEmotion("Frustração", 3), SelectedEmotion("Sobrecarga", 4)),
        wb = 2,
        en = 2,
        sl = 2,
        sit = "Reunião de alinhamento com a equipe onde meus pontos não foram ouvidos.",
        thoughts = "Pensamento: 'Parece que minha opinião não tem relevância no grupo'.",
        sensations = "Mandíbula tensa e calor no rosto.",
        behavior = "Fiquei em silêncio o restante da chamada.",
        help = "Conversar na sessão sobre como praticar a técnica de comunicação assertiva D.E.S.C.",
        flag = true
      ),
      logAt(
        daysAgo = 3,
        hour = 8,
        minute = 45,
        emotions = listOf(SelectedEmotion("Ansiedade", 3)),
        wb = 3,
        en = 3,
        sl = 2,
        sit = "Acordei após uma noite de sono entrecortado pensando na lista de afazeres.",
        thoughts = "Pensamento: 'Já começo o dia atrasada'.",
        sensations = "Sensação de cansaço nos olhos e peso na cabeça.",
        behavior = "Tomei um copo d'água e sentei na beira da cama respirando com calma.",
        help = "Fazer uma lista priorizada com apenas 3 metas principais para o dia.",
        flag = false
      ),
      logAt(
        daysAgo = 4,
        hour = 22,
        minute = 30,
        emotions = listOf(SelectedEmotion("Calma", 3)),
        wb = 4,
        en = 3,
        sl = 4,
        sit = "Leitura de um livro antes de dormir com luz suave.",
        thoughts = "Pensamento: 'O dia teve desafios, mas fiz o melhor possível'.",
        sensations = "Respiração suave e tranquila.",
        behavior = "Apliquei o ritual de higiene do sono prescrito pelo psicólogo.",
        help = "Manter o celular fora do alcance da cama.",
        flag = false
      ),
      logAt(
        daysAgo = 5,
        hour = 11,
        minute = 20,
        emotions = listOf(SelectedEmotion("Ansiedade", 4), SelectedEmotion("Medo", 2)),
        wb = 2,
        en = 3,
        sl = 3,
        sit = "Recebi feedback sobre um relatório.",
        thoughts = "Fiquei com receio de ser criticada antes mesmo de abrir o documento.",
        sensations = "Batimentos cardíacos acelerados.",
        behavior = "Bebi água e fiz o aterramento sensorial 5-4-3-2-1.",
        help = "Reconhecer que feedback é oportunidade de ajuste e não julgamento pessoal.",
        flag = false
      )
    )
  }

  private fun createInitialSeedActivities(): List<AssignedActivity> {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val cal = Calendar.getInstance(Locale("pt", "BR"))
    cal.add(Calendar.DAY_OF_YEAR, -3)
    val pastDate = dateFormat.format(cal.time)
    val todayDate = dateFormat.format(Date())

    return listOf(
      AssignedActivity(
        id = "act-1",
        patientId = "paciente-101",
        contentId = "lib-1",
        title = "Técnica da Respiração Diafragmática (4-7-8)",
        theme = "Ansiedade",
        format = "Exercício Prático",
        estimatedMinutes = 5,
        assignedByTherapistName = "Dr. Eduardo Silva",
        assignedDateFormatted = pastDate,
        isCompleted = true,
        patientNote = "Pratiquei na terça-feira à tarde após a notícia do trabalho. Me ajudou a desacelerar a respiração em cerca de 3 minutos.",
        completedDateFormatted = "$pastDate às 15:10"
      ),
      AssignedActivity(
        id = "act-2",
        patientId = "paciente-101",
        contentId = "lib-2",
        title = "Desarmando Pensamentos Catastróficos (Teste de Evidências)",
        theme = "Ansiedade",
        format = "Leitura & Exercício",
        estimatedMinutes = 7,
        assignedByTherapistName = "Dr. Eduardo Silva",
        assignedDateFormatted = todayDate,
        isCompleted = false,
        patientNote = ""
      ),
      AssignedActivity(
        id = "act-3",
        patientId = "paciente-101",
        contentId = "lib-4",
        title = "Higiene do Sono: Rituais de Desaceleração Mental",
        theme = "Sono",
        format = "Leitura Aplicada",
        estimatedMinutes = 6,
        assignedByTherapistName = "Dr. Eduardo Silva",
        assignedDateFormatted = pastDate,
        isCompleted = true,
        patientNote = "Deixar o celular em outro cômodo fez muita diferença na qualidade do sono.",
        completedDateFormatted = "$todayDate às 08:30"
      )
    )
  }
}
