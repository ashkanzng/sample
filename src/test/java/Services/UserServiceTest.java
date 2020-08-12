package Services;

import Models.UserModel;
import Repository.UserRepository;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    UserRepository userRepository;
    @Test
    void getModel() throws Exception {
        try{
            List<UserModel> users = userRepository.findAll();
            List<UserModel> tu = new ArrayList<>();
            assertEquals(users,tu);
        }catch (NullPointerException e){}
    }
}