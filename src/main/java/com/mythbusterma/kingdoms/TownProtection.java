package com.mythbusterma.kingdoms;

@Deprecated
public enum TownProtection {
    /** disables all interaction with this chunk */
    NO_INTERACTION(0),
    
    /** no protection, outsiders may interact, place, etc. */
    NO_PROTECTION(1),
    
    /** default level, residents can place and use, but outsiders cannot */
    PROTECTED(2),
    
    /** outsiders can interact with items, but not open containers*/
    INTERACT(3),
    
    /** outsiders can interact and open containers*/
    CONTAINER(4)
    ;
    
    private final int id;
    
    TownProtection(int id) {
        this.id = id;
        
    }

    public int getId() {
        return id;
    }

    public static TownProtection fromId(int id) {
        switch(id) {
            case 0:
                return NO_INTERACTION;
            case 1:
                return NO_PROTECTION;
            case 2:
                return PROTECTED;
            case 3:
                return INTERACT;
            case 4:
                return CONTAINER;
            default:
                throw new IllegalArgumentException("No TownProtection value found for: " + id);
        }
    }
}
