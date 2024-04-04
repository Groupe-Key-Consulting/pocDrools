package com.kc.poc.drools.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

//	public static double days360Ratio(LocalDate startDate, LocalDate endDate) {
//		return days360(startDate, endDate) / 360.0;
//	}
//
//	public static boolean isLastDayOfFebruary(LocalDate date) {
//		return date.getMonthOfYear() == 2 && date.getDayOfMonth() == date.dayOfMonth().withMaximumValue().getDayOfMonth();
//	}
//
//	public static LocalDate mostRecent(LocalDate date1, LocalDate date2) {
//		if (date1 != null && date1.isAfter(date2)) {
//			return date1;
//		} else {
//			return date2;
//		}
//	}
//
//	public static LocalDate mostOlder(LocalDate date1, LocalDate date2) {
//		if (date1 != null && date1.isBefore(date2)) {
//			return date1;
//		} else {
//			return date2;
//		}
//	}
//
//	// cf. method org.apache.poi.ss.formula.functions.Days360
//	public static int days360(LocalDate startDate, LocalDate endDate) {
//		boolean useEuropeanMethod = false; // https://support.microsoft.com/en-us/kb/235575
//		int startingDate[] = getStartingDate(startDate, useEuropeanMethod);
//		int endingDate[] = getEndingDate(endDate, startingDate, useEuropeanMethod);
//		return (endingDate[0] * 360 + endingDate[1] * 30 + endingDate[2]) - (startingDate[0] * 360 + startingDate[1] * 30 + startingDate[2]);
//	}
//
    // cf. method org.apache.poi.ss.formula.functions.Days360
    public static int dateDifMonths(LocalDate startDate, LocalDate endDate) {
        return (int) ChronoUnit.MONTHS.between(startDate, endDate);
    }
//
//	private static int[] getStartingDate(LocalDate date, boolean method) {
//		int yyyy = date.getYear();
//		int mm = date.getMonthOfYear();
//		int dd = Math.min(30, date.getDayOfMonth());
//		if (method == false && isLastDayOfMonth(date)) {
//			dd = 30;
//		}
//		return new int[] { yyyy, mm, dd };
//	}
//
//	private static int[] getEndingDate(LocalDate realEnd, int startingDate[], boolean method) {
//		LocalDate d = realEnd;
//		int yyyy = d.getYear();
//		int mm = d.getMonthOfYear();
//		int dd = Math.min(30, d.getDayOfMonth());
//		if (method == false && realEnd.getDayOfMonth() == 31) {
//			if (startingDate[2] < 30) {
//				d = new LocalDate(d.getYear(), d.getMonthOfYear(), 1);
//				d = d.plusMonths(1);
//				yyyy = d.getYear();
//				mm = d.getMonthOfYear();
//				dd = 1;
//			} else {
//				dd = 30;
//			}
//		}
//		return new int[] { yyyy, mm, dd };
//	}
//
//	private static boolean isLastDayOfMonth(LocalDate date) {
//		int dayOfMonth = date.getDayOfMonth();
//		int lastDayOfMonth = date.dayOfMonth().withMaximumValue().getDayOfMonth();
//		return (dayOfMonth == lastDayOfMonth);
//	}
//
//	/** get the usage ratio for the specified year. */
//	public static double calculateUsageRatio(LocalDate startDate, LocalDate endDate, ContractualYear year) {
//		double useRatio = 0;
//		if (endDate == null) {
//			endDate = year.getEndDate();
//		}
//		if (startDate != null && !endDate.isBefore(year.getStartDate()) && !startDate.isAfter(year.getEndDate())) {
//			if (DateUtil.days360Ratio(startDate, year.getEndDate()) <= 1) {
//				if (endDate.isBefore(year.getEndDate())) {
//					useRatio = 1 - DateUtil.days360Ratio(year.getStartDate(), startDate) - DateUtil.days360Ratio(endDate, year.getEndDate());
//				} else {
//					useRatio = DateUtil.days360Ratio(startDate, year.getEndDate());
//				}
//			} else {
//				if (endDate.isBefore(year.getEndDate())) {
//					useRatio = 1 - DateUtil.days360Ratio(endDate, year.getEndDate());
//				} else {
//					useRatio = 1;
//				}
//			}
//		}
//		return useRatio;
//	}
//
}
