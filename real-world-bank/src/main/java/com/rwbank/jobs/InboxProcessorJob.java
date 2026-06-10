package com.rwbank.jobs;

import com.rwbank.scheduler.JobResult;
import com.rwbank.scheduler.ScheduledJob;

/**
 * Processes the real-world bank's inbox: payment requests sent by the banking
 * application. SKELETON — you implement the logic.
 *
 * <p>Message types this job consumes (from our {@code inbox} table):</p>
 * <ul>
 *   <li>{@code DEBIT_REQUEST} — the bank wants to PULL money from a real-world
 *       account (funds a customer deposit). Payload e.g.
 *       {@code rw_account_number=RW-1001;amount=250.00;national_id=QQ123456A}</li>
 *   <li>{@code CREDIT_REQUEST} — the bank wants to PUSH money to a real-world
 *       account (a customer withdrawal lands there). Same payload shape.</li>
 * </ul>
 *
 * <p>For each message this job writes a result into the BANKING application's
 * inbox ({@code DatabaseManager.connectTo("banking_app.db")}):
 * {@code DEBIT_RESULT} / {@code CREDIT_RESULT}, echoing the request's
 * {@code correlation_id} and {@code transaction_id}, with a payload like
 * {@code outcome=OK} or {@code outcome=FAILED;reason=KYC_NOT_VERIFIED}.
 * Give the result its own fresh {@code idempotency_key} — it is a new message;
 * the UNIQUE constraint on their side dedupes redelivery.</p>
 *
 * <p>Workflow per pass:</p>
 * <ol>
 *   <li>Claim PENDING rows one at a time:
 *       {@code UPDATE inbox SET status='PROCESSING' WHERE id=? AND status='PENDING'}
 *       — if 0 rows were affected another run claimed it; skip. This is what
 *       makes a double-fired scheduler harmless.</li>
 *   <li>Parse the payload (split on {@code ;} then {@code =}).</li>
 *   <li>Run {@link com.rwbank.kyc.KycService#check}.</li>
 *   <li>If KYC passes: apply the debit/credit to {@code real_world_accounts}
 *       inside a transaction. A DEBIT_REQUEST must also fail (reason
 *       {@code INSUFFICIENT_FUNDS}) if the balance cannot cover the amount.</li>
 *   <li>Write the *_RESULT message into the banking application's inbox.</li>
 *   <li>Mark our inbox row DONE or FAILED (with the reason) and set
 *       {@code processed_at}.</li>
 * </ol>
 */
public class InboxProcessorJob implements ScheduledJob {

    @Override
    public String name() {
        return "rw-inbox-processor";
    }

    @Override
    public JobResult runOnce() throws Exception {
        // TODO: implement the workflow described above.
        //
        // DatabaseManager own = DatabaseManager.getInstance();              // our DB
        // DatabaseManager bank = DatabaseManager.connectTo("banking_app.db"); // their inbox ONLY
        // try { ... } finally { bank.close(); }
        //
        // return new JobResult(name(), processed, failed, "...");
        throw new UnsupportedOperationException("TODO: implement inbox processing");
    }
}
