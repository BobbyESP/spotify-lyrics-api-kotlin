package com.bobbyesp.spotifylyricsapi

import com.bobbyesp.spotifylyricsapi.data.remote.Spotify
import com.bobbyesp.spotifylyricsapi.plugins.configureHTTP
import com.bobbyesp.spotifylyricsapi.plugins.configureRouting
import io.ktor.server.application.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //get the sp_dc from the system environment variables
    val sp_dc = System.getenv("sp_dc") ?: throw IllegalArgumentException("SP_DC environment variable not found")
    val spotify = Spotify(sp_dc)

    launch(Dispatchers.IO) {
        spotify.getToken()
    }

    configureHTTP()
    configureRouting(spotify)
}
