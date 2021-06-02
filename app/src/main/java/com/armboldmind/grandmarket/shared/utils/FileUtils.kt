package com.armboldmind.grandmarket.shared.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.*

object FileUtils {

    private val EOF = -1

    @Throws(Exception::class)
    fun getFileFromUri(context: Context, uri: Uri, name: String): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val splitName = splitFileName(name)
        var tempFile = File.createTempFile(splitName[0], splitName[1])
        tempFile = rename(tempFile, name)
        tempFile.deleteOnExit()
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(tempFile)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        if (inputStream != null) {
            copy(inputStream, out!!)
            inputStream.close()
        }

        out?.close()
        return tempFile
    }

    @JvmStatic
    private fun rename(file: File, newName: String): File {
        val newFile = File(file.parent, newName)
        if (newFile != file) {
            if (newFile.exists() && newFile.delete()) {
                Log.d("FileUtil", "Delete old $newName file")
            }
            if (file.renameTo(newFile)) {
                Log.d("FileUtil", "Rename file to $newName")
            }
        }
        return newFile
    }

    @JvmStatic
    private fun splitFileName(fileName: String): Array<String> {
        var name = fileName
        var extension = ""
        val i = fileName.lastIndexOf(".")
        if (i != -1) {
            name = fileName.substring(0, i)
            extension = fileName.substring(i)
        }
        return arrayOf(name, extension)
    }

    @JvmStatic
    @Throws(IOException::class)
    private fun copy(input: InputStream, output: FileOutputStream): Long {
        var count: Long = 0
        var n: Int
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        n = input.read(buffer)
        while (EOF != n) {
            output.write(buffer, 0, n)
            count += n.toLong()
            n = input.read(buffer)
        }
        return count
    }

}