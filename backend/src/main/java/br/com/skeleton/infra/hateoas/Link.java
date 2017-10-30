package br.com.skeleton.infra.hateoas;

import org.springframework.hateoas.UriTemplate;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class Link extends org.springframework.hateoas.Link {
    private RequestMethod method;

    public Link(String href) {
        super(href);
    }

    public Link(String href, String rel) {
        super(href, rel);
    }

    public Link(UriTemplate template, String rel) {
        super(template, rel);
    }

    public Link() {
        super();
    }

    public Link(String href, String rel, RequestMethod method) {
        super(href, rel);
        this.method = method;
    }

    public RequestMethod getMethod() {
        return method;
    }

    public Link method(RequestMethod method) {
        this.method = method;
        return this;
    }
}
