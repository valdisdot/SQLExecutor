package com.valdisdot.sqlexecutor.util;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * A container for encapsulating an action with its verb, callable, and consumer for execution handling.
 *
 * @param <T> the type of the result produced by the action.
 */
public class ActionContainer<T> {

    private final String actionVerb;
    private final Callable<T> callable;
    private final Consumer<Future<T>> actionExecutionConsumer;

    /**
     * Creates an {@code ActionContainer} that holds the action details, including:
     * - The descriptive verb for the action.
     * - A {@link Callable} that defines the action to be executed.
     * - A {@link Consumer} for handling the {@link Future} result of the callable execution.
     *
     * @param actionVerb              the descriptive verb representing the action.
     * @param callable                the callable action to be executed.
     * @param actionExecutionConsumer the consumer to process the result of the callable's execution.
     * @throws NullPointerException if any argument is {@code null}.
     */
    public ActionContainer(String actionVerb, Callable<T> callable, Consumer<Future<T>> actionExecutionConsumer) {
        this.actionVerb = Objects.requireNonNull(actionVerb, "Action verb cannot be null");
        this.callable = Objects.requireNonNull(callable, "Callable cannot be null");
        this.actionExecutionConsumer = Objects.requireNonNull(actionExecutionConsumer, "Consumer cannot be null");
    }

    /**
     * Retrieves the descriptive verb for the action.
     *
     * @return the action verb.
     */
    public String getActionVerb() {
        return actionVerb;
    }

    /**
     * Retrieves the {@link Callable} representing the action.
     *
     * @return the callable action.
     */
    public Callable<T> getCallable() {
        return callable;
    }

    /**
     * Retrieves the {@link Consumer} responsible for processing the {@link Future} result of the action.
     *
     * @return the consumer for the callable's result.
     */
    public Consumer<Future<T>> getActionExecutionConsumer() {
        return actionExecutionConsumer;
    }
}
