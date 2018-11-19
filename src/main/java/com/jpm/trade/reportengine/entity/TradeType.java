package com.jpm.trade.reportengine.entity;

import com.jpm.trade.reportengine.util.Util;

/**
 * Object represents the type of instruction either Buy or Sell
 *
 * @author Sajadh
 *
 */
public enum TradeType {
	BUY("B"), SELL("S");

	private String text;

	TradeType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	/**
	 * It will used to construct the trade type object
	 * 
	 * @param text tradetype in string
	 * @return enum object of TradeType
	 */
	public static TradeType fromString(String text) {
		if (Util.isEmpty(text)) {
			throw new IllegalArgumentException("Trade Type should not be empty");
		}

		for (final TradeType tmp : TradeType.values()) {
			if (tmp.text.equalsIgnoreCase(text)) {
				return tmp;
			}
		}
		throw new IllegalArgumentException("Trade Type should be either B or S denotes Buy and Sell respectively");
	}
}
