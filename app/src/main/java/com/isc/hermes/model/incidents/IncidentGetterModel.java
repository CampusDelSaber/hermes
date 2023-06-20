package com.isc.hermes.model.incidents;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that represents a collection of incidents.
 */
public class IncidentGetterModel {
    private List<PointIncident> pointIncidetList;

    /**
     * Constructs an instance of IncidentGetterModel.
     */
    public IncidentGetterModel(){
        pointIncidetList = new ArrayList<>();
    }

    /**
     * Adds an pointIncidet to the collection.
     *
     * @param pointIncidet The pointIncidet to add.
     */
    public void addIncident(PointIncident pointIncidet){
        pointIncidetList.add(pointIncidet);
    }

    /**
     * Clears the collection of incidents.
     */
    public void clearIncidents(){
        pointIncidetList.clear();
    }

    /**
     * Retrieves the list of incidents.
     *
     * @return The list of incidents.
     */
    public List<PointIncident> getIncidentList() {
        return pointIncidetList;
    }

    /**
     * Sets the list of incidents.
     *
     * @param pointIncidetList The list of incidents.
     */
    public void setIncidentList(List<PointIncident> pointIncidetList) {
        this.pointIncidetList = pointIncidetList;
    }
}
