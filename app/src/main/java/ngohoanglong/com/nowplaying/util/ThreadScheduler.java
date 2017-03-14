package ngohoanglong.com.nowplaying.util;

import rx.Scheduler;

public interface ThreadScheduler {
    Scheduler subscribeOn();
    Scheduler observeOn();
}