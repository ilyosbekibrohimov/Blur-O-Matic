package com.example.background.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.*
import com.example.background.IMAGE_MANIPULATION_WORK_NAME
import com.example.background.KEY_IMAGE_URI
import com.example.background.TAG_OUTPUT
import com.example.background.workers.BlurWorker
import com.example.background.workers.CleanupWorker
import com.example.background.workers.SaveImageToFileWorker


class
BlurViewModel(application: Application) : AndroidViewModel(application) {

    internal val  outputWorkInfos:LiveData<List<WorkInfo>>
    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    private val workManager = WorkManager.getInstance(application)



    init {
        outputWorkInfos = workManager.getWorkInfosByTagLiveData(TAG_OUTPUT)
    }


    internal fun applyBlur(blurLevel: Int) {
        val constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .build()

        var continuation = workManager.beginUniqueWork(IMAGE_MANIPULATION_WORK_NAME, ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from(CleanupWorker::class.java))
        val save = OneTimeWorkRequest.Builder(SaveImageToFileWorker::class.java).setConstraints(constraints)
            .addTag(TAG_OUTPUT)
            .build()
        for (i in 0 until  blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()
            if(i == 0){ blurBuilder.setInputData(createInputDataForUri()) }
            continuation = continuation.then(blurBuilder.build())
        }
        continuation = continuation.then(save)
        continuation.enqueue()

    }


    internal  fun  cancelWork(){
        workManager.cancelUniqueWork(IMAGE_MANIPULATION_WORK_NAME)
    }
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let { builder.putString(KEY_IMAGE_URI, imageUri.toString()) }
        return builder.build()
    }


    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}
