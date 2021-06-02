package com.armboldmind.grandmarket.shared.utils

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Matrix
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.CallSuper


class GetContentBitmap(private val context: Context) : ActivityResultContract<String, Bitmap?>() {
    @CallSuper
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(Intent.ACTION_GET_CONTENT).addCategory(Intent.CATEGORY_OPENABLE)
            .setType(input)
    }

    override fun getSynchronousResult(
        context: Context,
        input: String,
    ): SynchronousResult<Bitmap?>? {
        return null
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        val bitmap: Bitmap?
        try {
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, intent?.data)
            } else {
                intent?.data?.let {
                    val source = ImageDecoder.createSource(context.contentResolver, it)
                    ImageDecoder.decodeBitmap(source)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return getResizedBitmap(bitmap)
    }

    private fun getResizedBitmap(bm: Bitmap?): Bitmap? {
        val width = bm?.width ?: 1
        val height = bm?.height ?: 1
        val scaleWidth = 100f / width
        val scaleHeight = 100f / height
        val matrix = Matrix()
        matrix.postScale(scaleWidth, scaleHeight)
        return bm?.let { Bitmap.createBitmap(it, 0, 0, width, height, matrix, false) }
    }
}