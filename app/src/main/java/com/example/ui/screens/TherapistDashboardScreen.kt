package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.data.model.LibraryContent
import com.example.data.model.PatientProfile
import com.example.data.model.TherapistProfile
import com.example.data.model.WeeklySummary

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TherapistDashboardScreen(
  therapistProfile: TherapistProfile,
  patientProfile: PatientProfile,
  patientLogs: List<EmotionLog>,
  assignedActivities: List<AssignedActivity>,
  weeklySummaries: List<WeeklySummary>,
  libraryContents: List<LibraryContent>,
  onAssignActivity: (contentId: String) -> Unit,
  onRegenerateCode: () -> Unit,
  onViewWeeklySummaryDetails: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showAssignDialog by remember { mutableStateOf(false) }

  // Restrição ética: o psicólogo só visualiza registros se o compartilhamento geral do paciente estiver ativo
  // e apenas os registros onde log.isSharedWithTherapist == true.
  val isSharingAuthorized = patientProfile.isSharingActive
  val authorizedLogs = if (isSharingAuthorized) patientLogs.filter { it.isSharedWithTherapist } else emptyList()
  val nextSessionFlaggedLogs = authorizedLogs.filter { it.flagForNextSession }
  val latestWeekly = weeklySummaries.firstOrNull()

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header do Psicólogo
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)),
        shape = RoundedCornerShape(18.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = therapistProfile.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSecondaryContainer
              )
              Text(
                text = "${therapistProfile.crp} • TCC e Práticas Baseadas em Evidências",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f)
              )
            }
            Box(
              modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.secondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
              )
            }
          }
        }
      }
    }

    // Card do Paciente Vinculado
    item {
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
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onPrimaryContainer,
                  modifier = Modifier.size(20.dp)
                )
              }
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = patientProfile.name,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "Vínculo ativo desde ${patientProfile.linkedDate}",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              color = if (isSharingAuthorized) MaterialTheme.colorScheme.primaryContainer
              else MaterialTheme.colorScheme.errorContainer,
              shape = RoundedCornerShape(8.dp)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (isSharingAuthorized) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                  contentDescription = null,
                  modifier = Modifier.size(12.dp),
                  tint = if (isSharingAuthorized) MaterialTheme.colorScheme.onPrimaryContainer
                  else MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = if (isSharingAuthorized) "Compartilhando" else "Acesso Revogado",
                  style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                  color = if (isSharingAuthorized) MaterialTheme.colorScheme.onPrimaryContainer
                  else MaterialTheme.colorScheme.onErrorContainer
                )
              }
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = "Código de Vinculação:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Text(
                text = patientProfile.inviteCode,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
            }
            OutlinedButton(
              onClick = onRegenerateCode,
              shape = RoundedCornerShape(8.dp)
            ) {
              Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Novo Código", style = MaterialTheme.typography.labelSmall)
            }
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "Frequência sugerida: ${patientProfile.suggestedFrequency}",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }

    // AVISO CASO COMPARTILHAMENTO ESTEJA DESATIVADO PELO PACIENTE
    if (!isSharingAuthorized) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
          shape = RoundedCornerShape(14.dp)
        ) {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.Lock,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.outline,
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
              text = "O paciente pausou o compartilhamento de registros. Conforme as diretrizes de privacidade e LGPD, os dados não estão acessíveis no momento.",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      }
    } else {
      // 1. DESTAQUE MÁXIMO: ASSUNTOS MARCADOS PARA A PRÓXIMA SESSÃO
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(
            containerColor = if (nextSessionFlaggedLogs.isNotEmpty())
              MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
            else MaterialTheme.colorScheme.surface
          ),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Forum,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.secondary,
                  modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Assuntos para a Próxima Sessão (${nextSessionFlaggedLogs.size})",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (nextSessionFlaggedLogs.isEmpty()) {
              Text(
                text = "Nenhum assunto marcado pelo paciente com esta prioridade.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            } else {
              Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                nextSessionFlaggedLogs.forEach { flaggedLog ->
                  Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                      Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                      ) {
                        Text(
                          text = "${flaggedLog.dateFormatted} às ${flaggedLog.timeFormatted}",
                          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                          color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                          text = "Bem-estar: ${flaggedLog.wellbeingScore}/5",
                          style = MaterialTheme.typography.labelSmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                      }
                      if (flaggedLog.situation.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                          text = "Situação: ${flaggedLog.situation}",
                          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                          color = MaterialTheme.colorScheme.onSurface
                        )
                      }
                      if (flaggedLog.automaticThoughts.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                          text = "Pensamentos: ${flaggedLog.automaticThoughts}",
                          style = MaterialTheme.typography.bodySmall,
                          color = MaterialTheme.colorScheme.onSurfaceVariant
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

      // 2. RESUMO SEMANAL COMPILADO DO PACIENTE
      item {
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
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.CalendarMonth,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = "Resumo Semanal do Paciente",
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
              TextButton(onClick = onViewWeeklySummaryDetails) {
                Text("Ver Gráficos", style = MaterialTheme.typography.labelSmall)
              }
            }

            Spacer(modifier = Modifier.height(6.dp))

            if (latestWeekly != null) {
              Text(
                text = latestWeekly.weekLabel,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = latestWeekly.descriptiveSynthesis,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
              )
            } else {
              Text(
                text = "Sem registros compilados nesta semana.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // 3. AÇÃO: INDICAR NOVA ATIVIDADE OU LEITURA
      item {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Atividades Prescritas ao Paciente",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Button(
            onClick = { showAssignDialog = true },
            modifier = Modifier.testTag("prescribe_activity_button"),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Indicar da Biblioteca", style = MaterialTheme.typography.labelSmall)
          }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          assignedActivities.forEach { act ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
              shape = RoundedCornerShape(12.dp)
            ) {
              Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = if (act.isCompleted) Icons.Default.CheckCircle else Icons.Default.Assignment,
                  contentDescription = null,
                  tint = if (act.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                  modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = act.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "Status: ${if (act.isCompleted) "Concluída (${act.completedDateFormatted ?: ""})" else "Pendente"}",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                  if (act.isCompleted && act.patientNote.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = "Nota do paciente: \"${act.patientNote}\"",
                      style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                      color = MaterialTheme.colorScheme.primary
                    )
                  }
                }
              }
            }
          }
        }
      }

      // 4. HISTÓRICO RECENTE COMPARTILHADO
      item {
        Text(
          text = "Registros Recentes Compartilhados (${authorizedLogs.size})",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onSurface
        )
      }

      items(authorizedLogs.take(5), key = { it.id }) { log ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          shape = RoundedCornerShape(12.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(
                text = "${log.dateFormatted} às ${log.timeFormatted}",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Bem-estar: ${log.wellbeingScore}/5 • Energia: ${log.energyScore}/5",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            Spacer(modifier = Modifier.height(6.dp))
            FlowRow(
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
              log.emotions.forEach { emotion ->
                Surface(
                  color = MaterialTheme.colorScheme.surfaceVariant,
                  shape = RoundedCornerShape(6.dp)
                ) {
                  Text(
                    text = "${emotion.name} (${emotion.intensity}/5)",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }
              }
            }
            if (log.situation.isNotBlank()) {
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "Situação: ${log.situation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(16.dp)) }
  }

  // Dialog para o Psicólogo Prescrever Material da Biblioteca
  if (showAssignDialog) {
    AlertDialog(
      onDismissRequest = { showAssignDialog = false },
      title = {
        Text("Indicar Material da Biblioteca", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
      },
      text = {
        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          item {
            Text(
              text = "Selecione a atividade para prescrever a ${patientProfile.name}:",
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
          items(libraryContents) { content ->
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clickable {
                  onAssignActivity(content.id)
                  showAssignDialog = false
                },
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = content.title,
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = "${content.theme} • ~${content.estimatedMinutes} min",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.primary
                )
              }
            }
          }
        }
      },
      confirmButton = {
        TextButton(onClick = { showAssignDialog = false }) {
          Text("Fechar")
        }
      }
    )
  }
}
