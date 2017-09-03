package ru.javawebinar.topjava.formatter;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class CustomDateTimeFormatAnnotationFormatterFactory implements
        AnnotationFormatterFactory<CustomDateTimeFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalDate.class, LocalTime.class));
    }

    @Override
    public Printer<?> getPrinter(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getCustomFormatter(annotation);
    }

    @Override
    public Parser<?> getParser(CustomDateTimeFormat annotation, Class<?> fieldType) {
        return getCustomFormatter(annotation);
    }

    private Formatter getCustomFormatter (CustomDateTimeFormat annotation) {
        return (annotation.type().equals(CustomDateTimeFormat.Type.DATE)
                ? new CustomDateFormatter() : new CustomTimeFormatter());
    }
}
