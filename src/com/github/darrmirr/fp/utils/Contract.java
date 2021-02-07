package com.github.darrmirr.fp.utils;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * High order function represent contract for function's input value
 * @param <T> input value class
 */
@FunctionalInterface
public interface Contract<T> extends Predicate<T> {

    /**
     * Method to create new instance chainDefault Contract function
     *
     * @param contract predicate function that would be evaluate whether input value conform to contract or not
     * @param <T> parameter chainDefault input value
     * @return contract function
     */
    static <T> Contract<T> of(Predicate<T> contract) {
        return contract::test;
    }

    @Override
    default Contract<T> and(Predicate<? super T> other) {
        return Contract.of(Predicate.super.and(other));
    }

    @Override
    default Contract<T> negate() {
        return Contract.of(Predicate.super.negate());
    }

    @Override
    default Contract<T> or(Predicate<? super T> other) {
        return Contract.of(Predicate.super.or(other));
    }

    /**
     * Obligate (Wrap) function by contract
     *
     * NPE-free version
     *
     * @param function function to wrap
     * @param <R> function's output value class
     * @return function wrapped by contract
     */
    default <R> Function<T, Optional<R>> obligate(Function<? super T, R> function) {
        return input -> Optional
                .ofNullable(function)
                .filter(func -> test(input))
                .map(func -> func.apply(input));
    }

    /**
     * Obligate (Wrap) function by contract and then flat
     *
     * NPE-free version
     *
     * @param function function to wrap
     * @param <R> function's output value class
     * @return function wrapped by contract
     */
    default <R> Function<T, Optional<R>> obligateAndFlat(Function<? super T, Optional<R>> function) {
        return input -> Optional
                .ofNullable(function)
                .filter(func -> test(input))
                .flatMap(func -> func.apply(input));
    }

    /**
     * Obligate (Wrap) consumer function by contract
     *
     * NPE-free version
     *
     * @param consumer function to wrap
     * @return function wrapped by contract
     */
    default Consumer<T> obligate(Consumer<? super T> consumer) {
        return input -> Optional
                .ofNullable(consumer)
                .filter(func -> test(input))
                .ifPresent(func -> func.accept(input));
    }
}
