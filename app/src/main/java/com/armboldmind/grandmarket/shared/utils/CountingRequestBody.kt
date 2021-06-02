package com.armboldmind.grandmarket.shared.utils

import androidx.annotation.NonNull
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.*
import java.io.IOException

class CountingRequestBody(private val delegate: RequestBody, private val onProgress: (progress: Int) -> Unit) : RequestBody() {
    var mProgress = -1
    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    override fun contentLength(): Long {
        try {
            return delegate.contentLength()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return -1
    }

    @Throws(IOException::class)
    override fun writeTo(@NonNull sink: BufferedSink) {
        val countingSink: CountingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()
        delegate.writeTo(bufferedSink)
        bufferedSink.flush()
    }

    internal inner class CountingSink(delegate: Sink?) : ForwardingSink(delegate!!) {
        private var bytesWritten: Long = 0
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            bytesWritten += byteCount
            if (mProgress > 0) {
                val percent = bytesWritten.times(100)
                    .div(contentLength())
                    .toInt()
                onProgress.invoke(percent)
            }
            if (bytesWritten.times(100)
                    .div(contentLength())
                    .toInt() == 100 && mProgress <= 0
            ) {
                mProgress++
            }
        }

    }

}