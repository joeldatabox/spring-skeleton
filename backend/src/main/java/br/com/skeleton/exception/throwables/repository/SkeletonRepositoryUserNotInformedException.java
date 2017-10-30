package br.com.skeleton.exception.throwables.repository;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonRepositoryUserNotInformedException extends SkeletonRepositoryException {

    public SkeletonRepositoryUserNotInformedException(final Class clazz) {
        this(clazz, "Atenção, o \"usuario\" do objeto está nullo");
    }

    public SkeletonRepositoryUserNotInformedException(final Class clazz, final String message) {
        this(clazz, "user", message);
    }

    public SkeletonRepositoryUserNotInformedException(final Class clazz, final String field, final String message) {
        super(clazz, field, message);
    }
}
