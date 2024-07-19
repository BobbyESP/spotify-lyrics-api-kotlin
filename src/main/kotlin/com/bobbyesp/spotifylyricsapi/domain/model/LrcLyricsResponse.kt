package com.bobbyesp.spotifylyricsapi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class LrcLyricsResponse(
    val error: Boolean,
    val syncType: String,
    val lines: List<SongLine>
)
