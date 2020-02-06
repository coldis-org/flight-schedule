package co.onboat;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.coldis.library.exception.IntegrationException;
import org.coldis.library.model.SimpleMessage;
import org.coldis.library.pdf.PdfHelper;

import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;

import co.onboat.schedule.CrewMember;
import co.onboat.schedule.MonthSchedule;
import co.onboat.schedule.ScheduleItem;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

public class Test {

	private static final String CREW_MEMBER_PATTERN = "Individual duty plan for (?<id>[0-9]*) (?<firstName>[^,]*), (?<lastName>.*) NetLine/Crew\\(GOL\\)";

	private static final String MONTH_PATTERN = "Period: [0-9]{2}(?<month>[a-zA-Z]{3})(?<year>[0-9]{2}) - [0-9]{2}[a-zA-Z]{3}[0-9]{2}";

	private static final String DAILY_START_PATTERN = "(Mon|Tue|Wed|Thu|Fri|Sat|Sun)([0-9]{1,2}) ";

	private static final String DAILY_PATTERN = "(?<daily>" + Test.DAILY_START_PATTERN + "(.(?!(Individual|"
			+ Test.DAILY_START_PATTERN + ")))*)";

	private static final String WORK_TIME_PATTERN = "\\[(?<workTime>[FDR]T [0-9]{2}:[0-9]{2})\\]";

	private static final String FLIGHT_PATTERN = "(?<flight>(?<type>[^ ]*) (?<number>[^ ]*) (?<origin>[a-zA-Z]{3}) (?<startHour>[0-9]{2})(?<startMinute>[0-9]{2}) (?<finalHour>[0-9]{2})(?<finalMinute>[0-9]{2}) (?<destination>[a-zA-Z]{3})( (?<aircraft>[^ ]*))?)";

	private static final Map<String, String> DICTONARY = Map.of("", "");

	private static String getCalendar(final MonthSchedule monthSchedule) {
		// Creates a new calendar.
		final Calendar calendar = new Calendar();
		calendar.getProperties().add(new ProdId("-//Events Calendar//iCal4j 1.0//EN"));
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Version.VERSION_2_0);

		// TimeZoneRegistry registry =
		// TimeZoneRegistryFactory.getInstance().createRegistry();
		// TimeZone timezone = registry.getTimeZone(ZoneId.systemDefault());
		// VTimeZone tz = timezone.getVTimeZone();

		// For each schedule item.
		for (final ScheduleItem scheduleItem : monthSchedule.getScheduleItems()) {
			// Creates an event.
			final VEvent calendarEvent = new VEvent(
					new Date(java.util.Date
							.from(scheduleItem.getStartTime().atZone(ZoneId.systemDefault()).toInstant())),
					new Date(java.util.Date.from(scheduleItem.getEndTime().atZone(ZoneId.systemDefault()).toInstant())),
					scheduleItem.getName()); // FIXME timezone

			// add timezone info.. FIXME
			// meeting.getProperties().add(new VTZo);

			// generate unique identifier..
			calendarEvent.getProperties().add(new Uid(scheduleItem.getId()));

			// Adds the main attendee.
			final Attendee attendee = new Attendee(URI.create("mailto:" + monthSchedule.getCrewMember().getEmail()));
			attendee.getParameters().add(Role.REQ_PARTICIPANT);
			attendee.getParameters().add(new Cn(
					monthSchedule.getCrewMember().getFirstName() + " " + monthSchedule.getCrewMember().getLastName()));
			calendarEvent.getProperties().add(attendee);
			// Adds the meeting to the calendar.
			calendar.getComponents().add(calendarEvent);
		}
		// Prepares for creating the calendar.
		final Writer writer = new StringWriter();
		final CalendarOutputter outputter = new CalendarOutputter();
		// Tries to generate and return the calendar.
		try {
			outputter.output(calendar, writer);
			return writer.toString();
		}
		// If there is an error generating the calendar.
		catch (final Exception exception) {
			// Throws a calendar generation error.
			throw new IntegrationException(new SimpleMessage("calendar.generation.error"), exception);
		}
		// At the end.
		finally {
			// Closes the writer.
			try {
				writer.close();
			}
			// If there is an error generating the calendar.
			catch (final Exception exception) {
				// Logs it. TODO

			}
		}
	}

	public static void main(final String[] args) throws IOException, URISyntaxException {
		// Reads the schedule PDF.
		final String schedulePdfContent = PdfHelper.readPdfText(
				FileUtils.readFileToByteArray(new File("src/main/resources/gol.pdf")),
				new SimpleTextExtractionStrategy(), true);

		System.out.println(schedulePdfContent);

		// Gets the crew member information.
		final CrewMember crewMember = new CrewMember();
		final Pattern crewMemberPattern = Pattern.compile(Test.CREW_MEMBER_PATTERN);
		final Matcher crewMemberMatcher = crewMemberPattern.matcher(schedulePdfContent);
		crewMemberMatcher.find();
		crewMember.setFlyerId(crewMemberMatcher.group("id"));
		crewMember.setFirstName(crewMemberMatcher.group("firstName"));
		crewMember.setLastName(crewMemberMatcher.group("lastName"));
		// Gets the month information.
		final Pattern monthPattern = Pattern.compile(Test.MONTH_PATTERN);
		final Matcher monthMatcher = monthPattern.matcher(schedulePdfContent);
		monthMatcher.find();
		// Gets month schedule information.
		final String year = monthMatcher.group("year");
		final String month = monthMatcher.group("month");
		final MonthSchedule monthSchedule = new MonthSchedule();
		monthSchedule.setCrewMember(crewMember);
		monthSchedule.setMonth(LocalDate.of(2000 + Integer.parseInt(year),
				List.of(Month.values()).parallelStream()
				.filter(currentMonth -> currentMonth.name().toLowerCase().startsWith(month.toLowerCase()))
				.findFirst().get(),
				1));
		// Defines the daily schedule information pattern.
		final Pattern dailyPattern = Pattern.compile(Test.DAILY_PATTERN, Pattern.DOTALL);
		final Matcher dailyMatcher = dailyPattern.matcher(schedulePdfContent);
		// Defines the daily work time information pattern.
		final Pattern dailyWorkTimePattern = Pattern.compile(Test.WORK_TIME_PATTERN);
		// Defines the schedule information pattern.
		final Pattern flightPattern = Pattern.compile(Test.FLIGHT_PATTERN);
		// For each day information.
		while (dailyMatcher.find()) {
			// Gets the raw day information.
			String dailyContent = dailyMatcher.group("daily");
			// Gets the day of month information.
			final Integer dayOfMonth = Integer.parseInt(dailyContent.substring(3, 5));
			// Gets the current date.
			final LocalDate baseDate = LocalDate.from(monthSchedule.getMonth()).withDayOfMonth(dayOfMonth);
			// Removes the day information from the day information.
			dailyContent = dailyContent.replaceFirst(Test.DAILY_START_PATTERN, "");
			// Prepares the matchers on the day information.
			final Matcher dailyWorkTimeMatcher = dailyWorkTimePattern.matcher(dailyContent);
			final Matcher flightMatcher = flightPattern.matcher(dailyContent);
			// For each work time information.
			while (dailyWorkTimeMatcher.find()) {
				// Gets the work time information.
				String workTime = dailyWorkTimeMatcher.group("workTime");
				// Gets the type of work time.
				final char workTimeType = workTime.charAt(0);
				// Converts the work time into a parseable duration.
				workTime = workTime.substring(2).strip().trim().replaceAll("^", "PT").replaceAll(":", "H")
						.replaceAll("$", "M");
				// If the work time is for flying time.
				if ('F' == workTimeType) {
					// Gets the flying time.
					final Duration flyingTime = Duration.parse(workTime);
				}
				// If the work time is for duty time.
				else if ('D' == workTimeType) {
					// Gets the duty time.
					final Duration dutyTime = Duration.parse(workTime);
				}
				// If the work time is for rest time.
				else if ('R' == workTimeType) {
					// Gets the duty time.
					final Duration restTime = Duration.parse(workTime);
				}
			}
			// Removes the work time info from the daily information.
			dailyContent = dailyContent.replaceAll(Test.WORK_TIME_PATTERN, "");
			// For each flight information.
			while (flightMatcher.find()) {
				// Creates a new schedule item.
				final ScheduleItem flight = new ScheduleItem();
				flight.setName("Flight #{flight} from {origin} to {destination}");
				flight.setFlight(flightMatcher.group("number"));
				flight.setOrigin(flightMatcher.group("origin"));
				flight.setDestination(flightMatcher.group("destination"));
				flight.setStartTime(
						LocalDateTime.of(baseDate, LocalTime.of(Integer.parseInt(flightMatcher.group("startHour")),
								Integer.parseInt(flightMatcher.group("startMinute")))));
				flight.setEndTime(
						LocalDateTime.of(baseDate, LocalTime.of(Integer.parseInt(flightMatcher.group("finalHour")),
								Integer.parseInt(flightMatcher.group("finalMinute")))));
				// Adds the item to the schedule.
				monthSchedule.getScheduleItems().add(flight);

			}

			FileUtils.writeStringToFile(new File("test.ical"), Test.getCalendar(monthSchedule),
					Charset.forName("UTF-8"));

			crewMember.getId();

		}

		crewMember.getId();

	}

}
