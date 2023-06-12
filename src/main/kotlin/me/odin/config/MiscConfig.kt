package me.odin.config

import com.google.gson.GsonBuilder
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.odin.features.m7.TerminalTimes
import java.io.File
import java.io.IOException

class MiscConfig(path: File) {
    private val gson = GsonBuilder().setPrettyPrinting().create()

    private val highlightConfigFile = File(path, "highlight-config.json")
    private val blacklistConfigFile = File(path, "blacklist-config.json")
    private val terminalPBFile = File(path, "terminalPB.json")
    private val hasJoinedFile = File(path, "hasJoined.json")

    var hightlightList: MutableList<String> = mutableListOf()
    var blacklist: MutableList<String> = mutableListOf()
    private inline val terminalPB get() = TerminalTimes.Times.values().map { "${it.fullName}: ${it.time}"}
    var hasJoined: Boolean = false

    init {
        try {
            if (!path.exists()) path.mkdirs()
            highlightConfigFile.createNewFile()
            blacklistConfigFile.createNewFile()
            terminalPBFile.createNewFile()
        } catch (e: Exception) {
            println("Error initializing configs.")
        }
    }


    fun loadConfig() {
        try {
            with(highlightConfigFile.bufferedReader().use { it.readText() }) {
                if (this == "") return
                hightlightList = gson.fromJson(this, object : TypeToken<MutableList<String>>() {}.type)
            }
            with(blacklistConfigFile.bufferedReader().use { it.readText() }) {
                if (this == "") return
                blacklist = gson.fromJson(this, object : TypeToken<MutableList<String>>() {}.type)
            }
            with(terminalPBFile.bufferedReader().use { it.readText() }) {
                if (this == "") return
                val a: MutableList<String> = gson.fromJson(this, object : TypeToken<MutableList<String>>() {}.type)
                a.forEach {
                    println(it)
                    val (name, time) = it.split(": ")
                    TerminalTimes.Times.values().find { a -> a.fullName == name }?.let { b ->
                        b.time = time.toDouble()
                        println("Loaded ${b.fullName} with time ${b.time}")
                    }
                }
            }
            with(hasJoinedFile.bufferedReader().use { it.readText() }) {
                if (this == "") return
                hasJoined = gson.fromJson(this, object : TypeToken<Boolean>() {}.type)
            }
        } catch (e: JsonSyntaxException) {
            println("Error parsing configs.")
            println(e.message)
            e.printStackTrace()
        } catch (e: JsonIOException) {
            println("Error reading configs.")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun saveAllConfigs() {
        GlobalScope.launch {
            try {
                highlightConfigFile.bufferedWriter().use {
                    it.write(gson.toJson(hightlightList))
                }
                blacklistConfigFile.bufferedWriter().use {
                    it.write(gson.toJson(blacklist))
                }
                terminalPBFile.bufferedWriter().use {
                    it.write(gson.toJson(terminalPB))
                }
                hasJoinedFile.bufferedWriter().use {
                    it.write(gson.toJson(hasJoined))
                }
            } catch (e: IOException) {
                println("Error saving configs.")
            }
        }
    }
}