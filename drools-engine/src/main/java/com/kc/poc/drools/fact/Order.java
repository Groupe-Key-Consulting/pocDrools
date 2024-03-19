package com.kc.poc.drools.fact;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Order {
    Customer customer;
    private Double amount;
}
