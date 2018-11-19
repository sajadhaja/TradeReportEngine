package com.jpm.trade.reportengine.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.exception.ReportEngineException;
import com.jpm.trade.reportengine.reader.InstructionReader;
import com.jpm.trade.reportengine.reader.impl.CSVInstructionReader;
import com.jpm.trade.reportengine.service.ReportService;

/**
 * Controller class to generate report
 *
 * @author Sajadh
 *
 */
public class ReportGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(ReportGenerator.class);

	private final InstructionReader reader;
	private final ReportService service;

	public ReportGenerator() {
		reader = new CSVInstructionReader();
		service = new ReportService();
	}

	/**
	 * Generate four reports i) Outgoing ranking ii) Incoming ranking iii) Daily
	 * incoming amount iv) Daily outgoing amount
	 * 
	 * @return String contains report
	 * @throws ReportEngineException if any exception happens
	 */
	public String generateInstructionsReport() throws ReportEngineException {
		final List<Instruction> instructions = service.readAndAdjustSettlementDate(reader.getInstructions());
		final StringBuilder output = new StringBuilder();

		// Building reports and appending to string
		output.append(generateOutgoingRanking(instructions));
		output.append(generateIncomingRanking(instructions));
		output.append(generateDailyIncomingAmount(instructions));
		output.append(generateDailyOutgoingAmount(instructions));
		return output.toString();
	}

	/**
	 * Generate daily outgoing report by amount
	 * 
	 * @param instructions list of instructions
	 * @return String report contains Date and trade amount in $
	 */
	private String generateDailyOutgoingAmount(List<Instruction> instructions) {
		LOG.debug("Generating Report Daily Outgoing amount ...");
		final Map<LocalDate, BigDecimal> dailyOutgoingAmount = service.calculateDailyOutgoingAmount(instructions);

		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n----------------------------------------\n")
				.append("         Outgoing Daily Amount          \n")
				.append("----------------------------------------\n")
				.append("      Date       |  Trade Amount (in $) \n")
				.append("----------------------------------------\n");

		for (final LocalDate date : dailyOutgoingAmount.keySet()) {
			stringBuilder.append(date).append("       |      ").append(dailyOutgoingAmount.get(date)).append("\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * Generate daily incoming report by amount
	 * 
	 * @param instructions list of instructions
	 * @return String report contains Date and trade amount in $
	 */
	private String generateDailyIncomingAmount(List<Instruction> instructions) {
		LOG.debug("Generating Report Daily inoming amount ...");
		final Map<LocalDate, BigDecimal> dailyOutgoingAmount = service.calculateDailyIncomingAmount(instructions);
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n----------------------------------------\n")
				.append("         Incoming Daily Amount          \n")
				.append("----------------------------------------\n")
				.append("      Date       |  Trade Amount (in $) \n")
				.append("----------------------------------------\n");

		for (final LocalDate date : dailyOutgoingAmount.keySet()) {
			stringBuilder.append(date).append("       |      ").append(dailyOutgoingAmount.get(date)).append("\n");
		}
		return stringBuilder.toString();
	}

	/**
	 * Generate outgoing rank based on trade amount
	 * 
	 * @param instructions list of instructions
	 * @return String report contains Rank, Entity name and trade amount in $
	 */
	private String generateOutgoingRanking(List<Instruction> instructions) {
		LOG.debug("Generating Report Outgoing rank ...");
		final Map<String, BigDecimal> dailyOutgoingRanking = service.calculateOutgoingRanking(instructions);
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n----------------------------------------\n")
				.append("            Outgoing Ranking            \n")
				.append("----------------------------------------\n")
				.append("     Rank    |     Entity   | Amount (in $) \n")
				.append("----------------------------------------\n");
		int rank = 1;
		for (final String entity : dailyOutgoingRanking.keySet()) {
			stringBuilder.append(rank).append("   |      ").append(entity).append("     |    ")
					.append(dailyOutgoingRanking.get(entity)).append("\n");
			rank++;

		}
		return stringBuilder.toString();
	}

	/**
	 * Generate incoming rank based on trade amount
	 * 
	 * @param instructions list of instructions
	 * @return String report contains Rank, Entity name and trade amount in $
	 */
	private String generateIncomingRanking(List<Instruction> instructions) {
		LOG.debug("Generating Report incoming rank ...");
		final Map<String, BigDecimal> dailyIncomingRanking = service.calculateIncomingRanking(instructions);
		final StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("\n----------------------------------------\n")
				.append("            Incoming Ranking            \n")
				.append("----------------------------------------\n")
				.append("     Rank    |     Entity   | Amount (in $) \n")
				.append("----------------------------------------\n");

		int rank = 1;
		for (final String entity : dailyIncomingRanking.keySet()) {

			stringBuilder.append(rank).append("   |      ").append(entity).append("     |    ")
					.append(dailyIncomingRanking.get(entity)).append("\n");
			rank++;
		}
		return stringBuilder.toString();
	}

}
