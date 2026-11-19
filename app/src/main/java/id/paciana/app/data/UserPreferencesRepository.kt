package id.paciana.app.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.pacianaDataStore by preferencesDataStore(name = "paciana_preferences")

data class UserPreferences(
    val language: String = "Bahasa Indonesia",
    val researchNote: String = ""
)

/**
 * Small, durable user settings and research-note storage.
 *
 * Larger structured collections (catalogue records, OCR documents and translations)
 * belong in Room and are intentionally not stored in Preferences DataStore.
 */
class UserPreferencesRepository(private val context: Context) {
    val preferences: Flow<UserPreferences> = context.pacianaDataStore.data.map { values ->
        UserPreferences(
            language = values[LANGUAGE] ?: "Bahasa Indonesia",
            researchNote = values[RESEARCH_NOTE] ?: ""
        )
    }

    suspend fun setLanguage(language: String) {
        context.pacianaDataStore.edit { values ->
            values[LANGUAGE] = language
        }
    }

    suspend fun setResearchNote(note: String) {
        context.pacianaDataStore.edit { values ->
            values[RESEARCH_NOTE] = note
        }
    }

    private companion object {
        val LANGUAGE = stringPreferencesKey("interface_language")
        val RESEARCH_NOTE = stringPreferencesKey("research_note")
    }
}
