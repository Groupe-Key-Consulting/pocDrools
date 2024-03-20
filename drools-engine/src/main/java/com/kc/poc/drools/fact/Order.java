package com.kc.poc.drools.fact;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Order {
    Customer customer;
    private Double amount;
}
