package Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import Models.CustomerModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CustomerRepository extends JpaRepository<CustomerModel, Integer> {

    @Query("SELECT customers FROM CustomerModel customers")
    Page<CustomerModel> findAllSort(Pageable pageable);

    @Query("SELECT customers FROM CustomerModel customers WHERE customers.customerid LIKE :customerid% ORDER BY customers.id DESC")
    List<CustomerModel> findAllByCustomerid (String customerid, Pageable pageable);

    // Searching Json address
    // select json_extract(addresses,"$[*].street1") from customers where json_extract(addresses,"$[*].street1") = json_array("11ABS");
    @Query("SELECT customer from CustomerModel customer where FUNCTION('JSON_EXTRACT',customer.addresses,'$[*].street1') = FUNCTION('JSON_ARRAY',:address)")
    List<CustomerModel> findByAddress(@Param("address") String address);

}
