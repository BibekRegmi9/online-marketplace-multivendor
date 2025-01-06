package com.bibek.response;

import com.bibek.model.Address;
import com.bibek.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
//@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String fullName;
    private String email;
    private String phoneNo;
    private String role;
    private List<Address> addressList;

    // Copy constructor with addressList
    public UserResponse(User user){
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.phoneNo = user.getMobile();
        this.role = user.getRole().name();
        this.addressList = user.getAddress() != null ? new ArrayList<>(user.getAddress()) : null;
    }

    // Constructor with addressList
    public UserResponse(String fullName, String email, String phoneNo, String role, List<Address> addressList) {
        this.fullName = fullName;
        this.email = email;
        this.phoneNo = phoneNo;
        this.role = role;
        this.addressList = addressList;
    }


}
