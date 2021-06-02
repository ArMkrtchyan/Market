package com.armboldmind.grandmarket.shared.globalextensions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import com.armboldmind.grandmarket.shared.utils.CountingRequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun Bitmap.toByteArray(): ByteArray {
    val byteArrayOutputStream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    return byteArrayOutputStream.toByteArray()
}

fun Bitmap.getUri(context: Context): Uri {
    val bytes = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path = MediaStore.Images.Media.insertImage(context.contentResolver, this, "Title", null)
    return Uri.parse(path)
}

fun Bitmap.createFile(context: Context): File {
    val file = File(context.cacheDir,
                    this.hashCode()
                        .toString() + "")
    file.createNewFile()
    val bos = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
    val bitmapData = bos.toByteArray()
    val fos = FileOutputStream(file)
    fos.write(bitmapData)
    fos.flush()
    fos.close()
    return file
}

fun Bitmap.createFormData(context: Context, name: String, onProgress: (percent: Int) -> Unit): MultipartBody.Part {
    return MultipartBody.Part.createFormData(name,
                                             "UserPhoto.jpeg",
                                             CountingRequestBody(this.createFile(context)
                                                                     .asRequestBody("image/jpeg".toMediaTypeOrNull()), onProgress))
}