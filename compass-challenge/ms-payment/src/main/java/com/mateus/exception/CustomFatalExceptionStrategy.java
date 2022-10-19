package com.mateus.exception;

import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;

public class CustomFatalExceptionStrategy extends ConditionalRejectingErrorHandler.DefaultExceptionStrategy {

    @Override
    public boolean isFatal(Throwable throwable){
        return !(throwable.getCause() instanceof BusinessException);
    }
}
