package me.odin.events.impl

import net.minecraft.block.state.IBlockState
import net.minecraft.util.BlockPos
import net.minecraftforge.fml.common.eventhandler.Event

/**
 * Sent when a block has been updated.
 * @see me.odin.mixin.MixinChunk.onBlockChange
 */
class BlockChangeEvent(val pos: BlockPos, val old: IBlockState, val update: IBlockState) : Event()