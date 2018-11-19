package com.jpm.trade.reportengine.service;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.jpm.trade.reportengine.entity.Instruction;
import com.jpm.trade.reportengine.entity.InstructionPriceInfo;
import com.jpm.trade.reportengine.entity.TradeType;
import com.jpm.trade.reportengine.exception.ReportEngineException;

/**
 * Unit test for simple App.
 */
public class ReportServiceTest {
	private static final LocalDate MONDAY = LocalDate.of(2018, 8, 20);
	private static final LocalDate TUESDAY = LocalDate.of(2018, 8, 21);
	private static final LocalDate WEDNESDAY = LocalDate.of(2018, 8, 22);
	private static final LocalDate SATURDAY = LocalDate.of(2018, 8, 18);
	private static final LocalDate SUNDAY = LocalDate.of(2018, 8, 19);

	private ReportService service;
	private List<Instruction> instructions;

	@Before
	public void setup() throws ReportEngineException {
		instructions = new ArrayList<>();
		instructions
				.add(new Instruction("E1", TradeType.fromString("B"), LocalDate.of(2017, 3, 10), MONDAY, new InstructionPriceInfo(
						Currency.getInstance("AED"), BigDecimal.valueOf(1), 100, BigDecimal.valueOf(1))));
		instructions
				.add(new Instruction("E2", TradeType.fromString("B"), LocalDate.of(2017, 3, 10), MONDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 200, BigDecimal.valueOf(1))));
		instructions
				.add(new Instruction("E3", TradeType.fromString("B"), LocalDate.of(2017, 3, 10), SATURDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 300, BigDecimal.valueOf(1))));
		instructions
				.add(new Instruction("E4", TradeType.fromString("S"), LocalDate.of(2017, 3, 10), SUNDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 200, BigDecimal.valueOf(1))));
		instructions
				.add(new Instruction("E5", TradeType.fromString("B"), LocalDate.of(2017, 3, 10), TUESDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 400, BigDecimal.valueOf(1))));
		instructions
				.add(new Instruction("E6", TradeType.fromString("S"), LocalDate.of(2017, 3, 10), TUESDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 1000, BigDecimal.valueOf(1))));
		instructions.add(
				new Instruction("E2", TradeType.fromString("B"), LocalDate.of(2017, 3, 10), WEDNESDAY, new InstructionPriceInfo(
						Currency.getInstance("SGD"), BigDecimal.valueOf(1), 7000, BigDecimal.valueOf(1))));

		service = new ReportService();
		instructions = service.readAndAdjustSettlementDate(instructions);
	}

	@Test
	public void testDailyIncomingAmount() throws Exception {
		final Map<LocalDate, BigDecimal> dailyIncomingAmount = service.calculateDailyIncomingAmount(instructions);

		assertEquals(2, dailyIncomingAmount.size());
		assertTrue(Objects.equals(dailyIncomingAmount.get(MONDAY),
				BigDecimal.valueOf(200.00).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		assertTrue(Objects.equals(dailyIncomingAmount.get(TUESDAY),
				BigDecimal.valueOf(1000.00).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
	}

	@Test
	public void testDailyOutgoingAmount() throws Exception {
		final Map<LocalDate, BigDecimal> dailyOutgoingAmount = service.calculateDailyOutgoingAmount(instructions);

		assertEquals(3, dailyOutgoingAmount.size());
		assertTrue(Objects.equals(dailyOutgoingAmount.get(MONDAY),
				BigDecimal.valueOf(600.00).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
		assertTrue(Objects.equals(dailyOutgoingAmount.get(TUESDAY),
				BigDecimal.valueOf(400.00).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
	}

	@Test
	public void testIncomingRanking() throws Exception {
		final Map<String, BigDecimal> incomingRanking = service.calculateIncomingRanking(instructions);

		assertEquals(2, incomingRanking.size());

		assertTrue(incomingRanking.containsKey("E6"));
		assertTrue(incomingRanking.containsKey("E4"));

		assertEquals("200.00", incomingRanking.get("E4").toString());

	}

	@Test
	public void testOutgoingRanking() throws Exception {
		final Map<String, BigDecimal> outgoingRanking = service.calculateOutgoingRanking(instructions);

		assertEquals(4, outgoingRanking.size());

		assertTrue(outgoingRanking.containsKey("E1"));
		assertTrue(outgoingRanking.containsKey("E2"));
		assertTrue(outgoingRanking.containsKey("E3"));
		assertTrue(outgoingRanking.containsKey("E5"));

		assertEquals("100.00", outgoingRanking.get("E1").toString());
		assertEquals("7200.00", outgoingRanking.get("E2").toString());
	}
}
