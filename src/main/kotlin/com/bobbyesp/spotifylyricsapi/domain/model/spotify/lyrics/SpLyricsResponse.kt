package com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class SpLyricsResponse(
    val colors: SpLyricsColors,
    val hasVocalRemoval: Boolean,
    val lyrics: SpLyrics
)