package com.ssafy.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserSecret extends User {
    private String pw;
    private String salt;

    /*
    UserSecret(User user){
        super(user.builder());
    }
    UserSecret(User user, String pw, String salt){
        super(user.builder());
        this.pw = pw;
        this.salt = salt;
    }
     */
}
