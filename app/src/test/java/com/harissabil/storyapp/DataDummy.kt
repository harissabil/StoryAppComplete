package com.harissabil.storyapp

import com.harissabil.storyapp.data.remote.response.ListStoryItem

object DataDummy {

    fun generateDummyStoryResponse(): List<ListStoryItem> {
        val items: MutableList<ListStoryItem> = arrayListOf()
        for (i in 0..100) {
            val story = ListStoryItem(
                i.toString(),
                "name $i",
                "description $i",
                "url $i",
                "createAt $i",
                i.toDouble(),
                i.toDouble(),
            )
            items.add(story)
        }
        return items
    }
}