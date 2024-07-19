package com.bobbyesp.spotifylyricsapi.data.remote

import com.bobbyesp.spotifylyricsapi.domain.model.SyncedLyrics
import com.bobbyesp.spotifylyricsapi.domain.model.Token
import com.bobbyesp.spotifylyricsapi.domain.model.spotify.SpCredentials
import com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics.SpLyricsLine
import com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics.SpLyricsResponse
import com.bobbyesp.spotifylyricsapi.ext.toSyncedLyrics
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.jetty.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Provides functionality to interact with Spotify's Web API for fetching lyrics.
 * This class encapsulates methods for authenticating with Spotify and retrieving both formatted and unformatted lyrics for tracks.
 * It uses the `spDc` cookie value for authentication and manages token retrieval and caching.
 *
 * @property spDc The Spotify Web API cookie value used for authentication.
 * It has to be placed in the environmental variables as `sp_dc`.
 */
class Spotify(private val spDc: String) {
    private val tokenUrl = "https://open.spotify.com/get_access_token?reason=transport&productType=web_player"
    private val lyricsUrl = "https://spclient.wg.spotify.com/color-lyrics/v2/track/"
    private val cacheFile = File(System.getProperty("java.io.tmpdir"), "spotify_token.json")

    private val client = HttpClient(Jetty) {
        install(ContentNegotiation) {
            json()
        }
    }

    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    /**
     * Fetches a new Spotify access token using the `spDc` cookie value.
     * This method sends a GET request to the Spotify token URL, including necessary headers.
     * If the `spDc` is empty, it throws an Exception advising to set SP_DC as an environmental variable.
     * After receiving the response, it checks if the access token is present in the response.
     * If the access token is missing, it throws an Exception indicating the SP_DC might be invalid.
     * Finally, it writes the response body, which contains the token, to a cache file for later use.
     *
     * @throws Exception if `spDc` is empty or if the fetched token is invalid.
     */
    suspend fun getToken() {
        if (spDc.isEmpty()) throw Exception("Please set SP_DC as an environmental variable.")

        val response: HttpResponse = client.get(tokenUrl) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36"
                )
                append("App-platform", "WebPlayer")
                append("Content-Type", "text/html; charset=utf-8")
                append("Cookie", "sp_dc=$spDc")
            }
        }

        println(response.bodyAsText())
        val tokenJson = response.body<SpCredentials>()
        if (tokenJson.accessToken.isEmpty()) throw Exception("The SP_DC set seems to be invalid, please correct it!")

        val token = Token(tokenJson.accessToken, tokenJson.accessTokenExpirationTimestampMs)
        cacheFile.writeText(json.encodeToString(Token.serializer(), token))
    }

    /**
     * Ensure that the token is valid and not expired.
     * If the token is expired, it will fetch a new one.
     * @return true if the token is valid, false if it was expired and a new one was fetched.
     */
    private suspend fun ensureToken(): Boolean {
        when (cacheFile.exists()) {
            true -> {
                val token = json.decodeFromString(Token.serializer(), cacheFile.readText())
                return if (token.isExpired) {
                    getToken()
                    false
                } else {
                    true
                }
            }

            false -> {
                getToken()
                return false
            }
        }
    }

    /**
     * Performs a common request to fetch lyrics for a given Spotify track ID.
     * This function first ensures that a valid Spotify access token is available by calling [ensureToken].
     * It then constructs a request to the Spotify lyrics endpoint, including the necessary headers for authorization and content type.
     * The response is parsed into a [SpLyricsResponse] object.
     * If the response body is blank, indicating that no lyrics were found for the track, an exception is thrown.
     *
     * @param trackId The Spotify track ID for which to fetch lyrics.
     * @return A [SpLyricsResponse] object containing the lyrics and metadata for the track.
     * @throws Exception if the lyrics cannot be found for the given track ID.
     */
    private suspend fun commonLyricsRequest(trackId: String): SpLyricsResponse {
        ensureToken()

        val spToken = json.decodeFromString<Token>(cacheFile.readText())
        val token = spToken.accessToken

        val formatedUrl = "$lyricsUrl$trackId?format=json&market=from_token"
        val response: HttpResponse = client.get(formatedUrl) {
            headers {
                append(
                    "User-Agent",
                    "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/101.0.0.0 Safari/537.36"
                )
                append("App-platform", "WebPlayer")
                append("Authorization", "Bearer $token")
            }
        }

        val responseText = response.bodyAsText()

        if (responseText.isBlank()) throw Exception("Lyrics not found for this track.")
        val spLyrics = json.decodeFromString<SpLyricsResponse>(response.bodyAsText())
        return spLyrics
    }

    /**
     * Fetches and returns the synced lyrics for a given Spotify track ID.
     * This function utilizes the [commonLyricsRequest] to send a request to Spotify's lyrics endpoint,
     * ensuring that a valid Spotify access token is available before making the request.
     * The response is then converted into a [SyncedLyrics] object, which contains the lyrics synchronized with the music timing.
     *
     * @param trackId The Spotify track ID for which to fetch synced lyrics.
     * @return A [SyncedLyrics] object containing the lyrics and their synchronization data.
     * @throws Exception if the lyrics cannot be found for the given track ID or if any network-related issues occur.
     */
    suspend fun getSyncedLyrics(trackId: String): SyncedLyrics {
        val spLyrics = commonLyricsRequest(trackId)
        return spLyrics.toSyncedLyrics()
    }

    /**
     * Fetches and returns the unformatted lyrics for a given Spotify track ID.
     * Similar to [getSyncedLyrics], this function calls [commonLyricsRequest] to retrieve lyrics from Spotify's endpoint.
     * Instead of returning a synchronized lyrics object, it returns a list of [SpLyricsLine], representing each
     * line of the lyrics directly from the Spotify endpoint's response.
     *
     * @param trackId The Spotify track ID for which to fetch unformatted lyrics.
     * @return A List of [SpLyricsLine] representing the unformatted lyrics of the track.
     * @throws Exception if the lyrics cannot be found for the given track ID or if any network-related issues occur.
     */
    suspend fun getUnformattedLyrics(trackId: String): List<SpLyricsLine> {
        val spLyrics = commonLyricsRequest(trackId)
        return spLyrics.lyrics.lines
    }
}