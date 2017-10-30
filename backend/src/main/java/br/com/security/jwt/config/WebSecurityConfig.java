package br.com.security.jwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@SuppressWarnings("SpringJavaAutowiringInspection")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Value("${security.jwt.controller.loginEndPoint}")
    private String loginEndPoint;
    @Value("${security.jwt.controller.refreshEndPoint}")
    private String refreshTokenEndPoin;
    @Value("${security.jwt.controller.createEndPoint}")
    private String createEndPoint;

    @Autowired
    private UnauthorizedHandler unauthorizedHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilter() {
        return new AuthenticationTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Autowired
    protected void configureAuthentication(AuthenticationManagerBuilder authentication) throws Exception {
        authentication
                .userDetailsService(this.userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //não é necessário CSRF pois nossos tokens evita isso
                .csrf().disable()
                //ouvinte que despacha requisiçõe não autorizadas
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .exceptionHandling().accessDeniedPage("/403").and()
                //desativando o controle de sessão
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // permite acesso a qualquer recurso estatico
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/**/*.{png,jpg,jpeg,svg.ico}",
                        "/**/*.{html,css,js,svg,woff,woff2}",
                        //endpoit padrão da aplicação
                        "/login",
                        "/create-user",
                        "/home/**"
                ).permitAll()
                //permitindo acesso aos endpoint de login
                .antMatchers(loginEndPoint, refreshTokenEndPoin, createEndPoint).permitAll()
                //barrando qualquer outra requisição não autenticada
                .anyRequest().authenticated();

        //adicionando o filtro de segurança
        http.addFilterBefore(authenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        //desabilitando controle de cache
        http.headers().cacheControl();
    }
}
