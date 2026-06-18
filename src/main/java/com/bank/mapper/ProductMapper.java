package com.bank.mapper;

import com.bank.dto.ProductDTO;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ProductMapper {

    public static ProductDTO toDTO(Map<String,Object> row){
         ProductDTO dto = new ProductDTO();

         dto.setId(((Integer) row.get("id")).longValue());
         dto.setProductName((String) row.get("product_name"));
         dto.setProductCategory((String) row.get("category"));
         dto.setInterestRate(new BigDecimal(row.get("interest_rate").toString()));
         dto.setMinOperatingBalance(new BigDecimal(row.get("min_operating_balance").toString()));
         if(row.get("term_months")!=null){
            dto.setTermMonths(((Integer) row.get("term_months")).longValue());
         }

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
