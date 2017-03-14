package ngohoanglong.com.nowplaying.util;

import rx.Scheduler;


public class ThreadSchedulerImpl implements ThreadScheduler {
    private Scheduler observerOn;
    private Scheduler subscribeOn;

    public ThreadSchedulerImpl(Scheduler observerOn, Scheduler subscribeOn) {
        this.observerOn = observerOn;
        this.subscribeOn = subscribeOn;
    }

    @Override
    public Scheduler subscribeOn() {
        return subscribeOn;
    }

    @Override
    public Scheduler observeOn() {
        return observerOn;
    }
}
