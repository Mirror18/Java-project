package com.mirror.action.chain;

import java.math.BigDecimal;

/**
 * @author mirror
 */
public class DirectorHandler implements Handler{
    @Override
    public Boolean process(Request request) {
        if(request.getAmount().compareTo(BigDecimal.valueOf(10000))>0){
            return null;
        }
        return true;

    }
}
