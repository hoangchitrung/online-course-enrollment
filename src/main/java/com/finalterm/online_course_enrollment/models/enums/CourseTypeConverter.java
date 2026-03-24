package com.finalterm.online_course_enrollment.models.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CourseTypeConverter implements AttributeConverter<CourseType, String> {
    @Override
    public String convertToDatabaseColumn(CourseType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public CourseType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) {
            return null;
        }
        return CourseType.fromString(dbData);
    }
}
