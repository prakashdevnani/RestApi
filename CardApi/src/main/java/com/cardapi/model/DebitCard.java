package com.cardapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class DebitCard {

    @Id
    @Getter @Setter private Long carNum=-1L;

    @Getter @Setter private Long accNum=-1L;
    @Getter @Setter private int cvvNum=-1;

}
