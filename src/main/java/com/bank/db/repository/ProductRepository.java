package com.bank.db.repository;

import com.bank.db.DatabaseManager;
import com.bank.utils.UuidGeneratorUtil;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductRepository {

    private final DatabaseManager db;

    public ProductRepository() {
        this.db = DatabaseManager.getInstance();
    }


    public Map<String,Object> findByProductId(String productId) throws SQLException
    {
        String sql = "SELECT * FROM products where id = ? ";
        List<Map<String,Object>> row = db.query(sql,productId);
        if(row.isEmpty()){
            return null;
        }

        return row.get(0);
    }

    public String insert(Map<String,Object> productFields) throws SQLException
    {
        String productId = UuidGeneratorUtil.generateUuid();
        productFields.put("id",productId);
        String sql = "INSERT INTO products (id , product_name , category, interest_rate ,min_operating_balance , term_months) "+
                "VALUES (?,?,?,?,?,?)";
        List<Map<String,Object>> productRow = db.query(sql,productFields.get("id"),productFields.get("product_name"),productFields.get("category"),
                                                    productFields.get("interest_rate"),productFields.get("min_operating_balance"),
                                                    productFields.get("term_months"));

        return (String) productRow.get(0).get("id");
        //return productId
    }

    public List<Map<String,Object>> findAllByProductCategory(String category) throws SQLException
    {
        String sql = "SELECT * FROM products WHERE category = ?";
        List<Map<String,Object>> productNames = db.query(sql,category);
        return productNames;
    }

    public List<Map<String,Object>> findAllProducts() throws SQLException
    {
        String sql = "SELECT * FROM products";
        List<Map<String,Object>> products = db.query(sql);

        return products;
    }
}
