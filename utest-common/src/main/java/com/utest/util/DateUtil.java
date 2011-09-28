/*
Case Conductor is a Test Case Management system.
Copyright (C) 2011 uTest Inc.

This file is part of Case Conductor.

Case Conductor is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Case Conductor is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Case Conductor.  If not, see <http://www.gnu.org/licenses/>.

*/
/**
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 
 * @author Vadim Kisen
 *
 * copyright 2010 by uTest 
 */
package com.utest.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Utility to format and compare Dates.
 */
public class DateUtil
{

	public static final int		YEAR				= Calendar.YEAR;
	public static final int		MONTH				= Calendar.MONTH;
	public static final int		DAY_OF_MONTH		= Calendar.DAY_OF_MONTH;
	public static final int		HOUR				= Calendar.HOUR;
	public static final int		MINUTE				= Calendar.MINUTE;
	public static final int		SECOND				= Calendar.SECOND;
	public static final int		START_YEAR			= 1900;
	public static final int		UTEST_START_DAY		= 10;
	public static final int		UTEST_START_MONTH	= 3;
	public static final int		UTEST_START_YEAR	= 2008;

	public static final Date	UTEST_INITIAL_DATE	= createDate(UTEST_START_DAY, UTEST_START_MONTH, UTEST_START_YEAR);

	// -----------------------------------------------------------------------------------------//

	private DateUtil()
	{
		// Cannot be instantiated
	}

	/**
	 * Returns a correctly formatted String representing a Date, according to
	 * the Locale but forcing 4-digit years.
	 */
	public static String dateToString(final Date date, final Locale locale)
	{

		// Fixes the year format
		final SimpleDateFormat df = (SimpleDateFormat) DateFormat.getDateInstance(DateFormat.SHORT, locale);
		final String pattern = DateUtil.formatYear(df.toPattern());
		// Returns the formatted String
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		final String dateStr = dateFormat.format(date);

		return dateStr;
	}

	public static String convertToFullDate(final Date date)
	{
		if (date == null)
		{
			return "";
		}
		final String pattern = DateUtil.formatYear("MM/dd/yyyy h:mm a");

		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		final String dateStr = dateFormat.format(date);

		return dateStr;
	}

	/**
	 * Fixes the date format so that year has 4 digits, and month and day have 2
	 * digits. This is needed because there is no default Java format that does
	 * this.
	 */
	public static String formatYear(final String pattern)
	{
		String newPattern = pattern.toLowerCase();
		if (!newPattern.contains("yyyy"))
		{
			newPattern = newPattern.replaceFirst("yy", "yyyy");
		}
		if (!newPattern.contains("dd"))
		{
			newPattern = newPattern.replaceFirst("d", "dd");
		}
		if (!newPattern.contains("mm"))
		{
			newPattern = newPattern.replaceFirst("m", "MM");
		}
		else
		{
			newPattern = newPattern.replaceFirst("mm", "MM");
		}
		return newPattern;
	}

	/**
	 * Compares two dates ignoring time: just year, month, day.
	 * 
	 * @return < 0 : if dateA < dateB == 0 : if dateA == dateB > 0 : if dateA >
	 *         dateB
	 */
	public static int compareDates(final Date dateA, final Date dateB)
	{

		final Calendar calendarA = GregorianCalendar.getInstance();

		calendarA.setTime(dateA);
		calendarA.set(Calendar.MILLISECOND, 0);
		calendarA.set(Calendar.SECOND, 0);
		calendarA.set(Calendar.MINUTE, 0);
		calendarA.set(Calendar.HOUR_OF_DAY, 0);

		final Calendar calendarB = GregorianCalendar.getInstance();

		calendarB.setTime(dateB);
		calendarB.set(Calendar.MILLISECOND, 0);
		calendarB.set(Calendar.SECOND, 0);
		calendarB.set(Calendar.MINUTE, 0);
		calendarB.set(Calendar.HOUR_OF_DAY, 0);

		return calendarA.getTime().compareTo(calendarB.getTime());
	}

	/**
	 * Checks if a date is contained within a date interval. Ignores time.
	 * 
	 * @param date
	 *            Date to be verified.
	 * @param startDate
	 *            Start of interval.
	 * @param endDate
	 *            End of interval.
	 * @return True if date is in [startDate, endDate]; false otherwise.
	 */
	public static boolean inDateInterval(final Date date, final Date startDate, final Date endDate)
	{

		return ((DateUtil.compareDates(startDate, date) <= 0) && (DateUtil.compareDates(endDate, date) >= 0));
	}

	/**
	 * Creates a date. Note: there is no need to call clearDate() afterwards,
	 * since this method creates a Date with zeroed time.
	 * 
	 * @param day
	 *            1-based (do not use Calendar.<DAY> constants!)
	 * @param month
	 *            1-based (do not use Calendar.<MONTH> constants!)
	 * @param year
	 */
	public static Date createDate(final int day, final int month, final int year)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(year, month - 1, day);
		return clearDate(calendar.getTime());
	}

	public static Date createDate(final Date date)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return clearDate(calendar.getTime());
	}

	/**
	 * Set hour, minute, second and millisecond to 0. Used for queries that need
	 * precision in these parameters.
	 */
	public static Date clearDate(final Date date)
	{
		if (date == null)
		{
			return null;
		}
		final Calendar calendar = GregorianCalendar.getInstance();

		calendar.setTime(date);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		return calendar.getTime();
	}

	/**
	 * Calculates the number of days that have in common the first interval and
	 * the second interval
	 */
	public static int daysBetween(final Date dateA, final Date dateB)
	{

		Date date1, date2;
		date1 = dateA;
		date2 = dateB;

		// Swap dates so that date1 <= date2
		if (date1.after(date2))
		{
			date1 = dateB;
			date2 = dateA;
		}
		Calendar calendar1 = GregorianCalendar.getInstance();
		calendar1.setTime(date1);
		final Calendar calendar2 = GregorianCalendar.getInstance();
		calendar2.setTime(date2);

		int days = calendar2.get(java.util.Calendar.DAY_OF_YEAR) - calendar1.get(java.util.Calendar.DAY_OF_YEAR) + 1;
		final int y1 = calendar1.get(java.util.Calendar.YEAR);
		final int y2 = calendar2.get(java.util.Calendar.YEAR);
		if (y1 != y2)
		{
			calendar1 = (Calendar) calendar1.clone();
			do
			{
				days += calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
				calendar1.add(java.util.Calendar.YEAR, 1);
			}
			while (calendar1.get(java.util.Calendar.YEAR) != y2);
		}
		return days - 1;
	}

	public static float daysDifference(final Date dateA, final Date dateB)
	{
		return (dateB.getTime() - dateA.getTime()) / (1000 * 60 * 60 * 24);
	}

	public static float minutesDifference(final Date dateA, final Date dateB)
	{
		return (dateB.getTime() - dateA.getTime()) / (1000 * 60);
	}

	/**
	 * @return Adds the given number of days to the given date and return a
	 *         copy.
	 */
	public static Date addDays(final Date date, final int days)
	{
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DAY_OF_MONTH, days);
		return gc.getTime();
	}

	/**
	 * @return Adds the given amount of months to the given date and return a
	 *         copy.
	 */
	public static Date addMonths(final Date date, final int months)
	{
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.MONTH, months);
		return gc.getTime();
	}

	/**
	 * @return Adds the given amount of years to the given date and return a
	 *         copy.
	 */
	public static Date addYears(final Date date, final int years)
	{
		final GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.YEAR, years);
		return gc.getTime();
	}

	/**
	 * 
	 * @param date
	 *            The date to inspect
	 * @param field
	 *            The field to return. See static values of DateUtil. If MONTH
	 *            is requested, value returned is between 1 and 12
	 * @return
	 */
	public static int getField(final Date date, final int field)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(field) + (field == DateUtil.MONTH ? 1 : 0);
	}

	/**
	 * Returns the age in years according to the date of birth passed and the
	 * date passed as current.
	 * 
	 * @param birthDay
	 *            Date of birth
	 * @param currDate
	 *            Date where the age is to be calculated
	 * 
	 * @return years old using current time
	 */
	public static int getAge(final Date birthDay, final Date currDate)
	{
		final Calendar endDate = Calendar.getInstance();
		final Calendar startDate = Calendar.getInstance();
		startDate.setTime(birthDay);
		endDate.setTime(currDate);
		int years = endDate.get(Calendar.YEAR) - startDate.get(Calendar.YEAR);
		startDate.set(Calendar.YEAR, endDate.get(Calendar.YEAR));
		if (startDate.after(endDate))
		{
			years--;
		}
		return years;
	}

	/**
	 * Given a Date as parameter, it sets it to the specified time and returns a
	 * copy.
	 * 
	 * @param date
	 *            Original date
	 * @param hours
	 *            Hours to set to the date
	 * @param minutes
	 *            Minutes to set to the date
	 * @param seconds
	 *            Seconds to set to the date
	 * 
	 * @return A new Date with the given time.
	 */
	public static Date setTime(final Date date, final int hours, final int minutes, final int seconds)
	{
		final Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		aux.set(Calendar.MILLISECOND, 0);
		aux.set(Calendar.HOUR, hours);
		aux.set(Calendar.MINUTE, minutes);
		aux.set(Calendar.SECOND, seconds);
		return aux.getTime();
	}

	/**
	 * @return A new Date set to the last day in the month for the given date.
	 */
	public static Date setToLastDayInMonth(final Date date)
	{
		final Calendar aux = Calendar.getInstance();
		aux.setTime(date);
		aux.set(Calendar.DAY_OF_MONTH, aux.getActualMaximum(Calendar.DAY_OF_MONTH));
		return aux.getTime();
	}

	public static Date maxDate(final Date date1, final Date date2)
	{
		return DateUtil.compareDates(date1, date2) > 0 ? date1 : date2;
	}

	public static Date minDate(final Date date1, final Date date2)
	{
		return DateUtil.compareDates(date1, date2) < 0 ? date1 : date2;
	}

	/**
	 * Get week days between two dates inclusive. (week days between now and
	 * now, is 1 if it's week day)
	 */
	public static int weekDaysBetween(final Date a, final Date b)
	{
		int ans = 0;
		final Calendar start = Calendar.getInstance();
		final Calendar end = Calendar.getInstance();
		if (compareDates(a, b) < 0)
		{
			start.setTime(a);
			end.setTime(b);
		}
		else
		{
			start.setTime(b);
			end.setTime(a);
		}
		int wd = start.get(Calendar.DAY_OF_WEEK);
		while ((start.getTime().compareTo(end.getTime()) <= 0) && (wd != Calendar.SUNDAY))
		{
			if (wd != Calendar.SATURDAY)
			{
				ans++;
			}
			start.add(Calendar.DATE, 1);
			wd = start.get(Calendar.DAY_OF_WEEK);
		}

		wd = end.get(Calendar.DAY_OF_WEEK);
		while ((start.getTime().compareTo(end.getTime()) <= 0) && (wd != Calendar.SUNDAY))
		{
			if (wd != Calendar.SATURDAY)
			{
				ans++;
			}
			end.add(Calendar.DATE, -1);
			wd = end.get(Calendar.DAY_OF_WEEK);
		}
		ans += 5 * Math.floor(daysBetween(start.getTime(), end.getTime()) / 7.0);
		return ans;
	}

	public static Map<Integer, String> getMonthsMap()
	{
		final Map<Integer, String> monthMap = new HashMap<Integer, String>();
		final List<String> months = getMonths();
		for (int month = Calendar.JANUARY; month <= Calendar.DECEMBER; month++)
		{
			monthMap.put(month, months.get(month));
		}
		return monthMap;
	}

	final static private String[]	_months	= new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November",
			"December"						};

	/**
	 * Return January 1st for the current year
	 * 
	 * @return
	 */
	public static Date getBeginDateForYear()
	{
		return createDate(1, 1, getYear(new Date()));
	}

	/**
	 * Return December 31st for the current year
	 * 
	 * @return
	 */
	public static Date getEndDateForYear()
	{
		return createDate(31, 12, getYear(new Date()));
	}

	public static List<String> getMonths()
	{
		return Arrays.asList(_months);
	}

	public static int getMonthNumber(final String month)
	{
		for (int i = 0; i <= Calendar.DECEMBER; i++)
		{
			if ((getMonths().get(i)).equals(month))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the first day of a month using the given month name ( for the
	 * current year)
	 * 
	 * @param month
	 */
	static public Date getDateByMonth(final String month, final Integer year)
	{
		if (!getMonths().contains(month) || (year == null))
		{
			return null;
		}
		return createDate(1, getMonthNumber(month) + 1, year);
	}

	static public Date getDateNextMonth(final String month, Integer year)
	{
		if (!getMonths().contains(month) || (year == null))
		{
			return null;
		}
		int nextMonth = DateUtil.getMonthNumber(month) + 1;
		if (nextMonth > Calendar.DECEMBER)
		{
			year++;
			nextMonth = Calendar.JANUARY;
		}
		return createDate(1, nextMonth, year);
	}

	public static List getYearRange(final int minYear, final int maxYear)
	{
		final List<Integer> years = new ArrayList<Integer>();
		for (int year = minYear; year <= minYear + maxYear; year++)
		{
			years.add(year);
		}
		return years;
	}

	public static Calendar getLastSecondOfADay(final Date date)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar;
	}

	public static Integer[] getDaysAndHoursBetween(final Double days)
	{
		final int daysCount = days.intValue();
		final int hours = (int) ((days - (daysCount)) * 24);
		return new Integer[] { daysCount, hours };
	}

	/**
	 * return true if this date is included in this year
	 * */
	public static Boolean isACurrentYearDate(final Date date)
	{
		if (date == null)
		{
			return Boolean.FALSE;
		}
		return (DateUtil.getField(date, DateUtil.YEAR) == Calendar.getInstance().get(Calendar.YEAR));
	}

	/**
	 * return true if this date is included in this month
	 * */
	public static Boolean isACurrentMonthDate(final Date date)
	{
		if (date == null)
		{
			return Boolean.FALSE;
		}
		// since in calendar January is 0 and not 1(first month).
		return (DateUtil.getField(date, DateUtil.MONTH) == (Calendar.getInstance().get(Calendar.MONTH) + 1));
	}

	public static int getYear(final Date date)
	{
		final Calendar calendar = GregorianCalendar.getInstance();
		calendar.clear();
		calendar.setTime(date);
		return calendar.get(Calendar.YEAR);
	}

	public static String daysBeetwen2Dates(final Date from, final Date to)
	{
		if ((from == null) || (to == null))
		{
			return null;
		}
		return String.valueOf((long) daysDifference(from, to));
	}

	public static String getShortDate(final Date date)
	{
		final String pattern = DateUtil.formatYear("MM/dd/yyyy");
		final SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);

	}

	public static boolean equalFieldValue(final Date dateA, final Date dateB, final int field)
	{
		return (getField(dateA, field) == getField(dateB, field));
	}

	public static boolean inTheSameMonth(final Date dateA, final Date dateB)
	{
		return equalFieldValue(dateA, dateB, Calendar.MONTH);
	}

	public static Calendar getDate(final long millis)
	{
		final Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return cal;
	}

	public static int getCurrentYear()
	{
		return getYear(new Date());
	}
}