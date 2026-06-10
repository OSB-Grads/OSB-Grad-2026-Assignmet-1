package com.rwbank;

import com.rwbank.db.DatabaseManager;
import com.rwbank.db.Seeder;

import java.util.List;
import java.util.Map;

/**
 * Small admin entry point for the real-world bank application. PROVIDED.
 *
 * <p>This application has no interactive menu — it is the world outside the
 * bank. Its real work happens in the scheduler
 * ({@code com.rwbank.scheduler.SchedulerMain}). This class just gives you
 * visibility:</p>
 *
 * <pre>
 * java -jar real-world-bank/target/real-world-bank-1.0.0.jar seed      # seed reference accounts
 * java -jar real-world-bank/target/real-world-bank-1.0.0.jar accounts  # list accounts + balances
 * java -jar real-world-bank/target/real-world-bank-1.0.0.jar inbox     # show inbox messages
 * </pre>
 *
 * <p>Run from the repository root so both applications resolve the same
 * database files.</p>
 */
public class Main {

    public static void main(String[] args) {
        DatabaseManager db = DatabaseManager.getInstance();
        String command = args.length > 0 ? args[0] : "";

        try {
            switch (command) {
                case "seed":
                    Seeder.run(db);
                    break;
                case "accounts":
                    List<Map<String, Object>> accounts = db.query(
                            "SELECT rw_account_number, sort_code, holder_name, national_id, balance, kyc_verified "
                            + "FROM real_world_accounts ORDER BY rw_account_number");
                    System.out.println("rw_account_number | sort_code | holder_name | national_id | balance | kyc_verified");
                    for (Map<String, Object> row : accounts) {
                        System.out.println(row.get("rw_account_number") + " | "
                                + row.get("sort_code") + " | "
                                + row.get("holder_name") + " | "
                                + row.get("national_id") + " | "
                                + row.get("balance") + " | "
                                + row.get("kyc_verified"));
                    }
                    System.out.println("(" + accounts.size() + " account(s))");
                    break;
                case "inbox":
                    List<Map<String, Object>> messages = db.query(
                            "SELECT id, message_type, status, correlation_id, payload, reason, created_at "
                            + "FROM inbox ORDER BY id");
                    for (Map<String, Object> row : messages) {
                        System.out.println("#" + row.get("id") + " [" + row.get("status") + "] "
                                + row.get("message_type") + " corr=" + row.get("correlation_id")
                                + " payload=" + row.get("payload")
                                + (row.get("reason") != null ? " reason=" + row.get("reason") : ""));
                    }
                    System.out.println("(" + messages.size() + " message(s))");
                    break;
                default:
                    System.out.println("Usage: java -jar real-world-bank-1.0.0.jar <seed|accounts|inbox>");
                    System.out.println("Scheduler jobs run separately via com.rwbank.scheduler.SchedulerMain");
            }
        } catch (Exception e) {
            System.err.println("[rw-bank] Command '" + command + "' failed: " + e.getMessage());
            System.exit(1);
        } finally {
            db.close();
        }
    }
}
