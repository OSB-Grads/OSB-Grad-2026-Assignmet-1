package com.bank.dto;

import java.math.BigDecimal;

public class ProductDTO {
    private Long id;
    private String productName;
    private String productCategory;
    private BigDecimal interestRate;
    private BigDecimal minOperatingBalance;
    private Long termMonths;

    public ProductDTO(){

    }

    public ProductDTO(Long id , String productName , String productCategory ,
                      BigDecimal interestRate, BigDecimal minOperatingBalance, Long termMonths){
        this.id = id;
        this.productName = productName;
        this.productCategory = productCategory;
        this.interestRate = interestRate;
        this.minOperatingBalance = minOperatingBalance;
        this.termMonths = termMonths;
    }

    public Long getId(){
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getProductName()
    {
        return this.productName;
    }

    public void setProductName(String productName)
    {
        this.productName = productName;
    }

    public String getProductCategory()
    {
        return this.productCategory;
    }
    public void setProductCategory(String productCategory)
    {
        this.productCategory = productCategory;
    }

    public BigDecimal getInterestRate(){
        return this.interestRate ;
    }

    public void setInterestRate(BigDecimal interestRate)
    {
        this.interestRate = interestRate;
    }

    public BigDecimal getMinOperatingBalance() {
        return minOperatingBalance;
    }

    public void setMinOperatingBalance(BigDecimal minOperatingBalance)
    {
        this.minOperatingBalance = minOperatingBalance;
    }

    public Long getTermMonths(){
        return this.termMonths;
    }

    public void setTermMonths(Long termMonths)
    {
        this.termMonths = termMonths;
    }

    @Override
    public String toString(){
        return "ProductDTO { "+
                "id = "+ id +'\''+
                ", productName = " + productName + '\''+
                ", productCategory = "+ productCategory + '\''+
                ", interestRate = " + interestRate +'\''+
                ", minOperatingBalance = "+ minOperatingBalance+'\''+
                ", termMonths = "+ termMonths+'\''+
                "}";
    }
}
