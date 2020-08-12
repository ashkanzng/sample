package Contracts;

import Models.UserModel;

import java.util.List;
import java.util.Map;

public interface UserInterface {
    List<UserModel> getModel();
    Map<String,String> saveUser(Map<String,String> data);
    Boolean updateUser(Integer id, Map<String, String> data);
    Integer removeUser(Integer id);
}
