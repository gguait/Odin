package me.odin.features.impl.skyblock

import me.odin.Odin.Companion.miscConfig

object BlackList {

    private val blackList
        get() = miscConfig.blacklist

    fun isInBlacklist(name: String) : Boolean = blackList.contains(name.lowercase())
}