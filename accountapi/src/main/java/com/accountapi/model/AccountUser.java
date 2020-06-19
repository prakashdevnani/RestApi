package com.accountapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;*/

@Entity
@NoArgsConstructor
public class AccountUser {
    @Getter @Setter
    @Id
    @GeneratedValue
    private Long accNum;

    @Getter @Setter private String name;
    @Getter @Setter private String email;
    @Getter @Setter private String mobileNum;
    @Getter @Setter private double balance = 0.0d;


    public AccountUser(String name, String email, String mobileNum) {
        this.name = name;
        this.email = email;
        this.mobileNum = mobileNum;
    }
}
