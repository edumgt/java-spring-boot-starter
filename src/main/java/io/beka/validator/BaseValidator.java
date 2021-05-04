package io.beka.validator;

import io.beka.configuration.I18n;
import io.beka.exception.BaseResponseException;
import io.beka.util.ConstantData;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.annotation.PreDestroy;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//public class BaseValidator<T> {
public class BaseValidator extends BaseResponseException {

    @Autowired
    private I18n i18n;

    @Autowired
    private HttpServletRequest request;
    private List<String> errors = new ArrayList<>();


    public boolean isNew() {
        return request.getMethod().equalsIgnoreCase(ConstantData.METHOD_POST);
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addErrorDuplicate(String data) {
        errors.add(i18n.getMessage("error.validateDuplicate", data));
    }
    public void addErrorNotFound() {
        errors.add(i18n.getMessage("error.dataNotfound"));
    }

    public void addErrorRequireField(String data) {
        errors.add(i18n.getMessage("error.validateRequireField", data));
    }

    public void checkValidate() {
        if (this.errors.size() > 0) {
            List<String> err = this.errors;
            this.errors = new ArrayList<>();
            throw this.responseError(HttpStatus.BAD_REQUEST, null, err);
        }
    }
//    public BaseValidator(T t) {
//        System.out.println("BaseValidator: Constructor");
//        for (Field field : t.getClass().getDeclaredFields()) {
//            javax.persistence.Column column = field.getAnnotation(javax.persistence.Column.class);
//            if (column != null) {
//                System.out.println("Columns: " + column);
//            }
//        }
//    }

}