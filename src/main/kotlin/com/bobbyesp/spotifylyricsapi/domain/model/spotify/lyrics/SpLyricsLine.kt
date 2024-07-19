package com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class SpLyricsLine(
    val endTimeMs: String,
    val startTimeMs: String,
    val syllables: List<String>,
    val words: String
)