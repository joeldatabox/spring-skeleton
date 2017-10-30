package br.com.skeleton.exception.throwables.repository;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonRepositoryIdIsNullException extends SkeletonRepositoryException {

    public SkeletonRepositoryIdIsNullException(final Class clazz) {
        this(clazz, "Atenção, o \"id\" do objeto está nullo");
    }

    public SkeletonRepositoryIdIsNullException(final Class clazz, final String message) {
        this(clazz, "id", message);
    }

    public SkeletonRepositoryIdIsNullException(final Class clazz, final String field, final String message) {
        super(clazz, field, message);
    }
}
