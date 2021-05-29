package com.example.background.workers
import android.content.Context
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.background.KEY_IMAGE_URI
import timber.log.Timber
import java.util.*

class SaveImageToFileWorker(ctx: Context, params: WorkerParameters):Worker(ctx, params){
    private  val title = "Blured Image";
    @RequiresApi(Build.VERSION_CODES.N)
    private val  dateFormatter = SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss z",  Locale.getDefault())

    @RequiresApi(Build.VERSION_CODES.N)
    override fun doWork(): Result {
        makeStatusNotification("Saving image", applicationContext)
        sleep()

        val resolver = applicationContext.contentResolver
        return try {
            val resourceUri = inputData.getString(KEY_IMAGE_URI)
            Timber.e(resourceUri)
            val bitmap = BitmapFactory.decodeStream(resolver.openInputStream(Uri.parse(resourceUri)))
            val imageUrl = MediaStore.Images.Media.insertImage(resolver, bitmap, title, dateFormatter.format(Date()))

            if (!imageUrl.isNullOrEmpty()) {
                Timber.e(imageUrl)
                val output = workDataOf(KEY_IMAGE_URI to imageUrl)
                Result.success(output)
            } else {
                Timber.e("Writing to MediaStore failed")
                Result.failure()
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            Result.failure()
        }
    }
}