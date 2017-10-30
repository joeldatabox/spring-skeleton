package br.com.skeleton.infra;

import br.com.skeleton.exception.throwables.SkeletonException;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class Annotations {

    public Annotation[] getAnnotations(String field, Class clazz) throws SkeletonException {
        Field f = null;
        try {
            f = clazz.getDeclaredField(field);
        } catch (NoSuchFieldException e) {
            throw new SkeletonException(e);
        }
        return getAnnotations(f);
    }

    public Annotation[] getAnnotations(Field field) {
        return field.getDeclaredAnnotations();
    }

    public boolean containsAnnotation(Class<? extends Annotation> annotation, Field field) {
        for (Annotation an : getAnnotations(field)) {
            if (an.getClass().equals(annotation)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsAnnotation(Class<? extends Annotation> annotation, String field, Class clazz) {
        for (Annotation an : getAnnotations(field, clazz)) {
            if (an.getClass().equals(annotation)) {
                return true;
            }
        }
        return false;
    }

    public Annotation getAnnotation(String field, Class<? extends Annotation> annotation, Class clazz) {
        try {
            return clazz.getDeclaredField(field).getAnnotation(annotation);
        } catch (NoSuchFieldException e) {
            throw new SkeletonException(e);
        }
    }

    public Annotation getAnnotation(Field field, Class<? extends Annotation> annotation) {
        return field.getAnnotation(annotation);
    }
}
