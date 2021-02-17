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
    void testMessageWithNullParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("{{myPlaceholder}}")
                .parameter("myPlaceholder", null).toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: <null>"));
    }

    @Test
    void testMessageWithNullUnquotedParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("{{myPlaceholder}}")
                .unquotedParameter("myPlaceholder", null).toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: <null>"));
    }

    @Test
    void testMessageWithUnquotedParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1")
                .message("Test message {{myPlaceholder}} and a number {{number}}.")
                .unquotedParameter("myPlaceholder", "myValue").unquotedParameter("number", 1, "a number").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Test message myValue and a number 1."));
    }

    @Test
    void testMessageWithoutParameterName() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-ERJ-TEST-1").message("test {{}}");
        assertThat(messageBuilder.toString(), equalTo("E-ERJ-TEST-1: test UNKNOWN PLACEHOLDER('')"));
    }

    @Test
    void testMessageUnknownParameter() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-ERJ-TEST-1").message("test {{unknown}}");
        assertThat(messageBuilder.toString(), equalTo("E-ERJ-TEST-1: test UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testMessageParameterWithGroupReferenceChar() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-ERJ-TEST-1")
                .message("test {{PLACEHOLDER}}").unquotedParameter("PLACEHOLDER", "$2");
        assertThat(messageBuilder.toString(), equalTo("E-ERJ-TEST-1: test $2"));
    }

    @Test
    void testSingleMitigation() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .mitigation("Fix it.").toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Something went wrong. Fix it."));
    }

    @Test
    void testSingleMitigationWithParameter() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .mitigation("Delete line {{LINE_NR}}.").parameter("LINE_NR", 1).toString();
        assertThat(message, equalTo("E-ERJ-TEST-1: Something went wrong. Delete line 1."));
    }

    @Test
    void testMitigations() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .mitigation("Fix it.").mitigation("Contact support under {{SUPPORT_HOTLINE}}.")
                .parameter("SUPPORT_HOTLINE", "1234/56789").toString();
        assertThat(message, equalTo(
                "E-ERJ-TEST-1: Something went wrong. Known mitigations:\n* Fix it.\n* Contact support under '1234/56789'."));
    }

    @Test
    void testTicketMitigation() {
        final String message = new ErrorMessageBuilder("E-ERJ-TEST-1").message("Something went wrong.")
                .ticketMitigation().toString();
        assertThat(message, equalTo(
                "E-ERJ-TEST-1: Something went wrong. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }
}