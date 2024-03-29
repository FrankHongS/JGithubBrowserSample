package com.frankhon.jgithubbrowsersample.util;

import android.os.SystemClock;
import android.util.ArrayMap;

import java.util.concurrent.TimeUnit;

/**
 * Created by Frank_Hon on 7/25/2019.
 * E-mail: v-shhong@microsoft.com
 */
public final class RateLimiter<KEY> {

    private ArrayMap<KEY, Long> timestamps;
    private long timeout;

    public RateLimiter(int timeout, TimeUnit timeUnit) {
        this.timestamps = new ArrayMap<>();
        this.timeout = timeUnit.toMillis(timeout);
    }

    public synchronized boolean shouldFetch(KEY key) {
        Long lastFetched = timestamps.get(key);
        Long now = now();

        if (lastFetched == null) {
            timestamps.put(key, now);
            return true;
        }

        if (now - lastFetched > timeout) {
            timestamps.put(key, now);
            return true;
        }

        return false;
    }

    private long now() {
        return SystemClock.uptimeMillis();
    }

    public synchronized void reset(KEY key) {
        timestamps.remove(key);
    }
}
