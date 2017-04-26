package net.senmori.simpleprotect.protection.type;

import java.util.regex.Pattern;

public class ProtectionType {
    public static final Pattern VALID_PATTERN = Pattern.compile("\\[(.*?)\\]");
    public static final Pattern ILLEGAL_CHARS = Pattern.compile("[~#@*+%{}<>|\"^]");


    /**
     * Represents a Protection that anyone can access; but only the owner can destroy
     */
    public static final ProtectionType PUBLIC = new ProtectionType("public", true, true);

    /**
     * Represents a Protection that only the owner, and those they specify can access
     */
    public static final ProtectionType PRIVATE = new ProtectionType("private", true, true);

    /**
     * Represents an additional Protection feature that adds users to an existing protection
     */
    public static final ProtectionType MORE_USERS = new ProtectionType("more_users", false, true);

    /**
     * Represents a Protection that can ONLY have this protection label.<br>
     * i.e. [Limited] is the only valid sign that block can have. Not even [More Users]
     */
    public static final ProtectionType LIMITED = new ProtectionType("limited", true, false);


    private final String name;
    private final boolean isUnique;
    private final boolean moreUsers;

    public ProtectionType(String name, boolean isUnique, boolean moreUsers) {
        this.name = name;
        this.isUnique = isUnique;
        this.moreUsers = moreUsers;
    }

    /**
     * Get the name of this protection type.<br>
     * This is the name inside brackets on signs. (i.e. [Private], [Public])<br>
     * These are not case-sensitive.
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get if this protection type is unique. Meaning there can only be one of it on a protection.
     *
     * @return
     */
    public boolean isUnique() {
        return this.isUnique;
    }

    /**
     * Get if this protection can have more users. (i.e. one or more [More Users] sign)
     *
     * @return
     */
    public boolean canHaveMoreUsers() {
        return moreUsers;
    }

    /**
     * Check if this protection type will conflict with another protection type<br>
     *
     * @param type
     *
     * @return true if either protection type is unique
     */
    public boolean conflictsWith(ProtectionType type) {
        return ( this.isUnique() || type.isUnique() ) || this.canHaveMoreUsers();
    }

    /**
     * Check if the protection type on the sign(i.e. [Public], [Private]) is valid
     *
     * @param identifier
     *
     * @return
     */
    public boolean isValidPattern(String identifier) {
        return ! ILLEGAL_CHARS.matcher(identifier).find() && VALID_PATTERN.matcher(identifier).matches();
    }

    public boolean equals(Object other) {
        if(other instanceof ProtectionType) {
            ProtectionType that = (ProtectionType) other;
            return this.name.equalsIgnoreCase(that.getName()) && this.isUnique() == that.isUnique() && this.canHaveMoreUsers() == that.canHaveMoreUsers();
        }
        return false;
    }
}
