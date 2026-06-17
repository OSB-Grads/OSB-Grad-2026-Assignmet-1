package com.bank.mapper;

import com.bank.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductMapper {

    public static ProductDTO toDTO(Map<String,Object> row){
         ProductDTO dto = new ProductDTO();

         dto.setId(((Number) row.get("id")).longValue());
         dto.setProductName((String) row.get("product_name"));
         dto.setProductCategory((String) row.get("category"));
        dto.setInterestRate(row.get("interest_rate") != null ? BigDecimal.valueOf(((Number) row.get("interest_rate")).doubleValue()) : null);
        dto.setMinOperatingBalance(row.get("min_operating_balance") != null ? BigDecimal.valueOf(((Number) row.get("min_operating_balance")).doubleValue()) : null);
         dto.setTermMonths(((Number)
                 row.get("term_months") != null
                 ? ((Number) row.get("term_months")).longValue()
                 : null));

        return dto;
    }

    public static Map<String, Object> toRow(ProductDTO dto) {
        Map<String,Object> row = new HashMap<>();

        row.put("id", dto.getId());
        row.put("product_name" , dto.getProductName());
        row.put("category", dto.getProductCategory());
        row.put("interest_rate", dto.getInterestRate());
        row.put("min_operating_balance",dto.getMinOperatingBalance());
        row.put("term_months",dto.getTermMonths());

        return row;
    }
}
