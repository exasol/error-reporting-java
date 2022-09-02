package com.exasol.errorreporting;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

class PlaceholderMatcherTest {
    @Test
    void testMatching() {
        final Iterable<Placeholder> matcher = PlaceholderMatcher.findPlaceholders("{{a}} {{b}}");
        final List<String> placeholders = StreamSupport.stream(matcher.spliterator(), false) //
                .map(Placeholder::getReference) //
                .collect(Collectors.toList());
        assertThat(placeholders, contains("a", "b"));
    }

    @Test
    void testHasNext() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("{{a}}").iterator();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    void testNextWithoutHasNext() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("{{a}}").iterator();
        assertThat(iterator.next().getReference(), equalTo("a"));
    }

    @Test
    void testExceptionAfterLastElement() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("{{a}}").iterator();
        iterator.next();
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testNoPlaceholders() {
        final Iterator<Placeholder> iterator = PlaceholderMatcher.findPlaceholders("some text with no placeholder")
                .iterator();
        assertFalse(iterator.hasNext());
    }
}
