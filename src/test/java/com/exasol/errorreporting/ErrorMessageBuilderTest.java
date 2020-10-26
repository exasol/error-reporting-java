package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ErrorMessageBuilderTest {

    @Test
    void testMessage() {
        final String message = new ErrorMessageBuilder("T-ERJ-1").message("Test message.").toString();
        assertThat(message, equalTo("T-ERJ-1: Test message."));
    }

    @Test
    void testMessageWithParameter() {
        final String message = new ErrorMessageBuilder("T-ERJ-1")
                .message("Test message {{myPlaceholder}} and a number {{number}}.")
                .parameter("myPlaceholder", "myValue").parameter("number", 1, "a number").toString();
        assertThat(message, equalTo("T-ERJ-1: Test message 'myValue' and a number 1."));
    }

    @Test
    void testMessageWithUnquotedParameter() {
        final String message = new ErrorMessageBuilder("T-ERJ-1")
                .message("Test message {{myPlaceholder}} and a number {{number}}.")
                .unquotedParameter("myPlaceholder", "myValue").unquotedParameter("number", 1, "a number").toString();
        assertThat(message, equalTo("T-ERJ-1: Test message myValue and a number 1."));
    }

    @Test
    void testUnknownParameter() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("T-ERJ-1").message("{{unknown}}");
        final IllegalStateException exception = assertThrows(IllegalStateException.class, messageBuilder::toString);
        assertThat(exception.getMessage(), equalTo("F-ERJ-1: Unknown placeholder 'unknown'."));
    }

    @Test
    void testSingleMitigation() {
        final String message = new ErrorMessageBuilder("T-ERJ-1").message("Something went wrong.").mitigation("Fix it.")
                .toString();
        assertThat(message, equalTo("T-ERJ-1: Something went wrong. Fix it."));
    }

    @Test
    void testMitigations() {
        final String message = new ErrorMessageBuilder("T-ERJ-1").message("Something went wrong.").mitigation("Fix it.")
                .mitigation("Contact support.").toString();
        assertThat(message,
                equalTo("T-ERJ-1: Something went wrong. Known mitigations:\n* Fix it.\n* Contact support."));
    }
}