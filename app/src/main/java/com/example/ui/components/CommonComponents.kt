package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
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
import com.example.data.model.EmotionInfo
import com.example.data.model.UserRole

@Composable
fun CrisisDisclaimerBanner(
  modifier: Modifier = Modifier,
  onEmergencyClick: () -> Unit = {}
) {
  Surface(
    modifier = modifier.fillMaxWidth(),
    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.85f),
    shape = RoundedCornerShape(12.dp)
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 14.dp, vertical = 10.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(
        modifier = Modifier.weight(1f),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Info,
          contentDescription = "Aviso de Não Emergência",
          tint = MaterialTheme.colorScheme.onTertiaryContainer,
          modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Apoio entre sessões. Sem monitoramento 24h. Em crise imediata: ligue 188 (CVV).",
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 14.sp),
          color = MaterialTheme.colorScheme.onTertiaryContainer
        )
      }
      Spacer(modifier = Modifier.width(8.dp))
      TextButton(
        onClick = onEmergencyClick,
        modifier = Modifier.testTag("emergency_helpline_button")
      ) {
        Text(
          text = "Ajuda",
          style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onTertiaryContainer
        )
      }
    }
  }
}

@Composable
fun RoleSwitcherHeader(
  currentRole: UserRole,
  onToggleRole: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 6.dp),
    color = MaterialTheme.colorScheme.surfaceVariant,
    shape = RoundedCornerShape(16.dp)
  ) {
    Row(
      modifier = Modifier
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(
              if (currentRole == UserRole.PATIENT) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.secondary
            ),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = if (currentRole == UserRole.PATIENT) Icons.Default.Person else Icons.Default.Psychology,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
          )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
          Text(
            text = if (currentRole == UserRole.PATIENT) "Visão: Paciente" else "Visão: Psicólogo Clínico",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          Text(
            text = if (currentRole == UserRole.PATIENT) "Ana Clara Souza" else "Dr. Eduardo Silva (CRP 06)",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
          )
        }
      }

      OutlinedButton(
        onClick = onToggleRole,
        modifier = Modifier.testTag("toggle_role_button"),
        shape = RoundedCornerShape(12.dp)
      ) {
        Icon(
          imageVector = Icons.Default.SwapHoriz,
          contentDescription = "Alternar Perfil",
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
          text = if (currentRole == UserRole.PATIENT) "Ver Psicólogo" else "Ver Paciente",
          style = MaterialTheme.typography.labelSmall
        )
      }
    }
  }
}

@Composable
fun EmotionInfoDialog(
  emotion: EmotionInfo?,
  onDismiss: () -> Unit
) {
  if (emotion == null) return

  AlertDialog(
    onDismissRequest = onDismiss,
    icon = {
      Icon(
        imageVector = Icons.Default.Psychology,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(32.dp)
      )
    },
    title = {
      Text(
        text = "Entendendo a ${emotion.name}",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = emotion.explanation,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(14.dp))
        Card(
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)),
          shape = RoundedCornerShape(10.dp)
        ) {
          Column(modifier = Modifier.padding(12.dp)) {
            Text(
              text = "Reflexão Guiada (TCC):",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = emotion.reflectionPrompt,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onPrimaryContainer
            )
          }
        }
      }
    },
    confirmButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
      ) {
        Text("Compreendi")
      }
    }
  )
}
