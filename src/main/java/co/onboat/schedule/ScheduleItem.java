package co.onboat.schedule;

import java.time.LocalDateTime;
import java.util.UUID;

public class ScheduleItem {

	private String id;

	private String name;

	private String description;

	private String flight;

	private String origin;

	private String destination;

	private LocalDateTime startTime;

	private LocalDateTime endTime;

	public ScheduleItem() {
		super();
		this.id = UUID.randomUUID().toString();
	}

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public String getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id New id.
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Gets the name.
	 *
	 * @return The name.
	 */
	public String getName() {
		// Gets the name.
		String name = this.name;
		// If there is a name.
		if (name != null) {
			// Replaces the wildcards in the name.
			name = name.replaceAll("\\{flight\\}", this.getFlight());
			name = name.replaceAll("\\{origin\\}", this.getOrigin());
			name = name.replaceAll("\\{destination\\}", this.getDestination());
		}
		// Returns the name.
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name New name.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return The description.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description New description.
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Gets the flight.
	 *
	 * @return The flight.
	 */
	public String getFlight() {
		return this.flight;
	}

	/**
	 * Sets the flight.
	 *
	 * @param flight New flight.
	 */
	public void setFlight(final String flight) {
		this.flight = flight;
	}

	/**
	 * Gets the origin.
	 *
	 * @return The origin.
	 */
	public String getOrigin() {
		return this.origin;
	}

	/**
	 * Sets the origin.
	 *
	 * @param origin New origin.
	 */
	public void setOrigin(final String origin) {
		this.origin = origin;
	}

	/**
	 * Gets the destination.
	 *
	 * @return The destination.
	 */
	public String getDestination() {
		return this.destination;
	}

	/**
	 * Sets the destination.
	 *
	 * @param destination New destination.
	 */
	public void setDestination(final String destination) {
		this.destination = destination;
	}

	/**
	 * Gets the startTime.
	 *
	 * @return The startTime.
	 */
	public LocalDateTime getStartTime() {
		return this.startTime;
	}

	/**
	 * Sets the startTime.
	 *
	 * @param startTime New startTime.
	 */
	public void setStartTime(final LocalDateTime startTime) {
		this.startTime = startTime;
	}

	/**
	 * Gets the endTime.
	 *
	 * @return The endTime.
	 */
	public LocalDateTime getEndTime() {
		return this.endTime;
	}

	/**
	 * Sets the endTime.
	 *
	 * @param endTime New endTime.
	 */
	public void setEndTime(final LocalDateTime endTime) {
		this.endTime = endTime;
	}

}
