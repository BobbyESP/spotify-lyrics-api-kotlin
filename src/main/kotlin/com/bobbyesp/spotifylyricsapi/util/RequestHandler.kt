package com.bobbyesp.spotifylyricsapi.util

import com.bobbyesp.spotifylyricsapi.data.remote.Spotify
import com.bobbyesp.spotifylyricsapi.domain.ServerMessage
import com.bobbyesp.spotifylyricsapi.domain.model.LrcLyricsResponse
import com.bobbyesp.spotifylyricsapi.domain.model.UnformattedLyricsResponse
import com.bobbyesp.spotifylyricsapi.util.spotify.getSpotifyTrackId
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

/**
 * Handles the response for lyrics requests based on the type specified in the query parameters.
 * This function determines whether the request is for synced lyrics ('lrc') or unformatted lyrics,
 * fetches the lyrics accordingly from Spotify, and sends the appropriate response back to the client.
 * If the type is not recognized, it responds with a BadRequest error.
 *
 * @param call The [ApplicationCall] instance, representing the context of the single HTTP call.
 * @param spotify The [Spotify] service instance used to fetch lyrics from Spotify.
 * @param spUrl The Spotify URL provided in the request, from which the track ID is extracted.
 * @throws IllegalArgumentException If the provided Spotify URL does not contain a valid track ID.
 */
suspend fun handleLyricsResponse(call: ApplicationCall, spotify: Spotify, spUrl: String) {
    val type = call.request.queryParameters["type"] ?: "lrc"
    val trackId = spUrl.getSpotifyTrackId() ?: throw IllegalArgumentException("Invalid provided Spotify URL")

    when (type) {
        "lrc" -> {
            val retrievedLyrics = spotify.getSyncedLyrics(trackId)
            call.respond(
                status = HttpStatusCode.OK,
                message = LrcLyricsResponse(
                    error = false,
                    syncType = retrievedLyrics.syncType,
                    lines = retrievedLyrics.lines
                )
            )
        }

        "unformatted" -> {
            val retrievedLyrics = spotify.getUnformattedLyrics(trackId)
            call.respond(
                status = HttpStatusCode.OK,
                message = UnformattedLyricsResponse(
                    error = false,
                    syncType = "LINE_SYNCED",
                    lines = retrievedLyrics
                )
            )
        }

        else -> call.respond(
            status = HttpStatusCode.BadRequest,
            message = ServerMessage(
                error = true,
                message = "Invalid type. Supported types are 'lrc' and 'unformatted'."
            )
        )
    }
}