package com.bobbyesp.spotifylyricsapi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SyncedLyrics(
    val syncType: String,
    val lines: List<SongLine>
)
