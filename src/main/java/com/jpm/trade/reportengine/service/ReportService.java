package com.jpm.trade.reportengine.service;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.reducing;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.entity.TradeType;
import com.jpm.trade.reportengine.exception.ReportEngineException;
import com.jpm.trade.reportengine.util.Util;

/**
 * Service class which generate various reports for the client instructions
 *
 * @author Sajadh
 *
 */
public class ReportService {

	/**
	 * Creates outgoing predicate
	 */
	private final static Predicate<Instruction> outgoingInstructionsPredicate = instruction -> instruction
			.getTradeType().equals(TradeType.BUY);

	/**
	 * Creates incoming predicate
	 */
	private final static Predicate<Instruction> incomingInstructionsPredicate = instruction -> instruction
			.getTradeType().equals(TradeType.SELL);

	/**
	 * Read instruction from source and adjust settlement date according to the
	 * holiday
	 * 
	 * @return List of instruction
	 * @throws ReportEngineException if any exception happens
	 */
	public List<Instruction> readAndAdjustSettlementDate(List<Instruction> instructions) throws ReportEngineException {
		return instructions.stream().map(Util.adjustSettlementDate())
				.sorted(Comparator.comparing(Instruction::getSettlementDate)).collect(Collectors.toList());
	}

	/**
	 * Calculates the daily incoming trade amount in USD
	 *
	 * @param instructions list of instructions to calculate
	 * @return map with date as key and total amount as value
	 */
	public Map<LocalDate, BigDecimal> calculateDailyIncomingAmount(List<Instruction> instructions) {
		return calculateDailyAmount(instructions, incomingInstructionsPredicate);
	}

	/**
	 * Calculates the daily outgoing trade amount in USD
	 *
	 * @param instructions list of instructions to calculate
	 * @return map with date as key and total amount as value
	 */
	public Map<LocalDate, BigDecimal> calculateDailyOutgoingAmount(List<Instruction> instructions) {
		return calculateDailyAmount(instructions, outgoingInstructionsPredicate);
	}

	/**
	 * Ranks the incoming by trade amount in USD
	 *
	 * @param instructions list of instructions to calculate
	 * @return map with entity name as key and total amount as value
	 */
	public Map<String, BigDecimal> calculateIncomingRanking(List<Instruction> instructions) {
		return calculateRankingByWhole(instructions, incomingInstructionsPredicate);
	}

	/**
	 * Ranks the outgoing by trade amount in USD
	 *
	 * @param instructions list of instructions to calculate
	 * @return map with entity name as key and total amount as value
	 */
	public Map<String, BigDecimal> calculateOutgoingRanking(List<Instruction> instructions) {
		return calculateRankingByWhole(instructions, outgoingInstructionsPredicate);
	}

	/**
	 * Calculate the daily trade amount in USD by predicates
	 * 
	 * @param instructions list of instructions to calculate
	 * @param predicate    the filter conditions for the list of instructions
	 * @return map with date as key and total amount as value
	 */
	private Map<LocalDate, BigDecimal> calculateDailyAmount(List<Instruction> instructions,
			Predicate<Instruction> predicate) {
		return instructions.stream().filter(predicate).collect(groupingBy(Instruction::getSettlementDate,
				mapping(Instruction::getTradeAmount, reducing(BigDecimal.ZERO, BigDecimal::add))));
	}

	/**
	 * Ranks by trade amount in USD
	 * 
	 * @param instructions list of instructions to calculate
	 * @param predicate    the filter conditions for the list of instructions
	 * @return map with entity name as key and total amount as value
	 */
	private Map<String, BigDecimal> calculateRankingByWhole(List<Instruction> instructions,
			Predicate<Instruction> predicate) {
		return instructions.stream().filter(predicate)
				.collect(groupingBy(Instruction::getEntity,
						mapping(Instruction::getTradeAmount, reducing(BigDecimal.ZERO, BigDecimal::add))))
				.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
						LinkedHashMap::new));
	}
}
