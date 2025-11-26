package org.usermanagement.smartshopy.enums;

import lombok.Data;
import lombok.Getter;
import lombok.experimental.PackagePrivate;

@Getter
public enum CustomerTier {
    BASIC(0,0,0,0) ,
    SILVER(3,1000,5,500),
    GOLD(10,5000,10,800) ,
    PLATINUM(20,15000,20,1200);

    private final int minorder;
    private final double  mintotalspent;
    private final int RemisePercentage;
    private final double minOrderForDiscount ;

    CustomerTier(int minOrders, double minSpent, int discountPercentage, double minOrderForDiscount) {
        this.minorder = minOrders;
        this.mintotalspent = minSpent;
        this.RemisePercentage = discountPercentage;
        this.minOrderForDiscount = minOrderForDiscount;
    }

    public static  CustomerTier CalculerTier(int totalorders , double totalspent ){
        if(totalorders >= PLATINUM.minorder || totalspent >= PLATINUM.mintotalspent){
            return PLATINUM;
        }
        if(totalorders >= GOLD.minorder || totalspent >= GOLD.mintotalspent){
            return GOLD;
        }
        if(totalorders >= SILVER.minorder || totalspent >= SILVER.mintotalspent){

           return  SILVER;
        }

        return BASIC;


    }



}
