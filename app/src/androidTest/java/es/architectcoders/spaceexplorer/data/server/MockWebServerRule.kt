package es.architectcoders.spaceexplorer.data.server

import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockWebServerRule : TestWatcher() {

    lateinit var server: MockWebServer

    override fun starting(description: Description) {
        server = MockWebServer()
        server.start(8080)
        server.dispatcher = object : Dispatcher(){
            override fun dispatch(request: RecordedRequest): MockResponse {
                val path = request.path
                if (path != null) {
                    return when {
                        path.contains("planetary/apod") -> MockResponse().fromJson("item_apod.json")
                        path.contains("mars-photos/api/v1/rovers/curiosity/photos") -> MockResponse().fromJson("items_rovers.json")
                        else -> MockResponse().setResponseCode(404).setBody("Not Found")
                    }
                }
                return MockResponse().setResponseCode(400).setBody("Bad Request")
            }

        }
    }

    override fun finished(description: Description) {
        server.shutdown()
    }
}