package com.reduceco2now.co2;

public record Co2Estimate(
        long foodId,
        double grams,
        double co2eGrams,
        String methodologyVersion,
        int confidence
) {}