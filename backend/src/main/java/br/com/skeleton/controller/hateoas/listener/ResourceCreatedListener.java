package br.com.skeleton.controller.hateoas.listener;


import br.com.skeleton.controller.hateoas.event.ResourceCreatedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class ResourceCreatedListener implements ApplicationListener<ResourceCreatedEvent> {
    @Override
    public void onApplicationEvent(final ResourceCreatedEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event has null");
        }
        event.getResponse()
                .setHeader(HttpHeaders.LOCATION, ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(event.getModel().getId())
                        .toUri().toASCIIString());
    }
}
