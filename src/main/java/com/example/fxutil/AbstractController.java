package com.example.fxutil;

import com.example.util.Cleaner;

public abstract class AbstractController implements Cleaner {

    /**
     * Subscriptions object that manages any subscriptions for the Controller.
     */
    protected final Subscriptions subscriptions = new Subscriptions();
}
