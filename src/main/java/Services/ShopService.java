package Services;

import Contracts.ItemInterface;
import Models.CategoryModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShopService extends CoreService {

    @Autowired
    CategoryService categoryService;

    @Autowired
    ItemInterface itemService;

    public JsonNode findFromTo(@Nullable Integer offset, @Nullable Integer limit) {
        try {
            offset = (offset != null) ? (Math.max(offset, 1) - 1) : 0;
            limit = (limit != null && limit > 0 && limit < 240) ? limit : 24;
            List<String> items = itemService.getLatestItems(offset, limit);
            JsonNode nodes = mapper.readTree(items.toString());
            return mapper.readTree(items.toString());
        } catch (JsonProcessingException jsonProcessingException) {
            return mapper.valueToTree(jsonProcessingException.getMessage());
        }
    }

    public JsonNode getItemsByParameter(@Nullable String itemid, @Nullable Integer categoryId, @Nullable Integer offset, @Nullable Integer limit) {
        try {
            ObjectNode result = mapper.createObjectNode();
            offset = (offset != null) ? (Math.max(offset, 1) - 1) : 0;
            limit = (limit != null && limit > 0 && limit < 240) ? limit : 24;
            int totalPages = (int) Math.ceil((double) itemService.totalItems(categoryId) / limit);
            List<String> items = itemService.getItemsByParameter(itemid, categoryId, (offset * limit), limit);
            JsonNode itemsJsonData = mapper.readTree(items.toString());

            List<CategoryModel> categoryBreadCroumb = new ArrayList<>();
            Map<Integer, CategoryModel> categoryMap = new HashMap<>();
            List<CategoryModel> categoryList = new ArrayList<>();
            Integer catid = (categoryId == null) ? 0 : categoryId;

            this.getCategory(catid, categoryList, categoryMap);
            JsonNode categoryTreeMenu = this.createTreeCategory(categoryMap);
            this.createBreadCroumb(catid, categoryMap, categoryBreadCroumb, 0);

            result.set("items", itemsJsonData);
            result.set("categories", mapper.convertValue(categoryMap, JsonNode.class));
            result.set("breadCroumbs", mapper.convertValue(categoryBreadCroumb, JsonNode.class));
            result.set("pagination", mapper.convertValue(this.createPagination(offset, totalPages), JsonNode.class));
            result.set("currentPage", mapper.convertValue(offset, JsonNode.class));
            result.set("totalPages", mapper.convertValue(totalPages, JsonNode.class));
            result.set("categoryTreeMenu", categoryTreeMenu);

            return result;
        } catch (JsonProcessingException jsonProcessingException) {
            return mapper.valueToTree(jsonProcessingException.getMessage());
        }
    }

    public void getCategory(Integer categoryId, List<CategoryModel> categoryList, Map<Integer, CategoryModel> categoryMap) {
        categoryService.getAsList().forEach(category -> {
            categoryMap.put(category.getId(), category);
            if (category.getParentId().equals(categoryId)) {
                categoryList.add(category);
            }
        });
    }

    public void addToCart(String itemid, short count,String img)
    {
        System.out.println(itemid);
        System.out.println(img);
        System.out.println(count);
    }

    /**
     * Create three menu for Jstree
     *
     * @return
     */
    JsonNode createTreeCategory(Map<Integer, CategoryModel> categoryMap) {
        try {
            class CategoryNode {
                public Integer id;
                public String name;
                public String label;
                public List<CategoryNode> children = new ArrayList<>();
            }
            Map<Integer, CategoryNode> root = new HashMap<>();
            categoryMap.keySet().forEach(key -> {
                if (root.get(key) == null) {
                    CategoryNode node = new CategoryNode();
                    node.name = categoryMap.get(key).getName();
                    node.label = categoryMap.get(key).getName();
                    node.id = categoryMap.get(key).getId();
                    root.put(key, node);
                }
                if (root.get(categoryMap.get(key).getParentId()) == null) {
                    CategoryNode node = new CategoryNode();
                    if (categoryMap.get(categoryMap.get(key).getParentId()) != null) {
                        node.name = categoryMap.get(categoryMap.get(key).getParentId()).getName();
                        node.label = categoryMap.get(categoryMap.get(key).getParentId()).getName();
                        node.id = categoryMap.get(categoryMap.get(key).getParentId()).getId();
                    }
                    node.children.add(root.get(key));
                    root.put(categoryMap.get(key).getParentId(), node);
                } else {
                    root.get(categoryMap.get(key).getParentId()).children.add(root.get(key));
                }
            });
            JsonNode tree = mapper.valueToTree(root);
            if (tree.get("0") != null) {
                return tree.at("/0/children");
            }
            return null;
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * @param categoryId
     * @param categoryMap         => get categories from category service as Map<Integer, CategoryModel>
     * @param categoryBreadCroumb
     * @param loop
     */
    void createBreadCroumb(Integer categoryId, Map<Integer, CategoryModel> categoryMap, List<CategoryModel> categoryBreadCroumb, int loop) {
        if (categoryMap.get(categoryId) != null && loop < categoryMap.size()) {
            loop += 1;
            categoryBreadCroumb.add(0, categoryMap.get(categoryId));
            int parentId = categoryMap.get(categoryId).getParentId();
            if (categoryMap.get(parentId) != null) {
                createBreadCroumb(categoryMap.get(parentId).getId(), categoryMap, categoryBreadCroumb, loop);
            }
        }
    }

    /**
     * @param currentPage
     * @param totalPages
     * @return
     */
    int[] createPagination(int currentPage, int totalPages) {
        if (totalPages == 0) {
            return new int[1];
        }
        int[] pagination = new int[5];
        int current_page = Math.max(currentPage, 2);
        int steps;
        if (current_page < 3) {
            steps = 5;
        } else if (current_page > totalPages - 2) {
            steps = 5;
        } else {
            steps = current_page + 2;
        }
        int index = 0;
        for (int i = (current_page - 2); i <= steps; i++) {
            if (i > 0 && i <= totalPages) {
                pagination[index] = i;
                index++;
            }
        }
        return pagination;
    }

}