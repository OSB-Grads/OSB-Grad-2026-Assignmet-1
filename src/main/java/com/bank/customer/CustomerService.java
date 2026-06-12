package com.bank.customer;

import com.bank.db.repository.CustomerRepository;
import com.bank.dto.CustomerDTO;

import java.util.List;

/**
 * Business logic for customer profiles.
 *
 * <p>Sits between the CLI and the repository. The service applies the rules
 * (email must be unique, customer must be 18 or over, a customer may only act
 * on their own data) and always returns {@link CustomerDTO}s &mdash; never raw
 * rows or entities. It uses {@link CustomerRepository} for persistence and
 * {@link com.bank.mapper.CustomerMapper} to convert rows to DTOs.</p>
 *
 * <p>This is a skeleton showing where the logic lives &mdash; method bodies are
 * intentionally unimplemented.</p>
 */
public class CustomerService {

    private final CustomerRepository repository;

    public CustomerService() {
        this.repository = new CustomerRepository();
    }

    /**
     * Create a new customer profile.
     *
     * <p>Validate inputs (unique email, age 18+, non-empty required fields),
     * hash the password, persist via the repository, then return the new
     * customer as a DTO.</p>
     *
     * @return the created customer
     */
    public CustomerDTO createCustomer(String username, String rawPassword, CustomerDTO profile) {
        // TODO: validate -> hash password -> repository.insert(...) -> return mapped DTO.
        throw new UnsupportedOperationException("TODO: implement createCustomer");
    }

    /**
     * Fetch a customer by id.
     * @throws RuntimeException replace with CustomerNotFoundException when not found
     */
    public CustomerDTO getById(Long id) {
        // TODO: repository.findById(id) -> CustomerMapper.toDTO(row) -> return.
        throw new UnsupportedOperationException("TODO: implement getById");
    }

    /**
     * Update the caller's own profile (email, phone, address).
     * @return the updated customer
     */
    public CustomerDTO updateProfile(Long id, CustomerDTO changes) {
        // TODO: validate -> repository.update(...) -> return refreshed DTO.
        throw new UnsupportedOperationException("TODO: implement updateProfile");
    }

    /**
     * List all customers (ADMIN only).
     */
    public List<CustomerDTO> listAll() {
        // TODO: repository.findAll() -> map each row -> return list.
        throw new UnsupportedOperationException("TODO: implement listAll");
    }
}
