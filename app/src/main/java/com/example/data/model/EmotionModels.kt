package com.example.data.model

data class EmotionInfo(
  val id: String,
  val name: String,
  val explanation: String,
  val category: String,
  val reflectionPrompt: String
)

data class SelectedEmotion(
  val name: String,
  val intensity: Int // 1 (muito leve) a 5 (muito intensa)
)

data class EmotionLog(
  val id: String,
  val patientId: String,
  val timestamp: Long,
  val dateFormatted: String,
  val timeFormatted: String,
  val emotions: List<SelectedEmotion>,
  val wellbeingScore: Int, // 1 a 5
  val energyScore: Int, // 1 a 5
  val sleepQuality: Int, // 1 a 5
  val situation: String = "",
  val automaticThoughts: String = "",
  val physicalSensations: String = "",
  val behaviors: String = "",
  val helpfulAction: String = "",
  val flagForNextSession: Boolean = false,
  val isSharedWithTherapist: Boolean = true
)

data class WeeklySummary(
  val weekLabel: String,
  val weekNumber: Int,
  val year: Int,
  val startDateTimestamp: Long,
  val endDateTimestamp: Long,
  val totalEntries: Int,
  val predominantEmotions: List<Pair<String, Int>>,
  val avgWellbeing: Float,
  val avgEnergy: Float,
  val avgSleep: Float,
  val topicsForSessionCount: Int,
  val sessionDiscussionItems: List<String>,
  val recurringThemes: List<String>,
  val descriptiveSynthesis: String,
  val clinicalDisclaimer: String = "Resumo descritivo para apoio ao diálogo terapêutico. Não constitui diagnóstico clínico nem estabelece relações de causa e efeito."
)
