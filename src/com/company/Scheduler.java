package com.company;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private Scheduler() {}

    public static void scheduleAtFixedRate(Preferences choices) {
        Future<?> task = scheduler.scheduleAtFixedRate(choices.getMainAction(), choices.getInitialDelayTime(), choices.getLoopTime(), choices.getTimeUnit());

        scheduler.schedule( () -> {
            task.cancel(false);
            choices.getOnCancel().run();
        }, choices.getCancelTime(), choices.getTimeUnit());
    }

    public static void scheduleWithFixedDelay(Preferences choices) {
        Future<?> task = scheduler.scheduleWithFixedDelay(choices.getMainAction(), choices.getInitialDelayTime(), choices.getLoopTime(), choices.getTimeUnit());

        scheduler.schedule( () -> {
            task.cancel(false);
            choices.getOnCancel().run();
        }, choices.getCancelTime(), choices.getTimeUnit());
    }

    public static void schedule(Runnable runnable, int delay, TimeUnit timeUnit) {
        scheduler.schedule(runnable, delay, timeUnit);
    }

    public static class Preferences {
        private int startTime;
        private int initialDelayTime;
        private int loopTime;
        private int cancelTime;
        private Runnable mainAction;
        private Runnable onCancel;
        private TimeUnit timeUnit;

        Preferences() {
            timeUnit = TimeUnit.MILLISECONDS;
            mainAction = () -> {};
            onCancel = () -> {};
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getInitialDelayTime() {
            return initialDelayTime;
        }

        public void setInitialDelayTime(int initialDelayTime) {
            this.initialDelayTime = initialDelayTime;
        }

        public int getLoopTime() {
            return loopTime;
        }

        public void setLoopTime(int loopTime) {
            this.loopTime = loopTime;
        }

        public int getCancelTime() {
            return cancelTime;
        }

        public void setCancelTime(int cancelTime) {
            this.cancelTime = cancelTime;
        }

        public Runnable getMainAction() {
            return mainAction;
        }

        public void setMainAction(Runnable mainAction) {
            this.mainAction = mainAction;
        }

        public Runnable getOnCancel() {
            return onCancel;
        }

        public void setOnCancel(Runnable onCancel) {
            this.onCancel = onCancel;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public void setTimeUnit(TimeUnit timeUnit) {
            this.timeUnit = timeUnit;
        }
    }
}
