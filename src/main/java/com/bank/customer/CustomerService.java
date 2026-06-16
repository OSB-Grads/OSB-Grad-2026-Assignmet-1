package com.bank.customer;

import com.bank.db.repository.CustomerRepository;
import com.bank.dto.CustomerDTO;
import com.bank.mapper.CustomerMapper;
import com.bank.utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.bank.enums.log.LogType;

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
    private final LoggerService loggerService;

    public CustomerService() {
        this.repository = new CustomerRepository();
        this.loggerService=new LoggerService();
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
    public void createCustomer(Long customerId,String firstName, String lastName, String dateOfBirth,String email,String phone, String address, String nationalId) throws SQLException {

        try{
            ValidationUtils.validateCustomer(firstName,lastName,dateOfBirth,email,phone,address,nationalId);

            CustomerDTO profile = new CustomerDTO();
            profile.setId(customerId);
            profile.setFirstName(firstName);
            profile.setLastName(lastName);
            profile.setDateOfBirth(dateOfBirth);
            profile.setEmail(email);
            profile.setPhone(phone);
            profile.setAddress(address);
            profile.setNationalId(nationalId);

            Map<String, Object> row = CustomerMapper.toRow(profile);

            repository.insert(row);
            loggerService.log(
                    customerId,
                    "CUSTOMER",
                    "Customer profile created successfully",
                    LogType.SUCCESS
            );
        }
        catch (RuntimeException e) {
            loggerService.log(
                    customerId,
                    "CUSTOMER",
                    "Customer profile creation failed",
                    LogType.FAILURE
            );
            throw e;
        }
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
