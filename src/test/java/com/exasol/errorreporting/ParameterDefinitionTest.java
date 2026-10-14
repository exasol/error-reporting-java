package com.exasol.errorreporting;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class ParameterDefinitionTest {
    @Test
    void testGetName() {
        final ParameterDefinition parameter = ParameterDefinition.builder("number").build();
        assertThat(parameter.getName(), equalTo("number"));
    }

    @Test
    void testGetValue() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").value(42).build();
        assertThat(parameter.getValue(), equalTo(42));
    }

    @Test
    void testGetDescription() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").description("small blue thing")
                .build();
        assertThat(parameter.getDescription(), equalTo("small blue thing"));
    }

    @Test
    void testHasNameTrue() {
        final ParameterDefinition parameter = ParameterDefinition.builder("name").build();
        assertThat(parameter.hasName(), equalTo(true));

    }

    @Test
    void testHasValueTrue() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").value(1).build();
        assertThat(parameter.hasValue(), equalTo(true));
    }

    @Test
    void testHasValueFalse() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").build();
        assertThat(parameter.hasValue(), equalTo(false));
    }

    @Test
    void testHasDescriptionTrue() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").description("mostly harmless")
                .build();
        assertThat(parameter.hasDescription(), equalTo(true));
    }

    @Test
    void testToString() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").value("the value").build();
        assertThat(parameter.toString(), equalTo("the value"));
    }

    @Test
    void testToStringWithNullValue() {
        final ParameterDefinition parameter = ParameterDefinition.builder("irrelevant").build();
        assertThat(parameter.toString(), equalTo("<null>"));
    }
}
