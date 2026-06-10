package com.rwbank.scheduler;

/**
 * Contract for a batch job run by an external scheduler. Mirror of the banking
 * application's contract — the two applications deliberately do NOT share code,
 * only the inbox table shape.
 *
 * <p>Implementations must be safe to run repeatedly: a job fired twice must
 * not move money twice. Claim work atomically
 * ({@code UPDATE ... SET status='PROCESSING' WHERE id=? AND status='PENDING'})
 * and rely on the inbox's UNIQUE idempotency_key when writing results.</p>
 */
public interface ScheduledJob {

    /**
     * Unique name used to select this job on the command line,
     * e.g. {@code rw-inbox-processor}.
     *
     * @return the job's registry name
     */
    String name();

    /**
     * Do one complete pass of the job's work and return a summary.
     *
     * @return counts and a human-readable summary of what happened
     * @throws Exception if the run fails entirely (exit code 1)
     */
    JobResult runOnce() throws Exception;
}
