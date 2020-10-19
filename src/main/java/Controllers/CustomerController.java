package Controllers;

import Contracts.CustomerInterface;
import Contracts.ServiceContract;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin(
        allowCredentials = "false",
        origins = "*",
        allowedHeaders = "*",
        maxAge = 3600,
        methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.DELETE,RequestMethod.PUT}
)
@RestController
public class CustomerController {

    @Autowired
    CustomerInterface customerService;

    @RequestMapping(value = "/api/customer/get",method = RequestMethod.GET)
    public JsonNode getCustomers(
            @RequestParam(required = false,name = "p") String page,
            @RequestParam(required = false,name = "sort") String sort,
            @RequestParam(required = false,name = "dir") String dir)
    {
        Map<String,String> filter = new HashMap<>();
        filter.put("page",page);
        filter.put("sort",sort);
        filter.put("dir",dir);
        return customerService.get(filter);
    }

    @RequestMapping(value = "/api/customer/add",method = RequestMethod.POST)
    public JsonNode saveCustomer(@RequestBody Map<String ,JsonNode> customer , Authentication authentication) {
        return customerService.save(customer);
    }

    @RequestMapping(value = "/api/customer/update/{id}", method = RequestMethod.POST)
    public JsonNode updateCustomer(@PathVariable Integer id,@RequestBody Map<String, JsonNode> customer , Authentication authentication) {
        return customerService.update(id,customer);
    }

    @RequestMapping(value = "/api/customer/delete/{id}",method = RequestMethod.DELETE)
    public JsonNode removeCustomer(@PathVariable int id){
        return customerService.remove(id);
    }

    @RequestMapping(value = "/api/customer/search/customerid",method = RequestMethod.POST)
    public JsonNode findAllByCustomerid(@RequestBody Map<String ,String> customerid , Authentication authentication) {
        return customerService.search(customerid);
    }

    @RequestMapping(value = "/api/customer/search/email",method = RequestMethod.POST,consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public JsonNode findAllByCustomerEmail(@RequestParam String email , Authentication authentication) {
        return customerService.findCustomerByEmail(email);
    }

    @RequestMapping(value = "/api/customer/all" ,method = RequestMethod.GET)
    public JsonNode countOfCustomers(){
        return customerService.count();
    }
}
