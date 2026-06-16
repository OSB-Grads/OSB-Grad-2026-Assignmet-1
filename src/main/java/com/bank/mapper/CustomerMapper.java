package com.bank.mapper;

import com.bank.dto.CustomerDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts between raw database rows and {@link CustomerDTO} objects.
 *
 * <p>Mappers are the single place that knows the column names. Keeping the
 * conversion here means a column rename only changes one file, and the rest of
 * the code talks in DTOs.</p>
 *
 * <p>This is a skeleton &mdash; the body is intentionally unimplemented. When
 * you fill it in, read each column from the row, e.g.
 * {@code dto.setUsername((String) row.get("username"));} and remember to cast
 * numeric ids via {@code ((Number) row.get("id")).longValue()}.</p>
 */
public class CustomerMapper {

    /**
     * Build a {@link CustomerDTO} from a database row.
     * @param row a single row as returned by the repository (column name to value)
     * @return the mapped DTO, or {@code null} if the row is {@code null}
     */
    public static CustomerDTO toDTO(Map<String, Object> row) {
        if(row == null) {
            return null;
        }
        CustomerDTO dto = new CustomerDTO();
        dto.setId(((Number) row.get("id")).longValue());
        dto.setFirstName((String) row.get("first_name"));
        dto.setLastName((String) row.get("last_name"));
        dto.setDateOfBirth((String) row.get("date_of_birth"));
        dto.setEmail((String) row.get("email"));
        dto.setPhone((String) row.get("phone"));
        dto.setAddress((String) row.get("address"));
        dto.setNationalId((String) row.get("national_id"));
        dto.setCreatedAt(String.valueOf(row.get("created_at")));
        dto.setUpdatedAt(String.valueOf(row.get("updated_at")));
        return dto;
    }

    /**
     * Build the column/value map used to persist a {@link CustomerDTO}.
     * @param dto the customer to persist
     * @return a map of column name to value for the repository to insert/update
     */
    public static Map<String, Object> toRow(CustomerDTO dto) {
        Map<String ,Object> row = new HashMap<>();
        row.put("id", dto.getId());
        row.put("first_name", dto.getFirstName());
        row.put("last_name", dto.getLastName());
        row.put("date_of_birth", dto.getDateOfBirth());
        row.put("email", dto.getEmail());
        row.put("phone", dto.getPhone());
        row.put("address", dto.getAddress());
        row.put("national_id", dto.getNationalId());
        row.put("created_at", dto.getCreatedAt());
        row.put("updated_at", dto.getUpdatedAt());
        return row;
    }
}
