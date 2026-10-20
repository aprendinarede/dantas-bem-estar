package com.example.data.model

enum class UserRole {
  PATIENT,
  THERAPIST
}

data class PatientProfile(
  val id: String,
  val name: String,
  val email: String,
  val inviteCode: String,
  val therapistId: String,
  val linkedDate: String,
  val suggestedFrequency: String = "1 a 2 vezes ao dia ou após situações marcantes",
  val isSharingActive: Boolean = true
)

data class TherapistProfile(
  val id: String,
  val name: String,
  val crp: String,
  val email: String,
  val defaultInviteCode: String
)
