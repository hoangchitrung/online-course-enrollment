package com.finalterm.online_course_enrollment.models.enums;

public enum CourseType {
    SELF_PACED("SELF_PACED"),
    LIVE_BOOT_CAMP("LIVE_BOOT_CAMP");

    private final String code;

    CourseType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
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

        // fallback for legacy values
        if ("SELF-PACED".equalsIgnoreCase(raw) || "Self-Paced".equalsIgnoreCase(raw)) {
            return SELF_PACED;
        }
        if ("Live-Boot-Camp".equalsIgnoreCase(raw) || "live boot camp".equalsIgnoreCase(raw)) {
            return LIVE_BOOT_CAMP;
        }

        throw new IllegalArgumentException("Unknown CourseType: " + raw);
    }
}
