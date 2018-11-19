package com.jpm.trade.reportengine.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Object represent Instruction sent by various clients for buy and sell
 *
 * @author Sajadh
 *
 */
public class Instruction {

	private String entity;
	private TradeType tradeType;
	private LocalDate instructionDate;
	private LocalDate settlementDate;
	private InstructionPriceInfo details;

	public Instruction(String entity, TradeType tradeType, LocalDate instructionDate, LocalDate settlementDate,
			InstructionPriceInfo details) {
		this.entity = entity;
		this.tradeType = tradeType;
		this.instructionDate = instructionDate;
		this.settlementDate = settlementDate;
		this.details = details;
	}

	public Instruction() {
	}

	/**
	 * 
	 * @return Financial entity
	 */
	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	/**
	 * 
	 * @return represent instruction is buy or sell
	 */
	public TradeType getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeType tradeType) {
		this.tradeType = tradeType;
	}

	public void setInstructionDate(LocalDate instructionDate) {
		this.instructionDate = instructionDate;
	}

	/**
	 * 
	 * @return Date on which instruction to be settled
	 */
	public LocalDate getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(LocalDate newDate) {
		settlementDate = newDate;
	}

	/**
	 * 
	 * @return price details of an instruction
	 */
	public InstructionPriceInfo getDetails() {
		return details;
	}

	public void setDetails(InstructionPriceInfo details) {
		this.details = details;
	}

	public BigDecimal getTradeAmount() {
		return getDetails().getTradeAmount().setScale(2, BigDecimal.ROUND_HALF_EVEN);
	}

}
