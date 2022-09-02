package com.exasol.errorreporting;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlaceholderTest {
    @Test
    void TestParseParameterNameWithoutSwitches()
    {
        final Placeholder placeholder = Placeholder.parse("the_parameter").build();
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo("the_parameter")),
                () -> assertThat(placeholder.getQuoting(), equalTo(Quoting.AUTOMATIC))
        );
    }

    @Test
    void TestParseForcedUnquoted() {
        final Placeholder placeholder = Placeholder.parse("unquoted_parameter|u").build();
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo("unquoted_parameter")),
                () -> assertThat(placeholder.getQuoting(), equalTo(Quoting.UNQUOTED))
        );
    }

    @Test
    void TestParseForcedSingleQuotes() {
        final Placeholder placeholder = Placeholder.parse("single-quoted_parameter|q").build();
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo("single-quoted_parameter")),
                () -> assertThat(placeholder.getQuoting(), equalTo(Quoting.SINGLE_QUOTES))
        );
    }

    @Test
    void TestParseForcedDoubleQuotes() {
        final Placeholder placeholder = Placeholder.parse("double-quoted_parameter|d").build();
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo("double-quoted_parameter")),
                () -> assertThat(placeholder.getQuoting(), equalTo(Quoting.DOUBLE_QUOTES))
        );
    }

    @Test
    void TestParseForcedUnquotedBackwardCompatibility() {
        final Placeholder placeholder = Placeholder.parse("unquoted_parameter|uq").build();
        assertAll(
                () -> assertThat(placeholder.getReference(), equalTo("unquoted_parameter")),
                () -> assertThat(placeholder.getQuoting(), equalTo(Quoting.UNQUOTED))
        );
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