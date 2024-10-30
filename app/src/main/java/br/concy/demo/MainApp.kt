package br.concy.demo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import br.concy.demo.health.PassiveDataRepository.Companion.PREFERENCES_FILE_NAME
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApp: Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_FILE_NAME)
const val TAG = "Demo Sensor App"