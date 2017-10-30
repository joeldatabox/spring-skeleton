package br.com.skeleton.controller.api.infra;

import br.com.skeleton.controller.hateoas.resource.PageableResource;
import br.com.skeleton.controller.infra.RestUtils;
import br.com.skeleton.model.Model;
import br.com.skeleton.service.SkeletonService;
import br.com.security.jwt.exception.SecurityCredentialException;
import br.com.security.model.enumeration.Authorities;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class DefaultRestController<T extends Model> extends RestUtils<T, String> {
    protected final Authorities createRole;
    protected final Authorities readRole;
    protected final Authorities updateRole;
    protected final Authorities deleteRole;

    public DefaultRestController(final SkeletonService service, final Authorities createRole, final Authorities readRole, final Authorities updateRole, final Authorities deleteRole) {
        super(service);
        this.createRole = createRole;
        this.readRole = readRole;
        this.updateRole = updateRole;
        this.deleteRole = deleteRole;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity save(@Valid @RequestBody final T value, final HttpServletResponse response, @RequestParam(required = false, value = "returnEntity", defaultValue = "") final String returnEntity) {
        checkCreate();
        T record = (T) service.save(getCurrentUser(), value);
        publishCreateResourceEvent(response, record);
        if (returnEntity != null && returnEntity.equals("true")) {
            return ResponseEntity.status(HttpStatus.CREATED).body(record);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity update(@PathVariable("id") final String id, @RequestBody final T model) {
        checkUpdate();
        model.setId(id);
        return ResponseEntity.ok(service.update(getCurrentUser(), model));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity deleteById(@PathVariable("id") final String id) {
        checkDelete();
        service.deleteById(getCurrentUser(), id);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity findById(@PathVariable("id") final String id) {
        checkRead();
        return ResponseEntity.ok(service.findById(getCurrentUser(), id));
    }

    @RequestMapping(value = "/first", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity first() {
        checkRead();
        return ResponseEntity.ok(service.findFirst(getCurrentUser()));
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<PageableResource> list(@RequestParam final Map<String, String> allRequestParams) {
        checkRead();
        return ResponseEntity.ok(toPageableResource(getCurrentUser(), allRequestParams));
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET, produces = {MediaType.TEXT_PLAIN_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public final ResponseEntity<String> count(final Map<String, Object> allRequestParams) {
        checkRead();
        return ResponseEntity.ok(String.valueOf(service.count(getCurrentUser(), allRequestParams)));
    }

    protected final void checkCreate() {
        this.checkCredentials(this.createRole);
    }

    protected final void checkRead() {
        this.checkCredentials(this.readRole);
    }

    protected final void checkUpdate() {
        this.checkCredentials(this.updateRole);
    }

    protected final void checkDelete() {
        this.checkCredentials(this.deleteRole);
    }

    protected final void checkCredentials(final Authorities role) {
        if (!getCurrentUser().inRole(role)) {
            throw new SecurityCredentialException("Você não tem permissão para \"" + role.getDescription() + "\"", role);
        }
    }
}
