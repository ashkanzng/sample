package Services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.istack.Nullable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;


@Service
public abstract class CoreService {

    protected String[] fields;

    protected ObjectMapper mapper = new ObjectMapper();

    protected Object model;

    protected void setFields(String[] fields) {
        this.fields = fields;
    }

    protected void getModelFields(Class<?> model) {
        Field[] modelFields = model.getDeclaredFields();
        String[] result = new String[modelFields.length];
        for (int f = 0; f < modelFields.length; f++) {
            result[f] = modelFields[f].getName();
        }
        this.fields = result;
    }

    protected void validateParameters(Map<String, JsonNode> requestObject, @Nullable String[] requiredParameters) throws Exception {
        if (requiredParameters != null) {
            for (String req_p : requiredParameters) {
                if (!requestObject.containsKey(req_p)) {
                    throw new Exception("Missing Parameters -> " + req_p);
                }
            }
        }
        for (String key : requestObject.keySet()) {
            if (!Arrays.asList(fields).contains(key)) {
                throw new Exception("Invalid Parameters -> " + key);
            }
        }
    }
}
