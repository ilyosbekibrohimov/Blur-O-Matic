package com.example.background.workers

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import timber.log.Timber
import java.lang.IllegalArgumentException


class BlueWorker(context: Context, param: WorkerParameters) : Worker(context, param) {
    override fun doWork(): Result {

        val uri  = inputData.getString(KEY_IMAGE_URI)

        makeStatusNotification("Blurring image", applicationContext)


        return try {
            if(TextUtils.isEmpty(uri)){
                Timber.e("Invalid inout uri")
                throw  IllegalArgumentException("Invalid URI")
            }

            val resolver  = applicationContext.contentResolver

            val picture = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(uri)))

            val outputUri = writeBitmapToFile(applicationContext,  blurBitmap(picture, applicationContext))
            val outputData = workDataOf(KEY_IMAGE_URI to outputUri.toString())

            Result.success(outputData)
        }

        catch (throwable:Throwable){
            Timber.e(throwable)
            Result.failure()
        }






    }
}