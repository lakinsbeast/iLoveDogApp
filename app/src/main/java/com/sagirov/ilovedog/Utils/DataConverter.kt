package com.sagirov.ilovedog

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.moshi.Moshi
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

class MapStringToStringConverter {

    private val mapAdapter = Moshi.Builder().build().adapter<Map<String, String>>(Map::class.java)
    val gson = Gson()

    @TypeConverter
    @JvmName("toJson1")
    fun toJson(map: Map<String, String>): String = gson.toJson(map)

    @TypeConverter
    fun fromJson(str: String): Map<String, String> =
        gson.fromJson(str, object : TypeToken<Map<String, String>>() {}.type)


//    @TypeConverter
//    fun fromJson(str: String?): Map<String,String>? = str?.let { mapAdapter.fromJson(it) }
//    @TypeConverter
//    fun toJson(map: Map<String,String>?): String? = map?.let { mapAdapter.toJson(it) }


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