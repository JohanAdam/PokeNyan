package com.nyan.data.util

import android.app.Activity
import androidx.test.core.app.ActivityScenario
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

fun MockWebServer.enqueueResponse(fileName: String, code: Int) {
    val inputStream = javaClass.classLoader?.getResourceAsStream("api-response/$fileName")

    val source = inputStream?.let { inputStream.source().buffer() }
    source?.let {
        enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(source.readString(StandardCharsets.UTF_8))
        )
    }
}

fun MockWebServer.registerApiRequest(httpRequest: HttpRequest, responseBody: String) {
    val mockServerDispatcher = dispatcher as MockServerDispatcher

    mockServerDispatcher.registerApiRequest(httpRequest, responseBody)
}

@Suppress("UNCHECKED_CAST")
fun ActivityScenario<out Activity>.withState(state: Any) {
    onActivity { activity ->
        val screen = activity as Screen<Any, Any>
        screen.handleState(state)
    }
}