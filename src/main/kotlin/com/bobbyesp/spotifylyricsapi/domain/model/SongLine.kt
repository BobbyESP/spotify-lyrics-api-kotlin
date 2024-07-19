package com.bobbyesp.spotifylyricsapi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SongLine(val timestamp: String, val text: String)
