package br.com.skeleton.controller.hateoas.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class PaginatedResultsRetrievedEvent<T extends Serializable> extends ApplicationEvent {
    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;
    private final Long page;
    private final Long pageSize;
    private final Long totalPages;
    private final Long totalRecords;

    public PaginatedResultsRetrievedEvent(
            final Object source,
            final UriComponentsBuilder uriBuilder,
            final HttpServletResponse response,
            final Long page,
            final Long pageSize,
            final Long totalPages,
            final Long totalRecords) {
        super(source);
        this.uriBuilder = uriBuilder;
        this.response = response;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalRecords = totalRecords;
    }

    public UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Long getPage() {
        return page;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    /**
     * The object on which the Event initially occurred.
     *
     * @return The object on which the Event initially occurred.
     */
    @SuppressWarnings("unchecked")
    public final Class<T> getClazz() {
        return (Class<T>) getSource();
    }

}
