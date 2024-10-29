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

//        server.dispatcher = object : Dispatcher(){
//            override fun dispatch(request: RecordedRequest): MockResponse {
//                val path = request.path
//                    return when{
//                        path != null && path.contains("/mars-photos/api/v1/rovers")
//                        -> MockResponse().fromJson("items_rovers.json")
//                        else -> {MockResponse().setResponseCode(404)}
//                    }
//            }
//
//        }
    }

    override fun finished(description: Description) {
        server.shutdown()
    }
}