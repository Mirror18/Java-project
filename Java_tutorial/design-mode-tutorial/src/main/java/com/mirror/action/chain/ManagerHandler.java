package com.mirror.action.chain;

import java.math.BigDecimal;

/**
 * @author mirror
 */
public class ManagerHandler implements Handler{
    @Override
    public Boolean process(Request request) {
        if(request.getAmount().compareTo(BigDecimal.valueOf(1000))>0){
            return null;
        }
        return !request.getName().equalsIgnoreCase("bob");

    }
}
