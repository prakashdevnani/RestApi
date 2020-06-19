package com.cardapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class CombinedData {
    @Getter @Setter private Long accNum;
    @Getter @Setter private String name;
    @Getter @Setter private String email;
    @Getter @Setter private String mobileNum;
    @Getter @Setter private double balance = 0.0d;
    @Getter @Setter private Long carNum=-1L;
    @Getter @Setter private int cvvNum=-1;
}
