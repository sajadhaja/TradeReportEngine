package com.jpm.trade.reportengine.reader.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.entity.InstructionPriceInfo;
import com.jpm.trade.reportengine.entity.TradeType;
import com.jpm.trade.reportengine.exception.ReportEngineException;
import com.jpm.trade.reportengine.reader.InstructionReader;
import com.jpm.trade.reportengine.util.Constants;

/**
 * CSV Reader which read Instruction from csv file located in resource folder
 *
 * @author Sajadh
 *
 */
public class CSVInstructionReader implements InstructionReader {

	private static final Logger LOG = LoggerFactory.getLogger(CSVInstructionReader.class);
	private static final String DATE_FORMATTER_PATTERN = "MM-dd-yyyy";

	@Override
	public List<Instruction> getInstructions() throws ReportEngineException {
		LOG.debug("Reading CSV file : {}", Constants.INSTRUCTION_FILENAME);
		String line = null;
		Integer lineNumber = 0;
		final List<Instruction> instructions = new ArrayList<Instruction>();
		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream(Constants.INSTRUCTION_FILENAME)));
		try {
			reader.readLine();
			while ((line = reader.readLine()) != null) {
				final String data[] = line.split(",");
				try {
					final Instruction instruction = constructInstruction(data);
					instructions.add(instruction);
				} catch (final NumberFormatException ex) {
					throw new ReportEngineException(
							"Error on reading CSV due to invalid decimal number, LineNumber: " + lineNumber + 1, ex);
				} catch (final DateTimeParseException ex) {
					throw new ReportEngineException(
							"Error on reading CSV due invalid date format, LineNumber: " + lineNumber + 1, ex);
				} catch (final ArrayIndexOutOfBoundsException ex) {
					throw new ReportEngineException(
							"Error on reading CSV due to missing of column, LineNumber: " + lineNumber + 1, ex);
				}
				lineNumber++;
			}
			LOG.debug("Succesfully read csv file, Found {} instructions", instructions.size());
		} catch (final IOException e) {
			throw new ReportEngineException(e.getMessage(), e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (final IOException e) {
					throw new ReportEngineException(e.getMessage(), e);
				}
			}
		}
		return instructions;
	}

	private Instruction constructInstruction(final String[] data) throws ReportEngineException {
		final Instruction instruction = new Instruction();
		instruction.setEntity(data[0]);
		instruction.setTradeType(TradeType.fromString(data[1]));

		final LocalDate instructionDate = LocalDate.parse(data[4], DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN));
		instruction.setInstructionDate(instructionDate);
		final LocalDate settlementDate = LocalDate.parse(data[5], DateTimeFormatter.ofPattern(DATE_FORMATTER_PATTERN));
		instruction.setSettlementDate(settlementDate);

		final InstructionPriceInfo details = new InstructionPriceInfo(Currency.getInstance(data[3]),
				new BigDecimal(data[2]), Integer.parseInt(data[6]), new BigDecimal(data[7]));
		instruction.setDetails(details);
		return instruction;

	}

}
