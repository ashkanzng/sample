package Repository;

import Models.ItemModel;
import com.fasterxml.jackson.databind.JsonNode;
import org.json.JSONArray;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel,Long> {

    @Query("SELECT item FROM ItemModel item WHERE (:inventoryId is null OR item.inventoryId = :inventoryId) ")
    Page<ItemModel> findAllByFilter(@Param("inventoryId") Integer inventoryId, Pageable pageable);

    @Query("SELECT item FROM ItemModel item WHERE FUNCTION('JSON_EXTRACT',item.details,'$.location') = 'Tampa' ")
    List<ItemModel> findItemByLocation(@Param("location") String location);

    @Query(value = "SELECT count(item.id) as total FROM ItemModel item WHERE (:categoryId is null OR item.categoryId = :categoryId) ")
    long countItems(@Param("categoryId") Integer categoryId);

    Optional<ItemModel> findByItemid(String itemid);

    @Query("SELECT item FROM ItemModel item WHERE item.itemid LIKE :itemid% ORDER BY item.id DESC")
    List<ItemModel> findAllByItemid(String itemid,Pageable pageable);

    @Query("SELECT item FROM ItemModel item ORDER BY item.id DESC")
    Page<ItemModel> findAllSort(Pageable pageable);

    @Query(value = "SELECT JSON_OBJECT('id',id,'itemid',itemid,'details',details,'name',name,'metadata',metadata)  FROM items ORDER BY id DESC LIMIT :offset,:limit" ,nativeQuery = true)
    List<String> findLatestItems(@Param("offset") int offset, @Param("limit") int limit);

    @Query(value = "SELECT JSON_OBJECT('id',id,'itemid',itemid,'details',details,'name',name,'metadata',metadata,'category_id',category_id) " +
            "FROM items WHERE (:itemid is null or itemid = :itemid) and (:categoryId is null or category_id = :categoryId) ORDER BY id DESC LIMIT :offset,:limit" ,nativeQuery = true)
    List<String> findItemsByParameter(@Param("itemid") String itemid, @Param("categoryId") Integer categoryId,
            @Param("offset") int offset,
            @Param("limit") int limit);
    /*
        #MySql query get parrent with all childs relations using this query
        1- select id,itemid,(select GROUP_CONCAT(CONCAT(item_attribute.id,",",item_attribute.item_id)) from item_attribute where item_attribute.item_id = items.id )  from items where id = 1131;
        2- select id,itemid,(select GROUP_CONCAT(JSON_OBJECT('id',item_attribute.id,'itemid',item_attribute.item_id)) from item_attribute where item_attribute.item_id = items.id )  from items where id = 1131;
    */
}

