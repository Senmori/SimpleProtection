package net.senmori.simpleprotect.protection;

import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

/**
 * Represents a type of protection available
 */
public abstract class Protection {

    /**
     * Gets if this block is protected
     * @param block the block to check
     * @return true if the block is protected
     */
    public abstract boolean isProtected(Block block);

    /**
     * Gets if this block can be protected
     *
     * @param block the block
     * @return true if this block can be protected
     */
    public abstract boolean canProtect(Block block);

    /**
     * Gets if the player can destroy this block.
     *
     * @param player the player
     * @param block the block the player is trying to destroy
     * @return true if the player can destroy this block
     */
    public abstract boolean canDestroy(Player player, Block block);

    /**
     * Gets if the player can interact with this block
     *
     * @param player the player
     * @param block the block the player is trying to interact with
     * @return true if the player can interact with this block
     */
    public abstract boolean canInteract(Player player, Block block);

    /**
     * Gets the owner's name of this block, if it is protected
     *
     * @param block the block
     * @return the owner's name if found, or null.
     */
    public abstract String getOwner(Block block);

    public abstract Sign getOwnerSign(Block block);
}
