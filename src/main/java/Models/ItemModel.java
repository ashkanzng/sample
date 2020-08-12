package Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
import org.hibernate.annotations.*;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Null;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Set;


@Entity
@Table(name = "items",indexes ={
        @Index(name="itemid_index",columnList="itemid", unique = true)
    })
public class ItemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name", columnDefinition = "varchar(64)", nullable=true, unique=false)
    private String name;

    @Column(name="itemid", columnDefinition = "varchar(64)", nullable=false, unique=true)
    private String itemid;

    @Column(name="serial_number", columnDefinition = "varchar(64)", nullable=true, unique=false)
    private String serialNumber;

    @Column(name="qty_on_hand", columnDefinition="Decimal(10,2)", nullable=true, unique=false)
    private Double quantityOnHand;

    @Column(name="reorder_qty", columnDefinition="Decimal(10,2)", nullable=true, unique=false)
    private Double reorderQuantity;

    @Type(type = "json")
    @Column(name="details", columnDefinition = "JSON", nullable=true, unique=false)
    private String details;

//    @Column(name="description", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String description;
//
//    @Column(name="sku", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String sku;
//
//    @Column(name="description_for_sales", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String description_for_sales;
//
//    @Column(name="item_class", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String item_class;
//
//    @Column(name="activision", columnDefinition = "TINYINT(1)", nullable=true, unique=false)
//    private Integer activision;
//
//    @Column(name="item_type", columnDefinition = "varchar(11)", nullable=true, unique=false)
//    private String item_type;
//
//    @Column(name="retail", columnDefinition="Decimal(10,2)", nullable=true, unique=false)
//    private Double retail;
//
//    @Column(name="quote_infolder", columnDefinition="Decimal(10,2)", nullable=true, unique=false)
//    private Double quote_infolder;
//
//    @Column(name="stocking_um", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String stocking_um;
//
//    @Column(name="use_multi_packs", columnDefinition = "boolean default false", nullable=true, unique=false)
//    private Boolean use_multi_packs;
//
//    @Column(name="sales_account",columnDefinition = "integer default 11",nullable=true, unique=false)
//    private Integer sales_account;
//
//    @Column(name="invoice_account",columnDefinition = "integer default 11",nullable=true, unique=false)
//    private Integer invoice_account;
//
//    @Column(name="last_unit_cost", columnDefinition="Decimal(10,2)", nullable=true, unique=false)
//    private Double last_unit_cost;
//
//    @Column(name="cos_account",columnDefinition = "integer default 11",nullable=true, unique=false)
//    private Integer cos_account;
//
//    @Column(name="cost_method", columnDefinition = "varchar(11)", nullable=true, unique=false)
//    private String cost_method;
//
//    @Column(name="commission", columnDefinition = "boolean default false", nullable=true, unique=false)
//    private Boolean commission;

//    @Column(name="min_stock",columnDefinition = "integer default 11",nullable=true, unique=false)
//    private Integer min_stock;
//

//
//    @Column(name="location", columnDefinition = "varchar(64)", nullable=true, unique=false)
//    private String location;


    @Column(name="inventory_id",columnDefinition = "integer default 11",nullable=false)
    private Integer inventoryId;

    //@Formula(value = "(select JSON_OBJECT( 'name',inventories.name ,'id',inventories.id) from inventories where inventories.id = inventory_id)")
    @Formula(value = "(select inventories.name from inventories where inventories.id = inventory_id)")
    private String inventoryName;

    @OneToMany(mappedBy = "item" , cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
//    @OrderBy("id ASC")
    private Set<ItemAttributeModel> attributes;

    @CreationTimestamp
    @Column(name="created_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created_at;

    @UpdateTimestamp
    @Column(name="updated_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updated_at;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Double getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Double quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public Double getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(Double reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }

    public JsonNode getDetails() {
        if (details != null) return JacksonUtil.toJsonNode(details);
        return null;
    }

    public void setDetails(JsonNode details) {
        this.details = JacksonUtil.toString(details);
    }

    public Set<ItemAttributeModel> getAttributes() {
        return attributes;
    }

    public void setAttributes(Set<ItemAttributeModel> attributes) {
        attributes.forEach(attr -> attr.setItem(this));
        this.attributes = attributes;
    }

    public String getCreated_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        if (created_at != null) return dateFormat.format(created_at);
        return null;
    }

    public String getUpdated_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (updated_at != null)return dateFormat.format(updated_at);
        return null;
    }
}
