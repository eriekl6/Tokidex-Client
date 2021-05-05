package ca.tokidex.client.model;

/**
 * Enum for possible types/abilities of a tokimon
 */
public enum TokimonType {
    FLYING,
    FIRE,
    WATER,
    ELECTRIC,
    ICE;

    public static TokimonType fromString(String text) {
        for (TokimonType type : TokimonType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        return null;
    }

}
