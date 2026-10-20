package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AssignedActivity
import com.example.data.model.EmotionInfo
import com.example.data.model.EmotionLog
import com.example.data.model.LibraryContent
import com.example.data.model.PatientProfile
import com.example.data.model.SelectedEmotion
import com.example.data.model.TherapistProfile
import com.example.data.model.UserRole
import com.example.data.model.WeeklySummary
import com.example.data.repository.EloPsiRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

enum class PatientTab {
  HOME,
  CHECK_IN,
  HISTORY_AND_WEEKLY,
  LIBRARY,
  PROFILE_PRIVACY
}

data class CheckInDraft(
  val selectedEmotions: Map<String, Int> = emptyMap(), // Emotion name -> Intensity 1..5
  val wellbeingScore: Int = 3,
  val energyScore: Int = 3,
  val sleepQuality: Int = 3,
  val situation: String = "",
  val automaticThoughts: String = "",
  val physicalSensations: String = "",
  val behaviors: String = "",
  val helpfulAction: String = "",
  val flagForNextSession: Boolean = false,
  val isSharedWithTherapist: Boolean = true,
  val isDeeperTccExpanded: Boolean = false
)

data class UiNotification(
  val message: String,
  val isError: Boolean = false
)

class EloPsiViewModel(
  private val repository: EloPsiRepository = EloPsiRepository()
) : ViewModel() {

  // Current active role for seamless testing
  private val _currentRole = MutableStateFlow(UserRole.PATIENT)
  val currentRole: StateFlow<UserRole> = _currentRole.asStateFlow()

  // Navigation tab for patient
  private val _patientTab = MutableStateFlow(PatientTab.HOME)
  val patientTab: StateFlow<PatientTab> = _patientTab.asStateFlow()

  // Repository flows
  val emotionLogs: StateFlow<List<EmotionLog>> = repository.emotionLogsFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val assignedActivities: StateFlow<List<AssignedActivity>> = repository.assignedActivitiesFlow
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  val emotionCatalog: List<EmotionInfo> = repository.getEmotionCatalog()
  val libraryContents: List<LibraryContent> = repository.getLibraryContents()

  private val _patientProfile = MutableStateFlow(repository.getPatientProfile())
  val patientProfile: StateFlow<PatientProfile> = _patientProfile.asStateFlow()

  val therapistProfile: TherapistProfile = repository.getTherapistProfile()

  // Weekly summaries derived from logs
  val weeklySummaries: StateFlow<List<WeeklySummary>> = emotionLogs
    .combine(_patientProfile) { logs, _ ->
      repository.generateWeeklySummaries(logs)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

  // Selected week index in Weekly Summary view (0 = most recent)
  private val _selectedWeekIndex = MutableStateFlow(0)
  val selectedWeekIndex: StateFlow<Int> = _selectedWeekIndex.asStateFlow()

  // Draft for emotion check-in
  private val _checkInDraft = MutableStateFlow(CheckInDraft())
  val checkInDraft: StateFlow<CheckInDraft> = _checkInDraft.asStateFlow()

  // Dialog for emotion explanation (TCC Psychoeducation)
  private val _viewingEmotionInfo = MutableStateFlow<EmotionInfo?>(null)
  val viewingEmotionInfo: StateFlow<EmotionInfo?> = _viewingEmotionInfo.asStateFlow()

  // Dialog for viewing a library item detail
  private val _viewingLibraryContent = MutableStateFlow<LibraryContent?>(null)
  val viewingLibraryContent: StateFlow<LibraryContent?> = _viewingLibraryContent.asStateFlow()

  // Dialog for completing an activity with notes
  private val _completingActivity = MutableStateFlow<AssignedActivity?>(null)
  val completingActivity: StateFlow<AssignedActivity?> = _completingActivity.asStateFlow()

  // Dialog for therapist assigning content
  private val _assigningContent = MutableStateFlow<LibraryContent?>(null)
  val assigningContent: StateFlow<LibraryContent?> = _assigningContent.asStateFlow()

  // Toast / feedback message
  private val _notification = MutableStateFlow<UiNotification?>(null)
  val notification: StateFlow<UiNotification?> = _notification.asStateFlow()

  fun setRole(role: UserRole) {
    _currentRole.value = role
    val roleName = if (role == UserRole.PATIENT) "Paciente" else "Psicólogo"
    showNotification("Visualização alterada para $roleName")
  }

  fun setPatientTab(tab: PatientTab) {
    _patientTab.value = tab
  }

  fun setSelectedWeekIndex(index: Int) {
    _selectedWeekIndex.value = index
  }

  fun showEmotionInfo(emotion: EmotionInfo?) {
    _viewingEmotionInfo.value = emotion
  }

  fun showLibraryContent(content: LibraryContent?) {
    _viewingLibraryContent.value = content
  }

  fun openCompleteActivity(activity: AssignedActivity?) {
    _completingActivity.value = activity
  }

  fun openAssignContent(content: LibraryContent?) {
    _assigningContent.value = content
  }

  fun dismissNotification() {
    _notification.value = null
  }

  private fun showNotification(message: String, isError: Boolean = false) {
    _notification.value = UiNotification(message, isError)
  }

  // Check-in form mutators
  fun toggleEmotion(emotionName: String) {
    val current = _checkInDraft.value.selectedEmotions.toMutableMap()
    if (current.containsKey(emotionName)) {
      current.remove(emotionName)
    } else {
      current[emotionName] = 3 // default moderate intensity
    }
    _checkInDraft.value = _checkInDraft.value.copy(selectedEmotions = current)
  }

  fun setEmotionIntensity(emotionName: String, intensity: Int) {
    val current = _checkInDraft.value.selectedEmotions.toMutableMap()
    current[emotionName] = intensity.coerceIn(1, 5)
    _checkInDraft.value = _checkInDraft.value.copy(selectedEmotions = current)
  }

  fun setWellbeingScore(score: Int) {
    _checkInDraft.value = _checkInDraft.value.copy(wellbeingScore = score.coerceIn(1, 5))
  }

  fun setEnergyScore(score: Int) {
    _checkInDraft.value = _checkInDraft.value.copy(energyScore = score.coerceIn(1, 5))
  }

  fun setSleepQuality(score: Int) {
    _checkInDraft.value = _checkInDraft.value.copy(sleepQuality = score.coerceIn(1, 5))
  }

  fun updateSituation(text: String) {
    _checkInDraft.value = _checkInDraft.value.copy(situation = text)
  }

  fun updateAutomaticThoughts(text: String) {
    _checkInDraft.value = _checkInDraft.value.copy(automaticThoughts = text)
  }

  fun updatePhysicalSensations(text: String) {
    _checkInDraft.value = _checkInDraft.value.copy(physicalSensations = text)
  }

  fun updateBehaviors(text: String) {
    _checkInDraft.value = _checkInDraft.value.copy(behaviors = text)
  }

  fun updateHelpfulAction(text: String) {
    _checkInDraft.value = _checkInDraft.value.copy(helpfulAction = text)
  }

  fun toggleFlagForNextSession() {
    _checkInDraft.value = _checkInDraft.value.copy(
      flagForNextSession = !_checkInDraft.value.flagForNextSession
    )
  }

  fun toggleShareWithTherapist() {
    _checkInDraft.value = _checkInDraft.value.copy(
      isSharedWithTherapist = !_checkInDraft.value.isSharedWithTherapist
    )
  }

  fun toggleDeeperTccSection() {
    _checkInDraft.value = _checkInDraft.value.copy(
      isDeeperTccExpanded = !_checkInDraft.value.isDeeperTccExpanded
    )
  }

  fun resetCheckInDraft() {
    _checkInDraft.value = CheckInDraft()
  }

  fun saveEmotionLog() {
    val draft = _checkInDraft.value
    if (draft.selectedEmotions.isEmpty()) {
      showNotification("Por favor, selecione ao menos uma emoção para registrar.", isError = true)
      return
    }

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
    val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
    val now = Date()

    val emotionsList = draft.selectedEmotions.map { (name, intensity) ->
      SelectedEmotion(name = name, intensity = intensity)
    }

    val newLog = EmotionLog(
      id = UUID.randomUUID().toString(),
      patientId = _patientProfile.value.id,
      timestamp = now.time,
      dateFormatted = dateFormat.format(now),
      timeFormatted = timeFormat.format(now),
      emotions = emotionsList,
      wellbeingScore = draft.wellbeingScore,
      energyScore = draft.energyScore,
      sleepQuality = draft.sleepQuality,
      situation = draft.situation.trim(),
      automaticThoughts = draft.automaticThoughts.trim(),
      physicalSensations = draft.physicalSensations.trim(),
      behaviors = draft.behaviors.trim(),
      helpfulAction = draft.helpfulAction.trim(),
      flagForNextSession = draft.flagForNextSession,
      isSharedWithTherapist = draft.isSharedWithTherapist
    )

    repository.addEmotionLog(newLog)
    resetCheckInDraft()
    _patientTab.value = PatientTab.HOME
    showNotification("Registro concluído com sucesso. Acolha o seu momento.")
  }

  fun deleteLog(logId: String) {
    repository.deleteEmotionLog(logId)
    showNotification("Registro excluído com segurança.")
  }

  fun submitActivityCompletion(activityId: String, notes: String) {
    repository.completeActivity(activityId, notes.trim())
    _completingActivity.value = null
    showNotification("Atividade marcada como concluída! Suas reflexões foram salvas.")
  }

  fun assignActivityToPatient(contentId: String) {
    repository.assignActivity(contentId, _patientProfile.value.id)
    _assigningContent.value = null
    showNotification("Atividade atribuída ao paciente com sucesso.")
  }

  fun updateSharingPermission(active: Boolean) {
    repository.updatePatientSharing(active)
    _patientProfile.value = repository.getPatientProfile()
    val status = if (active) "ativado" else "desativado"
    showNotification("Compartilhamento com o psicólogo $status.")
  }

  fun regenerateInviteCode(): String {
    val newCode = repository.generateNewInviteCode()
    _patientProfile.value = repository.getPatientProfile()
    showNotification("Novo código gerado: $newCode")
    return newCode
  }
}
