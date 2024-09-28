package com.sample.samplingserver.exceptions;

public class SamplingErrors {
    public static class SamplingFailedException extends RuntimeException {
        public SamplingFailedException() {
            super("Fetch failed");
        }
    }
}
