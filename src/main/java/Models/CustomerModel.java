package Models;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Entity
@Table(name = "customers")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class CustomerModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", columnDefinition = "varchar(64)", length = 64, nullable = false, unique = false)
    private String name;

    @Column(name = "customerid", columnDefinition = "varchar(64)", nullable = false, unique = true)
    private String customerid;

    @Column(name = "userid", columnDefinition = "int(11)", nullable = false)
    private Integer userid;

    @Type(type = "json")
    @Column(name = "contact_info", columnDefinition = "JSON", nullable = true, unique = false)
    private String contactInfo;

    @Type(type = "json")
    @Column(name = "general_info", columnDefinition = "JSON", nullable = true, unique = false)
    private String generalInfo;

    @Email
    @Pattern(regexp = "\\w+@\\w+\\.\\w+(,\\s*\\w+@\\w+\\.\\w+)*")
    @Column(name = "email", columnDefinition = "varchar(255)", nullable = true, unique = true, length = 512)
    private String email;

    @Column(name = "emailverified", columnDefinition = "boolean default false", unique = false , nullable = true)
    private Boolean emailVerified;

    @Type(type = "json")
    @Column(name = "sales_info", columnDefinition = "JSON", nullable = true, unique = false)
    private String salesInfo;

    @Type(type = "json")
    @Column(name = "payment_credit", columnDefinition = "JSON", nullable = true, unique = false)
    private String paymentCredit;

    @Type(type = "json")
    @Column(name = "addresses", columnDefinition = "JSON", nullable = true, unique = false)
    private String addresses;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created_at;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp updated_at;

    //@OneToMany(mappedBy="customer",cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    //@Where( clause = "address = 'Florida'") it works for where
    //private Set<AddressCustomerModel> addresses;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCustomerid() {
        return customerid;
    }

    public void setCustomerid(String customerid) {
        this.customerid = customerid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setContactInfo(JsonNode contactInfo) {
        this.contactInfo = JacksonUtil.toString(contactInfo);
    }

    public void setGeneralInfo(JsonNode generalInfo) {
        this.generalInfo = JacksonUtil.toString(generalInfo);
    }

    public void setSalesInfo(JsonNode salesInfo) {
        this.salesInfo = JacksonUtil.toString(salesInfo);
    }

    public void setPaymentCredit(JsonNode paymentCredit) {
        this.paymentCredit = JacksonUtil.toString(paymentCredit);
    }

    public void setAddresses(JsonNode addresses) {
        this.addresses = JacksonUtil.toString(addresses);
    }

    public JsonNode getGeneralInfo() {
        if (generalInfo != null) return JacksonUtil.toJsonNode(generalInfo);
        return null;
    }

    public JsonNode getContactInfo() {
        if (contactInfo != null) return JacksonUtil.toJsonNode(contactInfo);
        return null;
    }

    public JsonNode getSalesInfo() {
        if (salesInfo != null) return JacksonUtil.toJsonNode(salesInfo);
        return null;
    }

    public JsonNode getAddresses() {
        if (addresses != null) return JacksonUtil.toJsonNode(addresses);
        return null;
    }

    public JsonNode getPaymentCredit() {
        if (paymentCredit != null) return JacksonUtil.toJsonNode(paymentCredit);
        return null;
    }

    public String getCreated_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        if (created_at != null) return dateFormat.format(created_at);
        return null;
    }

    public String getUpdated_at() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        if (updated_at != null) return dateFormat.format(updated_at);
        return null;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    //public void setAddresses(Set<AddressCustomerModel> addresses) {
    //	if (this.addresses != null){
    //		Map<Integer,Integer> validAddressIdes = this.addresses.stream().collect(Collectors.toMap(AddressCustomerModel::getId, addr->addr.getId()));
    //		addresses.forEach(addr->{
    //			if (addr.getId() != null){
    //				if (validAddressIdes.get(addr.getId()) == null){
    //					throw new InputMismatchException("InputMismatchException for address id ");
    //				}
    //			}
    //		});
    //	}
    //	this.addresses = addresses.stream().peek(address -> address.setCustomer(this)).collect(Collectors.toSet());
    //}
}

