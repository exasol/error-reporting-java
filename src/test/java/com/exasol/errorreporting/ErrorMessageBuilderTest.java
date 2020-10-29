package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class ErrorMessageBuilderTest {

    @AfterEach
    void afterEach() {
        final ClassLoader loader = ClassLoader.getSystemClassLoader();
        loader.clearAssertionStatus();
    }

    @Test
    void testMessage() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Test message.").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Test message."));
    }

    @Test
    void testMessageWithParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1")
                .message("Test message {{myPlaceholder}} and a number {{number}}.")
                .parameter("myPlaceholder", "myValue").parameter("number", 1, "a number").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Test message 'myValue' and a number 1."));
    }

    @Test
    void testMessageWithUnquotedParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1")
                .message("Test message {{myPlaceholder}} and a number {{number}}.")
                .unquotedParameter("myPlaceholder", "myValue").unquotedParameter("number", 1, "a number").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Test message myValue and a number 1."));
    }

    @Test
    void testUnknownParameter() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-ERJ-TEST-1").message("test {{unknown}}");
        assertThat(messageBuilder.toString(), equalTo("E-ERJ-TEST-1: test UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testSingleMitigation() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .mitigation("Fix it.").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Something went wrong. Fix it."));
    }

    @Test
    void testMitigations() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .mitigation("Fix it.").mitigation("Contact support.").toString();
        assertThat(message,
                equalTo("E-ERJ-TEST-1: Something went wrong. Known mitigations:\n* Fix it.\n* Contact support."));
    }
}