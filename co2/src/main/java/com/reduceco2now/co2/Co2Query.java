package com.reduceco2now.co2;

import java.util.Optional;

public interface Co2Query {
    Optional<Co2Estimate> estimate(long foodId, double grams);
}