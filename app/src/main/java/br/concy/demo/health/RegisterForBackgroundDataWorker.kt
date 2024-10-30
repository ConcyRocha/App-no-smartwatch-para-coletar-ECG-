package br.concy.demo.health

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import br.concy.demo.TAG
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltWorker
class RegisterForBackgroundDataWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
) : Worker(appContext, workerParams) {

    @Inject
    lateinit var healthServicesManager: HealthServicesManager

    override fun doWork(): Result {
        Log.i(TAG, "Worker running")
        runBlocking {

        }
        return Result.success()
    }
}