package com.bobbyesp.spotifylyricsapi.plugins

import com.bobbyesp.spotifylyricsapi.data.remote.Spotify
import com.bobbyesp.spotifylyricsapi.domain.Lyrics
import com.bobbyesp.spotifylyricsapi.domain.ServerMessage
import com.bobbyesp.spotifylyricsapi.util.handleLyricsResponse
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(spotify: Spotify) {
    install(Resources)
    install(ContentNegotiation) { // This adds the ContentNegotiation feature to the API (Serialization)
        json()
    }
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                status = HttpStatusCode.InternalServerError,
                message = ServerMessage(
                    error = true,
                    message = "An unknown occurred: ${cause.localizedMessage}"
                )
            )
        }
    }
    routing {
        get("/") {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerMessage(
                    error = false,
                    message = "This is the root of the API, no content here. Try other endpoints explained in the documentation."
                )
            )
        }

        get<Lyrics> {
            val spUrl = call.queryParameters["url"]

            spUrl?.let {
                handleLyricsResponse(call, spotify, spUrl)
                return@get
            }
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = ServerMessage(
                    error = false,
                    message = "You must provide a Spotify URL to get the lyrics. " +
                            "Try '/lyrics/<a-spotify-link>' instead or other mentioned methods in the docs."
                )
            )
        }

        get<Lyrics.Url> { lyrics ->
            handleLyricsResponse(call, spotify, lyrics.spotifyUrl)
        }
    }
}