package br.com.skeleton.exception.controller;

import br.com.skeleton.exception.throwables.SkeletonException;
import br.com.skeleton.exception.throwables.SkeletonNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@RestController
public class ControllerError {
    @RequestMapping("/404")
    public void show404() {
        throw new SkeletonNotFoundException(null, null, HttpStatus.NOT_FOUND.getReasonPhrase());
    }

    @RequestMapping("/403")
    public void show403() {
        throw new SkeletonException("403", HttpStatus.FORBIDDEN);
    }
}
