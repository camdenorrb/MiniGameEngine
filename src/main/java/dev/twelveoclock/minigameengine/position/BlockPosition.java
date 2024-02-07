package dev.twelveoclock.minigameengine.position;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 * @param bitMask The bitMask used to create the position
 */
public record BlockPosition(Long bitMask) {

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
     * @param world The world to convert the BlockPosition to a Location in
     * @return The Location representation of the BlockPosition
     */
    public Location toLocation(final World world) {
        return new Location(world, getX(), getY(), getZ());
    }

    public BlockPosition add(final int x, final int y, final int z) {
        return new BlockPosition(getX() + x, getY() + y, getZ() + z);
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


    public static long getAsBitMask(final int x, final int y, final int z) {
        return ((((long) x & 0x3FFFFFF) << 38) | (((long) z & 0x3FFFFFF) << 12) | ((long) y & 0xFFF));
    }

    public static int getX(final long bitMask) {
        return (int) (bitMask >> 38);
    }

    public static int getY(final long bitMask) {
        return (int) (bitMask & 0xFFF);
    }

    public static int getZ(final long bitMask) {
        return (int) (bitMask << 26 >> 38);
    }

}
