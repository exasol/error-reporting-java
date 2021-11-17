package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QuoterTest {
    static Stream<Arguments> getQuotingExamples() {
        return Stream.of(//
                Arguments.of(1, "1"), //
                Arguments.of(1L, "1"), //
                Arguments.of(1f, "1.0"), //
                Arguments.of(1d, "1.0"), //
                Arguments.of(Arrays.asList(1, "test"), "[1, 'test']"), //
                Arguments.of(new CustomObject(), "<CustomObject>"), //
                Arguments.of(null, "<null>"), //
                Arguments.of("test", "'test'")//
        );
    }

    @ParameterizedTest
    @MethodSource("getQuotingExamples")
    void testQuoting(final Object input, final String expectedOutput) {
        assertThat(Quoter.quoteObject(input), equalTo(expectedOutput));
    }

    private static class CustomObject {
        @Override
        public String toString() {
            return "<CustomObject>";
        }
    }
}