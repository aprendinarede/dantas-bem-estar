package com.example.data.model

data class LibraryContent(
  val id: String,
  val title: String,
  val description: String,
  val theme: String, // Ansiedade, Sono, Estresse, Regulação Emocional, Comunicação
  val format: String, // Leitura, Exercício Prático, Áudio/Guia
  val estimatedMinutes: Int,
  val authorOrSource: String,
  val clinicalObjective: String,
  val contentSteps: List<String>
)

data class AssignedActivity(
  val id: String,
  val patientId: String,
  val contentId: String,
  val title: String,
  val theme: String,
  val format: String,
  val estimatedMinutes: Int,
  val assignedByTherapistName: String,
  val assignedDateFormatted: String,
  val isCompleted: Boolean = false,
  val patientNote: String = "",
  val completedDateFormatted: String? = null
)
