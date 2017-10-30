package br.com.skeleton.listener.postcreateuser.security;

import br.com.security.events.UserCreatedEvent;
import br.com.security.model.Authority;
import br.com.security.model.enumeration.Authorities;
import br.com.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.hibernate.validator.internal.util.CollectionHelper.asSet;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class CredentialsPostCreateUser implements ApplicationListener<UserCreatedEvent> {
    private final UserService service;

    @Autowired
    public CredentialsPostCreateUser(final UserService service) {
        this.service = service;
    }

    @Override
    public void onApplicationEvent(final UserCreatedEvent event) {
        this.service.update(event.getUser().setAuthorities(configReceitasPostCreateUser()));
    }

    private static final Set<Authority> configReceitasPostCreateUser() {
        return asSet(
                //<editor-fold desc="Conta">
                new Authority(Authorities.RoleContaCreate),
                new Authority(Authorities.RoleContaRead),
                new Authority(Authorities.RoleContaUpdate),
                new Authority(Authorities.RoleContaDelete),
                //</editor-fold>
                //<editor-fold desc="Categoria despesa">
                new Authority(Authorities.RoleCategoriaDespesaCreate),
                new Authority(Authorities.RoleCategoriaDespesaRead),
                new Authority(Authorities.RoleCategoriaDespesaUpdate),
                new Authority(Authorities.RoleCategoriaDespesaDelete),
                //</editor-fold>
                //<editor-fold desc="Tipo despesa">
                new Authority(Authorities.RoleTipoDespesaCreate),
                new Authority(Authorities.RoleTipoDespesaRead),
                new Authority(Authorities.RoleTipoDespesaUpdate),
                new Authority(Authorities.RoleTipoDespesaDelete),
                //</editor-fold>
                //<editor-fold desc="Categoria receita">
                new Authority(Authorities.RoleCategoriaReceitaCreate),
                new Authority(Authorities.RoleCategoriaReceitaRead),
                new Authority(Authorities.RoleCategoriaReceitaUpdate),
                new Authority(Authorities.RoleCategoriaReceitaDelete)
                //</editor-fold>
        );
    }
}
