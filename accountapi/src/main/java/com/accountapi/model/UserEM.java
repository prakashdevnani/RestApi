package com.accountapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class UserEM {
    @Getter @Setter String mobileNum;
    @Getter @Setter String email;

    public UserEM(String mobileNum, String email){
        this.mobileNum=mobileNum;
        this.email=email;
    }
}
