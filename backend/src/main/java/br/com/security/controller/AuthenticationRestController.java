package br.com.security.controller;

import br.com.security.jwt.JwtTokenResponse;
import br.com.security.jwt.JwtUser;
import br.com.security.jwt.exception.SecurityBadRequestException;
import br.com.security.jwt.exception.SecurityUserNameOrPasswordInvalidException;
import br.com.security.jwt.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@RestController
public class AuthenticationRestController {
    @Value("${security.jwt.controller.tokenHeader}")
    private String tokenHeader;
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthenticationRestController(final AuthenticationManager authenticationManager, final JwtTokenUtil jwtTokenUtil, final UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(value = "${security.jwt.controller.loginEndPoint}", method = RequestMethod.POST)
    public ResponseEntity createAuthenticationToken(@RequestBody Map<String, String> payload, Device device, HttpServletRequest request) {
        if (payload.isEmpty() || payload.size() < 2 || !payload.containsKey(USERNAME) || !payload.containsKey(PASSWORD)) {
            throw new SecurityBadRequestException(User.class, null, "Informe os campos de usuário e senha")
                    .addDetails(USERNAME, "algum usuário válido")
                    .addDetails(PASSWORD, "uma senha válida!");
        }

        if (payload.size() > 2) {
            throw new SecurityBadRequestException(User.class, null, "Por favor informe somente os campos de usuário e senha")
                    .addDetails(USERNAME, "algum usuário válido")
                    .addDetails(PASSWORD, "uma senha válida!");
        }

        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            payload.get(USERNAME),
                            payload.get(PASSWORD)
                    )
            );

            //despachando a requisição para a validação do spring
            SecurityContextHolder.getContext().setAuthentication(authentication);

            //se chegou até aqui é sinal que o usuário e senha é valido
            final UserDetails userDetails = userDetailsService.loadUserByUsername(payload.get(USERNAME));
            //gerando o token de autorização
            JwtTokenResponse token = new JwtTokenResponse(jwtTokenUtil.generateToken(userDetails, device));
            return ResponseEntity.ok(token);
        } catch (BadCredentialsException ex) {
            throw new SecurityUserNameOrPasswordInvalidException();
        }
    }

    @RequestMapping(value = "${security.jwt.controller.refreshEndPoint}", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtTokenResponse(refreshedToken));
        }
        throw new SecurityBadRequestException(null, null, "Token invalido. Faça login novamente");
    }
}
