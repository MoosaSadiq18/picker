package org.example.picker.ratelimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.Bucket;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimiterService {

    private final Map<String, Map<String,Bucket>> buckets = new ConcurrentHashMap<>();

    @Autowired
    @Qualifier("loginRateLimiter")
}
