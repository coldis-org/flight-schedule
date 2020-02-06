package co.onboat.schedule;

public class CrewMember {

	private Long id;

	private String flyerId;

	private String email;

	private String firstName;

	private String lastName;

	/**
	 * Gets the id.
	 *
	 * @return The id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id New id.
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Gets the flyerId.
	 *
	 * @return The flyerId.
	 */
	public String getFlyerId() {
		return this.flyerId;
	}

	/**
	 * Sets the flyerId.
	 *
	 * @param flyerId New flyerId.
	 */
	public void setFlyerId(final String flyerId) {
		this.flyerId = flyerId;
	}

	/**
	 * Gets the email.
	 *
	 * @return The email.
	 */
	public String getEmail() {
		return this.email;
	}

	/**
	 * Sets the email.
	 *
	 * @param email New email.
	 */
	public void setEmail(final String email) {
		this.email = email;
	}

	/**
	 * Gets the firstName.
	 *
	 * @return The firstName.
	 */
	public String getFirstName() {
		return this.firstName;
	}

	/**
	 * Sets the firstName.
	 *
	 * @param firstName New firstName.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the lastName.
	 *
	 * @return The lastName.
	 */
	public String getLastName() {
		return this.lastName;
	}

	/**
	 * Sets the lastName.
	 *
	 * @param lastName New lastName.
	 */
	public void setLastName(final String lastName) {
		this.lastName = lastName;
	}

}