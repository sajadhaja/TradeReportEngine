[![Build Status](https://travis-ci.org/sajadhaja/TradeReportEngine.svg?branch=master)](https://travis-ci.org/sajadhaja/TradeReportEngine)

[![Coverage Status](https://coveralls.io/repos/github/sajadhaja/TradeReportEngine/badge.svg?branch=master)](https://coveralls.io/github/sajadhaja/TradeReportEngine?branch=master)

# TradeReportEngine
Application which generates Trade Reports

# Introduction:
Trade Reporting Engine needs to be devoloped which has input in below format:

| Entity | Buy/Sell | AgreedFx | Currency | InstructionDate | SettlementDate | Units | Price per unit |
| --- | --- |--- |--- |--- |--- |--- |--- |
|foo| B | 0.50 | SGP | 01 Jan 2016 | 02 Jan 2016 | 200 | 100.25 |
| bar | S | 0.22 | AED | 05 Jan 2016 | 07 Jan 2016 | 450 | 150.5 |

1. A work week starts Monday and ends Friday, unless the currency of the trade is AED or SAR, where
the work week starts Sunday and ends Thursday. No other holidays to be taken into account.
2. A trade can only be settled on a working day.
3. If an instructed settlement date falls on a weekend, then the settlement date should be changed to
the next working day.
4. USD amount of a trade = Price per unit * Units * Agreed Fx

# Assumptions:
1. The code will only ever be executed in a single threaded environment
2. We need this to be very lightweight that is why external dependencies are minimized and not done using H2 Embedded DB or SpringBoot containers.
3. All data is in memory
4. Output format is in plain text, printed out to the console.

# What it offers:
This generates the below types of Reports on Daily basis in given dates:

1. Amount in USD settled incoming everyday
2. Amount in USD settled outgoing everyday
3. Ranking of entities based on incoming amount. 
4. Ranking of entities based on outgoing amount. 
    Eg: If entity foo instructs the highest amount for a buy instruction, then foo is rank 1 for outgoing

# Prerequisite:
1. Java8 
2. Apache Maven 3+

# Building and running the program

To build the software: This will run the JUnit tests after the application has been built.

    mvn package


To create javadoc: The generated documentation will be placed in the folder target/site/apidocs

    mvn javadoc:javadoc


To test the software:

    mvn test

To check the code coverage: The generated report will be placed in the folder target/site/jacoco

    mvn test jacoco:report

Example of samples file:

    CSV file is provided in the projects resource folder (src/main/resources).

Instructions to run the software:

    mvn exec:java -Dexec.mainClass="com.jpm.trade.reportengine.ReportEngineApplication"