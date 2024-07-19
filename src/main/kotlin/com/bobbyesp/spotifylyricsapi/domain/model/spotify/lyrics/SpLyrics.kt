package com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics

import kotlinx.serialization.Serializable

@Serializable
data class SpLyrics(
    val alternatives: List<String>,
    val capStatus: String,
    val isDenseTypeface: Boolean,
    val isRtlLanguage: Boolean,
    val isSnippet: Boolean,
    val language: String,
    val lines: List<SpLyricsLine>,
    val provider: String,
    val providerDisplayName: String,
    val providerLyricsId: String,
    val showUpsell: Boolean,
    val syncLyricsUri: String,
    val syncType: String
)