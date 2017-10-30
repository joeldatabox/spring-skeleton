package br.com.security.events;

import br.com.security.model.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class UserCreatedEvent extends ApplicationEvent {
    private final User user;

    public UserCreatedEvent(final User user) {
        super(user);
        this.user = user;
    }

    public final User getUser() {
        return this.user;
    }
}
