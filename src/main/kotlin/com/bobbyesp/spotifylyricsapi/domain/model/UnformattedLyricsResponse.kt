package com.bobbyesp.spotifylyricsapi.domain.model

import com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics.SpLyricsLine
import kotlinx.serialization.Serializable

@Serializable
data class UnformattedLyricsResponse(
    val error: Boolean,
    val syncType: String,
    val lines: List<SpLyricsLine>
)