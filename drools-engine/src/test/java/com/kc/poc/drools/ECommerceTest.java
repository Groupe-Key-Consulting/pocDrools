package com.kc.poc.drools;

import com.kc.poc.drools.fact.Customer;
import com.kc.poc.drools.fact.Order;
import com.kc.poc.drools.service.ECommerceService;
import org.junit.jupiter.api.Test;
import org.kie.api.runtime.KieContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ECommerceTest {

    @Test
    public void customerPromotedToSilverAndGetNotified_WhenPurchase200AmountOrder() {
        // Given
        Config config = new Config();
        KieContainer kieContainer = config.createKieContainer();
        Order order = new Order();
        Customer customer = new Customer();
        ECommerceService eCommerceService = new ECommerceService(kieContainer);

        customer.setName("John");
        customer.setCategory("Bronze");

        order.setCustomer(customer);
        order.setAmount(688.0);


        System.out.println("\n----- Before Rules Execution -----");
        System.out.printf("Customer name: %s%n", order.getCustomer().getName());
        System.out.printf("Order amount: %s%n", order.getAmount());
        System.out.printf("%s's category: %s%n", order.getCustomer().getName(), order.getCustomer().getCategory());

        // When
        System.out.println("\n--------- Rules Execution ---------");
        String category;
        category = eCommerceService.makeAnOrder(customer, order);

        // Then
        assertNotNull(category);
        assertEquals("Gold", category);


        System.out.println("\n----- After Rules Execution -----");
        System.out.printf("%s's category: %s%n", order.getCustomer().getName(), order.getCustomer().getCategory());
    }
}
