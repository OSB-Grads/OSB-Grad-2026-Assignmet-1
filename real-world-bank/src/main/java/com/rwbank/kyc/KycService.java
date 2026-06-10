package com.rwbank.kyc;

import java.util.Map;

/**
 * KYC (Know Your Customer) checks for incoming payment requests.
 * SKELETON — you implement the logic.
 *
 * <p>The real-world bank refuses to move money unless the request passes KYC.
 * The checks are independent and fail for different reasons — return a
 * distinct reason for each so the banking application can tell its customer
 * what went wrong:</p>
 *
 * <ol>
 *   <li>The real-world account must exist → {@code "ACCOUNT_NOT_FOUND"}</li>
 *   <li>It must be KYC-verified ({@code kyc_verified = 1}) → {@code "KYC_NOT_VERIFIED"}</li>
 *   <li>Its national ID must match the national ID claimed in the request
 *       payload → {@code "ID_MISMATCH"} (an account can be verified and STILL
 *       belong to someone else — seed account RW-1005 exists to catch code
 *       that conflates these two checks)</li>
 * </ol>
 */
public class KycService {

    /**
     * Run the KYC checks for one payment request.
     *
     * @param accountRow        the {@code real_world_accounts} row for the requested
     *                          account, or {@code null} if no such account exists
     * @param claimedNationalId the national ID the banking application sent in
     *                          the request payload
     * @return {@code null} if all checks pass, otherwise the failure reason
     *         ({@code ACCOUNT_NOT_FOUND}, {@code KYC_NOT_VERIFIED}, {@code ID_MISMATCH})
     */
    public String check(Map<String, Object> accountRow, String claimedNationalId) {
        // TODO: implement the three checks, in order
        throw new UnsupportedOperationException("TODO: implement KYC checks");
    }
}
