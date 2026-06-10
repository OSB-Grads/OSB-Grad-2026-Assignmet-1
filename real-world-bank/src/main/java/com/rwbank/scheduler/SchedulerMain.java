package com.rwbank.scheduler;

import com.rwbank.jobs.InboxProcessorJob;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Scheduler entry point for the real-world bank. This is the application's
 * heartbeat — cron / Windows Task Scheduler (or a person) runs:
 *
 * <pre>
 * java -cp real-world-bank/target/real-world-bank-1.0.0.jar com.rwbank.scheduler.SchedulerMain rw-inbox-processor
 * </pre>
 *
 * <p>This scheduler is deliberately separate from the banking application's
 * ({@code com.bank.scheduler.SchedulerMain}) — different application, different
 * environment, different schedule. Exit codes: {@code 0} job ran,
 * {@code 1} job threw, {@code 2} unknown job / bad usage.</p>
 */
public class SchedulerMain {

    private static final Map<String, ScheduledJob> JOBS = new LinkedHashMap<>();

    static {
        register(new InboxProcessorJob());
        // TODO (optional stretch): register more jobs, e.g. an interest job that
        // grows real-world balances so source-of-wealth data moves over time.
    }

    private static void register(ScheduledJob job) {
        JOBS.put(job.name(), job);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: SchedulerMain <job-name>");
            System.err.println("Known jobs: " + JOBS.keySet());
            System.exit(2);
        }

        ScheduledJob job = JOBS.get(args[0]);
        if (job == null) {
            System.err.println("Unknown job: " + args[0]);
            System.err.println("Known jobs: " + JOBS.keySet());
            System.exit(2);
        }

        try {
            JobResult result = job.runOnce();
            System.out.println(result);
            System.exit(0);
        } catch (Exception e) {
            System.err.println("Job '" + args[0] + "' failed: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
