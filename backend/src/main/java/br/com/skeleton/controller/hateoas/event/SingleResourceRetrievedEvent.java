package br.com.skeleton.controller.hateoas.event;

import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class SingleResourceRetrievedEvent extends ApplicationEvent {
    private final HttpServletResponse response;

    public SingleResourceRetrievedEvent(final Object source, final HttpServletResponse response) {
        super(source);
        this.response = response;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

}