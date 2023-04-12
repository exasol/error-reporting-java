package com.exasol.errorreporting;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertAll;

class ParameterDefinitionListTest {
    @Test
    void testGet() {
        ParameterDefinitionList list = new ParameterDefinitionList();
        final ParameterDefinition expectedA = ParameterDefinition.builder("A").build();
        final ParameterDefinition expectedB = ParameterDefinition.builder("B").build();
        list.add(expectedA);
        list.add(expectedB);
        assertAll(
                () ->  assertThat(list.get("A"), sameInstance(expectedA)),
                () ->  assertThat(list.get("B"), sameInstance(expectedB))
        );
    }

    @Test
    void testContainsKey() {
        ParameterDefinitionList list = new ParameterDefinitionList();
        final ParameterDefinition expectedParameter = ParameterDefinition.builder("THERE").build();
        list.add(expectedParameter);
        assertAll(
                ()->assertThat(list.containsKey("THERE"), equalTo(true)),
                ()-> assertThat(list.containsKey("NOT THERE"), equalTo(false))
        );
    }
}
