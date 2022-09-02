package com.exasol.errorreporting;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceholderTest {
    @Test
    void TestParseParameterNameWithoutSwitches()
    {
        assertPlaceholder(Placeholder.parse("the_parameter").build(), "the_parameter", Quoting.AUTOMATIC);
    }

    private static void assertPlaceholder(final Placeholder placeholder, final String parameterName,
                                          final Quoting quoting) {
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo(parameterName)),
                () -> assertThat(placeholder.getQuoting(), equalTo(quoting))
        );
    }

    @Test
    void TestParseForcedUnquoted() {
        assertPlaceholder(Placeholder.parse("unquoted_parameter|u").build(), "unquoted_parameter", Quoting.UNQUOTED);
    }

    @Test
    void TestParseForcedSingleQuotes() {
        assertPlaceholder(Placeholder.parse("single-quoted_parameter|q").build(), "single-quoted_parameter",
                Quoting.SINGLE_QUOTES);
    }

    @Test
    void TestParseForcedDoubleQuotes() {
        assertPlaceholder(Placeholder.parse("double-quoted_parameter|d").build(), "double-quoted_parameter",
                Quoting.DOUBLE_QUOTES);
    }

    @Test
    void TestParseForcedUnquotedBackwardCompatibility() {
        assertPlaceholder(Placeholder.parse("unquoted_parameter|uq").build(), "unquoted_parameter", Quoting.UNQUOTED);
    }

    @Test
    void getStartIndex() {
        final Placeholder placeholder = Placeholder.parse("irrelevant").startIndex(13).build();
        assertThat(placeholder.getStartIndex(), equalTo(13));
    }

    @Test
    void getEndIndex() {
        final Placeholder placeholder = Placeholder.parse("irrelevant").endIndex(21).build();
        assertThat(placeholder.getEndIndex(), equalTo(21));
    }
}