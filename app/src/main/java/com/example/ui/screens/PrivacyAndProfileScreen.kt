package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LinkOff
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EmotionLog
import com.example.data.model.PatientProfile
import com.example.data.model.TherapistProfile
import com.example.data.model.UserRole

@Composable
fun PrivacyAndProfileScreen(
  currentRole: UserRole,
  patientProfile: PatientProfile,
  therapistProfile: TherapistProfile,
  logs: List<EmotionLog>,
  onToggleRole: () -> Unit,
  onToggleSharing: (Boolean) -> Unit,
  onRegenerateCode: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var showExportDialog by remember { mutableStateOf(false) }
  var showRevokeDialog by remember { mutableStateOf(false) }
  var showCrisisInfoDialog by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header do Perfil Ativo
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(18.dp)
      ) {
        Column(modifier = Modifier.padding(18.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(48.dp)
                  .clip(CircleShape)
                  .background(
                    if (currentRole == UserRole.PATIENT) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary
                  ),
                contentAlignment = Alignment.Center
              ) {
                Icon(
                  imageVector = Icons.Default.Person,
                  contentDescription = null,
                  tint = Color.White,
                  modifier = Modifier.size(24.dp)
                )
              }
              Spacer(modifier = Modifier.width(12.dp))
              Column {
                Text(
                  text = if (currentRole == UserRole.PATIENT) patientProfile.name else therapistProfile.name,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (currentRole == UserRole.PATIENT) patientProfile.email else "${therapistProfile.crp} • ${therapistProfile.email}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }

            Surface(
              color = MaterialTheme.colorScheme.primaryContainer,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = if (currentRole == UserRole.PATIENT) "Paciente" else "Psicólogo",
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          Spacer(modifier = Modifier.height(14.dp))

          // Botão para alternar modo de teste
          OutlinedButton(
            onClick = onToggleRole,
            modifier = Modifier
              .fillMaxWidth()
              .testTag("toggle_profile_mode_button"),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = if (currentRole == UserRole.PATIENT) "Alternar para Painel do Psicólogo" else "Alternar para Modo Paciente",
              style = MaterialTheme.typography.labelMedium
            )
          }
        }
      }
    }

    // Gestão do Vínculo e Compartilhamento
    if (currentRole == UserRole.PATIENT) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
          shape = RoundedCornerShape(16.dp)
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = "Vínculo com seu Psicólogo",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Profissional: ${therapistProfile.name} (${therapistProfile.crp})",
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Controle de Compartilhamento Imediato
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .background(
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                  RoundedCornerShape(10.dp)
                )
                .padding(12.dp),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Column(modifier = Modifier.weight(1f)) {
                Text(
                  text = "Compartilhamento Ativo",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (patientProfile.isSharingActive)
                    "Seu psicólogo pode visualizar seus registros e resumos semanais autorizados."
                  else "Compartilhamento pausado. Seu psicólogo não tem acesso aos dados até você reativar.",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
              Spacer(modifier = Modifier.width(8.dp))
              Switch(
                checked = patientProfile.isSharingActive,
                onCheckedChange = { onToggleSharing(it) },
                modifier = Modifier.testTag("toggle_patient_sharing_switch")
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Código de vinculação: ${patientProfile.inviteCode}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              TextButton(onClick = { showRevokeDialog = true }) {
                Text("Desvincular", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
              }
            }
          }
        }
      }
    }

    // Canais de Crise e Apoio Imediato
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f)),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CrisisAlert,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.onTertiaryContainer,
              modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Canais de Apoio em Situações Difíceis",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onTertiaryContainer
            )
          }

          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = "O EloPsi não é serviço de emergência e não conta com monitoramento 24h. Em caso de sofrimento agudo:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Button(
              onClick = { showCrisisInfoDialog = true },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(6.dp))
              Text("CVV 188 (24h)", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }

            OutlinedButton(
              onClick = { showCrisisInfoDialog = true },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text("SAMU 192", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold))
            }
          }
        }
      }
    }

    // Diretrizes de Privacidade e LGPD
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
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Privacidade e Direitos (LGPD)",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "• Dados de saúde sensíveis (Art. 5º, II da LGPD): Seus registros pertencem exclusivamente a você.\n" +
              "• Princípio da transparência e finalidade: Usado apenas como suporte à psicoterapia com seu terapeuta.\n" +
              "• Sigilo Ético (CFP): Respeita as resoluções CFP 11/2018 e 01/2009 sobre guarda e sigilo documental.\n" +
              "• Sem publicidade: Nunca compartilhamos ou monetizamos registros com terceiros.\n" +
              "• Notificações discretas: Lembretes sem menção a sintomas na tela bloqueada.",
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Ações de Portabilidade e Exclusão
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            OutlinedButton(
              onClick = { showExportDialog = true },
              modifier = Modifier.weight(1f),
              shape = RoundedCornerShape(10.dp)
            ) {
              Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("Exportar Dados", style = MaterialTheme.typography.labelSmall)
            }
          }
        }
      }
    }

    item { Spacer(modifier = Modifier.height(20.dp)) }
  }

  // Dialog de Exportação de Dados
  if (showExportDialog) {
    val exportText = buildString {
      append("=== DADOS EXPORTADOS DO ELOPSI ===\n")
      append("Paciente: ${patientProfile.name}\n")
      append("Data de Exportação: 20/10/2026\n")
      append("Total de Registros: ${logs.size}\n\n")
      logs.forEach { log ->
        append("[${log.dateFormatted} às ${log.timeFormatted}]\n")
        append("Emoções: ${log.emotions.joinToString { "${it.name} (${it.intensity}/5)" }}\n")
        append("Bem-estar: ${log.wellbeingScore}/5 | Energia: ${log.energyScore}/5 | Sono: ${log.sleepQuality}/5\n")
        if (log.situation.isNotBlank()) append("Situação: ${log.situation}\n")
        if (log.automaticThoughts.isNotBlank()) append("Pensamentos: ${log.automaticThoughts}\n")
        if (log.helpfulAction.isNotBlank()) append("O que ajudou: ${log.helpfulAction}\n")
        append("Assunto para sessão: ${if (log.flagForNextSession) "Sim" else "Não"}\n")
        append("------------------------------------\n")
      }
    }

    AlertDialog(
      onDismissRequest = { showExportDialog = false },
      title = { Text("Exportação de Registros") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Text(
            text = "Seus registros foram compilados (${logs.size} entradas). Você pode copiar este relatório estruturado para seus próprios arquivos.",
            style = MaterialTheme.typography.bodySmall
          )
          Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = exportText.take(280) + "...",
              modifier = Modifier.padding(10.dp),
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("EloPsi Export", exportText))
            Toast.makeText(context, "Relatório copiado para a área de transferência!", Toast.LENGTH_SHORT).show()
            showExportDialog = false
          }
        ) {
          Text("Copiar Relatório")
        }
      },
      dismissButton = {
        TextButton(onClick = { showExportDialog = false }) {
          Text("Fechar")
        }
      }
    )
  }

  // Dialog de Desvinculação
  if (showRevokeDialog) {
    AlertDialog(
      onDismissRequest = { showRevokeDialog = false },
      title = { Text("Encerrar Vínculo Terapêutico") },
      text = {
        Text(
          text = "Ao encerrar o vínculo, o Dr. Eduardo Silva deixará de ter acesso aos seus registros e atividades. Você poderá se vincular a outro profissional quando desejar através de um novo código de convite.",
          style = MaterialTheme.typography.bodyMedium
        )
      },
      confirmButton = {
        Button(
          onClick = {
            onToggleSharing(false)
            showRevokeDialog = false
            Toast.makeText(context, "Vínculo pausado e compartilhamento revogado.", Toast.LENGTH_SHORT).show()
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
          Text("Confirmar")
        }
      },
      dismissButton = {
        TextButton(onClick = { showRevokeDialog = false }) {
          Text("Cancelar")
        }
      }
    )
  }

  // Dialog de Informações de Crise
  if (showCrisisInfoDialog) {
    AlertDialog(
      onDismissRequest = { showCrisisInfoDialog = false },
      icon = {
        Icon(
          imageVector = Icons.Default.Phone,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(28.dp)
        )
      },
      title = { Text("Canais de Apoio em Crise") },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "• CVV (Centro de Valorização da Vida): Disque 188. Atendimento telefônico gratuito, confidencial e disponível 24 horas por dia em todo o Brasil.",
            style = MaterialTheme.typography.bodySmall
          )
          Text(
            text = "• SAMU: Disque 192 em situações de urgência médica ou risco imediato à vida.",
            style = MaterialTheme.typography.bodySmall
          )
          Text(
            text = "• CAPS (Centros de Atenção Psicossocial) e UPAs de sua cidade oferecem acolhimento presencial na rede pública de saúde.",
            style = MaterialTheme.typography.bodySmall
          )
        }
      },
      confirmButton = {
        Button(onClick = { showCrisisInfoDialog = false }) {
          Text("Entendido")
        }
      }
    )
  }
}
