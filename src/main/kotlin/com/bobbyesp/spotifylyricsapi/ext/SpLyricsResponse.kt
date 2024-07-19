package com.bobbyesp.spotifylyricsapi.ext

import com.bobbyesp.spotifylyricsapi.domain.model.SongLine
import com.bobbyesp.spotifylyricsapi.domain.model.SyncedLyrics
import com.bobbyesp.spotifylyricsapi.domain.model.spotify.lyrics.SpLyricsResponse
import com.bobbyesp.spotifylyricsapi.util.spotify.formatLineTimestamp

/**
 * Converts a [SpLyricsResponse] to a [SyncedLyrics] object.
 *
 * This extension function takes the response from the Spotify API containing the lyrics of a song,
 * and transforms it into a [SyncedLyrics] object that is more suitable for use within the application.
 * The transformation involves mapping each line of the lyrics, represented by [SpLyricsLine],
 * to a [SongLine] object. The [SongLine] object contains the timestamp (formatted as a string)
 * and the words of the lyric line.
 *
 * @receiver The [SpLyricsResponse] object containing the raw lyrics response from Spotify.
 * @return A [SyncedLyrics] object containing the formatted timestamp and lyrics suitable for display or further processing.
 */
fun SpLyricsResponse.toSyncedLyrics(): SyncedLyrics {
    return SyncedLyrics(
        syncType = lyrics.syncType,
        lines = lyrics.lines.map { SongLine(it.startTimeMs.toLong().formatLineTimestamp(), it.words) }
    )
}