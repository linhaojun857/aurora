package com.aurora.constant;

public class ScheduleConstant {

    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    public static final int MISFIRE_DEFAULT = 0;

    public static final int MISFIRE_IGNORE_MISFIRES = 1;

    public static final int MISFIRE_FIRE_AND_PROCEED = 2;

    public static final int MISFIRE_DO_NOTHING = 3;

    public enum Status {

        NORMAL(1),

        PAUSE(0);

        private final Integer value;

        Status(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
