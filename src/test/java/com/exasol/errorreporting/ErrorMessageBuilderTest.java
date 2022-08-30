package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

class ErrorMessageBuilderTest {
    final String NULL_STRING = null;

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
                .message("test {{PLACEHOLDER}}").parameter("PLACEHOLDER", "$2");
        assertThat(messageBuilder.toString(), equalTo("E-ERJ-TEST-1: test '$2'"));
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

    @Test
    void testMitigationInlineSingleParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message.")
                .mitigation("Mitigation with {{parameterName}}.", "value").toString();
        assertThat(message, equalTo("ERROR-CODE: Message. Mitigation with 'value'."));
    }

    @Test
    void testMitigationInlineMultipleParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message.")
                .mitigation("Mitigation with {{parameterName1}} and {{parameterName2}}.", "value", 1).toString();
        assertThat(message, equalTo("ERROR-CODE: Message. Mitigation with 'value' and 1."));
    }

    @Test
    void testMitigationInlineParameterWithoutValue() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message.")
                .mitigation("Mitigation with {{unknown}}").toString();
        assertThat(message, equalTo("ERROR-CODE: Message. Mitigation with UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testMitigationInlineSingleNullParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message.")
                .mitigation("Mitigation with {{parameterName1}}.", NULL_STRING).toString();
        assertThat(message, equalTo("ERROR-CODE: Message. Mitigation with <null>."));
    }

    @Test
    void testMitigationInlineMultipleNullParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message.")
                .mitigation("Mitigation with {{parameterName1}} {{parameterName2}}.", NULL_STRING, NULL_STRING)
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message. Mitigation with <null> <null>."));
    }

    @Test
    void testMessageInlineSingleQuotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{parameterName}}.", "value")
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value'."));
    }

    @Test
    void testMessageInlineQuotedMultipleParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1}} and {{parameterName2}}.", "value", 1).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value' and 1."));
    }

    @Test
    void testMessageInlineQuoteParameterWithoutName() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{}}.", "value").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with 'value'."));
    }

    @Test
    void testMessageInlineQuotedParameterWithoutValue() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{unknown}}").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testMessageInlineQuotedParameterWithGroupReferenceChar() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName}}.", "$2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with '$2'."));
    }

    @Test
    void testMessageInlineSingleNullQuotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{parameterName1}}.",
                        NULL_STRING).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null>."));
    }

    @Test
    void testMessageInlineMultipleNullQuotedParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1}} {{parameterName2}}.", null, null).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null> <null>."));
    }

    @Test
    void testMessageInlineSingleUnquotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName|uq}}.", "value").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value."));
    }

    @Test
    void testMessageInlineUnquotedMultipleParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1|uq}} and {{parameterName2|uq}}.", "value", 1).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value and 1."));
    }

    @Test
    void testMessageInlineUnquoteParameterWithoutName() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{|uq}}.", "value")
                .toString();
        assertThat(message, equalTo("ERROR-CODE: Message with value."));
    }

    @Test
    void testMessageInlineUnquotedParameterWithoutValue() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{unknown|uq}}").toString();
        assertThat(message, equalTo("ERROR-CODE: Message with UNKNOWN PLACEHOLDER('unknown')"));
    }

    @Test
    void testMessageInlineSingleNullUnquotedParameter() {
        final String message = new ErrorMessageBuilder("ERROR-CODE").message("Message with {{parameterName1}}.",
                        NULL_STRING).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null>."));
    }

    @Test
    void testMessageInlineMultipleNullUnquotedParameters() {
        final String message = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1}} {{parameterName2}}.", null, null).toString();
        assertThat(message, equalTo("ERROR-CODE: Message with <null> <null>."));
    }

    @Test
    void testMessageInlineUnquotedParameterWithGroupReferenceChar() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName|uq}}.", "$2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with $2."));
    }

    @Test
    void testMessageInlineAndOutlineInOrder() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1}}", "value1").message(" {{parameterName2}}.")
                .parameter("parameterName2", "value2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with 'value1' 'value2'."));
    }

    @Test
    void testMessageOutlineAndInlineInOrder() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName1}}").parameter("parameterName1", "value1")
                .message(" {{parameterName2}}.", "value2");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with 'value1' 'value2'."));
    }


    @Test
    void testMessage_() {
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("ERROR-CODE")
                .message("Message with {{parameterName|uq}}.").parameter("parameterName", "value");
        assertThat(messageBuilder.toString(), equalTo("ERROR-CODE: Message with value."));
    }

    @Test
    void testPathQuoting() {
        final Path path = Path.of("/foo/bar/baz");
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-PATH-1")
                .message("Path not found: {{path}}").parameter("path", path);
        assertThat(messageBuilder.toString(), endsWith("Path not found: '/foo/bar/baz'"));
    }

    @Test
    void testFileQuoting() {
        final File file = new File("/foo/bar/baz.txt");
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-FILE-1")
                .message("File not found: {{file}}").parameter("file", file);
        assertThat(messageBuilder.toString(), endsWith("File not found: '/foo/bar/baz.txt'"));
    }

    @Test
    void testUrlQuoting() throws MalformedURLException {
        final URL url = new URL("https://example.org");
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-URL-1")
                .message("URL not found: {{url}}").parameter("url", url);
        assertThat(messageBuilder.toString(), endsWith("URL not found: 'https://example.org'"));
    }
    @Test
    void testUriQuoting() throws URISyntaxException {
        final URI uri = new URI("URN:ISBN:0-330-28700-1");
        final ErrorMessageBuilder messageBuilder = new ErrorMessageBuilder("E-URI-1")
                .message("ISBN not found: {{isbn}}").parameter("isbn", uri);
        assertThat(messageBuilder.toString(), endsWith("ISBN not found: 'URN:ISBN:0-330-28700-1'"));
    }
}