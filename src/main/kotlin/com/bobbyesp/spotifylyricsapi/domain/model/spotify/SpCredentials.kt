package com.bobbyesp.spotifylyricsapi.domain.model.spotify

import kotlinx.serialization.Serializable

@Serializable
data class SpCredentials(
    val clientId: String,
    val accessToken: String,
    val accessTokenExpirationTimestampMs: Long,
    val isAnonymous: Boolean
)
