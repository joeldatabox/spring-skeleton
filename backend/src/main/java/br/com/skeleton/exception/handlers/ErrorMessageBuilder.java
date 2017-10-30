package br.com.skeleton.exception.handlers;

import br.com.skeleton.exception.throwables.SkeletonException;
import br.com.skeleton.exception.throwables.repository.SkeletonRepositoryException;
import br.com.security.jwt.exception.SecurityUnauthorizedException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Map;

/**
 * @author Joel Rodrigues Moreira on 20/10/17.
 * @project skeleton
 */
@Component
public class ErrorMessageBuilder {
    private static final Logger logger = LoggerFactory.getLogger(ErrorMessageBuilder.class);

    private ErrorMessage message;

    @Value("${skeleton.print.stackTrace:false}")
    private boolean STACK_TRACE;

    @Value("${skeleton.print.responseException:false}")
    private boolean RESPONSE_EXCEPTION;

    public ErrorMessage build(final MethodArgumentNotValidException ex) {
        this.message = new ErrorMessage();
        setStatus(HttpStatus.BAD_REQUEST);
        setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        setObjectName(ex.getBindingResult().getObjectName());
        for (ObjectError fieldError : ex.getBindingResult().getAllErrors()) {
            String key = fieldError.getCodes()[0].replace(fieldError.getCodes()[fieldError.getCodes().length - 1] + ".", "");
            addDetails(key, fieldError.getDefaultMessage());
        }
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final ConstraintViolationException ex) {
        this.message = new ErrorMessage();
        setStatus(HttpStatus.BAD_REQUEST);
        setMessage(HttpStatus.BAD_REQUEST.getReasonPhrase());
        boolean cont = false;
        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            if (!cont) {
                setObjectName(violation.getLeafBean().getClass().getSimpleName().toLowerCase());
                cont = true;
            }
            String[] path = violation.getPropertyPath().toString().split("arg");
            if (path.length > 1) {
                addDetails(this.message.objectName + "." + path[1].substring(path[1].indexOf(".") + 1), violation.getMessage());
            } else {
                addDetails(this.message.objectName + "." + path[0], violation.getMessage());
            }
        }
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final HttpMediaTypeNotSupportedException ex) {
        this.message = new ErrorMessage();
        setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        setMessage(ex.getMessage().replace("'null' ", ""));
        String contentType = "uninformed";
        addDetails("ContentType", ex.getContentType() == null ? contentType : ex.getContentType().toString());
        //addDetails("SupportedMediaTypes", ex.getSupportedMediaTypes().stream().map(mt -> mt.toString()).collect(Collectors.toList()));
        addDetails("SupportedMediaTypes", MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final HttpMessageNotReadableException ex) {
        this.message = new ErrorMessage();
        setStatus(HttpStatus.BAD_REQUEST);
        //procurando exceções de negocio
        SkeletonException de = (SkeletonException) findInivistateException(ex, null);

        if (de != null) {
            return build(de);
        } else if (ex.getCause() instanceof com.fasterxml.jackson.core.JsonParseException) {
            JsonLocation location = ((com.fasterxml.jackson.core.JsonParseException) ex.getCause()).getLocation();
            setMessage("Illegal character in line:" + location.getLineNr() + " column:" + location.getColumnNr());
        } else if (ex.getCause() instanceof JsonMappingException) {
            JsonLocation location = ((JsonMappingException) ex.getCause()).getLocation();
            if (location != null) {
                setMessage("Illegal character in line:" + location.getLineNr() + " column:" + location.getColumnNr());
            }
        } else if (ex.getMessage().contains("Required request body is missing:")) {
            setMessage("Insira o corpo na requisição!");
            addDetails("body", "body is empty");
        }
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final SkeletonException ex) {
        this.message = new ErrorMessage();
        setStatus(ex.getStatus());
        setMessage(ex.getMessage());
        setObjectName(ex.getObjectName());
        addDetails(ex.getDetails());
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final SkeletonRepositoryException ex) {
        this.message = new ErrorMessage();
        setStatus(ex.getStatus());
        printException(ex);
        return this.message;
    }

    public ErrorMessage build(final SecurityUnauthorizedException ex) {
        this.message = new ErrorMessage();
        setStatus(ex.getStatus());
        setMessage(ex.getMessage());
        setObjectName(ex.getObjectName());
        addDetails(ex.getDetails());
        printException(ex);
        return this.message;
    }

    public ErrorMessageBuilder setStatus(final HttpStatus status) {
        this.message.status = status;
        return this;
    }

    public ErrorMessageBuilder setStatus(final Integer status) {
        this.message.status = HttpStatus.valueOf(status);
        return this;
    }

    public ErrorMessageBuilder setObjectName(final String objectName) {
        this.message.objectName = objectName;
        return this;
    }

    public ErrorMessageBuilder setObjectName(final Object objectName) {
        this.message.objectName = objectName.getClass().getSimpleName();
        return this;
    }

    public ErrorMessageBuilder setMessage(final String message) {
        this.message.message = message;
        return this;
    }

    public ErrorMessageBuilder addDetails(final String key, Object value) {
        this.message.details.put(key, value);
        return this;
    }

    public ErrorMessageBuilder addDetails(final String key, final Object... value) {
        this.message.details.put(key, value);
        return this;
    }

    public ErrorMessageBuilder addDetails(final String key, final List<Object> value) {
        this.message.details.put(key, value);
        return this;
    }

    public ErrorMessageBuilder addDetails(final Map<String, Object> details) {
        this.message.details.putAll(details);
        return this;
    }

    /**
     * Imprime a pilha de Exceptions de acordo com application.properties
     *
     * @param ex ->exceção para ser logada!
     */
    private void printException(final Exception ex) {
        if (STACK_TRACE) {
            ex.printStackTrace();
        }
        if (RESPONSE_EXCEPTION) {
            logger.info(this.message.toJson());
        }
    }

    /**
     * Verifica se na arvore de exceptions existe algum erro advindo de regra de negocio.
     * Caso exista esse erro é retornado primordialmente;
     *
     * @param exActual  ->Exception atual
     * @param exPrevius ->Exception que aponta para Exception atual
     */
    private Throwable findInivistateException(final Throwable exActual, final Throwable exPrevius) {
        if (exActual == null || exActual.equals(exPrevius)) {
            return null;
        }
        if (exActual instanceof SkeletonException) {
            return exActual;
        } else {
            return findInivistateException(exActual.getCause(), exActual);
        }

    }
}
