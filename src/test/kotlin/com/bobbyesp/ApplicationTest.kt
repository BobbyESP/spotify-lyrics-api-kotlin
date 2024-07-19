package com.bobbyesp

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        client.get("/").apply {
            assertEquals(HttpStatusCode.BadRequest, status)
            assertEquals(
                """
                {"error":false,"message":"This is the root of the API, no content here. Try other endpoints explained in the documentation."}
            """.trimIndent(), bodyAsText()
            )
        }
    }
}
