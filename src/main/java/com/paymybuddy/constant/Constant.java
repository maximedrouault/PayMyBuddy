package com.paymybuddy.constant;

import java.math.BigDecimal;

public final class Constant {


    public static final String MIN_AMOUNT = "0.01";

    public static final String MAX_AMOUNT = "99999999.99";

    public static final BigDecimal MAX_BALANCE = new BigDecimal("99999999.99");

    public static final BigDecimal COMMISSION_FACTOR = new BigDecimal("0.05"); // 0.05 is the commission factor (5%)
}