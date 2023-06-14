package com.isc.hermes.model.incidents;

import java.util.ArrayList;
import java.util.List;

public class IncidentGetterModel {
    private List<Incident> incidentList;

    public IncidentGetterModel(){
        incidentList = new ArrayList<>();
    }

    public void addIncident(Incident incident){
        incidentList.add(incident);
    }

    public void clearIncidents(){
        incidentList.clear();
    }

    public List<Incident> getIncidentList() {
        return incidentList;
    }

    public void setIncidentList(List<Incident> incidentList) {
        this.incidentList = incidentList;
    }
}
