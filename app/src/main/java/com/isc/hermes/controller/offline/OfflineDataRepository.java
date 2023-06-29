package com.isc.hermes.controller.offline;

import com.isc.hermes.model.RegionData;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository class for managing offline data related to a region.
 */
public class OfflineDataRepository {
    private final Map<String, RegionData> dataRegions;
    public static final String DATA_TRANSACTION = "DATA_TRANSACTION";
    private static OfflineDataRepository offlineDataRepository;

    /**
     * This is a constructor method to initialize the repository.
     */

    public OfflineDataRepository() {
        dataRegions = new HashMap<>();
    }

    /**
     * Retrieves the singleton instance of the OfflineDataRepository.
     *
     * @return The OfflineDataRepository instance.
     */
    public static OfflineDataRepository getInstance() {
        if (offlineDataRepository == null) {
            offlineDataRepository = new OfflineDataRepository();
        }
        return offlineDataRepository;
    }

    /**
     * Retrieves the region data associated with the offline data repository.
     *
     * @return The region data.
     */
    public RegionData getRegionData(String dataName) {
        return dataRegions.get(dataName);
    }

    /**
     * Sets the region data for the offline data repository.
     *
     * @param regionData The region data to be set.
     */
    public void saveData(String dataName, RegionData regionData) {
        dataRegions.put(dataName, regionData);
    }
}
