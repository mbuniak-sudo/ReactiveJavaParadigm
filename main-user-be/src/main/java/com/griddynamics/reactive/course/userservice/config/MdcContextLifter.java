package com.griddynamics.reactive.course.userservice.config;

import lombok.RequiredArgsConstructor;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

@RequiredArgsConstructor
public class MdcContextLifter<T> implements CoreSubscriber<T> {

    private final CoreSubscriber<T> delegate;


    @Override
    public Context currentContext() {
        return delegate.currentContext();
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        delegate.onSubscribe(subscription);
    }

    @Override
    public void onNext(T value) {
        try {
            copyToMdc(currentContext());
            delegate.onNext(value);
        } finally {
            MDC.remove(RequestIdFilter.REQUEST_ID);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        try {
            copyToMdc(currentContext());
            delegate.onError(throwable);
        } finally {
            MDC.remove(RequestIdFilter.REQUEST_ID);
        }
    }

    @Override
    public void onComplete() {
        try {
            copyToMdc(currentContext());
            delegate.onComplete();
        } finally {
            MDC.remove(RequestIdFilter.REQUEST_ID);
        }
    }


    private void copyToMdc(Context context) {
        if (context.hasKey(RequestIdFilter.REQUEST_ID)) {
            MDC.put(
                    RequestIdFilter.REQUEST_ID,
                    context.get(RequestIdFilter.REQUEST_ID)
            );
        }
    }
}
