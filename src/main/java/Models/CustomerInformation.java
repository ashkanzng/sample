package Models;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter
public class CustomerInformation {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String[] phone;
    private String city;
    private String zip;
    private String state;
    private String recipient;
    private List<String> addresses = new ArrayList<>();
    
}

