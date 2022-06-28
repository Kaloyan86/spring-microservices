package com.app.web.aop;

import com.app.web.annotations.TrackLatency;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    @Value(value = "${latencyThreshold}")
    private String latencyThreshold;


    @Around(value = "@annotation(trackLatency)")
    public Object trackLatency(ProceedingJoinPoint pjp, TrackLatency trackLatency) throws Throwable {

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object object = pjp.proceed();
        stopWatch.stop();

        long actualLatency = stopWatch.getLastTaskTimeMillis();
        long threshold = Integer.parseInt(latencyThreshold);

        if (actualLatency > threshold) {
            // IN reality - more complicated tracking.
            System.out.printf("WARN: WE ARE TOO SLOW - %s is executed for %d milliseconds\n", trackLatency.value(), actualLatency);
        } else {
            System.out.printf("INFO: We are OK - %s is executed for %d milliseconds\n", trackLatency.value(), actualLatency);
        }
        return object;
    }
}
