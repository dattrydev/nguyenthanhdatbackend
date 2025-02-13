package com.nguyenthanhdat.blog.services;

import com.nguyenthanhdat.blog.exceptions.TooManyLoginAttemptsException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Bandwidth;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    private static final int REQUEST_LIMIT = 5;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    private static final Duration REFRESH_DURATION = Duration.ofMinutes(1);

    public boolean tryConsume(String key) {
        Bucket bucket = buckets.computeIfAbsent(key, k -> createNewBucket());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        return probe.isConsumed();
    }

    public void recordFailedAttempt(String key) {
        failedAttempts.put(key, failedAttempts.getOrDefault(key, 0) + 1);
        if (failedAttempts.get(key) >= MAX_FAILED_ATTEMPTS) {
            throw new TooManyLoginAttemptsException("Too many failed login attempts. Please try again later.");
        }
    }

    public void resetFailedAttempts(String key) {
        failedAttempts.remove(key);
    }

    private Bucket createNewBucket() {
        Bandwidth limit = Bandwidth.simple(REQUEST_LIMIT, REFRESH_DURATION);
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
}
