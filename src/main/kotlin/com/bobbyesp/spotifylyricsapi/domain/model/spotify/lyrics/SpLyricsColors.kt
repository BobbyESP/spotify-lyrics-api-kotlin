package com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class SpLyricsColors(
    val background: Int,
    val highlightText: Int,
    val text: Int
)