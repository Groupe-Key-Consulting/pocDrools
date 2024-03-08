package com.kc.poc.drools;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Vehicule {

    LocalDate dateDebut;
    LocalDate dateFin;
    BigDecimal contractualNetValueStartYear;
    GrantType grantType;

    public BigDecimal getGrantPercentage(GrantType grantType) {
        BigDecimal grantPercentage;
        switch (grantType) {
            case OTHER:
                grantPercentage = BigDecimal.valueOf(10);
                break;
            case STIF:
                grantPercentage = BigDecimal.valueOf(20);
                break;
            default:
                grantPercentage = null;
                break;
        }
        return grantPercentage;
    }
    public enum GrantType {
        STIF,
        OTHER
    }

}



