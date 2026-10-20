package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmotionLog
import com.example.data.model.WeeklySummary
import java.util.Locale

@Composable
fun HistoryAndWeeklySummaryScreen(
  logs: List<EmotionLog>,
  weeklySummaries: List<WeeklySummary>,
  selectedWeekIndex: Int,
  onSelectWeekIndex: (Int) -> Unit,
  onDeleteLog: (String) -> Unit,
  initialTab: Int = 0, // 0 = Resumo Semanal, 1 = Histórico & Gráficos
  modifier: Modifier = Modifier
) {
  var currentSubTab by remember { mutableIntStateOf(initialTab) }
  var selectedEmotionFilter by remember { mutableStateOf<String?>(null) }

  Column(modifier = modifier.fillMaxSize()) {
    TabRow(
      selectedTabIndex = currentSubTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary
    ) {
      Tab(
        selected = currentSubTab == 0,
        onClick = { currentSubTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Resumo Semanal", style = MaterialTheme.typography.labelMedium)
          }
        },
        modifier = Modifier.testTag("tab_weekly_summary")
      )
      Tab(
        selected = currentSubTab == 1,
        onClick = { currentSubTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.ShowChart, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Histórico & Gráficos", style = MaterialTheme.typography.labelMedium)
          }
        },
        modifier = Modifier.testTag("tab_history_graphs")
      )
    }

    if (currentSubTab == 0) {
      WeeklySummaryView(
        summaries = weeklySummaries,
        selectedIndex = selectedWeekIndex,
        onSelectIndex = onSelectWeekIndex
      )
    } else {
      HistoryAndChartsView(
        logs = logs,
        selectedEmotionFilter = selectedEmotionFilter,
        onFilterEmotion = { selectedEmotionFilter = it },
        onDeleteLog = onDeleteLog
      )
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WeeklySummaryView(
  summaries: List<WeeklySummary>,
  selectedIndex: Int,
  onSelectIndex: (Int) -> Unit
) {
  if (summaries.isEmpty()) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      contentAlignment = Alignment.Center
    ) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
          imageVector = Icons.Default.CalendarMonth,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.outline,
          modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
          text = "Nenhum resumo semanal gerado ainda",
          style = MaterialTheme.typography.titleMedium,
          color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
          text = "Realize registros de emoções durante a semana para compilar sínteses descritivas de segunda a domingo.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }
    return
  }

  val activeSummary = summaries.getOrElse(selectedIndex) { summaries.first() }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Seletor de Semanas (Segunda a Domingo)
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = "Selecione o período (Segunda a Domingo):",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
      Spacer(modifier = Modifier.height(8.dp))
      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        itemsIndexed(summaries) { index, item ->
          val isSelected = index == selectedIndex
          FilterChip(
            selected = isSelected,
            onClick = { onSelectIndex(index) },
            label = {
              Text(
                text = item.weekLabel.replace("Semana de ", ""),
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                )
              )
            }
          )
        }
      }
    }

    // Banner de Esclarecimento Clínico
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.Top
        ) {
          Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = activeSummary.clinicalDisclaimer,
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 15.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // Indicadores Médicos da Semana (Bem-estar, Energia, Sono)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Médias Observadas no Período",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "${activeSummary.totalEntries} registros catalogados nesta semana",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(16.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
          ) {
            MetricMetricBox(
              label = "Bem-estar",
              value = String.format(Locale("pt", "BR"), "%.1f", activeSummary.avgWellbeing),
              color = MaterialTheme.colorScheme.primary
            )
            MetricMetricBox(
              label = "Energia",
              value = String.format(Locale("pt", "BR"), "%.1f", activeSummary.avgEnergy),
              color = MaterialTheme.colorScheme.secondary
            )
            MetricMetricBox(
              label = "Qualidade Sono",
              value = String.format(Locale("pt", "BR"), "%.1f", activeSummary.avgSleep),
              color = MaterialTheme.colorScheme.tertiary
            )
          }
        }
      }
    }

    // Emoções Predominantes na Semana
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "Emoções Mais Frequentes",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(12.dp))

          if (activeSummary.predominantEmotions.isEmpty()) {
            Text(
              text = "Nenhuma emoção específica registrada.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            FlowRow(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              activeSummary.predominantEmotions.forEach { (emotionName, count) ->
                Surface(
                  color = MaterialTheme.colorScheme.primaryContainer,
                  shape = RoundedCornerShape(12.dp)
                ) {
                  Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    Text(
                      text = emotionName,
                      style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                      color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Surface(
                      color = MaterialTheme.colorScheme.primary,
                      shape = CircleShape
                    ) {
                      Text(
                        text = "${count}x",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                      )
                    }
                  }
                }
              }
            }
          }
        }
      }
    }

    // Destaque: Assuntos Marcados para a Próxima Sessão nesta semana
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
          containerColor = if (activeSummary.topicsForSessionCount > 0)
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
          else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Forum,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Tópicos Assinalados para a Próxima Sessão (${activeSummary.topicsForSessionCount})",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          if (activeSummary.sessionDiscussionItems.isEmpty()) {
            Text(
              text = "Nenhum evento desta semana foi marcado com a flag de sessão.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              activeSummary.sessionDiscussionItems.forEachIndexed { i, topic ->
                Row(verticalAlignment = Alignment.Top) {
                  Text(
                    text = "• ",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary
                  )
                  Text(
                    text = topic,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                  )
                }
              }
            }
          }
        }
      }
    }

    // Temas Recorrentes em Pensamentos e Situações
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Psychology,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Temas Recorrentes Observados",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            activeSummary.recurringThemes.forEach { theme ->
              Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = theme,
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                  style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
          }
        }
      }
    }

    // Síntese Descritiva Terapêutica
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Text(
            text = "Síntese Descritiva da Semana",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = activeSummary.descriptiveSynthesis,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun MetricMetricBox(label: String, value: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(
      text = value,
      style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
      color = color
    )
    Text(
      text = label,
      style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistoryAndChartsView(
  logs: List<EmotionLog>,
  selectedEmotionFilter: String?,
  onFilterEmotion: (String?) -> Unit,
  onDeleteLog: (String) -> Unit
) {
  val allEmotions = logs.flatMap { it.emotions.map { e -> e.name } }.distinct()
  val filteredLogs = if (selectedEmotionFilter == null) logs
  else logs.filter { log -> log.emotions.any { it.name == selectedEmotionFilter } }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Gráfico de Tendências de Bem-Estar e Energia
    item {
      Spacer(modifier = Modifier.height(8.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Tendência dos Registros Recentes",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Bem-estar", style = MaterialTheme.typography.labelSmall)
              }
              Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.secondary))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Energia", style = MaterialTheme.typography.labelSmall)
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Mostra flutuações nos momentos registrados. Ausência de ponto indica dia sem registro, não ausência de sintomas.",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Canvas Line Graph
          SimpleTrendCanvas(
            logs = logs.take(7).reversed(),
            primaryColor = MaterialTheme.colorScheme.primary,
            secondaryColor = MaterialTheme.colorScheme.secondary
          )
        }
      }
    }

    // Filtros por Emoção
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.FilterList,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Filtrar por emoção:",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item {
          FilterChip(
            selected = selectedEmotionFilter == null,
            onClick = { onFilterEmotion(null) },
            label = { Text("Todas (${logs.size})") }
          )
        }
        items(allEmotions) { emotionName ->
          val count = logs.count { it.emotions.any { e -> e.name == emotionName } }
          FilterChip(
            selected = selectedEmotionFilter == emotionName,
            onClick = {
              onFilterEmotion(if (selectedEmotionFilter == emotionName) null else emotionName)
            },
            label = { Text("$emotionName ($count)") }
          )
        }
      }
    }

    // Lista Cronológica de Registros
    if (filteredLogs.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
          Text(
            text = "Nenhum registro encontrado com o filtro selecionado.",
            modifier = Modifier.padding(20.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    } else {
      items(filteredLogs, key = { it.id }) { log ->
        EmotionLogCard(log = log, onDelete = { onDeleteLog(log.id) })
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

@Composable
private fun SimpleTrendCanvas(
  logs: List<EmotionLog>,
  primaryColor: Color,
  secondaryColor: Color
) {
  if (logs.isEmpty()) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(120.dp),
      contentAlignment = Alignment.Center
    ) {
      Text("Sem registros suficientes para o gráfico.", style = MaterialTheme.typography.bodySmall)
    }
    return
  }

  Canvas(
    modifier = Modifier
      .fillMaxWidth()
      .height(120.dp)
  ) {
    val width = size.width
    val height = size.height
    val paddingHorizontal = 24.dp.toPx()
    val paddingVertical = 16.dp.toPx()

    val availableWidth = width - (paddingHorizontal * 2)
    val availableHeight = height - (paddingVertical * 2)
    val stepX = if (logs.size > 1) availableWidth / (logs.size - 1) else availableWidth / 2

    // Draw baseline grid lines (level 1, 3, 5)
    for (level in listOf(1, 3, 5)) {
      val y = paddingVertical + (5 - level) * (availableHeight / 4)
      drawLine(
        color = Color.LightGray.copy(alpha = 0.4f),
        start = Offset(paddingHorizontal, y),
        end = Offset(width - paddingHorizontal, y),
        strokeWidth = 1.dp.toPx()
      )
    }

    // Path for Wellbeing
    val pathWellbeing = Path()
    logs.forEachIndexed { i, log ->
      val x = paddingHorizontal + i * stepX
      val y = paddingVertical + (5 - log.wellbeingScore) * (availableHeight / 4)
      if (i == 0) pathWellbeing.moveTo(x, y) else pathWellbeing.lineTo(x, y)
      drawCircle(color = primaryColor, radius = 4.dp.toPx(), center = Offset(x, y))
    }
    drawPath(path = pathWellbeing, color = primaryColor, style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round))

    // Path for Energy
    val pathEnergy = Path()
    logs.forEachIndexed { i, log ->
      val x = paddingHorizontal + i * stepX
      val y = paddingVertical + (5 - log.energyScore) * (availableHeight / 4)
      if (i == 0) pathEnergy.moveTo(x, y) else pathEnergy.lineTo(x, y)
      drawCircle(color = secondaryColor, radius = 4.dp.toPx(), center = Offset(x, y))
    }
    drawPath(path = pathEnergy, color = secondaryColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EmotionLogCard(
  log: EmotionLog,
  onDelete: () -> Unit
) {
  var isExpanded by remember { mutableStateOf(false) }

  Card(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { isExpanded = !isExpanded },
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    shape = RoundedCornerShape(14.dp)
  ) {
    Column(modifier = Modifier.padding(14.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "${log.dateFormatted} às ${log.timeFormatted}",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Text(
              text = "Bem-estar: ${log.wellbeingScore}/5 • Energia: ${log.energyScore}/5",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Icon(
              imageVector = if (log.isSharedWithTherapist) Icons.Default.Share else Icons.Default.Lock,
              contentDescription = null,
              modifier = Modifier.size(12.dp),
              tint = MaterialTheme.colorScheme.outline
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          if (log.flagForNextSession) {
            Surface(
              color = MaterialTheme.colorScheme.secondaryContainer,
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.Forum,
                  contentDescription = null,
                  modifier = Modifier.size(12.dp),
                  tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "Para a Sessão",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSecondaryContainer
                )
              }
            }
          }
          IconButton(
            onClick = onDelete,
            modifier = Modifier.size(32.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Delete,
              contentDescription = "Excluir registro",
              tint = MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Emoções e intensidades
      FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
      ) {
        log.emotions.forEach { emotion ->
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp)
          ) {
            Text(
              text = "${emotion.name} (${emotion.intensity}/5)",
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }

      // Detalhes TCC Expandidos
      if (log.situation.isNotBlank() || log.automaticThoughts.isNotBlank() || log.helpfulAction.isNotBlank()) {
        Spacer(modifier = Modifier.height(8.dp))
        if (!isExpanded) {
          Text(
            text = if (log.situation.isNotBlank()) "Situação: ${log.situation.take(70)}..." else "Toque para ver reflexão TCC completa",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        } else {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 8.dp)
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
              .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            if (log.situation.isNotBlank()) {
              Text(
                text = "Situação: ${log.situation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            if (log.automaticThoughts.isNotBlank()) {
              Text(
                text = "Pensamentos: ${log.automaticThoughts}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            if (log.physicalSensations.isNotBlank()) {
              Text(
                text = "Sensações Corporais: ${log.physicalSensations}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            if (log.helpfulAction.isNotBlank()) {
              Text(
                text = "O que ajudou: ${log.helpfulAction}",
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    }
  }
}
