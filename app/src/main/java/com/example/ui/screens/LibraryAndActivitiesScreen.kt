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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.LibraryContent

@Composable
fun LibraryAndActivitiesScreen(
  assignedActivities: List<AssignedActivity>,
  libraryContents: List<LibraryContent>,
  onOpenActivityDetail: (AssignedActivity) -> Unit,
  onCompleteActivity: (String, String) -> Unit,
  onOpenContentDetail: (LibraryContent) -> Unit,
  modifier: Modifier = Modifier
) {
  var selectedTab by remember { mutableIntStateOf(0) }
  var selectedThemeFilter by remember { mutableStateOf<String?>(null) }
  var activityToComplete by remember { mutableStateOf<AssignedActivity?>(null) }
  var selectedContentDialog by remember { mutableStateOf<LibraryContent?>(null) }

  val themes = listOf("Todos", "Ansiedade", "Sono", "Regulação Emocional", "Comunicação")

  Column(modifier = modifier.fillMaxSize()) {
    TabRow(
      selectedTabIndex = selectedTab,
      containerColor = MaterialTheme.colorScheme.surface,
      contentColor = MaterialTheme.colorScheme.primary
    ) {
      Tab(
        selected = selectedTab == 0,
        onClick = { selectedTab = 0 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.AssignmentTurnedIn, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Atividades Indicadas (${assignedActivities.size})", style = MaterialTheme.typography.labelMedium)
          }
        },
        modifier = Modifier.testTag("tab_assigned_activities")
      )
      Tab(
        selected = selectedTab == 1,
        onClick = { selectedTab = 1 },
        text = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Biblioteca Geral", style = MaterialTheme.typography.labelMedium)
          }
        },
        modifier = Modifier.testTag("tab_general_library")
      )
    }

    if (selectedTab == 0) {
      // Lista de Atividades Atribuídas pelo Psicólogo
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Exercícios e leituras prescritos pelo seu psicólogo entre sessões:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        if (assignedActivities.isEmpty()) {
          item {
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
              Text(
                text = "Nenhuma atividade prescrita no momento.",
                modifier = Modifier.padding(20.dp),
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        } else {
          items(assignedActivities, key = { it.id }) { activity ->
            Card(
              modifier = Modifier.fillMaxWidth(),
              colors = CardDefaults.cardColors(
                containerColor = if (activity.isCompleted)
                  MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                else MaterialTheme.colorScheme.surface
              ),
              elevation = CardDefaults.cardElevation(defaultElevation = if (activity.isCompleted) 0.dp else 2.dp),
              shape = RoundedCornerShape(16.dp)
            ) {
              Column(modifier = Modifier.padding(16.dp)) {
                Row(
                  modifier = Modifier.fillMaxWidth(),
                  horizontalArrangement = Arrangement.SpaceBetween,
                  verticalAlignment = Alignment.CenterVertically
                ) {
                  Surface(
                    color = if (activity.isCompleted) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.secondaryContainer,
                    shape = RoundedCornerShape(8.dp)
                  ) {
                    Text(
                      text = if (activity.isCompleted) "Concluída" else "Pendente",
                      modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                      style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                      color = if (activity.isCompleted) MaterialTheme.colorScheme.onPrimaryContainer
                      else MaterialTheme.colorScheme.onSecondaryContainer
                    )
                  }
                  Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                      imageVector = Icons.Default.Schedule,
                      contentDescription = null,
                      tint = MaterialTheme.colorScheme.onSurfaceVariant,
                      modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = "~${activity.estimatedMinutes} min",
                      style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                      color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                  }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                  text = activity.title,
                  style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                  text = "${activity.theme} • ${activity.format} • Prescrito em ${activity.assignedDateFormatted}",
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (activity.isCompleted && activity.patientNote.isNotBlank()) {
                  Spacer(modifier = Modifier.height(10.dp))
                  Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                  ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                      Text(
                        text = "Sua observação registrada:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                      )
                      Spacer(modifier = Modifier.height(2.dp))
                      Text(
                        text = activity.patientNote,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface
                      )
                    }
                  }
                }

                Spacer(modifier = Modifier.height(12.dp))

                if (!activity.isCompleted) {
                  Button(
                    onClick = { activityToComplete = activity },
                    modifier = Modifier
                      .fillMaxWidth()
                      .testTag("complete_activity_button_${activity.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(10.dp)
                  ) {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Realizar e Concluir", style = MaterialTheme.typography.labelMedium)
                  }
                }
              }
            }
          }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
      }
    } else {
      // Biblioteca Geral Temática
      val filteredContent = if (selectedThemeFilter == null || selectedThemeFilter == "Todos") libraryContents
      else libraryContents.filter { it.theme == selectedThemeFilter }

      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Spacer(modifier = Modifier.height(8.dp))
          LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(themes) { theme ->
              val isSelected = (theme == "Todos" && selectedThemeFilter == null) || selectedThemeFilter == theme
              FilterChip(
                selected = isSelected,
                onClick = { selectedThemeFilter = if (theme == "Todos") null else theme },
                label = { Text(theme) }
              )
            }
          }
        }

        items(filteredContent, key = { it.id }) { item ->
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .clickable { selectedContentDialog = item },
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(16.dp)
          ) {
            Column(modifier = Modifier.padding(16.dp)) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Surface(
                  color = MaterialTheme.colorScheme.primaryContainer,
                  shape = RoundedCornerShape(8.dp)
                ) {
                  Text(
                    text = item.theme,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                  )
                }
                Text(
                  text = "${item.format} • ~${item.estimatedMinutes} min",
                  style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                  color = MaterialTheme.colorScheme.onSurfaceVariant
                )
              }

              Spacer(modifier = Modifier.height(10.dp))

              Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = item.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
              Spacer(modifier = Modifier.height(8.dp))
              Text(
                text = "Fonte: ${item.authorOrSource}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.outline
              )
            }
          }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
      }
    }
  }

  // Dialog para Concluir Atividade com Observações
  activityToComplete?.let { activity ->
    var patientNoteInput by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { activityToComplete = null },
      title = {
        Text(
          text = "Concluir: ${activity.title}",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      },
      text = {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
          Text(
            text = "Parabéns por reservar esse momento para seu autocuidado! Deseja registrar alguma percepção ou dificuldade para seu psicólogo?",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
          OutlinedTextField(
            value = patientNoteInput,
            onValueChange = { patientNoteInput = it },
            label = { Text("Como foi realizar essa prática? (Opcional)") },
            placeholder = { Text("Ex: Senti a respiração desacelerar após a 3ª repetição...") },
            modifier = Modifier.fillMaxWidth(),
            maxLines = 3,
            shape = RoundedCornerShape(10.dp)
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            onCompleteActivity(activity.id, patientNoteInput)
            activityToComplete = null
          },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Confirmar Conclusão")
        }
      },
      dismissButton = {
        TextButton(onClick = { activityToComplete = null }) {
          Text("Voltar")
        }
      }
    )
  }

  // Dialog para Visualizar Detalhes do Conteúdo da Biblioteca
  selectedContentDialog?.let { content ->
    AlertDialog(
      onDismissRequest = { selectedContentDialog = null },
      icon = {
        Icon(
          imageVector = Icons.Default.MenuBook,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
          modifier = Modifier.size(28.dp)
        )
      },
      title = {
        Text(
          text = content.title,
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )
      },
      text = {
        LazyColumn(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          item {
            Text(
              text = "Objetivo Clínico:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = content.clinicalObjective,
              style = MaterialTheme.typography.bodySmall,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          item {
            Text(
              text = "Passo a Passo Guiado:",
              style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          }

          items(content.contentSteps) { step ->
            Row(verticalAlignment = Alignment.Top) {
              Text(
                text = "• ",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
              )
              Text(
                text = step,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp
              )
            }
          }

          item {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
              text = "Fonte/Curadoria: ${content.authorOrSource}",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
              color = MaterialTheme.colorScheme.outline
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { selectedContentDialog = null },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
          Text("Fechar")
        }
      }
    )
  }
}
