package com.armboldmind.grandmarket.shared.globalextensions

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.armboldmind.grandmarket.shared.utils.CountingRequestBody
import com.armboldmind.grandmarket.shared.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun Uri.getFileName(context: Context): String {
    var result: String? = null
    if (scheme.equals("content")) {
        val cursor = context.contentResolver.query(this, null, null, null, null)
        try {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
    }
    if (result == null) {
        result = path
        val cut = result!!.lastIndexOf(File.separator)
        if (cut != -1) {
            result = result.substring(cut + 1)
        }
    }
    return result
}

fun Uri.createFormData(context: Context, name: String, onProgress: (percent: Int) -> Unit): MultipartBody.Part {
    val requestBody = CountingRequestBody(FileUtils.getFileFromUri(context, this, this.getFileName(context))
                                              .asRequestBody((context.contentResolver.getType(this) ?: "").toMediaTypeOrNull()), onProgress)
    return MultipartBody.Part.createFormData(name, this.getFileName(context), requestBody)
}