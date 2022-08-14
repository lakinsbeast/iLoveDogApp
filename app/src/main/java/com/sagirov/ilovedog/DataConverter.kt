package com.sagirov.ilovedog

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type
import java.util.*


class DataConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun fromTimestamp(date: Long): Date {
        return Date(date)
    }
}

class MapConverter {

    private val mapAdapter = Moshi.Builder().build().adapter<Map<String, String>>(Map::class.java)

    @TypeConverter
    fun fromJson(str: String?): Map<String,String>? = str?.let { mapAdapter.fromJson(it) }
    @TypeConverter
    fun toJson(map: Map<String,String>?): String? = map?.let { mapAdapter.toJson(it) }


//    @TypeConverter
//    fun fromString(value: String?): Map<String?, String?>? {
//        val mapType: Type = object : TypeToken<Map<String?, String?>?>() {}.type
//        return Gson().fromJson(value, mapType)
//    }
//
//    @TypeConverter
//    fun fromStringMap(map: Map<String?, String?>?): String? {
//        val gson = Gson()
//        return gson.toJson(map)
//    }
}