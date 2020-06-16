package br.uel.icp.merge.log;

import java.util.List;

public class Caso {
	
	private int caseId;
	private List<Event>  events;
	private int totalEvents;
	
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public List<Event> getEvents() {
		return events;
	}
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	public int getTotalEvents() {
		return totalEvents;
	}
	public void setTotalEvents(int totalEvents) {
		this.totalEvents = totalEvents;
	}

}
