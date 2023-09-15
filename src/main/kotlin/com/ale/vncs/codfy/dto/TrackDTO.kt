package com.ale.vncs.codfy.dto

import com.ale.vncs.codfy.utils.Constants
import com.intellij.util.ui.ImageUtil
import org.imgscalr.Scalr
import se.michaelthelin.spotify.model_objects.specification.ArtistSimplified
import se.michaelthelin.spotify.model_objects.specification.Track
import java.awt.Image
import java.awt.image.BufferedImage
import java.net.URI
import javax.imageio.ImageIO
import javax.swing.ImageIcon

open class TrackDTO {
    var songId = ""
    var songName = ""
    var songAlbumName = ""
    var songArtistName = ""
    var songImage = ImageIcon(resizeSongImage(ImageUtil.toBufferedImage(fallbackImage())))
    var durationMs = 0
    var uri = ""
    var albumUri = ""

    constructor(track: Track) {
        this.songId = track.id
        this.songName = track.name
        this.songAlbumName = track.album.name
        this.songArtistName = track.artists.joinToString(", ", transform = ArtistSimplified::getName)
        this.songImage = getImage(track.album.images[0].url)
        this.durationMs = track.durationMs
        this.uri = track.uri
        this.albumUri = track.album.uri
    }

    private fun getImage(imageUrl: String): ImageIcon {
        var image = ImageUtil.toBufferedImage(fallbackImage())

        try {
            image = ImageIO.read(URI.create(imageUrl).toURL())
        } catch (_: Exception) {}

        return ImageIcon(resizeSongImage(image))
    }

    private fun resizeSongImage(image: BufferedImage): BufferedImage {
        return Scalr.resize(image, 100)
    }

    private fun fallbackImage(): Image {
        return Constants.FALLBACK_SONG_IMAGE
    }
}
