package Services;

import Contracts.ServiceContract;
import Models.InventoryModel;
import Models.ItemModel;
import Repository.AttributeRepository;
import Repository.InventoryRepository;
import Repository.ItemAttributeRepository;
import Repository.ItemRepository;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.*;
import com.fasterxml.jackson.databind.JsonNode;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class ItemService extends CoreService implements ServiceContract {

    ItemRepository itemRepository;
    InventoryRepository inventoryRepository;
    ItemAttributeRepository itemAttrRepository;
    AttributeRepository attributeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public ItemService(
            ItemRepository itemRepository,
            InventoryRepository inventoryRepository,
            ItemAttributeRepository itemAttrRepository,
            AttributeRepository attributeRepository
    ) {
        this.itemRepository = itemRepository;
        this.inventoryRepository = inventoryRepository;
        this.itemAttrRepository = itemAttrRepository;
        this.attributeRepository = attributeRepository;
        getModelFields(ItemModel.class);
    }

    public JsonNode get(Map<String, String> filter) {
        try {
            Integer inventoryId = (filter.get("inventory_id") != null) ? Integer.parseInt(filter.get("inventory_id")) : null;
            String sort = (filter.get("sort") != null) ? filter.get("sort") : "id ";
            String dir = (filter.get("dir") != null) ? filter.get("dir") : "DESC";
            int pageRequest = (filter.get("page") != null) ? Integer.parseInt(filter.get("page")) : 0;
            Pageable number = PageRequest.of(pageRequest, 5, Sort.Direction.fromString(dir), "id");
            Page<ItemModel> items = itemRepository.findAllByFilter(inventoryId, number);
            ObjectNode node = mapper.createObjectNode();
            node.putPOJO("items", items.getContent());
            node.putPOJO("pages", items.getTotalPages());
            node.putPOJO("number", items.getTotalElements());
            return mapper.valueToTree(node);
        } catch (Exception e) {
            HashMap<String, String> error = new HashMap<String, String>() {{
                put("error", e.getMessage());
            }};
            return mapper.valueToTree(error);
        }
    }

    public JsonNode save(Map<String, JsonNode> jsonData) {
        try {
            InventoryModel inventory = inventoryRepository.findById(Integer.parseInt(jsonData.get("inventoryId").asText())).orElseThrow(() -> new Exception("Inventory not found!"));
            ItemModel item = mapper.convertValue(jsonData, ItemModel.class);
            itemRepository.save(item);
            entityManager.clear();
            item = itemRepository.findById(item.getId()).get();
            return mapper.valueToTree(item);
        } catch (Exception e) {
            HashMap<String, String> error = new HashMap<String, String>() {{
                put("error", e.getMessage());
            }};
            return mapper.valueToTree(error);
        }
    }

    public JsonNode update(Object id, Map<String, JsonNode> jsonData) {
        try {
            ItemModel item = itemRepository.findById((long) id).orElseThrow(() -> new Exception("Item not found!"));
            ObjectReader reader = mapper.readerForUpdating(item);
            ItemModel updatedItem = reader.readValue(mapper.writeValueAsString(jsonData));
            itemRepository.save(updatedItem);
            entityManager.clear();
            updatedItem = itemRepository.findById(updatedItem.getId()).get();
            return mapper.valueToTree(updatedItem);
        } catch (Exception e) {
            HashMap<String, String> error = new HashMap<String, String>() {{
                put("error", e.getMessage());
            }};
            return mapper.valueToTree(error);
        }
    }

    public JsonNode search(Map<String, String> searchData) {
        try {
            return mapper.valueToTree(itemRepository.findAllByItemid(searchData.get("itemid"), PageRequest.of(0, 5)));
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    public JsonNode remove(Object id) {
        try {
            itemRepository.deleteById((long) id);
            return mapper.valueToTree(id);
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    public JsonNode removeItemAttribute(Long id) {
        try {
            itemAttrRepository.deleteById(id);
            return mapper.valueToTree(id);
        } catch (Exception e) {
            return mapper.valueToTree(new HashMap<String, String>() {{
                put("error", "Invalid Data");
            }});
        }
    }

    public JsonNode count() {
        return mapper.valueToTree(itemRepository.countItems());
    }

}
