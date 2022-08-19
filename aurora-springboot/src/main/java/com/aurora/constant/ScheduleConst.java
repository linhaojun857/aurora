package com.aurora.constant;

/**
 * @author 花未眠
 * 任务调度通用常量
 */
public class ScheduleConst {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";

    /**
     * 执行目标key
     */
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";

    /**
     * 默认
     */
    public static final int MISFIRE_DEFAULT = 0;

    /**
     * 立即触发执行
     */
    public static final int MISFIRE_IGNORE_MISFIRES = 1;

    /**
     * 触发一次执行
     */
    public static final int MISFIRE_FIRE_AND_PROCEED = 2;

    /**
     * 不触发立即执行
     */
    public static final int MISFIRE_DO_NOTHING = 3;

    public enum Status {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 暂停
         */
        PAUSE(0);

        private final Integer value;

        private Status(Integer value) {
            this.value = value;
        }

        public Integer getValue() {
            return value;
        }
    }
}
