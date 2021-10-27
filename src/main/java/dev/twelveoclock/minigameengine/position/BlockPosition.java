package dev.twelveoclock.minigameengine.position;

import org.bukkit.Location;
import org.bukkit.block.Block;

/**
 * @param bitMask The bitMask used to create the position
 */
public final record BlockPosition(Long bitMask) {

    /**
     * @param x The x position
     * @param y The y position
     * @param z The z position
     */
    public BlockPosition(final int x, final int y, final int z) {
        this(((((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF)));
    }

    /**
     * @param block The block to construct the BlockPosition with
     */
    public BlockPosition(final Block block) {
        this(block.getX(), block.getY(), block.getZ());
    }

    /**
     * @param location The location to construct the BlockPosition with
     */
    public BlockPosition(final Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }


    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ", " + getZ() + ")";
    }


    /**
     * @return The calculated x position from the bitmask
     */
    public int getX() {
        return (int) (bitMask >> 38);
    }

    /**
     * @return The calculated y position from the bitmask
     */
    public int getY() {
        return (int) (bitMask & 0xFFF);
    }

    /**
     * @return The calculated z position from the bitmask
     */
    public int getZ() {
        return (int) (bitMask << 26 >> 38);
    }

}
