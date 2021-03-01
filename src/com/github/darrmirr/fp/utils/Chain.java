package com.github.darrmirr.fp.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * It is implementation of Chain of Responsibility pattern at functional way
 *
 * Top level interface hides low level logic of pattern and direct method invocation to required way
 *
 * CAUTION!
 * Chain is built as LIFO queue (Last Input First Output)
 */
public interface Chain {

    /**
     * Entry point to create Chain sequence with default Chain
     *
     * Default chain is always last chain at sequence
     *
     * @param defaultChain java.util.function.Function instance that would be represent default chain
     * @param <A> function's input value type
     * @param <R> function's output value type
     * @return Chain that represent java.util.function.Function
     */
    static <A, R> HandlerFunction<A, R> chainDefault(HandlerFunction<A, R> defaultChain) {
        return defaultChain;
    }

    /**
     * Entry point to create Chain sequence without default Chain
     *
     * Default chain is added implicitly and return null if it is invoked
     *
     * @param handler function that would be represent one element of chain
     * @param <A> function's input value type
     * @param <R> function's output value type
     * @return function that create contract for input chain function
     */
    static <A, R> HandlerFunction.HandlerContract<A, R> chain(Function<A, R> handler) {
        return Chain.<A, R>chainDefault(a -> null).chain(handler);
    }

    /**
     * Entry point to create Chain sequence with default Chain
     *
     * Default chain is always last chain at sequence
     *
     * @param defaultChain java.util.function.Consumer instance that would be represent default chain
     * @param <A> function's input value type
     * @return Chain that represent java.util.function.Consumer
     */
    static <A> HandlerConsumer<A> chainDefault(HandlerConsumer<A> defaultChain) {
        return defaultChain;
    }

    /**
     * Entry point to create Chain sequence without default Chain
     *
     * Default chain is added implicitly and do nothing if it is invoked
     *
     * @param handler function that would be represent one element of chain
     * @param <A> function's input value type
     * @return function that create contract for input chain function
     */
    static <A> HandlerConsumer.HandlerContract<A> chain(HandlerConsumer<A> handler) {
        return Chain.<A>chainDefault(a -> {}).chain(handler);
    }

    /**
     * Handler at structure of Chain of Responsibility pattern
     *
     * Handler is invoked if its responsibility evaluates true
     * Rest handlers at chain sequence are not invoked if one of the handler is invoked
     *
     * @param <A> function's input value type
     * @param <R> function's output value type
     */
    @FunctionalInterface
    interface HandlerFunction<A, R> extends Function<A, R> {

        /**
         * Implementation of HandlerContract function
         *
         * This function makes two actions:
         *   - protect current handler function
         *   - invoke current handler function and next one if current contract evaluates false
         *
         * @param handler to protect
         * @return HandlerContract function
         */
        default HandlerContract<A, R> chain(Function<A, R> handler) {
            return handlerContract ->
                    Optional.ofNullable(handlerContract)
                            .map(contract -> contract.obligate(handler))
                            .map(newChain -> (HandlerFunction<A, R>) a -> newChain.apply(a).orElseGet(() -> apply(a)))
                            .orElse(this);
        }

        /**
         * Contract function
         *
         * This function make decision to allow or disallow function invocation that stay under contract protection
         *
         * @param <A> function's input value type
         * @param <R> function's output value type
         */
        @FunctionalInterface
        interface HandlerContract<A, R> {
            HandlerFunction<A, R> responsibility(Contract<A> handlerContract);
        }
    }

    /**
     * Handler at structure of Chain of Responsibility pattern
     *
     * Handler is invoked if its responsibility evaluates true
     * Rest handlers at chain sequence are always invoked
     *
     * @param <A> function's input value type
     */
    @FunctionalInterface
    interface HandlerConsumer<A> extends Consumer<A> {

        /**
         * Implementation of HandlerContract function
         *
         * This function makes two actions:
         *   - protect current handler function
         *   - invoke current handler function and next one
         *
         * @param handlerConsumer to protect
         * @return HandlerContract function
         */
        default HandlerContract<A> chain(Consumer<A> handlerConsumer) {
            return handlerContract ->
                    Optional.ofNullable(handlerConsumer)
                            .filter(nextHandler -> handlerContract != null)
                            .map(nextHandler -> (HandlerConsumer<A>) a -> { if(handlerContract.test(a)) nextHandler.accept(a); else accept(a); })
                            .orElse(this);
        }

        /**
         * Contract function
         *
         * This function make decision to allow or disallow function invocation that stay under contract protection
         *
         * @param <A> function's input value type
         */
        @FunctionalInterface
        interface HandlerContract<A> {
            HandlerConsumer<A> responsibility(Contract<A> handlerContract);
        }
    }
}
