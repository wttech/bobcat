/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2019 Cognifide Ltd.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.cognifide.qa.bb.analytics;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.similarity.JaroWinklerDistance;

import net.javacrumbs.jsonunit.core.internal.JsonUtils;
import com.google.common.collect.Streams;
import net.javacrumbs.jsonunit.core.internal.Node;

import static org.junit.jupiter.api.Assertions.fail;

/*
That assertion is based on the json elements similarity.
The similarity is expressed as value between 0 and 1. It's averaged value of similarity for all child nodes. If similarity will be less than 1 assertion will fail.
Assertion ignores order of elements, and elements which are present in "actual" and are absent in "expected".
The similarity is especially useful to find most suitable elements of tables to show differences between them.
*/
public class AssertJson {

    private static final JaroWinklerDistance distanceMeter = new JaroWinklerDistance();

    public static void assertEquals(String message, String expected, String actual) {
        MatchResult match = makeStatistic(JsonUtils.convertToJson(expected, ""),
                JsonUtils.convertToJson(actual, ""), "");

        if (match.matchRatio < 1d) {
            fail(match.difference + "\n\n\nACTUAL:\n" + actual + "\n\nEXPECTED:\n" + expected);
        }
    }

    private static MatchResult makeStatistic(Node expected, Node actual, String description) {
        MatchResult result = null;

        switch (expected.getNodeType()) {
            case OBJECT:
                result = new MatchResult(expected, actual, 0, description);
                result.reduce(Streams.stream(expected.fields())
                        .map(keyValue -> makeStatistic(keyValue.getValue(), actual.get(keyValue.getKey()),
                                description + "." + keyValue.getKey()))
                        .collect(Collectors.toList()));
                break;
            case ARRAY:
                result = new MatchResult(expected, actual, 0, description);
                result.reduce(Streams.stream(expected.arrayElements()).map(node -> mapNodeToStatistic(
                        node, actual, description)).collect(Collectors.toList()));
                break;
            case STRING:
                result = new MatchResult(expected, actual,
                        distanceMeter.apply(expected.toString(), actual.toString()),
                        description);
                break;
            case NUMBER:
            case BOOLEAN:
                result = new MatchResult(expected, actual,
                        expected.toString().equals(actual.toString()) ? 1 : 0,
                        description);
                break;
            case NULL:
                result = new MatchResult(expected, actual, actual.isNull() ? 1 : 0, description);
        }
        return result;
    }

    private static MatchResult mapNodeToStatistic(Node expected, Node actual, String description) {
        AtomicInteger counter = new AtomicInteger(0);
        return Streams.stream(actual.arrayElements())
                .map(actualChild -> makeStatistic(expected, actualChild,
                        description + ".[" + counter.getAndIncrement() + "]")).sorted(
                        Comparator.<MatchResult>reverseOrder()).findFirst()
                .orElse(makeStatistic(expected, actual, description));
    }

    private static class MatchResult implements Comparable<MatchResult> {

        private Node expected;
        private Node actual;
        private double matchRatio;
        private String difference = "";
        private List<MatchResult> results = new LinkedList<>();

        public MatchResult(Node expected, Node actual,
                           double matchRatio, String difference) {

            this.expected = expected;
            this.actual = actual;
            this.matchRatio = matchRatio;
            this.difference = difference;

            updateDifferenceIfNeeded();
        }

        public void updateDifferenceIfNeeded() {
            if (actual.isMissingNode()) {
                this.difference += " missed";
            } else if (matchRatio < 1d) {
                this.difference += " is " + actual.toString() + " should be " + expected.toString();
            }
        }

        public void reduce(List<MatchResult> result) {
            results.addAll(result);
            if (!(actual.isMissingNode() || actual.isNull())) {
                difference = Streams.stream(result).filter(matchResult -> matchResult.matchRatio < 1d)
                        .map(matchResult -> matchResult.difference)
                        .filter(StringUtils::isNotBlank)
                        .reduce("", (s, s2) -> s + "\n" + s2);
            }
            matchRatio = Streams.stream(result).mapToDouble(matchResult -> matchResult.matchRatio)
                    .average().getAsDouble();
        }

        @Override
        public int compareTo(MatchResult o) {
            return Double.compare(matchRatio, o.matchRatio);
        }
    }
}
