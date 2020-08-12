package Controllers;

import Contracts.ServiceContract;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(
        allowCredentials = "false",
        origins = "*",
        allowedHeaders = "*",
        maxAge = 3600,
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE, RequestMethod.PUT}
)
@RestController
public class ItemController {

    @Autowired
    private ServiceContract itemService;

    @RequestMapping(value = "/item/get", method = RequestMethod.GET)
    public JsonNode getItem(
            @RequestParam(required = false, name = "p") String page,
            @RequestParam(required = false, name = "in_id") String inventory_id,
            @RequestParam(required = false, name = "sort") String sort,
            @RequestParam(required = false, name = "dir") String dir,
            Authentication authentication) {

        Map<String, String> filter = new HashMap<>();
        filter.put("page", page);
        filter.put("inventory_id", inventory_id);
        filter.put("sort", sort);
        filter.put("dir", dir);
        return itemService.get(filter);
    }

    @RequestMapping(value = "/item/add", method = RequestMethod.POST)
    public JsonNode createItem(@RequestBody Map<String, JsonNode> item, Authentication authentication) {
        return itemService.save(item);
    }

    @RequestMapping(value = "/item/update/{id}", method = RequestMethod.POST)
    public JsonNode updateItem(@PathVariable Long id, @RequestBody Map<String, JsonNode> item, Authentication authentication) {
        return itemService.update(id, item);
    }

    @RequestMapping(value = "/item/delete/{id}", method = RequestMethod.DELETE)
    public JsonNode removeItem(@PathVariable Long id) {
        return itemService.remove(id);
    }

    @RequestMapping(value = "/item/all", method = RequestMethod.GET)
    public JsonNode countOfItems() {
        return itemService.count();
    }

    @RequestMapping(value = "/item/search", method = RequestMethod.POST)
    public JsonNode findAllItemsByItemid(@RequestBody Map<String, String> search) {
        return itemService.search(search);
    }

}
