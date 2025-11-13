package com.example.nutripekes_android

import androidx.room.TypeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class ContentConverter {

    private val moshi = Moshi.Builder().build()

    private val listListStringType = Types.newParameterizedType(
        List::class.java,
        Types.newParameterizedType(List::class.java, String::class.java)
    )
    private val adapter = moshi.adapter<List<List<String>>>(listListStringType)

    @TypeConverter
    fun fromContentList(content: List<List<String>>): String {
        return adapter.toJson(content)
    }

    @TypeConverter
    fun toContentList(json: String): List<List<String>> {
        return adapter.fromJson(json) ?: emptyList()
    }
}