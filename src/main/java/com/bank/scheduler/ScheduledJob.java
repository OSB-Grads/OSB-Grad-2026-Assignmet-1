package com.bank.scheduler;

/**
 * Contract for a batch job that an external scheduler runs periodically.
 *
 * <p>The application does NOT run jobs on a timer itself. Instead, an external
 * scheduler (cron, Windows Task Scheduler, or a person at a terminal) starts a
 * short-lived process via {@link SchedulerMain}, which calls {@link #runOnce()}
 * exactly once and exits. This keeps scheduling concerns (when, how often,
 * retries, alerting) outside the application.</p>
 *
 * <p>Implementations must be safe to run repeatedly: a job fired twice in a row
 * must not move money twice. Claim work atomically, e.g.
 * {@code UPDATE ... SET status='PROCESSING' WHERE id=? AND status='PENDING'}
 * and skip rows you failed to claim.</p>
 */
public interface ScheduledJob {

    /**
     * Unique name used to select this job on the command line,
     * e.g. {@code payment-processor}.
     *
     * @return the job's registry name
     */
    String name();

    /**
     * Do one complete pass of the job's work and return a summary.
     * Called exactly once per process; the process exits afterwards.
     *
     * @return counts and a human-readable summary of what happened
     * @throws Exception if the run fails entirely (exit code 1)
     */
    JobResult runOnce() throws Exception;
}
