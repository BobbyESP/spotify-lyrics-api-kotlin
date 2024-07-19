package com.bobbyesp.spotifylyricsapi.domain

import kotlinx.serialization.Serializable

@Serializable
data class ServerMessage(val error: Boolean, val message: String)
