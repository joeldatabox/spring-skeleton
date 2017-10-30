package br.com.security.model.resource;

import br.com.security.jwt.JwtUser;
import br.com.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class UserResource extends ResourceSupport {
    private Resource<User> user;

    public UserResource(final User user) {
        this.user = new Resource<>(user);

        //this.user.add(linkTo(methodOn(UserManagerController.class).get()).withRel("self"));
        this.user.add(getSelfLink());
    }

    public UserResource(final JwtUser user) {
        this(user.getOriginUser());
    }

    public UserResource(final Authentication authentication) {
        this((JwtUser) authentication.getPrincipal());
    }

    public Resource<User> getUser() {
        return user;
    }

    @JsonIgnore
    private Link getSelfLink() {
        final UriTemplate uri = new UriTemplate(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .buildAndExpand()
                        .toUri().toASCIIString());
        return new Link(uri, "self");
    }
}
