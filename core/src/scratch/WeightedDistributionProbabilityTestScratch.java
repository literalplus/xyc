/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

import li.l1t.common.util.math.WeightedDistribution;

import java.security.SecureRandom;
import java.text.MessageFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class WeightedDistributionProbabilityTestScratch {
    public static void main (String... args) {
        WeightedDistribution<Integer> dist = new WeightedDistribution<>();
        SecureRandom random = new SecureRandom();
        IntStream.range(0, 15)
                .forEach(lel -> dist.put(lel, 1));
        IntStream.range(16, 20)
                .forEach(lel -> dist.put(lel, 5));
        Stream.generate(() -> dist.next(random))
                .limit(5_000_000)
                .collect(Collectors.groupingBy(Function.identity()))
                .entrySet().stream()
                .map(e -> MessageFormat.format(" -> {0} = {1}% ^ {2}", e.getKey(), (
                        (double) e.getValue().size() / 5_000_000D) * 100D, expectedProbability(dist, e)))
                .forEach(System.out::println);
    }

    private static double expectedProbability(WeightedDistribution<Integer> dist, Map.Entry<Integer, List<Integer>> e) {
        double prob = dist.probabilities().entrySet().stream()
                .filter(e2 -> e2.getValue().equals(e.getKey()))
                .mapToDouble(Map.Entry::getKey)
                .findFirst().orElseThrow(AssertionError::new);
        Double lowerKey = Optional.ofNullable(dist.probabilities().lowerKey(prob)).orElse(0D);
        return ((prob - lowerKey) / dist.probabilitySum()) * 100;
    }
}