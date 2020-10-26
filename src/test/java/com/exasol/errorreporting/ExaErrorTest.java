package com.exasol.errorreporting;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.Test;

class ExaErrorTest {

    @Test
    void testCreateErrorCodeMessage() {
        assertThat(ExaError.messageBuilder("T-ERJ-1").toString(), equalTo("T-ERJ-1"));
    }
}