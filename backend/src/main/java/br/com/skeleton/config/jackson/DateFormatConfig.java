package br.com.skeleton.config.jackson;

import java.text.SimpleDateFormat;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
public class DateFormatConfig extends SimpleDateFormat {
    private static final String PATTHERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public DateFormatConfig() {
        super(PATTHERN);
    }
}
