package com.bank.mapper;

import com.bank.dto.CustomerDTO;

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
        // TODO: map each column from `row` onto a new CustomerDTO and return it.
        throw new UnsupportedOperationException("TODO: implement toDTO");
    }

    /**
     * Build the column/value map used to persist a {@link CustomerDTO}.
     * @param dto the customer to persist
     * @return a map of column name to value for the repository to insert/update
     */
    public static Map<String, Object> toRow(CustomerDTO dto) {
        // TODO: put each DTO field into a Map keyed by column name and return it.
        throw new UnsupportedOperationException("TODO: implement toRow");
    }
}
