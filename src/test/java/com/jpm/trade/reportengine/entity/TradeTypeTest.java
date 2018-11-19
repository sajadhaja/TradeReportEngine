package com.jpm.trade.reportengine.entity;

import static junit.framework.TestCase.assertEquals;

import org.junit.Test;

public class TradeTypeTest {

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenEmptyType() {
		TradeType.fromString("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void shouldThrowExceptionWhenInvalidType() {
		TradeType.fromString("F");
	}

	@Test
	public void shouldReturnTradeWhenPassStringType() {
		assertEquals(TradeType.BUY, TradeType.fromString("B"));
		assertEquals(TradeType.SELL, TradeType.fromString("S"));
		assertEquals(TradeType.SELL.getText(), TradeType.fromString("S").getText());
		assertEquals(TradeType.BUY.getText(), TradeType.fromString("B").getText());
	}
}
