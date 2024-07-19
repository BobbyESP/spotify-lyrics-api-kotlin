package com.bobbyesp.spotifylyricsapi.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Token(val accessToken: String, val accessTokenExpirationTimestampMs: Long) {
    val isExpired: Boolean
        get() = System.currentTimeMillis() > accessTokenExpirationTimestampMs
}
