package net.senmori.simpleprotect.util;

public enum TimeUtil {
    SECONDS {
        public long inTicks(int length) {
            return length * 20;
        }
    };


    public long inTicks(int length) {
        return 0L;
    }
}
