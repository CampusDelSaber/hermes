package com.isc.hermes.model.incidents;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class that represents a collection of incidents.
 */
public class IncidentGetterModel {
    private List<Incident> incidentList;

    /**
     * Constructs an instance of IncidentGetterModel.
     */
    public IncidentGetterModel(){
        incidentList = new ArrayList<>();
    }

    /**
     * Adds an incident to the collection.
     *
     * @param incident The incident to add.
     */
    public void addIncident(Incident incident){
        incidentList.add(incident);
    }

    /**
     * Clears the collection of incidents.
     */
    public void clearIncidents(){
        incidentList.clear();
    }

    /**
     * Retrieves the list of incidents.
     *
     * @return The list of incidents.
     */
    public List<Incident> getIncidentList() {
        return incidentList;
    }

    /**
     * Sets the list of incidents.
     *
     * @param incidentList The list of incidents.
     */
    public void setIncidentList(List<Incident> incidentList) {
        this.incidentList = incidentList;
    }
}
