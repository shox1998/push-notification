package uz.kibera.project.mapper.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS)
@Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
public @interface CreateNewEntity {
}
