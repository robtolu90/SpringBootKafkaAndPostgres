package com.zombie.solution.metric;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ControllerAdviceExceptionHandler {

//    MeterRegistry registry;
//
//    @Autowired
//    public ControllerAdviceExceptionHandler(MeterRegistry registry){
//        this.registry = registry;
//    }
//
//    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public String handleInternalError(Exception e) {
//        countHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//        log.error("Returned HTTP Status 500 due to the following exception:", e);
//        return "Internal Server Error";
//    }
//
//    private void countHttpStatus(HttpStatus status){
//        Meter meter = r.meter(String.format("http.status.%d", status.value()));
//        meter.mark();
//    }

}