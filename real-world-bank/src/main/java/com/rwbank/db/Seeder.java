package com.rwbank.db;

import java.sql.SQLException;

/**
 * Seeds the real-world accounts — the reference dataset the whole integration
 * runs against. PROVIDED AND COMPLETE: do not change the data, your KYC and
 * payment logic is expected to handle every row below.
 *
 * <p>The dataset deliberately includes failure cases. If your code never
 * rejects anything, it is wrong — these rows are the test suite:</p>
 *
 * <ul>
 *   <li><b>RW-1001..1003</b> — clean: KYC-verified, healthy balances (happy path)</li>
 *   <li><b>RW-1004</b> — NOT KYC-verified → must be rejected</li>
 *   <li><b>RW-1005</b> — verified, but national ID will not match any customer
 *       you create → identity mismatch is a different failure from unverified</li>
 *   <li><b>RW-1006</b> — balance £0.50 → debit requests fail on insufficient funds</li>
 *   <li><b>RW-1007</b> — £2,000,000 → exercises the upper band of loan eligibility</li>
 *   <li><b>RW-1008</b> — nobody's account → must not break listings or feeds</li>
 * </ul>
 *
 * <p>For the happy path, create customers in the banking application whose
 * national IDs match RW-1001..1003 (QQ123456A, QQ234567B, QQ345678C).</p>
 *
 * <p>Idempotent: INSERT OR IGNORE on the unique account number, so re-running
 * never duplicates rows.</p>
 */
public class Seeder {

    public static void run(DatabaseManager db) throws SQLException {
        String sql = "INSERT OR IGNORE INTO real_world_accounts "
                + "(rw_account_number, sort_code, holder_name, national_id, balance, kyc_verified) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Object[][] rows = {
            // rw_account_number, sort_code, holder_name, national_id, balance, kyc_verified
            {"RW-1001", "20-00-01", "Alice Hargreaves", "QQ123456A", "5000.00", 1},
            {"RW-1002", "20-00-01", "Bikram Chatterjee", "QQ234567B", "12000.50", 1},
            {"RW-1003", "40-47-87", "Carol Okafor", "QQ345678C", "800.00", 1},
            {"RW-1004", "20-00-01", "Daniel Price", "QQ456789D", "3000.00", 0},
            {"RW-1005", "60-83-71", "Erin Walsh", "ZZ999999Z", "4500.00", 1},
            {"RW-1006", "40-47-87", "Farid Khan", "QQ567890E", "0.50", 1},
            {"RW-1007", "20-00-01", "Grace Lindqvist", "QQ678901F", "2000000.00", 1},
            {"RW-1008", "60-83-71", "Henry Nobody", "QQ789012G", "150.00", 1}
        };

        int inserted = 0;
        for (Object[] row : rows) {
            Object affected = db.query(sql, row).get(0).get("affected_rows");
            inserted += ((Number) affected).intValue();
        }
        System.out.println("[rw-bank] Seed complete: " + inserted + " new account(s) inserted "
                + "(" + (rows.length - inserted) + " already present).");
    }
}
