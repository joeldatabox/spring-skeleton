package br.com.skeleton.exception.throwables.repository;

import br.com.skeleton.exception.throwables.SkeletonException;
import org.springframework.http.HttpStatus;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SkeletonRepositoryException extends SkeletonException {

    public SkeletonRepositoryException(final Class clazz, final String field, final String message) {
        super("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, clazz, field, message);
        /*this.message = "Conflict";
        this.status = HttpStatus.CONFLICT;
        this.objectName = clazz.getSimpleName().toLowerCase();
        this.details.put(this.objectName + "." + field, message);*/
    }
}
