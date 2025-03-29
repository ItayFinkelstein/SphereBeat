package fullstack.application.spherebeat.dal.networking

class SpotifyResponse {
    val tracks: Tracks? = null

    class Tracks {
        val items: List<Item>? = null
    }

    class Item {
        val album: Album? = null
        val artists: List<Artist>? = null
        val id: String? = null
        val name: String? = null
        val duration_ms: Int? = null
    }

    class Album {
        val images: List<Image>? = null
        val release_date: String? = null
    }

    class Image {
        val url: String? = null
    }

    class Artist {
        val name: String? = null
    }
}
