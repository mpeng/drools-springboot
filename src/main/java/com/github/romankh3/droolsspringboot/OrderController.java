package com.github.romankh3.droolsspringboot;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    private final KieSession kieSession;
    private FactHandle factHandle;

    public OrderController(KieSession kieSession) {
        this.kieSession = kieSession;
    }

    @PostMapping("/order")
    public Order processRulesForInsert(@RequestBody Order order) {
        factHandle = kieSession.insert(order);
        kieSession.fireAllRules();
        return order;
    }

    @PutMapping("/order")
    public Order processRulesForUpdate(@RequestBody Order order) {
        if (null != factHandle) {
            kieSession.update(factHandle, order);
            kieSession.fireAllRules();
        } else {
            factHandle = kieSession.insert(order);
            kieSession.fireAllRules();
        }
        return order;
    }

    @GetMapping("/order")
    public Order processRulesForGet() {
        System.out.println("processRulesForGet: " + factHandle );
        Order order = null;
        if (null != factHandle) {
            order = (Order)kieSession.getObject(factHandle);
            kieSession.fireAllRules();
        }
        return order;
    }

}
