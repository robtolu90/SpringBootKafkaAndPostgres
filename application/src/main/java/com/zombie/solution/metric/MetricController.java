package com.zombie.solution.metric;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricController {

    @Autowired
    MeterRegistry registry;

    @GetMapping("/metrics")
    StringBuilder metrics(){
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println("Size of registry is" + registry.getMeters().size());
        StringBuilder metrics = new StringBuilder();
        for(Meter meter: registry.getMeters()) {
            metrics.append("id: "+meter.getId() + "value" + meter.measure());
            System.out.println("id: "+meter.getId() + "value" + meter.measure());
        }
        System.out.println("##########################################################################");

        return metrics;
    }
}
