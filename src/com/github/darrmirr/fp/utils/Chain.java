package com.github.darrmirr.fp.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

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
    static <A, R> ChainFunction<A, R> chainDefault(ChainFunction<A, R> defaultChain) {
        return defaultChain;
    }

    /**
     * Entry point to create Chain sequence without default Chain
     *
     * Default chain is added implicitly and return null if it is invoked
     *
     * @param function function that would be represent one element of chain
     * @param <A> function's input value type
     * @param <R> function's output value type
     * @return function that create contract for input chain function
     */
    static <A, R> ChainFunction.HandlerContract<A, R> chain(Function<A, R> function) {
        return Chain.<A, R>chainDefault(a -> null).chain(function);
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
    static <A> ChainConsumer<A> chainDefault(ChainConsumer<A> defaultChain) {
        return defaultChain;
    }

    /**
     * Entry point to create Chain sequence without default Chain
     *
     * Default chain is added implicitly and do nothing if it is invoked
     *
     * @param consumer function that would be represent one element of chain
     * @param <A> function's input value type
     * @return function that create contract for input chain function
     */
    static <A> ChainConsumer.HandlerContract<A> chain(ChainConsumer<A> consumer) {
        return Chain.<A>chainDefault(a -> {}).chain(consumer);
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
    interface ChainFunction<A, R> extends Function<A, R> {

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
                            .map(newChain -> (ChainFunction<A, R>) a -> newChain.apply(a).orElse(apply(a)))
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
            ChainFunction<A, R> responsibility(Contract<A> handlerContract);
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
    interface ChainConsumer<A> extends Consumer<A> {

        /**
         * Implementation of HandlerContract function
         *
         * This function makes two actions:
         *   - protect current handler function
         *   - invoke current handler function and next one
         *
         * @param handler to protect
         * @return HandlerContract function
         */
        default HandlerContract<A> chain(Consumer<A> handler) {
            return handlerContract ->
                    Optional.ofNullable(handlerContract)
                            .map(contract -> contract.obligate(handler))
                            .map(newChain -> (ChainConsumer<A>) a -> { newChain.accept(a); accept(a); })
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
            ChainConsumer<A> responsibility(Contract<A> handlerContract);
        }
    }
}
