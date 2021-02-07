package com.github.darrmirr.fp;

import static com.github.darrmirr.fp.utils.Chain.chainDefault;

/**
 * Example 1:
 *
 *     Chain java.util.function.Function
 *
 */
public interface Main {

    static void main(String[] args) {
        var chainFn = chainDefault((String input) -> input + " is executed by default chain")
                .chain(input -> input + " is executed by handler 1").responsibility("test 1"::equals)
                .chain(input -> input + " is executed by handler 2").responsibility("test 2"::equals)
                .chain(String::toUpperCase).responsibility("test 3"::equals);

        System.out.println("chain responsibility output : " + chainFn.apply("test 2"));
    }
}
