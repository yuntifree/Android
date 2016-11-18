package com.yunxingzh.wireless.wifi;

public enum WifiSignalLevel {
    NO_SIGNAL(0, "no signal"),
    POOR(1, "poor"),
    FAIR(2, "fair"),
    GOOD(3, "good"),
    EXCELLENT(4, "excellent");

    public final int level;
    public final String description;

    WifiSignalLevel(int level, String description) {
        this.level = level;
        this.description = description;
    }

    public static WifiSignalLevel from(final int level) {
        switch (level) {
            case 0:
                return NO_SIGNAL;
            case 1:
                return POOR;
            case 2:
                return FAIR;
            case 3:
                return GOOD;
            case 4:
                return EXCELLENT;
            default:
                return NO_SIGNAL;
        }
    }

    public static int getMaxLevel() {
        return 5;
    }

    @Override
    public String toString() {
        return "WifiSignalLevel{" + "level=" + level + ", description='" + description + '\'' + '}';
    }
}