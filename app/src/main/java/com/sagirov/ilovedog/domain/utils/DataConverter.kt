package com.sagirov.ilovedog.domain.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    val gson = Gson()

    @TypeConverter
    @JvmName("toJson1")
    fun toJson(map: Map<String, String>): String = gson.toJson(map)

    @TypeConverter
    fun fromJson(str: String): Map<String, String> =
        gson.fromJson(str, object : TypeToken<Map<String, String>>() {}.type)
}