package me.odin.events.impl

import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Event

/**
 * Sent when a block has been updated.
 * @see me.odin.mixin.MixinWorld.onsetBlockState
 */
class BlockUpdateEvent(var pos: BlockPos, var state: IBlockState) : Event()