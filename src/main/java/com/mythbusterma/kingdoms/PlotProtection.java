package com.mythbusterma.kingdoms;
@Deprecated
public enum PlotProtection {
    /** disables all interaction with this chunk */
    NO_INTERACTION(0),

    /** no protection, outsiders may interact, place, etc. */
    NO_PROTECTION(1),

    /** default level, residents can place and use, but outsiders cannot */
    PROTECTED(2),

    /** outsiders can interact with items, but not open containers*/
    INTERACT(3),
    
    /** only the owner and his friends can place on this plot, residents of the town can interact*/
    RESIDENT_INTERACT(5),

    /** outsiders can interact and open containers*/
    CONTAINER(4)

    ;

    private final int id;
    
    PlotProtection(int id) {
        this.id = id;
    }
    
    public int getId() {
        return id;        
    }
    
    public static PlotProtection fromId(int id) {
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
            case 5:
                return RESIDENT_INTERACT;
            default:
                throw new IllegalArgumentException("No PlotProtection value found for: " + id);
        }
    }
}
