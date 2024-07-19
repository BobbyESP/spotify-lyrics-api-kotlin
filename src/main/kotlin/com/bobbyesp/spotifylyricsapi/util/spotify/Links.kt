package com.bobbyesp.spotifylyricsapi.util.spotify

private val SpotifyLinksRegex = Regex("(https:\\/\\/)?open\\.spotify\\.com(\\/intl-[a-z]{2})?\\/track\\/([A-Za-z0-9]+)")

fun String.getSpotifyTrackId(): String? {
    val match = SpotifyLinksRegex.find(this)
    return match?.groups?.get(3)?.value
}