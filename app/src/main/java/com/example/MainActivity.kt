package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.UserRole
import com.example.ui.components.EmotionInfoDialog
import com.example.ui.components.RoleSwitcherHeader
import com.example.ui.screens.EmotionCheckInScreen
import com.example.ui.screens.HistoryAndWeeklySummaryScreen
import com.example.ui.screens.LibraryAndActivitiesScreen
import com.example.ui.screens.PatientHomeScreen
import com.example.ui.screens.PrivacyAndProfileScreen
import com.example.ui.screens.TherapistDashboardScreen
import com.example.ui.theme.EloPsiTheme
import com.example.ui.viewmodel.EloPsiViewModel
import com.example.ui.viewmodel.PatientTab

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      EloPsiTheme {
        EloPsiApp()
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EloPsiApp(
  viewModel: EloPsiViewModel = viewModel()
) {
  val currentRole by viewModel.currentRole.collectAsStateWithLifecycle()
  val currentTab by viewModel.patientTab.collectAsStateWithLifecycle()
  val logs by viewModel.emotionLogs.collectAsStateWithLifecycle()
  val assignedActivities by viewModel.assignedActivities.collectAsStateWithLifecycle()
  val weeklySummaries by viewModel.weeklySummaries.collectAsStateWithLifecycle()
  val patientProfile by viewModel.patientProfile.collectAsStateWithLifecycle()
  val therapistProfile = viewModel.therapistProfile
  val checkInDraft by viewModel.checkInDraft.collectAsStateWithLifecycle()
  val selectedWeekIndex by viewModel.selectedWeekIndex.collectAsStateWithLifecycle()
  val viewingEmotionInfo by viewModel.viewingEmotionInfo.collectAsStateWithLifecycle()
  val notification by viewModel.notification.collectAsStateWithLifecycle()

  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(notification) {
    notification?.let {
      snackbarHostState.showSnackbar(it.message)
      viewModel.dismissNotification()
    }
  }

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      Column(
        modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)
      ) {
        TopAppBar(
          title = {
            Text(
              text = "EloPsi",
              style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
          },
          colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
          )
        )
        // Seletor de Perfil no Topo para Demonstração e Testes
        RoleSwitcherHeader(
          currentRole = currentRole,
          onToggleRole = {
            val newRole = if (currentRole == UserRole.PATIENT) UserRole.THERAPIST else UserRole.PATIENT
            viewModel.setRole(newRole)
          }
        )
      }
    },
    bottomBar = {
      NavigationBar(
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface
      ) {
        if (currentRole == UserRole.PATIENT) {
          NavigationBarItem(
            selected = currentTab == PatientTab.HOME,
            onClick = { viewModel.setPatientTab(PatientTab.HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Início") },
            label = { Text("Início") },
            modifier = Modifier.testTag("nav_patient_home")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.CHECK_IN,
            onClick = { viewModel.setPatientTab(PatientTab.CHECK_IN) },
            icon = { Icon(Icons.Default.AddCircle, contentDescription = "Registrar") },
            label = { Text("Registrar") },
            modifier = Modifier.testTag("nav_patient_checkin")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.HISTORY_AND_WEEKLY,
            onClick = { viewModel.setPatientTab(PatientTab.HISTORY_AND_WEEKLY) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Resumo & Histórico") },
            label = { Text("Resumo") },
            modifier = Modifier.testTag("nav_patient_history")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.LIBRARY,
            onClick = { viewModel.setPatientTab(PatientTab.LIBRARY) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Atividades") },
            label = { Text("Atividades") },
            modifier = Modifier.testTag("nav_patient_library")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.PROFILE_PRIVACY,
            onClick = { viewModel.setPatientTab(PatientTab.PROFILE_PRIVACY) },
            icon = { Icon(Icons.Default.Security, contentDescription = "Privacidade") },
            label = { Text("Privacidade") },
            modifier = Modifier.testTag("nav_patient_privacy")
          )
        } else {
          NavigationBarItem(
            selected = currentTab == PatientTab.HOME,
            onClick = { viewModel.setPatientTab(PatientTab.HOME) },
            icon = { Icon(Icons.Default.Dashboard, contentDescription = "Painel Clínico") },
            label = { Text("Painel") },
            modifier = Modifier.testTag("nav_therapist_dashboard")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.HISTORY_AND_WEEKLY,
            onClick = { viewModel.setPatientTab(PatientTab.HISTORY_AND_WEEKLY) },
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Resumo Semanal") },
            label = { Text("Resumo Semanal") },
            modifier = Modifier.testTag("nav_therapist_weekly")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.LIBRARY,
            onClick = { viewModel.setPatientTab(PatientTab.LIBRARY) },
            icon = { Icon(Icons.Default.MenuBook, contentDescription = "Biblioteca") },
            label = { Text("Biblioteca") },
            modifier = Modifier.testTag("nav_therapist_library")
          )
          NavigationBarItem(
            selected = currentTab == PatientTab.PROFILE_PRIVACY,
            onClick = { viewModel.setPatientTab(PatientTab.PROFILE_PRIVACY) },
            icon = { Icon(Icons.Default.Security, contentDescription = "Privacidade e Vínculo") },
            label = { Text("Privacidade") },
            modifier = Modifier.testTag("nav_therapist_privacy")
          )
        }
      }
    },
    snackbarHost = { SnackbarHost(snackbarHostState) }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
    ) {
      if (currentRole == UserRole.PATIENT) {
        when (currentTab) {
          PatientTab.HOME -> {
            PatientHomeScreen(
              patientName = patientProfile.name,
              recentLogs = logs,
              assignedActivities = assignedActivities,
              weeklySummaries = weeklySummaries,
              onNavigateToCheckIn = { viewModel.setPatientTab(PatientTab.CHECK_IN) },
              onNavigateToHistory = { viewModel.setPatientTab(PatientTab.HISTORY_AND_WEEKLY) },
              onNavigateToWeeklySummary = { viewModel.setPatientTab(PatientTab.HISTORY_AND_WEEKLY) },
              onNavigateToLibrary = { viewModel.setPatientTab(PatientTab.LIBRARY) },
              onOpenActivity = { viewModel.setPatientTab(PatientTab.LIBRARY) },
              onEmergencyClick = { viewModel.setPatientTab(PatientTab.PROFILE_PRIVACY) }
            )
          }

          PatientTab.CHECK_IN -> {
            EmotionCheckInScreen(
              draft = checkInDraft,
              emotionCatalog = viewModel.emotionCatalog,
              onToggleEmotion = { viewModel.toggleEmotion(it) },
              onSetEmotionIntensity = { name, intensity -> viewModel.setEmotionIntensity(name, intensity) },
              onShowEmotionInfo = { viewModel.showEmotionInfo(it) },
              onSetWellbeing = { viewModel.setWellbeingScore(it) },
              onSetEnergy = { viewModel.setEnergyScore(it) },
              onSetSleep = { viewModel.setSleepQuality(it) },
              onUpdateSituation = { viewModel.updateSituation(it) },
              onUpdateThoughts = { viewModel.updateAutomaticThoughts(it) },
              onUpdateSensations = { viewModel.updatePhysicalSensations(it) },
              onUpdateBehaviors = { viewModel.updateBehaviors(it) },
              onUpdateHelpfulAction = { viewModel.updateHelpfulAction(it) },
              onToggleNextSession = { viewModel.toggleFlagForNextSession() },
              onToggleShare = { viewModel.toggleShareWithTherapist() },
              onToggleDeeperTcc = { viewModel.toggleDeeperTccSection() },
              onSave = { viewModel.saveEmotionLog() },
              onCancel = {
                viewModel.resetCheckInDraft()
                viewModel.setPatientTab(PatientTab.HOME)
              }
            )
          }

          PatientTab.HISTORY_AND_WEEKLY -> {
            HistoryAndWeeklySummaryScreen(
              logs = logs,
              weeklySummaries = weeklySummaries,
              selectedWeekIndex = selectedWeekIndex,
              onSelectWeekIndex = { viewModel.setSelectedWeekIndex(it) },
              onDeleteLog = { viewModel.deleteLog(it) }
            )
          }

          PatientTab.LIBRARY -> {
            LibraryAndActivitiesScreen(
              assignedActivities = assignedActivities,
              libraryContents = viewModel.libraryContents,
              onOpenActivityDetail = { /* Handled in dialog */ },
              onCompleteActivity = { id, notes -> viewModel.submitActivityCompletion(id, notes) },
              onOpenContentDetail = { /* Handled in dialog */ }
            )
          }

          PatientTab.PROFILE_PRIVACY -> {
            PrivacyAndProfileScreen(
              currentRole = currentRole,
              patientProfile = patientProfile,
              therapistProfile = therapistProfile,
              logs = logs,
              onToggleRole = { viewModel.setRole(UserRole.THERAPIST) },
              onToggleSharing = { viewModel.updateSharingPermission(it) },
              onRegenerateCode = { viewModel.regenerateInviteCode() }
            )
          }
        }
      } else {
        // Therapist Screens
        when (currentTab) {
          PatientTab.HOME -> {
            TherapistDashboardScreen(
              therapistProfile = therapistProfile,
              patientProfile = patientProfile,
              patientLogs = logs,
              assignedActivities = assignedActivities,
              weeklySummaries = weeklySummaries,
              libraryContents = viewModel.libraryContents,
              onAssignActivity = { contentId -> viewModel.assignActivityToPatient(contentId) },
              onRegenerateCode = { viewModel.regenerateInviteCode() },
              onViewWeeklySummaryDetails = { viewModel.setPatientTab(PatientTab.HISTORY_AND_WEEKLY) }
            )
          }

          PatientTab.HISTORY_AND_WEEKLY -> {
            // Authorized view of logs and weekly summary
            HistoryAndWeeklySummaryScreen(
              logs = if (patientProfile.isSharingActive) logs.filter { it.isSharedWithTherapist } else emptyList(),
              weeklySummaries = if (patientProfile.isSharingActive) weeklySummaries else emptyList(),
              selectedWeekIndex = selectedWeekIndex,
              onSelectWeekIndex = { viewModel.setSelectedWeekIndex(it) },
              onDeleteLog = { /* Therapist cannot delete patient logs */ }
            )
          }

          PatientTab.LIBRARY -> {
            LibraryAndActivitiesScreen(
              assignedActivities = assignedActivities,
              libraryContents = viewModel.libraryContents,
              onOpenActivityDetail = {},
              onCompleteActivity = { _, _ -> },
              onOpenContentDetail = {}
            )
          }

          PatientTab.PROFILE_PRIVACY, PatientTab.CHECK_IN -> {
            PrivacyAndProfileScreen(
              currentRole = currentRole,
              patientProfile = patientProfile,
              therapistProfile = therapistProfile,
              logs = logs,
              onToggleRole = { viewModel.setRole(UserRole.PATIENT) },
              onToggleSharing = { viewModel.updateSharingPermission(it) },
              onRegenerateCode = { viewModel.regenerateInviteCode() }
            )
          }
        }
      }
    }
  }

  // Psicoeducação emocional dialog
  EmotionInfoDialog(
    emotion = viewingEmotionInfo,
    onDismiss = { viewModel.showEmotionInfo(null) }
  )
}
