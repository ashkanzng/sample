package Contracts;

import Models.CustomerInformation;
import Models.CustomerModel;
import com.fasterxml.jackson.databind.JsonNode;

public interface CustomerInterface extends ServiceContract {

    int findCustomerByUserId(int userid);
    JsonNode findCustomerByEmail(String email);
    CustomerModel findCustomerByEmailAsObject(String email);
    CustomerInformation getCustomerInformation(String email);
    void updateCustomerInformation(CustomerInformation customerInformation,Integer customerId);
}
