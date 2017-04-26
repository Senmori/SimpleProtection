package net.senmori.simpleprotect.protection;

import net.senmori.simpleprotect.protection.type.ProtectionType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Protection {
    protected final ProtectionType type;

    protected Protection(ProtectionType protectionType) {
        this.type = protectionType;
    }

    /**
     * Checks if the {@link Location} is protected.
     *
     * @param location
     *
     * @return
     */
    public abstract boolean isProtected(Location location);

    /**
     * Check if this location CAN be protected<br>
     * i.e. It might already be protected, or the location is not valid(i.e. on top of sand/gravel)
     *
     * @param location
     *
     * @return
     */
    public abstract boolean canProtect(Location location);

    /**
     * Check if the player is the owner of the Protection at this location<br>
     * Returns false if there is no Protection at the location
     *
     * @param loc
     * @param player
     *
     * @return
     */
    public abstract boolean isOwner(Location loc, Player player);

    /**
     * Check if the player can interact with the Protection at this location.<br>
     * Returns true if there is no Protection at this location
     *
     * @param loc
     * @param player
     *
     * @return
     */
    public abstract boolean canInteract(Location loc, Player player);

    /**
     * Called when a player interacts at a location that contains a protection
     *
     * @param location
     * @param player
     */
    public void onInteract(Location location, Player player) {
    }

    /**
     * Called when a player destroys a protection at a location
     *
     * @param location
     * @param player
     */
    public void onDestroy(Location location, Player player) {
    }

    /**
     * Called when a player creates a protection at a location
     *
     * @param location
     * @param player
     */
    public void onCreate(Location location, Player player) {
    }


    public String getIdentifier() {
        return this.type.getName();
    }
}
