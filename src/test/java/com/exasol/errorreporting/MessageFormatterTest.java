package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class MessageFormatterTest {

    @Test
    void testFormat() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Test message.").toString();
        assertThat(message, equalTo("ERROR-CODE: Test message."));
    }

    @Test
    void testFormatSingleQuotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {parameterName}.", "value")
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value'."));
    }

    @Test
    void testFormatQuotedMultipleParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName1} and {parameterName2}.", "value", 1).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value' and '1'."));
    }

    @Test
    void testFormatQuoteParameterWithoutName() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {}.", "value").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value'."));
    }

    @Test
    void testFormatQuotedParameterWithoutValue() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {unknown}").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testFormatQuotedParameterWithGroupReferenceChar() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName}.", "$2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with '$2'."));
    }

    @Test
    void testFormatSingleNullQuotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {parameterName1}.", null)
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null>."));
    }

    @Test
    void testFormatMultipleNullQuotedParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName1} {parameterName2}.", null, null).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null> <null>."));
    }

    @Test
    void testFormatSingleUnquotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {parameterName|uq}.", "value")
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value."));
    }

    @Test
    void testFormatUnquotedMultipleParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName1|uq} and {parameterName2|uq}.", "value", 1).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value and 1."));
    }

    @Test
    void testFormatUnquoteParameterWithoutName() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {|uq}.", "value").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value."));
    }

    @Test
    void testFormatUnquotedParameterWithoutValue() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {unknown|uq}").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testFormatSingleNullUnquotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").format("Message with {parameterName1}.", null)
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null>."));
    }

    @Test
    void testFormatMultipleNullUnquotedParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName1} {parameterName2}.", null, null).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null> <null>."));
    }

    @Test
    void testFormatUnquotedParameterWithGroupReferenceChar() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .format("Message with {parameterName|uq}.", "$2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with $2."));
    }
}