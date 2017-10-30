package br.com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@SpringBootApplication
@ServletComponentScan(value = "br.com.skeleton.controller.filter")
@PropertySource("classpath:application.properties")
public class SkeletonApplication {
    public static void main(String[] args) {
        SpringApplication.run(SkeletonApplication.class, args);
        System.out.println(java.lang.management.ManagementFactory.getRuntimeMXBean().getName());
    }
}
