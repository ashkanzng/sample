package Contracts;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.istack.Nullable;
import java.util.Map;
import java.util.Optional;

public interface ServiceContract {

    JsonNode get(@Nullable Map<String,String>filter);
    JsonNode save(Map<String,JsonNode> data);
    JsonNode update(Object id,Map<String,JsonNode> data);
    JsonNode remove(Object id);
    JsonNode search(Map<String,String>data);
    JsonNode count();
}
