package com.jpm.trade.reportengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jpm.trade.reportengine.controller.ReportGenerator;
import com.jpm.trade.reportengine.exception.ReportEngineException;

/**
 * Report Engine application
 *
 * @author Sajadh
 *
 */
public class ReportEngineApplication {
	private static final Logger LOG = LoggerFactory.getLogger(ReportEngineApplication.class);

	private static ReportGenerator reportGenerator;

	public static void main(String[] args) {
		LOG.debug("Generating report ...");
		try {
			reportGenerator = new ReportGenerator();
			LOG.info(reportGenerator.generateInstructionsReport());
		} catch (final ReportEngineException e) {
			LOG.error("Exception while generating the report:", e);
		}
		LOG.debug("Succesfully generated report...");
	}
}
