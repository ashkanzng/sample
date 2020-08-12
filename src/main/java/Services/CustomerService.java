package Services;

import Contracts.ServiceContract;
import Models.CustomerModel;
import Repository.CustomerRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service
public class CustomerService extends CoreService implements ServiceContract {

    CustomerRepository customerRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CustomerService(CustomerRepository customerRepository) {
        getModelFields(CustomerModel.class);
        this.customerRepository = customerRepository;
    }

    @Override
    public JsonNode get(Map<String, String> filter) {
        try {
            int pageRequest = (filter.get("page") != null) ? Integer.parseInt(filter.get("page")) : 0;
            String sort = (filter.get("sort") != null) ? filter.get("sort") : "id";
            String dir = (filter.get("dir") != null) ? filter.get("dir") : "DESC";
            Pageable number = PageRequest.of(pageRequest, 5, Sort.Direction.fromString(dir), sort);
            Page<CustomerModel> customers = customerRepository.findAllSort(number);
            ObjectNode node = mapper.createObjectNode();
            node.putPOJO("customers", customers.getContent());
            node.putPOJO("pages", customers.getTotalPages());
            node.putPOJO("number", customers.getTotalElements());
            return mapper.valueToTree(node);
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @Override
    public JsonNode save(Map<String, JsonNode> data) {
        try {
            validateParameters(data, new String[]{"customerid"});
            CustomerModel customer = mapper.convertValue(data, CustomerModel.class);
            customerRepository.save(customer);
            return mapper.valueToTree(customer);
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    public JsonNode update(Object id, Map<String, JsonNode> data) {
        try {
            CustomerModel customer = customerRepository.findById((int) id).orElseThrow(() -> new Exception("Customer doesn't exist"));
            ObjectReader reader = mapper.readerForUpdating(customer);
            CustomerModel updatedCustomer = reader.readValue(mapper.writeValueAsString(data));
            customerRepository.save(updatedCustomer);
            entityManager.clear();
            return mapper.valueToTree(customerRepository.findById(updatedCustomer.getId()).get());
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    @Override
    public JsonNode remove(Object id) {
        try {
            customerRepository.deleteById((int) id);
            return mapper.valueToTree(id);
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", e.getMessage());
            }});
        }
    }

    @Override
    public JsonNode search(Map<String, String> customerid) {
        try {
            return mapper.valueToTree(customerRepository.findAllByCustomerid(customerid.get("customerid"), PageRequest.of(0, 5)));
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    @Override
    public JsonNode count() {
        return mapper.valueToTree(customerRepository.findAll().size());
    }

}
