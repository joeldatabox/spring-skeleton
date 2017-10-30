package br.com.skeleton.controller.api.v1;

import br.com.security.model.enumeration.Authorities;
import br.com.skeleton.controller.api.infra.DefaultRestController;
import br.com.skeleton.model.Conta;
import br.com.skeleton.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "/api/v1/contas", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
public class ContaRestController extends DefaultRestController<Conta> {

    private final ContaService service;

    @Autowired
    public ContaRestController(final ContaService service) {
        super(service, Authorities.RoleContaCreate, Authorities.RoleContaRead, Authorities.RoleContaUpdate, Authorities.RoleContaDelete);
        this.service = service;
    }

}
