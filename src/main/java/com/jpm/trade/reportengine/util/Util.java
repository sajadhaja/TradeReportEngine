package com.jpm.trade.reportengine.util;

import static com.jpm.trade.reportengine.util.Constants.AED;
import static com.jpm.trade.reportengine.util.Constants.SAR;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.Currency;
import java.util.Optional;
import java.util.function.Function;

import com.jpm.trade.reportengine.entity.Instruction;

/**
 * Utility functions , used to define all common utility functions which can
 * reuse through out application
 *
 * @author Sajadh
 *
 */
public class Util {

	/**
	 * Trading should be settle only on weekdays, this function will adjust
	 * settlement day if it is holiday. Also holidays will change based on the
	 * currency. If AED or SAR then holidays are Friday and Saturday, for all other
	 * currency holiday will be Saturday and Sunday
	 * 
	 * @return Function with changed Instruction
	 */
	public static Function<? super Instruction, ? extends Instruction> adjustSettlementDate() {
		return instruction -> {
			final Currency currency = instruction.getDetails().getCurrency();
			adjustSettlementDate(instruction.getSettlementDate(), currency.getCurrencyCode())
					.ifPresent(adjustDate -> instruction.setSettlementDate(adjustDate));
			return instruction;
		};
	}

	private static Optional<LocalDate> adjustSettlementDate(LocalDate settlementDate, String currencyCode) {
		LocalDate adjustedSettlementDate = null;
		if (AED.equals(currencyCode) || SAR.equals(currencyCode)) {
			if (settlementDate.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
				adjustedSettlementDate = settlementDate.plus(Period.ofDays(2));
			} else if (settlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				adjustedSettlementDate = settlementDate.plus(Period.ofDays(1));
			}
		} else {
			if (settlementDate.getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
				adjustedSettlementDate = settlementDate.plus(Period.ofDays(2));
			} else if (settlementDate.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
				adjustedSettlementDate = settlementDate.plus(Period.ofDays(1));
			}
		}
		return Optional.ofNullable(adjustedSettlementDate);
	}

	/**
	 * String util function to check is null or empty.
	 * 
	 * @param val Input String
	 * @return boolean flag, If empty return true else false
	 */
	public static boolean isEmpty(String val) {
		return val == null || val.isEmpty();
	}

}
