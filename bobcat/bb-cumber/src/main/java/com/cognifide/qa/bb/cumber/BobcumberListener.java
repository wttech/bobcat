/*-
 * #%L
 * Bobcat
 * %%
 * Copyright (C) 2016 Cognifide Ltd.
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
package com.cognifide.qa.bb.cumber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.CharEncoding;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.google.common.collect.Sets;

public class BobcumberListener extends RunListener {

	private static final String FEATURE = "feature";

	private static final String SCENARIO = "Scenario";

	private static final Map<String, Set<String>> ADDED_FEATURES = new HashMap<>();

	private static final String COLON = ":";

	private final Bobcumber bobcumber;

	private AtomicInteger scenarioCounter = new AtomicInteger();

	private AtomicInteger testFailureCounter = new AtomicInteger();

	private boolean alreadyRegistered;

	public BobcumberListener(Bobcumber bobcumber) {
		this.bobcumber = bobcumber;
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		try (PrintWriter writer = new PrintWriter(bobcumber.getStatisticsFile(), CharEncoding.UTF_8)) {
			writer.println(scenarioCounter.get());
			writer.println(testFailureCounter.get());
		}
	}

	@Override
	public void testStarted(Description description) throws Exception {
		String displayName = description.getDisplayName();
		String testStep = displayName.substring(0, displayName.lastIndexOf(COLON));
		if (SCENARIO.equals(testStep)) {
			scenarioCounter.incrementAndGet();
			alreadyRegistered = false;
		}
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		String trace = normalizeTrace(failure.getTrace());
		if (trace.contains(FEATURE)) {
			addScenario(trace);
			if (!alreadyRegistered) {
				testFailureCounter.incrementAndGet();
				alreadyRegistered = true;
			}
		}
	}

	private String normalizeTrace(String trace) {
		return trace.substring(trace.lastIndexOf("(") + 1, trace.lastIndexOf(")"));
	}

	private synchronized void addScenario(String failedScenario) throws IOException {

		String featureName = failedScenario.substring(0, failedScenario.lastIndexOf(COLON));
		String failedLineNumber = failedScenario.substring(failedScenario.lastIndexOf(COLON) + 1,
				failedScenario.length());

		if (ADDED_FEATURES.containsKey(featureName)) {
			Set<String> featureFailedLines = ADDED_FEATURES.get(featureName);
			featureFailedLines.add(failedLineNumber);
			ADDED_FEATURES.put(featureName, featureFailedLines);
		} else {
			ADDED_FEATURES.put(featureName, Sets.newHashSet(failedLineNumber));
		}

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(bobcumber.getFeatureFile(), false)));
		for (String feature : ADDED_FEATURES.keySet()) {
			out.print(feature);
			Set<String> lines = ADDED_FEATURES.get(feature);
			for (String line : lines) {
				out.print(COLON + line);
			}
			out.print(" ");
		}
		out.close();
	}
}
