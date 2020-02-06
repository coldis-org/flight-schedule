package co.onboat.schedule;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MonthSchedule {

	private CrewMember crewMember;

	private LocalDate month;

	private List<ScheduleItem> scheduleItems;

	/**
	 * Gets the crewMember.
	 *
	 * @return The crewMember.
	 */
	public CrewMember getCrewMember() {
		return this.crewMember;
	}

	/**
	 * Sets the crewMember.
	 *
	 * @param crewMember New crewMember.
	 */
	public void setCrewMember(final CrewMember crewMember) {
		this.crewMember = crewMember;
	}

	/**
	 * Gets the month.
	 *
	 * @return The month.
	 */
	public LocalDate getMonth() {
		return this.month;
	}

	/**
	 * Sets the month.
	 *
	 * @param month New month.
	 */
	public void setMonth(final LocalDate month) {
		this.month = month;
	}

	/**
	 * Gets the scheduleItems.
	 *
	 * @return The scheduleItems.
	 */
	public List<ScheduleItem> getScheduleItems() {
		// If the list has not been initialized.
		if (this.scheduleItems == null) {
			// Creates an empty list.
			this.scheduleItems = new ArrayList<>();
		}
		// Returns the list.
		return this.scheduleItems;
	}

	/**
	 * Sets the scheduleItems.
	 *
	 * @param scheduleItems New scheduleItems.
	 */
	public void setScheduleItems(final List<ScheduleItem> scheduleItems) {
		this.scheduleItems = scheduleItems;
	}

}
