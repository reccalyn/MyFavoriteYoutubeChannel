package com.example.myfavoriteyoutubechannel.handler

import com.example.myfavoriteyoutubechannel.model.YoutubeVideo
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class YoutubeVideosHandler {
    var database: FirebaseDatabase
    var youtubeVideosReference: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubeVideosReference = database.getReference("youtube_videos")
    }

    fun create(youtubeVideo: YoutubeVideo): Boolean{
        val id = youtubeVideosReference.push().key
        youtubeVideo.id = id

        youtubeVideosReference.child(id!!).setValue(youtubeVideo)


        return true
    }

    fun update(youtubeVideo: YoutubeVideo):Boolean {
        youtubeVideosReference.child(youtubeVideo.id!!).setValue(youtubeVideo)
        return true
    }

    fun delete(youtubeVideo: YoutubeVideo): Boolean {
        youtubeVideosReference.child(youtubeVideo.id!!).removeValue()
        return true
    }
}