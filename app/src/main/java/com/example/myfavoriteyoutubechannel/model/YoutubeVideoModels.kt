package com.example.myfavoriteyoutubechannel.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class YoutubeVideo(var id: String? = "",var name: String? = "", var link: String? = "", var rank: Int = 0, var reason: String? = "") {
    override fun toString(): String {
        return "($rank)\t $name \nLink: $link\nReason: $reason"
    }


}