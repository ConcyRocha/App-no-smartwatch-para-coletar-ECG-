package br.concy.demo.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Intent
import androidx.core.content.ContextCompat.startForegroundService
import androidx.lifecycle.ViewModel
import br.concy.demo.health.HeartRateService
import br.concy.demo.health.PassiveDataRepository
import br.concy.demo.view.HomeUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    repository: PassiveDataRepository
): ViewModel() {

    @SuppressLint("StaticFieldLeak")
    private val ctx = application.applicationContext
    private val _uiState = MutableStateFlow<HomeUIState>(HomeUIState.Default)
    val uiState = _uiState.asStateFlow()
    val latestHeartRate = repository.latestHeartRate

    fun startCollect() {
        val intent = Intent(ctx, HeartRateService::class.java)
        startForegroundService(ctx, intent)
        _uiState.value = HomeUIState.Collecting
    }

    fun stopCollect() {
        val intent = Intent(ctx, HeartRateService::class.java)
        intent.action = "STOP_SERVICE"
        startForegroundService(ctx, intent)
        _uiState.value = HomeUIState.Default
    }
}