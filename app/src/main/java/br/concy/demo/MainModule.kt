package br.concy.demo

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import br.concy.demo.health.HealthServicesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Singleton
    @Provides
    fun provideDataStore(
        application: Application
    ): DataStore<Preferences> = application.applicationContext.dataStore

    @Singleton
    @Provides
    fun provideHealthServicesClient(@ApplicationContext context: Context): HealthServicesClient =
        HealthServices.getClient(context)

    @Singleton
    @Provides
    fun provideHealthServiceManager(healthServicesClient: HealthServicesClient) =
        HealthServicesManager(healthServicesClient)
}