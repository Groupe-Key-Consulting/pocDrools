package com.kc.poc.drools.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
public class Taux implements Serializable {

    private String etatDuPret;
    private double tauxDePret;

    public Taux(String etatDuPret, double tauxDePret) {
        this.etatDuPret = etatDuPret;
        this.tauxDePret = tauxDePret;
    }

    public Taux() {
    }

    public String getEtatDuPret() {
        return etatDuPret;
    }

    public void setEtatDuPret(String etatDuPret) {
        this.etatDuPret = etatDuPret;
    }

    public double getTauxDePret() {
        return tauxDePret;
    }

    public void setTauxDePret(double tauxDePret) {
        this.tauxDePret = tauxDePret;
    }
}
