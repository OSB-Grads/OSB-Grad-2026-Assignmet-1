package com.bank.scheduler;

/**
 * Summary of a single {@link ScheduledJob#runOnce()} pass.
 */
public class JobResult {
    private final String jobName;
    private final int processed;
    private final int failed;
    private final String message;

    public JobResult(String jobName, int processed, int failed, String message) {
        this.jobName = jobName;
        this.processed = processed;
        this.failed = failed;
        this.message = message;
    }

    public String getJobName() {
        return jobName;
    }

    public int getProcessed() {
        return processed;
    }

    public int getFailed() {
        return failed;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "JobResult{job='" + jobName + "', processed=" + processed
                + ", failed=" + failed + ", message='" + message + "'}";
    }
}
