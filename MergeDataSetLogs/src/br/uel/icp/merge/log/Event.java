package br.uel.icp.merge.log;

public class Event {
	
	private int caseId;
	private int eventPosition;
	private String activityName;
	private String timeStamp;
	private String classe;
	private String label;
	private String anomalyType;
	private String anomalyDescription;
	
	public int getCaseId() {
		return caseId;
	}
	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}
	public int getEventPosition() {
		return eventPosition;
	}
	public void setEventPosition(int eventPosition) {
		this.eventPosition = eventPosition;
	}
	public String getActivityName() {
		return activityName;
	}
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getClasse() {
		return classe;
	}
	public void setClasse(String classe) {
		this.classe = classe;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getAnomalyType() {
		return anomalyType;
	}
	public void setAnomalyType(String anomalyType) {
		this.anomalyType = anomalyType;
	}
	public String getAnomalyDescription() {
		return anomalyDescription;
	}
	public void setAnomalyDescription(String anomalyDescription) {
		this.anomalyDescription = anomalyDescription;
	}
	
	@Override
	public String toString() {
		return "Event [caseId=" + caseId + ", eventPosition=" + eventPosition + ", activityName=" + activityName
				+ ", timeStamp=" + timeStamp + ", classe=" + classe + ", label=" + label + ", anomalyType="
				+ anomalyType + ", anomalyDescription=" + anomalyDescription + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activityName == null) ? 0 : activityName.hashCode());
		result = prime * result + ((anomalyDescription == null) ? 0 : anomalyDescription.hashCode());
		result = prime * result + ((anomalyType == null) ? 0 : anomalyType.hashCode());
		result = prime * result + caseId;
		result = prime * result + ((classe == null) ? 0 : classe.hashCode());
		result = prime * result + eventPosition;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Event other = (Event) obj;
		if (activityName == null) {
			if (other.activityName != null)
				return false;
		} else if (!activityName.equals(other.activityName))
			return false;
		if (anomalyDescription == null) {
			if (other.anomalyDescription != null)
				return false;
		} else if (!anomalyDescription.equals(other.anomalyDescription))
			return false;
		if (anomalyType == null) {
			if (other.anomalyType != null)
				return false;
		} else if (!anomalyType.equals(other.anomalyType))
			return false;
		if (caseId != other.caseId)
			return false;
		if (classe == null) {
			if (other.classe != null)
				return false;
		} else if (!classe.equals(other.classe))
			return false;
		if (eventPosition != other.eventPosition)
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

}
