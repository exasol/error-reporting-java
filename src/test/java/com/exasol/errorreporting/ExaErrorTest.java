package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ExaErrorTest {

    @Test
    void testCreateErrorCodeMessage() {
        assertThat(ExaError.messageBuilder("E-ERJ-TEST-1").toString(), equalTo("E-ERJ-TEST-1"));
    }
}