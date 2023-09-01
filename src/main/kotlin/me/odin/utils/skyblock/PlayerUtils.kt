package me.odin.utils.skyblock

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.odin.Odin.Companion.mc
import me.odin.utils.AsyncUtils
import me.odin.utils.VecUtils.floored
import me.odin.utils.skyblock.ChatUtils.modMessage
import me.odin.utils.skyblock.ItemUtils.getItemIndexInContainerChest
import me.odin.utils.skyblock.ItemUtils.getItemSlot
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.settings.KeyBinding
import net.minecraft.inventory.ContainerChest
import net.minecraft.util.BlockPos
import net.minecraft.util.Vec3
import net.minecraft.util.Vec3i
import net.minecraftforge.client.event.GuiOpenEvent


object PlayerUtils {

    /**
     * Right-clicks the next tick
     */
    fun rightClick() {
        KeyBinding.onTick(-99) // Simple way of making completely sure the right-clicks are sent at the same time as vanilla ones.
    }


    fun alert(title: String, playSound: Boolean = true) {
        if (playSound) mc.thePlayer.playSound("note.pling", 100f, 1f)
        mc.ingameGUI.run {
            displayTitle(title, null, 10, 250, 10)
            displayTitle(null, "", 10, 250, 10)
            displayTitle(null, null, 10, 250, 10)
        }
    }

    fun getFlooredPlayerCoords(): Vec3i = mc.thePlayer.positionVector.floored()

    inline val posX get() = mc.thePlayer.posX
    inline val posY get() = mc.thePlayer.posY
    inline val posZ get() = mc.thePlayer.posZ
}