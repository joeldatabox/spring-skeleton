package br.com.skeleton.model;

import br.com.security.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by joel on 19/01/17.
 */
public interface Model<T> extends Serializable {
    T getId();

    Model setId(T id);

    Model setDtCreate(Date dtCreate);

    Date getDtCreate();

    Model setLastUpdate(Date dtUpdate);

    Date getLastUpdate();

    Model setUser(User user);

    User getUser();

    Model setCreatedBy(User user);

    @JsonIgnore
    User getCreatedBy();

    /*ResourceSupport toResource();*/

    default String toJson() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValueAsString(this);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    default BigDecimal setDefaultBigDecimal(final BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO.setScale(5, BigDecimal.ROUND_HALF_EVEN);
        }
        return value.setScale(5, BigDecimal.ROUND_HALF_EVEN);
    }
}
