package com.paymybuddy.constant;

import java.math.BigDecimal;

/**
 * This class holds the constant values used throughout the PayMyBuddy application.
 * It includes constants for minimum and maximum transaction amounts, maximum account balance, and the commission factor for transactions.
 */
public final class Constant {

    /**
     * The minimum transaction amount allowed in the application.
     * This is set to 0.01.
     */
    public static final String MIN_AMOUNT = "0.01";

    /**
     * The maximum transaction amount allowed in the application.
     * This is set to 99999999.99.
     */
    public static final String MAX_AMOUNT = "99999999.99";

    /**
     * The maximum account balance allowed in the application.
     * This is set to 99999999.99.
     */
    public static final BigDecimal MAX_BALANCE = new BigDecimal("99999999.99");

    /**
     * The commission factor for transactions in the application.
     * This is set to 0.05, representing a 5% commission on transactions.
     */
    public static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.05");
}