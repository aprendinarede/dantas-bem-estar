package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.EmotionLog
import com.example.data.model.SelectedEmotion
import com.example.data.repository.EloPsiRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.UUID

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context matches EloPsi app name`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("EloPsi", appName)
  }

  @Test
  fun `verify weekly summary generation groups monday to sunday and computes metrics`() {
    val repository = EloPsiRepository()
    val testLogs = listOf(
      EmotionLog(
        id = UUID.randomUUID().toString(),
        patientId = "paciente-101",
        timestamp = System.currentTimeMillis(),
        dateFormatted = "20/10/2026",
        timeFormatted = "10:00",
        emotions = listOf(SelectedEmotion("Ansiedade", 4), SelectedEmotion("Sobrecarga", 3)),
        wellbeingScore = 2,
        energyScore = 3,
        sleepQuality = 2,
        situation = "Reunião de trabalho sobre prazos",
        automaticThoughts = "Não vou dar conta",
        flagForNextSession = true,
        isSharedWithTherapist = true
      ),
      EmotionLog(
        id = UUID.randomUUID().toString(),
        patientId = "paciente-101",
        timestamp = System.currentTimeMillis() - 86400000L,
        dateFormatted = "19/10/2026",
        timeFormatted = "18:00",
        emotions = listOf(SelectedEmotion("Calma", 4)),
        wellbeingScore = 4,
        energyScore = 3,
        sleepQuality = 4,
        situation = "Pausa com respiração guiada",
        flagForNextSession = false,
        isSharedWithTherapist = true
      )
    )

    val summaries = repository.generateWeeklySummaries(testLogs)
    assertTrue("Should generate at least one weekly summary", summaries.isNotEmpty())

    val summary = summaries.first()
    assertEquals("Should calculate correct total entries", 2, summary.totalEntries)
    assertEquals("Average wellbeing should be (2 + 4) / 2 = 3.0", 3.0f, summary.avgWellbeing, 0.1f)
    assertEquals("Flagged topics for session should be 1", 1, summary.topicsForSessionCount)
    assertTrue("Summary synthesis should mention records and non-diagnostic tone", summary.descriptiveSynthesis.contains("registros"))
    assertFalse("Clinical disclaimer must exist", summary.clinicalDisclaimer.isBlank())
  }

  @Test
  fun `verify patient sharing permission toggle`() {
    val repository = EloPsiRepository()
    assertTrue(repository.getPatientProfile().isSharingActive)

    repository.updatePatientSharing(false)
    assertFalse(repository.getPatientProfile().isSharingActive)

    repository.updatePatientSharing(true)
    assertTrue(repository.getPatientProfile().isSharingActive)
  }
}
