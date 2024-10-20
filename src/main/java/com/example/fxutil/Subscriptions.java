package com.example.fxutil;

import org.reactfx.Subscription;

public class Subscriptions {
    private Subscription mainSubscription = Subscription.EMPTY;

    public Subscriptions() {

    }

    public Subscriptions(Subscription subscription) {
        addSubscription(subscription);
    }

    public void addSubscription(Subscription subscription) {
        mainSubscription = mainSubscription.and(subscription);
    }

    public void unsubscribeFromSubs() {
        mainSubscription.unsubscribe();
        mainSubscription = Subscription.EMPTY;
    }
}
