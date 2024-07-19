package com.bobbyesp.spotifylyricsapi.util.spotify

/**
 * Formats a duration from milliseconds to a string representation in the format of "MM:SS.CC".
 * This function is designed to convert the duration of a line in a song from milliseconds to a more readable format,
 * where MM represents minutes, SS represents seconds, and CC represents centiseconds.
 * It's particularly useful for displaying synchronized lyrics timestamps. ex. '[00:2.43] I don't know myself'.
 *
 * @receiver A [Long] value representing the duration in milliseconds.
 * @return A [String] that represents the formatted duration.
 */
fun Long.formatLineTimestamp(): String {
    val thSecs = this / 1000 // Total seconds
    val minutes = thSecs / 60 // Minutes part of the duration
    val seconds = thSecs % 60 // Seconds part of the duration
    val centiseconds = (this % 1000) / 10 // Centiseconds part of the duration
    return String.format("%02d:%02d.%02d", minutes, seconds, centiseconds) // Formatting to "MM:SS.CC"
}