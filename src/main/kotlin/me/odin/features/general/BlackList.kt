package me.odin.features.general

import me.odin.Odin.Companion.miscConfig

object BlackList {

    private val blackList
        get() = miscConfig.blacklist

    fun isInBlacklist(name: String) : Boolean = blackList.contains(name.lowercase())
}