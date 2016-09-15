package com.cognifide.qa.bb.cumber;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import com.google.common.collect.Sets;

public class BobcumberListener extends RunListener {

	private static final String FEATURE = "feature";

	private static final Map<String, Set<String>> ADDED_FEATURES = new HashMap<>();

	private static final String COLON = ":";

	private Bobcumber bobcumber;

	public BobcumberListener(Bobcumber bobcumber) {
		this.bobcumber = bobcumber;
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		String trace = normalizeTrace(failure.getTrace());
		if (trace.contains(FEATURE)) {
			addScenario(trace);
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

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(bobcumber.getFile(), false)));
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
