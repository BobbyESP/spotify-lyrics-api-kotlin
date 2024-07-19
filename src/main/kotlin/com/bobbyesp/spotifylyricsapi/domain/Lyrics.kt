package com.bobbyesp.spotifylyricsapi.domain

import io.ktor.resources.*
import kotlinx.serialization.Serializable

@Serializable
@Resource("/lyrics")
class Lyrics(val spotifyUrl: String? = null) {
    @Serializable
    @Resource("{spotifyUrl}")
    data class Url(val parent: Lyrics = Lyrics(), val spotifyUrl: String)
}