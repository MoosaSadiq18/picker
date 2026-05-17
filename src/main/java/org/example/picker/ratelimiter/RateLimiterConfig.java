package org.example.picker.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean("loginRateLimiter")
    public Bucket loginRateLimiter(){
        return Bucket.builder()
                .addLimit(limit -> limit
                        .capacity(10000)
                        .refillIntervally(10000,Duration.ofMinutes(1)))
                .build();
    }

}
