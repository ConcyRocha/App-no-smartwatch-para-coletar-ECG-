package br.concy.demo.health

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PassiveDataRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val latestHeartRate: Flow<Double> = dataStore.data.map { prefs ->
        prefs[LATEST_HEART_RATE] ?: 0.0
    }

    suspend fun storeLatestHeartRate(heartRate: Double) {
        dataStore.edit { prefs ->
            prefs[LATEST_HEART_RATE] = heartRate
        }
    }

    companion object {
        const val PREFERENCES_FILE_NAME = "passive_data_preferences"
        private val LATEST_HEART_RATE = doublePreferencesKey("latest_heart_rate")
    }
}