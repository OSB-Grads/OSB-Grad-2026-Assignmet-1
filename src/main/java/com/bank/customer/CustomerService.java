package com.bank.customer;

import com.bank.db.repository.CustomerRepository;
import com.bank.dto.CustomerDTO;
import com.bank.enums.log.LogType;
import com.bank.mapper.CustomerMapper;
import com.bank.utils.ValidationUtils;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import com.bank.enums.log.LogType;

private final LoggerService loggerService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

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


    public Map<String, Object> updateProfile(
            Long id,
            String firstName,
            String lastName,
            String email,
            String phone,
            String nationalId
    ) throws SQLException {

        Map<String, Object> customer = repository.findById(id);

        if (customer == null) {throw new RuntimeException("Customer not found with id: " + id);}
        if (firstName != null) {customer.put("first_name", firstName);}
        if (lastName != null) {customer.put("last_name", lastName);}
        if (email != null) {customer.put("email", email);}
        if (phone != null) {customer.put("phone", phone);}
        if (nationalId != null) {customer.put("national_id", nationalId);}

        try { // here we are updating the user, in the try block
            repository.update(id, customer);
            loggerService.log(
                    id,
                    "UPDATE_PROFILE",
                    "Customer profile updated successfully",
                    LogType.SUCCESS
            );
            return repository.findById(id);
        } catch (SQLException e) {
            loggerService.log(
                    id,
                    "UPDATE_PROFILE",
                    "Failed to update customer profile: " + e.getMessage(),
                    LogType.FAILURE
            );
            throw e;
        }
    }

    /**
     * List all customers (ADMIN only).
     */
    public List<CustomerDTO> listAll() {
        // TODO: repository.findAll() -> map each row -> return list.
        throw new UnsupportedOperationException("TODO: implement listAll");
    }
}
