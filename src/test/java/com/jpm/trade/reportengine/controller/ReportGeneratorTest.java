package com.jpm.trade.reportengine.controller;

import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jpm.trade.reportengine.exception.ReportEngineException;
import com.jpm.trade.reportengine.util.Constants;

public class ReportGeneratorTest {

	private ReportGenerator controller;

	@Before
	public void setup() throws ReportEngineException {
		Constants.INSTRUCTION_FILENAME = "instructions.csv";
		controller = new ReportGenerator();
	}

	@Test
	public void testFullReportGeneration() throws Exception {
		final String output = controller.generateInstructionsReport();
		assertEquals(1528, output.length());
		assertEquals(44, output.split("\r\n|\r|\n").length);
	}
}
