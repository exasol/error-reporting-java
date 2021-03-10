package com.exasol.errorreporting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

class PlaceholderMatcherTest {

    @Test
    void testMatching() {
        final PlaceholderMatcher matcher = PlaceholderMatcher.findPlaceholders("{{a}} {{b}}");
        final List<String> placeholders = StreamSupport.stream(matcher.spliterator(), false).map(Placeholder::getName)
                .collect(Collectors.toList());
        assertThat(placeholders, contains("a", "b"));
    }

    @Test
    void testCallHasNextMoreOften() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("{{a}}").iterator();
        iterator.hasNext();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testNextWithoutHasNext() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("{{a}}").iterator();
        assertThat(iterator.next().getName(), equalTo("a"));
    }
}