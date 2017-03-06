package com.max.algs.patterns.factory;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;


public class PizzaShop {

    public Pizza order(String type) {
        checkNotNull(type);

        Pizza pizza = null;
        if ("mario".equals(type)) {
            pizza = new MarioPizza();
        }
        else {
            checkArgument(false, "Unknown pizza type '%s'", type);
        }

        pizza.prepare();
        pizza.cook();
        pizza.pack();

        return pizza;
    }


}
