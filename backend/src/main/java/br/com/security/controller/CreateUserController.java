package br.com.security.controller;

import br.com.security.events.UserCreatedEvent;
import br.com.security.jwt.exception.SecurityBadRequestException;
import br.com.security.model.User;
import br.com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@RestController
public class CreateUserController {

    private final ApplicationEventPublisher eventPublisher;
    private UserService service;
    private static final String NOME = "nome";
    private static final String EMAIL = "email";
    private static final String PASSWD = "password";

    @Autowired
    public CreateUserController(final ApplicationEventPublisher eventPublisher, final UserService service) {
        this.eventPublisher = eventPublisher;
        this.service = service;
    }

    @RequestMapping(value = "${security.jwt.controller.createEndPoint}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody Map<String, String> payload, HttpServletResponse response) {
        if (payload.isEmpty() || payload.size() < 3 || !payload.containsKey(NOME) || !payload.containsKey(EMAIL) || !payload.containsKey(PASSWD)) {
            throw new SecurityBadRequestException(User.class, null, "Informe o nome, email e a senha")
                    .addDetails(NOME, "Nome completo")
                    .addDetails(EMAIL, "Informe um email válido")
                    .addDetails(PASSWD, "Informe uma senha válida");
        }

        if (payload.size() > 3) {
            throw new SecurityBadRequestException(User.class, null, "Por favor informe somente o nome, email e a senha")
                    .addDetails(NOME, "Nome completo")
                    .addDetails(EMAIL, "Informe um email válido")
                    .addDetails(PASSWD, "Informe uma senha válida");
        }
        final User user = new User();
        user.setNome(payload.get(NOME));
        user.setEmail(payload.get(EMAIL));
        user.setPasswd(payload.get(PASSWD));
        this.eventPublisher.publishEvent(new UserCreatedEvent(service.save(user)));
    }
}
