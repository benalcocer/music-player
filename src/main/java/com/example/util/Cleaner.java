package com.example.util;

public interface Cleaner {
    /**
     * Initialize subscriptions/listeners that will ultimately be cleaned up.
     */
    void initialize();
    /**
     * Run clean up routines.
     */
    void cleanUp();
}
