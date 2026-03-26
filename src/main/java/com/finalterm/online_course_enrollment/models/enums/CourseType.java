package com.finalterm.online_course_enrollment.models.enums;

public enum CourseType {
    SELF_PACED("SELF_PACED", "Self-Paced"),
    LIVE_BOOT_CAMP("LIVE_BOOT_CAMP", "Live Boot Camp");

    private final String code;
    private final String displayName;

    CourseType(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CourseType fromString(String raw) {
        if (raw == null) {
            return null;
        }

        String normalized = raw.trim().replace("-", "_").replace(" ", "_").toUpperCase();

        for (CourseType type : values()) {
            if (type.name().equalsIgnoreCase(normalized) || type.code.equalsIgnoreCase(normalized)) {
                return type;
            }
        }

        throw new IllegalArgumentException("Unknown CourseType: " + raw);
    }
}
