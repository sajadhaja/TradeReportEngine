package com.jpm.trade.reportengine.reader;

import java.util.List;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.exception.ReportEngineException;

/**
 * Reader interface : Fetching instructions from the source (DB, FileSystem,
 * Queue etc.. )
 *
 * @author Sajadh
 *
 */
public interface InstructionReader {

	/**
	 * Read instruction given by clients
	 * 
	 * @return List of instructions
	 * @throws ReportEngineException If any exceptions
	 */
	public List<Instruction> getInstructions() throws ReportEngineException;
}
