package com.example.myfavoriteyoutubechannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.myfavoriteyoutubechannel.handler.YoutubeVideosHandler
import com.example.myfavoriteyoutubechannel.model.YoutubeVideo
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

lateinit var youtubeVideosHandler: YoutubeVideosHandler

class MainActivity : AppCompatActivity() {
    lateinit var nameEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var button: Button
    lateinit var youtubeVideos: ArrayList<YoutubeVideo>
    lateinit var youtubeVideosArrayAdapter: ArrayAdapter<YoutubeVideo>
    lateinit var youtubeVideosListView: ListView
    lateinit var youtubeVideoGettingEdited: YoutubeVideo


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        nameEditText = findViewById(R.id.videoTitleEditText)
        linkEditText = findViewById(R.id.videoLinkEditText)
        rankEditText = findViewById(R.id.videoRankEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        button = findViewById(R.id.button)
        youtubeVideosListView = findViewById(R.id.listView)
        youtubeVideosHandler = YoutubeVideosHandler()
        youtubeVideos = ArrayList()

        button.setOnClickListener{
            val name = nameEditText.text.toString()
            val link = linkEditText.text.toString()
            var rank: Int
            if (rankEditText.text.toString() == "" ) {
                rank = 0
            } else {
                rank = rankEditText.text.toString().toInt()
            }

            val reason = reasonEditText.text.toString()

            if(button.text.toString() == "Add"){
                val restaurant = YoutubeVideo(name = name , link = link ,rank =  rank, reason = reason  )
                if(youtubeVideosHandler.create(restaurant)){
                    Toast.makeText(this, "Youtube video added.", Toast.LENGTH_SHORT).show()
                }
                clear()
            } else if(button.text.toString() == "Update") {
                val restaurant = YoutubeVideo( id = youtubeVideoGettingEdited.id, name = name , link = link ,rank =  rank, reason = reason )
                if(youtubeVideosHandler.update(restaurant)){
                    Toast.makeText(this, "Youtube video updated.", Toast.LENGTH_SHORT).show()
                }
                clear()

            }

        }

        registerForContextMenu(youtubeVideosListView)
    }
    override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.editRestaurant -> {
                youtubeVideoGettingEdited = youtubeVideos[info.position]
                nameEditText.setText(youtubeVideoGettingEdited.name)
                linkEditText.setText(youtubeVideoGettingEdited.link)
                rankEditText.setText(youtubeVideoGettingEdited.rank.toString())
                reasonEditText.setText(youtubeVideoGettingEdited.reason)
                button.setText("Update")
                return true
            }
            R.id.deleteRestaurant -> {
                if (youtubeVideosHandler.delete(youtubeVideos[info.position])){
                    Toast.makeText(this, "Youtube video updated", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onStart() {
        super.onStart()
        youtubeVideosHandler.youtubeVideosReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                youtubeVideos.clear()
                p0.children.forEach {
                    it -> val video = it.getValue(YoutubeVideo::class.java)
                    youtubeVideos.add(video!!)
                    youtubeVideos.sortWith(object: Comparator<YoutubeVideo>{
                        override fun compare(o1: YoutubeVideo, o2: YoutubeVideo): Int = when {
                            o1.rank > o2.rank -> 1
                            o1.rank == o2.rank -> 0
                            else -> -1
                        }
                    })

                }
                youtubeVideosArrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, youtubeVideos)
                youtubeVideosListView.adapter = youtubeVideosArrayAdapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })

    }

    fun clear(){
        nameEditText.text.clear()
        linkEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        button.setText("Add")
    }
}