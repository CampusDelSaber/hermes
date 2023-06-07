package com.isc.hermes;

import static org.junit.Assert.assertEquals;

import com.isc.hermes.model.Searcher;

import org.junit.Test;

public class SearchTest {
    @Test
    public void getSearchResultsByQuerySent() {
        Searcher searcher = new Searcher();
        int actual = searcher.performSearch("Aniceto Arce");
        int expected = 10;
        assertEquals(expected, actual);
    }
}
