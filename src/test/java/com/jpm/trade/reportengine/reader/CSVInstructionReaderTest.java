package com.jpm.trade.reportengine.reader;

import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jpm.trade.reportengine.exception.ReportEngineException;
import com.jpm.trade.reportengine.reader.impl.CSVInstructionReader;
import com.jpm.trade.reportengine.util.Constants;

public class CSVInstructionReaderTest {

	private CSVInstructionReader reader;

	@Before
	public void setup() {

	}

	@Test(expected = ReportEngineException.class)
	public void shouldThrowExceptionWhenDateUnparsable() throws ReportEngineException {
		Constants.INSTRUCTION_FILENAME = "instructions-error1.csv";
		reader = new CSVInstructionReader();
		reader.getInstructions();
	}

	@Test
	public void shouldExceptionReadableWhenDateUnparsable() {
		Constants.INSTRUCTION_FILENAME = "instructions-error1.csv";
		reader = new CSVInstructionReader();
		try {
			reader.getInstructions();
		} catch (final ReportEngineException e) {
			assertEquals("Error on reading CSV due invalid date format, LineNumber: 01", e.getMessage());
		}
	}

	@Test(expected = ReportEngineException.class)
	public void shouldThrowExceptionWhenEmptyExchangeRate() throws ReportEngineException {
		Constants.INSTRUCTION_FILENAME = "instructions-error2.csv";
		reader = new CSVInstructionReader();
		reader.getInstructions();
	}

	@Test
	public void shouldExceptionReadableWhenEmptyExchangeRate() {
		Constants.INSTRUCTION_FILENAME = "instructions-error2.csv";
		reader = new CSVInstructionReader();
		try {
			reader.getInstructions();
		} catch (final ReportEngineException e) {
			assertEquals("Error on reading CSV due to invalid decimal number, LineNumber: 01", e.getMessage());
		}
	}

	@Test
	public void shouldExceptionReadableWhenColumnMissing() {
		Constants.INSTRUCTION_FILENAME = "instructions-error3.csv";
		reader = new CSVInstructionReader();
		try {
			reader.getInstructions();
		} catch (final ReportEngineException e) {
			assertEquals("Error on reading CSV due to missing of column, LineNumber: 01", e.getMessage());
		}
	}

}
