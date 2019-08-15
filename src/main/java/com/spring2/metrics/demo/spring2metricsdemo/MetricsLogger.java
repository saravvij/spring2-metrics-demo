package com.spring2.metrics.demo.spring2metricsdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Scheduled logging service for metrics data. The log data is for splunk to consume.
 */
@Component
@ConditionalOnProperty(prefix = "metrics.logging", name="enabled", havingValue="true", matchIfMissing = false)
public class MetricsLogger {

    @Autowired
    MetricsEndpoint metricsEndpoint;

    public Map <String, String> getMetrics() {
        Map<String, String> metricsMap = new HashMap<>();
        metricsMap.put("messageType", "ScheduledMetrics");
        System.out.println("\n\n");

        metricsEndpoint.listNames().getNames().stream().forEach(name -> {
            if (!metricsEndpoint.metric(name, null).getMeasurements().isEmpty()) {
                String val = metricsEndpoint.metric(name, null).getMeasurements().get(0).getValue().longValue() + "";
                metricsMap.put(name, val);
                System.out.println(name + ":" + val);
            }
            if (!metricsEndpoint.metric(name, null).getAvailableTags().isEmpty()) {
                MetricsEndpoint.AvailableTag availableTag = metricsEndpoint.metric(name, null).getAvailableTags().get(0);
                availableTag.getValues().stream().forEach(value -> {
                    List<String> tags = Arrays.asList(availableTag.getTag()+":"+value);
                    String tagValue = metricsEndpoint.metric(name, tags).getMeasurements().get(0).getValue().longValue()+"";
                    metricsMap.put(name+"."+value, tagValue);
                });
            }
        });
        return metricsMap;
    }


    @Scheduled(fixedDelayString="${metrics.logging.fixedDelay}")
    public void reportMetrics() {
        System.out.println("\n\n");
        Map<String, String> metrics = getMetrics();
        metrics.forEach((key, value) -> {
            System.out.println(key+":"+value);
        });
        System.out.println("\n\n");
    }

}
