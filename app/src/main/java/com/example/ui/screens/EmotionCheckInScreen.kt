package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
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
import com.example.ui.viewmodel.CheckInDraft

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EmotionCheckInScreen(
  draft: CheckInDraft,
  emotionCatalog: List<EmotionInfo>,
  onToggleEmotion: (String) -> Unit,
  onSetEmotionIntensity: (String, Int) -> Unit,
  onShowEmotionInfo: (EmotionInfo) -> Unit,
  onSetWellbeing: (Int) -> Unit,
  onSetEnergy: (Int) -> Unit,
  onSetSleep: (Int) -> Unit,
  onUpdateSituation: (String) -> Unit,
  onUpdateThoughts: (String) -> Unit,
  onUpdateSensations: (String) -> Unit,
  onUpdateBehaviors: (String) -> Unit,
  onUpdateHelpfulAction: (String) -> Unit,
  onToggleNextSession: () -> Unit,
  onToggleShare: () -> Unit,
  onToggleDeeperTcc: () -> Unit,
  onSave: () -> Unit,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Header & Orientações
    item {
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Registrar Como Estou",
        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = "Identifique suas emoções sem pressa e sem julgamento. Leva cerca de 1 minuto, com opção de aprofundar se desejar.",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }

    // 1. Seleção de Emoções com Explicação Didática
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "1. Emoções presentes agora",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "Você pode selecionar mais de uma. Toque em '?' para entender a função da emoção.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )

          Spacer(modifier = Modifier.height(12.dp))

          FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            emotionCatalog.forEach { emotion ->
              val isSelected = draft.selectedEmotions.containsKey(emotion.name)
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .clip(RoundedCornerShape(12.dp))
                  .background(
                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                  )
                  .clickable { onToggleEmotion(emotion.name) }
                  .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
              ) {
                Text(
                  text = emotion.name,
                  style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                  ),
                  color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
                  else MaterialTheme.colorScheme.onSurfaceVariant
                )
                IconButton(
                  onClick = { onShowEmotionInfo(emotion) },
                  modifier = Modifier.size(32.dp)
                ) {
                  Icon(
                    imageVector = Icons.Default.HelpOutline,
                    contentDescription = "Ver explicação de ${emotion.name}",
                    tint = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    modifier = Modifier.size(16.dp)
                  )
                }
              }
            }
          }

          // Ajuste de intensidade para as emoções selecionadas
          if (draft.selectedEmotions.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
              text = "Intensidade percebida (1: Leve a 5: Muito Intensa):",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
              color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
              draft.selectedEmotions.forEach { (emotionName, intensity) ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .background(
                      MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                      RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = emotionName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.width(100.dp)
                  )
                  Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    (1..5).forEach { level ->
                      val isCurrent = level == intensity
                      Box(
                        modifier = Modifier
                          .size(32.dp)
                          .clip(CircleShape)
                          .background(
                            if (isCurrent) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.surface
                          )
                          .clickable { onSetEmotionIntensity(emotionName, level) },
                        contentAlignment = Alignment.Center
                      ) {
                        Text(
                          text = level.toString(),
                          style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                          ),
                          color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onSurface
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
    }

    // 2. Bem-estar, Energia e Sono (Escala 1 a 5)
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "2. Como está seu estado geral?",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
          Spacer(modifier = Modifier.height(14.dp))

          // Bem-estar
          ScoreSelectorRow(
            label = "Bem-estar Geral",
            score = draft.wellbeingScore,
            onSelectScore = onSetWellbeing,
            lowLabel = "Desconfortável",
            highLabel = "Pleno"
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Energia
          ScoreSelectorRow(
            label = "Nível de Energia",
            score = draft.energyScore,
            onSelectScore = onSetEnergy,
            lowLabel = "Esgotado(a)",
            highLabel = "Disposto(a)"
          )

          Spacer(modifier = Modifier.height(14.dp))

          // Sono
          ScoreSelectorRow(
            label = "Qualidade do Sono Recente",
            score = draft.sleepQuality,
            onSelectScore = onSetSleep,
            lowLabel = "Insone/Agitado",
            highLabel = "Reparador"
          )
        }
      }
    }

    // 3. Marcações Especiais: Próxima Sessão e Compartilhamento
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          // Flag para próxima sessão
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onToggleNextSession() },
            verticalAlignment = Alignment.CenterVertically
          ) {
            Checkbox(
              checked = draft.flagForNextSession,
              onCheckedChange = { onToggleNextSession() },
              modifier = Modifier.testTag("flag_next_session_checkbox")
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
              Text(
                text = "Quero conversar sobre isso na próxima sessão",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Ficará destacado para o psicólogo e em seu histórico",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Compartilhar com psicólogo
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onToggleShare() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              modifier = Modifier.weight(1f),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = if (draft.isSharedWithTherapist) Icons.Default.Share else Icons.Default.Lock,
                contentDescription = null,
                tint = if (draft.isSharedWithTherapist) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = if (draft.isSharedWithTherapist) "Compartilhar com meu psicólogo" else "Registro estritamente privado",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                  text = if (draft.isSharedWithTherapist) "Visível no painel do Dr. Eduardo" else "Visível apenas no seu celular",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }
            }
            Switch(
              checked = draft.isSharedWithTherapist,
              onCheckedChange = { onToggleShare() }
            )
          }
        }
      }
    }

    // 4. Aprofundamento TCC Opcional
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(16.dp)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { onToggleDeeperTcc() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "Aprofundamento TCC (Opcional)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = "Situação, pensamentos automáticos e sensações corporais",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
            IconButton(onClick = onToggleDeeperTcc) {
              Icon(
                imageVector = if (draft.isDeeperTccExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = "Expandir aprofundamento TCC",
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }

          AnimatedVisibility(visible = draft.isDeeperTccExpanded) {
            Column(
              modifier = Modifier.padding(top = 16.dp),
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              OutlinedTextField(
                value = draft.situation,
                onValueChange = onUpdateSituation,
                label = { Text("O que aconteceu? (Situação / Fatos)") },
                placeholder = { Text("Ex: Reunião de trabalho, conversa em família, momento sozinho...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
              )

              OutlinedTextField(
                value = draft.automaticThoughts,
                onValueChange = onUpdateThoughts,
                label = { Text("Pensamentos automáticos ('O que passou pela cabeça?')") },
                placeholder = { Text("Ex: 'Não vou conseguir', 'Vão me julgar mal'...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
              )

              OutlinedTextField(
                value = draft.physicalSensations,
                onValueChange = onUpdateSensations,
                label = { Text("Sensações físicas percebidas no corpo") },
                placeholder = { Text("Ex: Tensão nos ombros, aperto no peito, respiração acelerada...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp)
              )

              OutlinedTextField(
                value = draft.behaviors,
                onValueChange = onUpdateBehaviors,
                label = { Text("Comportamento ('O que você fez ou teve vontade de fazer?')") },
                placeholder = { Text("Ex: Me isolei, respirei fundo, fui caminhar...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp)
              )

              OutlinedTextField(
                value = draft.helpfulAction,
                onValueChange = onUpdateHelpfulAction,
                label = { Text("O que ajudou ou poderia ajudar neste momento?") },
                placeholder = { Text("Ex: Parar 2 minutos para respirar, beber água, falar com alguém...") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2,
                shape = RoundedCornerShape(12.dp)
              )
            }
          }
        }
      }
    }

    // Botões de Ação
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedButton(
          onClick = onCancel,
          modifier = Modifier.weight(1f),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text("Cancelar")
        }

        Button(
          onClick = onSave,
          modifier = Modifier
            .weight(2f)
            .testTag("save_emotion_log_button"),
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp)
        ) {
          Icon(imageVector = Icons.Default.Check, contentDescription = null)
          Spacer(modifier = Modifier.width(8.dp))
          Text("Salvar Registro", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
        }
      }
      Spacer(modifier = Modifier.height(20.dp))
    }
  }
}

@Composable
private fun ScoreSelectorRow(
  label: String,
  score: Int,
  onSelectScore: (Int) -> Unit,
  lowLabel: String,
  highLabel: String
) {
  Column(modifier = Modifier.fillMaxWidth()) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = label,
        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.onSurface
      )
      Text(
        text = "$score / 5",
        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
      )
    }
    Spacer(modifier = Modifier.height(8.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      (1..5).forEach { level ->
        val isSelected = level == score
        Box(
          modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(
              if (isSelected) MaterialTheme.colorScheme.primary
              else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
            )
            .clickable { onSelectScore(level) },
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = level.toString(),
            style = MaterialTheme.typography.bodyMedium.copy(
              fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            ),
            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }
    }
    Spacer(modifier = Modifier.height(4.dp))
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = "1: $lowLabel",
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
      )
      Text(
        text = "5: $highLabel",
        style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
      )
    }
  }
}
