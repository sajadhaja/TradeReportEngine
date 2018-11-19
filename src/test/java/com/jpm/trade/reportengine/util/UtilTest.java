package com.jpm.trade.reportengine.util;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import org.junit.Test;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.entity.InstructionPriceInfo;
import com.jpm.trade.reportengine.entity.TradeType;

public class UtilTest {
	
	private static final LocalDate FRIDAY = LocalDate.of(2018, 8, 17);
	private static final LocalDate SATURDAY = LocalDate.of(2018, 8, 18);
	private static final LocalDate SUNDAY = LocalDate.of(2018, 8, 19);
	private static final LocalDate MONDAY = LocalDate.of(2018, 8, 20);
	private final List<Instruction> instructions = new ArrayList<>();


	@Test
	public void shoudAdjustSettlementDateWhenCurrencyAED() {
		instructions.add(new Instruction("E4", TradeType.fromString("S"), LocalDate.of(2017, 3, 10), FRIDAY,
				new InstructionPriceInfo(Currency.getInstance("AED"), BigDecimal.valueOf(1), 200,
						BigDecimal.valueOf(1))));
		instructions.stream().map(Util.adjustSettlementDate()).forEach(instruction-> {
			assertEquals(SUNDAY, instruction.getSettlementDate());
		});;
	}

	@Test
	public void shoudAdjustSettlementDateWhenCurrencySAR() {
		instructions.add(new Instruction("E4", TradeType.fromString("S"), LocalDate.of(2017, 3, 10), SATURDAY,
				new InstructionPriceInfo(Currency.getInstance("AED"), BigDecimal.valueOf(1), 200,
						BigDecimal.valueOf(1))));
		instructions.stream().map(Util.adjustSettlementDate()).forEach(instruction-> {
			assertEquals(SUNDAY, instruction.getSettlementDate());
		});;
	}
	
	@Test
	public void shoudAdjustSettlementDateWhenAllOtherCurrency() {
		instructions.add(new Instruction("E4", TradeType.fromString("S"), LocalDate.of(2017, 3, 10), SATURDAY,
				new InstructionPriceInfo(Currency.getInstance("SGD"), BigDecimal.valueOf(1), 200,
						BigDecimal.valueOf(1))));
		instructions.stream().map(Util.adjustSettlementDate()).forEach(instruction-> {
			assertEquals(MONDAY, instruction.getSettlementDate());
		});;
	}
}
