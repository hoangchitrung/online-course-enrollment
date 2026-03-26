package com.finalterm.online_course_enrollment.models.enums;

public enum CourseType {
    SELF_PACED("SELF_PACED", "Tự học"),
    LIVE_BOOT_CAMP("LIVE_BOOT_CAMP", "Lớp học trực tiếp");

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
