package com.bank.scheduler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Second entry point of the application, used by an external scheduler.
 *
 * <p>Cron / Windows Task Scheduler (or a person) runs:</p>
 *
 * <pre>
 * java -cp target/cli-banking-app-1.0.0.jar com.bank.scheduler.SchedulerMain payment-processor
 * </pre>
 *
 * <p>The named job's {@link ScheduledJob#runOnce()} is called once, the
 * {@link JobResult} is printed, and the process exits. Exit codes:</p>
 *
 * <ul>
 *   <li>{@code 0} — job ran and returned a result (check the printed counts)</li>
 *   <li>{@code 1} — job threw; nothing should be half-applied (use transactions)</li>
 *   <li>{@code 2} — unknown job name / bad usage</li>
 * </ul>
 *
 * <p>Schedulers use the exit code for alerting — keep the contract honest.</p>
 */
public class SchedulerMain {

    private static final Map<String, ScheduledJob> JOBS = new LinkedHashMap<>();

    static {
        // TODO: register your jobs here as you build them, e.g.:
        // register(new PaymentProcessorJob());   // sends DEBIT/CREDIT_REQUESTs, applies *_RESULTs from the inbox
        // register(new LoanRepaymentJob());      // deducts due installments
    }

    private static void register(ScheduledJob job) {
        JOBS.put(job.name(), job);
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: SchedulerMain <job-name>");
            System.err.println("Known jobs: " + (JOBS.isEmpty() ? "(none registered yet)" : JOBS.keySet()));
            System.exit(2);
        }

        ScheduledJob job = JOBS.get(args[0]);
        if (job == null) {
            System.err.println("Unknown job: " + args[0]);
            System.err.println("Known jobs: " + (JOBS.isEmpty() ? "(none registered yet)" : JOBS.keySet()));
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
