package server.error;

import com.google.gson.Gson;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides a JSON representation for a collection of FieldError.
 * Useful for communicating a list of validation errors back to the frontend layer.
 */
public class JsonErrors {

    List<JsonFieldError> fieldErrors = new ArrayList<>();

    public JsonErrors(Errors errors) {
        for (FieldError e : errors.getFieldErrors()) {
            JsonFieldError fieldError = new JsonFieldError(e.getField(), e.getRejectedValue().toString(), e.getCode());
            fieldErrors.add(fieldError);
        }
    }

    /**
     * Represent a single field error.
     */
    public class JsonFieldError {
        private String fieldName;
        private String rejectedValue;
        private String errorCode;

        public JsonFieldError(String fieldName, String rejectedValue, String errorCode) {
            this.fieldName = fieldName;
            this.rejectedValue = rejectedValue;
            this.errorCode = errorCode;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getRejectedValue() {
            return rejectedValue;
        }

        public String getErrorCode() {
            return errorCode;
        }
    }

    public static JsonErrors FromJson(String jsonDoc) {
        Gson gson = new Gson();
        return gson.fromJson(jsonDoc, JsonErrors.class);
    }


}
