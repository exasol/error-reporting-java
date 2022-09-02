package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class QuoterTest {
    final static String SEP = File.separator;

    static Stream<Arguments> getAutoQuotingExamples() throws MalformedURLException, URISyntaxException {
        return Stream.of(//
                Arguments.of(1, "1"), //
                Arguments.of(1L, "1"), //
                Arguments.of(1f, "1.0"), //
                Arguments.of(1d, "1.0"), //
                Arguments.of(List.of(1, "test"), "[1, 'test']"), //
                Arguments.of(new CustomObject(), "<CustomObject>"), //
                Arguments.of(null, "<null>"), //
                Arguments.of("test", "'test'"), //
                Arguments.of(new File(SEP + "foo" + SEP + "baz.txt"), "'"+ SEP + "foo" + SEP + "baz.txt'"), //
                Arguments.of(Path.of("foo").resolve("bar"), "'foo" + SEP + "bar'"), //
                Arguments.of(new URL("https://example.org"), "'https://example.org'"), //
                Arguments.of(new URI("URN:ISBN:0-330-28700-1"), "'URN:ISBN:0-330-28700-1'")
        );
    }

    @ParameterizedTest
    @MethodSource("getAutoQuotingExamples")
    void testAutoQuoting(final Object input, final String expectedOutput) {
        assertThat(Quoter.quoteObject(input, Quoting.AUTOMATIC), equalTo(expectedOutput));
    }

    static Stream<Arguments> getUnquotedExamples() throws MalformedURLException, URISyntaxException {
        return Stream.of(//
                Arguments.of(1, "1"), //
                Arguments.of(1L, "1"), //
                Arguments.of(1f, "1.0"), //
                Arguments.of(1d, "1.0"), //
                Arguments.of(List.of(1, "test"), "[1, test]"), //
                Arguments.of(new CustomObject(), "<CustomObject>"), //
                Arguments.of(null, "<null>"), //
                Arguments.of("test", "test"), //
                Arguments.of(new File(SEP + "foo" + SEP + "baz.txt"), SEP + "foo" + SEP + "baz.txt"), //
                Arguments.of(Path.of("foo").resolve("bar"), "foo" + SEP + "bar"), //
                Arguments.of(new URL("https://example.org"), "https://example.org"), //
                Arguments.of(new URI("URN:ISBN:0-330-28700-1"), "URN:ISBN:0-330-28700-1")
        );
    }

    @ParameterizedTest
    @MethodSource("getUnquotedExamples")
    void testForcedUnquoted(final Object input, final String expectedOutput) {
        assertThat(Quoter.quoteObject(input, Quoting.UNQUOTED), equalTo(expectedOutput));
    }

    static Stream<Arguments> getSingleQuoteExamples() throws MalformedURLException, URISyntaxException {
        return Stream.of(//
                Arguments.of(1, "'1'"), //
                Arguments.of(1L, "'1'"), //
                Arguments.of(1f, "'1.0'"), //
                Arguments.of(1d, "'1.0'"), //
                Arguments.of(List.of(1, "test"), "['1', 'test']"), //
                Arguments.of(new CustomObject(), "'<CustomObject>'"), //
                Arguments.of(null, "<null>"), //
                Arguments.of("test", "'test'"), //
                Arguments.of(new File(SEP + "foo" + SEP + "baz.txt"), "'"+ SEP + "foo" + SEP + "baz.txt'"), //
                Arguments.of(Path.of("foo").resolve("bar"), "'foo" + SEP + "bar'"), //
                Arguments.of(new URL("https://example.org"), "'https://example.org'"), //
                Arguments.of(new URI("URN:ISBN:0-330-28700-1"), "'URN:ISBN:0-330-28700-1'")
        );
    }

    @ParameterizedTest
    @MethodSource("getSingleQuoteExamples")
    void testForcedSingleQuotes(final Object input, final String expectedOutput) {
        assertThat(Quoter.quoteObject(input, Quoting.SINGLE_QUOTES), equalTo(expectedOutput));
    }

    static Stream<Arguments> getDoubleQuoteExamples() throws MalformedURLException, URISyntaxException {
        return Stream.of(//
                Arguments.of(1, "\"1\""), //
                Arguments.of(1L, "\"1\""), //
                Arguments.of(1f, "\"1.0\""), //
                Arguments.of(1d, "\"1.0\""), //
                Arguments.of(List.of(1, "test"), "[\"1\", \"test\"]"), //
                Arguments.of(new CustomObject(), "\"<CustomObject>\""), //
                Arguments.of(null, "<null>"), //
                Arguments.of("test", "\"test\""), //
                Arguments.of(new File(SEP + "foo" + SEP + "baz.txt"), "\""+ SEP + "foo" + SEP + "baz.txt\""), //
                Arguments.of(Path.of("foo").resolve("bar"), "\"foo" + SEP + "bar\""), //
                Arguments.of(new URL("https://example.org"), "\"https://example.org\""), //
                Arguments.of(new URI("URN:ISBN:0-330-28700-1"), "\"URN:ISBN:0-330-28700-1\"")
        );
    }

    @ParameterizedTest
    @MethodSource("getDoubleQuoteExamples")
    void testForcedDoubleQuotes(final Object input, final String expectedOutput) {
        assertThat(Quoter.quoteObject(input, Quoting.DOUBLE_QUOTES), equalTo(expectedOutput));
    }

    private static class CustomObject {
        @Override
        public String toString() {
            return "<CustomObject>";
        }
    }
}