package com.isc.hermes;

import static org.junit.Assert.assertEquals;
import com.isc.hermes.model.Searcher;
import com.isc.hermes.model.WayPoint;
import org.junit.Test;
import java.util.List;


/**
 * Class to test the searcher functionality calling the methods to receive the suggestions
 */
public class SearchTest {

    /**
     * This method checks the suggestions places' names received by a query given
     */
    @Test
    public void getSearchResultsByQuerySentExample1() {
        Searcher searcher = new Searcher();
        List<WayPoint> actualSuggestions = searcher.getSearcherSuggestionsPlacesInfo("Cochabamba");

        assertEquals(actualSuggestions.get(0).getPlaceName(), "Cochabamba, Cochabamba, Bolivia");
        assertEquals(actualSuggestions.get(1).getPlaceName(), "Cochabamba, Bolivia");
        assertEquals(actualSuggestions.get(2).getPlaceName(), "Cochabamba, Av las encinas 1390 local 1, Concón, Valparaíso 2510000, Chile");
        assertEquals(actualSuggestions.get(3).getPlaceName(), "Hotel Toloma, Cochabamba, Cochabamba, Bolivia");
        assertEquals(actualSuggestions.get(4).getPlaceName(), "Camara de Industria, Av. Ballivian, Cochabamba, Cochabamba, Bolivia");
    }

    /**
     * Method to get the suggestions list size to test the correct call for the query
     */
    @Test
    public void getSearchSuggestionsFeaturesSizeByQuery() {
        Searcher searcher = new Searcher();
        int actualSuggestionsListSize = searcher.getSearcherSuggestionsPlacesInfo("Coch").size();
        int expectedSize = 5;

        assertEquals(expectedSize, actualSuggestionsListSize);
    }

}
