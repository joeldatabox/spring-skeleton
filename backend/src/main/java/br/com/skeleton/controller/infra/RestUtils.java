package br.com.skeleton.controller.infra;

import br.com.security.jwt.JwtUser;
import br.com.security.model.User;
import br.com.skeleton.controller.hateoas.event.PaginatedResultsRetrievedEvent;
import br.com.skeleton.controller.hateoas.event.ResourceCreatedEvent;
import br.com.skeleton.controller.hateoas.event.SingleResourceRetrievedEvent;
import br.com.skeleton.controller.hateoas.resource.MetadataPageable;
import br.com.skeleton.controller.hateoas.resource.PageableResource;
import br.com.skeleton.exception.throwables.SkeletonNoContentException;
import br.com.skeleton.exception.throwables.SkeletonPageableRequestException;
import br.com.skeleton.model.Model;
import br.com.skeleton.repository.infra.Operators;
import br.com.skeleton.service.SkeletonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class RestUtils<T extends Serializable, ID extends Serializable> {
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    protected final SkeletonService<Model, ID> service;

    public RestUtils(final SkeletonService service) {
        this.service = service;
    }

    public final Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }


    public final JwtUser getCurrentJwtUser() {
        return (JwtUser) getCurrentAuthentication().getPrincipal();
    }

    public final User getCurrentUser() {
        return getCurrentJwtUser().getOriginUser();
    }

    protected final void publishCreateResourceEvent(final HttpServletResponse response, final Model model) {
        this.eventPublisher.publishEvent(new ResourceCreatedEvent(this, response, model));
    }

    protected final void publishSingleResourceRetrievedEvent(final HttpServletResponse response) {
        this.eventPublisher.publishEvent(new SingleResourceRetrievedEvent(this, response));
    }

    protected final void publishPaginatedResultsRetrievedEvent(final HttpServletResponse response, final UriComponentsBuilder uriComponentsBuilder, final Long page, final Long pageSize, final Long totalpages, final Long totalRecords) {
        this.eventPublisher.publishEvent(
                new PaginatedResultsRetrievedEvent<T>(this, uriComponentsBuilder, response, page, pageSize, totalpages, totalRecords)
        );
    }

    protected final PageableResource toPageableResource(final User user, final Map<String, String> params) {
        //validando os parametros passados
        final Map<String, Object> allRequestParams = validPageable(params);
        final Long SKIP = Long.valueOf(allRequestParams.get(Operators.SKIP.toString()).toString());
        final Long LIMIT = Long.valueOf(allRequestParams.get(Operators.LIMIT.toString()).toString());

        final long total = service.count(user, createQueryParamForCount(allRequestParams));

        if (total == 0) {
            throw new SkeletonNoContentException(null, null, "registros não encontrados!");
        }

        final List records = service
                .findAll(user, allRequestParams);

        return new PageableResource(
                records,
                new MetadataPageable(
                        ServletUriComponentsBuilder.fromCurrentRequest(),
                        LIMIT,
                        SKIP,
                        Long.valueOf(records.size()), total));
    }

    /**
     * Valida parametros passado na requisição para paginação
     */
    private final Map<String, Object> validPageable(final Map<String, String> allRequestParams) {
        final SkeletonPageableRequestException ex = new SkeletonPageableRequestException();

        if (allRequestParams.containsKey(Operators.LIMIT.toString())) {
            Integer limit = null;
            try {
                limit = Integer.valueOf(allRequestParams.get(Operators.LIMIT.toString()));
                if (limit > 100) {
                    ex.addDetails(Operators.LIMIT.toString(), "o limite informado foi (" + limit + ") mas o maxímo é(100)");
                }
            } catch (NumberFormatException nex) {
                ex.addDetails(Operators.LIMIT.toString(), "deve conter um numero com o tamanho maximo de 100");
            }
        } else {
            allRequestParams.put(Operators.LIMIT.toString(), "100");
        }

        if (allRequestParams.containsKey(Operators.SKIP.toString())) {
            Integer page = null;
            try {
                page = Integer.valueOf(allRequestParams.get(Operators.SKIP.toString()));
                if (page < 0) {
                    ex.addDetails(Operators.SKIP.toString(), "a pagina informada foi (" + page + ") mas a deve ter o tamanho minimo de (0)");
                }
            } catch (final NumberFormatException nex) {
                ex.addDetails(Operators.SKIP.toString(), "deve conter um numero com o tamanho minimo de 0");
            }
        } else {
            allRequestParams.put(Operators.SKIP.toString(), "0");
        }

        if (ex.containsDetais()) {
            throw ex;
        }
        return new HashMap<>(allRequestParams);
    }

    /**
     * Remove parametros denecessarios para contagem (limit, page)
     */
    private Map<String, Object> createQueryParamForCount(final Map<String, Object> allRequestParams) {
        return allRequestParams
                .entrySet()
                .stream()
                .filter(key ->
                        !key.getKey().equals(Operators.LIMIT.toString()) && !key.getKey().equals(Operators.SKIP.toString())
                )
                .collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
    }
}
