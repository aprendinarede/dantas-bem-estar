package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AssignedActivity
import com.example.data.model.EmotionLog
import com.example.data.model.WeeklySummary
import com.example.ui.components.CrisisDisclaimerBanner

@Composable
fun PatientHomeScreen(
  patientName: String,
  recentLogs: List<EmotionLog>,
  assignedActivities: List<AssignedActivity>,
  weeklySummaries: List<WeeklySummary>,
  onNavigateToCheckIn: () -> Unit,
  onNavigateToHistory: () -> Unit,
  onNavigateToWeeklySummary: () -> Unit,
  onNavigateToLibrary: () -> Unit,
  onOpenActivity: (AssignedActivity) -> Unit,
  onEmergencyClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  val pendingActivities = assignedActivities.filter { !it.isCompleted }
  val flaggedTopicsCount = recentLogs.count { it.flagForNextSession }
  val latestWeekly = weeklySummaries.firstOrNull()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    item {
      Spacer(modifier = Modifier.height(4.dp))
      CrisisDisclaimerBanner(onEmergencyClick = onEmergencyClick)
    }

    // Acolhimento Header
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(20.dp)
      ) {
        Column(modifier = Modifier.padding(20.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Olá, $patientName",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "Como você está se sentindo agora?",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
              )
            }
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Spa,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(16.dp))

          Button(
            onClick = onNavigateToCheckIn,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("start_quick_checkin_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(14.dp)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Registrar como estou (~1 min)",
              style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold)
            )
          }
        }
      }
    }

    // Destaque da Próxima Sessão (Tópicos Marcados)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToHistory() },
        colors = CardDefaults.cardColors(
          containerColor = if (flaggedTopicsCount > 0) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
          else MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = RoundedCornerShape(18.dp)
      ) {
        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(40.dp)
              .clip(CircleShape)
              .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Forum,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.secondary,
              modifier = Modifier.size(22.dp)
            )
          }
          Spacer(modifier = Modifier.width(14.dp))
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = "Próxima Sessão com o Psicólogo",
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (flaggedTopicsCount > 0)
                "$flaggedTopicsCount assunto(s) assinalado(s) para conversar"
              else "Nenhum assunto marcado até agora",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          Icon(
            imageVector = Icons.AutoMirroredFilledArrowForward,
            contentDescription = "Ver assuntos",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
          )
        }
      }
    }

    // Card do Resumo Semanal (Destaque solicitado)
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onNavigateToWeeklySummary() }
          .testTag("weekly_summary_card_home"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(18.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.CalendarMonth,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "Resumo Semanal (Seg – Dom)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
            }
            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = "Novo",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          if (latestWeekly != null) {
            Text(
              text = latestWeekly.weekLabel,
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = latestWeekly.descriptiveSynthesis.take(130) + "...",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant,
              lineHeight = 18.sp
            )
          } else {
            Text(
              text = "Faça registros durante a semana para gerar uma visão descritiva das suas emoções e bem-estar.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          OutlinedButton(
            onClick = onNavigateToWeeklySummary,
            modifier = Modifier.align(Alignment.End),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text("Consultar Resumo Completo", style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.AutoMirroredFilledArrowForward,
              contentDescription = null,
              modifier = Modifier.size(14.dp)
            )
          }
        }
      }
    }

    // Atividades Prescritas pelo Psicólogo
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Atividades e Leituras Indicadas",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
        TextButton(onClick = onNavigateToLibrary) {
          Text("Ver Todas", style = MaterialTheme.typography.labelSmall)
        }
      }

      if (pendingActivities.isEmpty()) {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.CheckCircle,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "Todas as atividades indicadas foram concluídas! Explore a biblioteca quando desejar.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      } else {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          pendingActivities.take(2).forEach { activity ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable { onOpenActivity(activity) },
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
              shape = RoundedCornerShape(14.dp)
            ) {
              Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Box(
                  modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.size(18.dp)
                  )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = activity.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${activity.theme} • ~${activity.estimatedMinutes} min • Indicado por ${activity.assignedByTherapistName}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
                Icon(
                  imageVector = Icons.AutoMirroredFilledArrowForward,
                  contentDescription = "Abrir atividade",
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(18.dp)
                )
              }
            }
          }
        }
      }
    }

    // Mensagem de Respeito ao Tempo e Sem Julgamentos
    item {
      Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp)
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "Lembre-se: não há obrigatoriedade de registrar todos os dias. O aplicativo está aqui para apoiar o seu processo no seu próprio ritmo, sem julgamentos.",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
      Spacer(modifier = Modifier.height(16.dp))
    }
  }
}

// Icon helper for directional arrow
private val Icons.AutoMirroredFilledArrowForward get() = Icons.Default.ArrowForward
