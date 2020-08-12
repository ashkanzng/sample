package Repository;

import Models.ItemModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel,Long> {


    @Query("SELECT item FROM ItemModel item WHERE (:inventoryId is null OR item.inventoryId = :inventoryId) ")
    Page<ItemModel> findAllByFilter(@Param("inventoryId") Integer inventoryId, Pageable pageable);

    @Query("SELECT item FROM ItemModel item WHERE FUNCTION('JSON_EXTRACT',item.details,'$.location') = 'Tampa' ")
    List<ItemModel> findItemByLocation(@Param("location") String location);


    @Query(value = "SELECT count(item.id) as total FROM ItemModel item")
    long countItems();

    Optional<ItemModel> findByItemid(String itemid);

    @Query("SELECT item FROM ItemModel item WHERE item.itemid LIKE :itemid% ORDER BY item.id DESC")
    List<ItemModel> findAllByItemid(String itemid,Pageable pageable);

    @Query("SELECT item FROM ItemModel item ORDER BY item.id DESC")
    Page<ItemModel> findAllSort(Pageable pageable);

}
