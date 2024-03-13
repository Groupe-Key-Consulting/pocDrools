package com.kc.poc.drools.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class Participant {
    private String name;
    private int age;
    private int creditScore;
    private long salaireAnnuel;
    private long dettes;
    private long montantDuPret;

    public Participant(String name, int age, int creditScore, long salaireAnnuel, long dettes, long montantDuPret) {
        this.name = name;
        this.age = age;
        this.creditScore = creditScore;
        this.salaireAnnuel = salaireAnnuel;
        this.dettes = dettes;
        this.montantDuPret = montantDuPret;
    }

}
