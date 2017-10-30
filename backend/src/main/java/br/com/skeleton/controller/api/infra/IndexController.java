package br.com.skeleton.controller.api.infra;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Controller
public class IndexController {
    private static final String ROOT = "forward:/";
    private static final String INDEX = "forward:index.html";

    @RequestMapping("/")
    public String index() {
        return INDEX;
    }

    @RequestMapping("/login")
    public String login() {
        return ROOT;
    }

    @RequestMapping("/create-user")
    public String createUser() {
        return ROOT;
    }

    @RequestMapping("/home/**")
    public String app() {
        return ROOT;
    }
}
