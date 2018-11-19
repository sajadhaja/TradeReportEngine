package com.jpm.trade.reportengine.entity;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * Object represents the price details of an instruction
 *
 * @author Sajadh
 *
 */
public class InstructionPriceInfo {

	private final Currency currency;
	private final BigDecimal agreedFx;
	private final int units;
	private final BigDecimal pricePerUnit;
	private final BigDecimal tradeAmount;

	public InstructionPriceInfo(Currency currency, BigDecimal agreedFx, int units, BigDecimal pricePerUnit) {
		this.currency = currency;
		this.agreedFx = agreedFx;
		this.units = units;
		this.pricePerUnit = pricePerUnit;
		tradeAmount = calculateAmount(this);
	}

	/**
	 * 
	 * @return currency of the instruction
	 */
	public Currency getCurrency() {
		return currency;
	}

	/**
	 * 
	 * @return foreign exchange rate with respect to USD
	 */
	public BigDecimal getAgreedFx() {
		return agreedFx;
	}

	/**
	 * 
	 * @return Number of shares to be bought or sold
	 */
	public int getUnits() {
		return units;
	}

	/**
	 * 
	 * @return The price per unit
	 */
	public BigDecimal getPricePerUnit() {
		return pricePerUnit;
	}

	/**
	 * 
	 * @return The total trade amount in USD
	 */
	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	private static BigDecimal calculateAmount(InstructionPriceInfo ins) {
		return ins.getPricePerUnit().multiply(BigDecimal.valueOf(ins.getUnits())).multiply(ins.getAgreedFx());
	}
}
