package me.odin.utils.gui.animations

class LinearAnimation(duration: Long): Animation<Float>(duration) {

    override fun getValue(start: Float, end: Float, reverse: Boolean): Float {
        val startVal = if (reverse) end else start
        val endVal = if (reverse) start else end
        return startVal + (endVal - startVal) * (getPercent() / 100f)
    }
}