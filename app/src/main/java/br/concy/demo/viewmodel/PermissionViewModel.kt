package br.concy.demo.viewmodel

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PermissionViewModel : ViewModel() {
    private val _hasPermission = MutableLiveData<Boolean>()
    val hasPermission: LiveData<Boolean> = _hasPermission

    @RequiresApi(Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
    fun checkPermission(activity: ComponentActivity) {
        val permissionLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            val isGranted = permissions.filter { it.value }.isNotEmpty()
            updatePermissionStatus(isGranted)
        }

        permissionLauncher.launch(
            arrayOf(
                android.Manifest.permission.BODY_SENSORS,
                android.Manifest.permission.FOREGROUND_SERVICE_HEALTH
            )
        )
    }

    private fun updatePermissionStatus(isGranted: Boolean) {
        _hasPermission.value = isGranted
    }
}
