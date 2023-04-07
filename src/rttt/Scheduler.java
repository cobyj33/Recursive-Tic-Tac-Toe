package rttt;


import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

public class Scheduler {
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

    private Scheduler() {}

    public static <T> void scheduleAtFixedRate(Preferences<T> choices) {
        Future<?> task = scheduler.scheduleAtFixedRate(choices.getMainAction(), choices.getInitialDelayTime(), choices.getLoopTime(), choices.getTimeUnit());

        scheduler.schedule( () -> {
            task.cancel(false);
            choices.getOnCancel().run();
        }, choices.getCancelTime(), choices.getTimeUnit());
    }

    public static <T> void scheduleWithFixedDelay(Preferences<T> choices) {
        Future<?> task = scheduler.scheduleWithFixedDelay(choices.getMainAction(), choices.getInitialDelayTime(), choices.getLoopTime(), choices.getTimeUnit());

        scheduler.schedule( () -> {
            task.cancel(false);
            choices.getOnCancel().run();
        }, choices.getCancelTime(), choices.getTimeUnit());
    }

    public static void schedule(Runnable runnable, int delay, TimeUnit timeUnit) {
        scheduler.schedule(runnable, delay, timeUnit);
    }

    public static class Preferences<T> {
        private int startTime;
        private int initialDelayTime;
        private int loopTime;
        private int cancelTime;
        private Runnable mainAction;
        private Runnable onCancel;
        private TimeUnit timeUnit;
        private Predicate<T> condition;
        private T testObject;

        Preferences() {
            timeUnit = TimeUnit.MILLISECONDS;
            mainAction = () -> {};
            onCancel = () -> {};
            int dummyVal = 5;
            condition = (T) -> dummyVal == 5;
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

        public void setCondition(T testObject, Predicate<T> condition) {
            this.testObject = testObject;
            this.condition = condition;
        }

        public T getTestObject() {
            return testObject;
        }

        public Predicate<T> getCondition() {
            return condition;
        }
    }
}
