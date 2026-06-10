package com.bank.orchestrator;

import com.bank.customer.CustomerService;
import com.bank.dto.AccountDTO;

/**
 * Coordinates the "open an account" workflow, which touches more than one
 * service: it confirms the customer exists, checks the chosen product, applies
 * any opening rules (e.g. minimum opening balance), then creates the account.
 *
 * <p>Orchestrators exist precisely for flows that span multiple services /
 * repositories. A single service should not reach across modules; the
 * orchestrator is the coordinator. Services hold the per-module rules; the
 * orchestrator sequences them and owns the transaction boundary.</p>
 *
 * <p>This is a skeleton &mdash; it shows the dependencies and the shape of the
 * workflow without implementing it. The product and account services are noted
 * as TODO because those modules are built later.</p>
 */
public class AccountOpeningOrchestrator {

    private final CustomerService customerService;
    // TODO: add ProductService and AccountService once those modules exist.

    public AccountOpeningOrchestrator() {
        this.customerService = new CustomerService();
    }

    /**
     * Open a new account for a customer under a chosen product.
     *
     * <p>Workflow: verify the customer owns the request &rarr; load the product
     * (and its category defaults) &rarr; validate opening rules &rarr; create
     * the account &rarr; write a log entry &rarr; return the new account.</p>
     *
     * @param customerId the customer opening the account
     * @param productId  the product chosen (pick category &rarr; pick product)
     * @return the newly opened account
     */
    public AccountDTO openAccount(Long customerId, Long productId) {
        // TODO:
        //  1. customerService.getById(customerId)            -> confirm customer exists
        //  2. productService.getById(productId)              -> load product + category defaults
        //  3. validate opening rules (minimum opening balance, etc.)
        //  4. accountService.createAccount(customerId, productId)
        //  5. logService.log(...)                            -> audit trail
        throw new UnsupportedOperationException("TODO: implement openAccount");
    }
}
