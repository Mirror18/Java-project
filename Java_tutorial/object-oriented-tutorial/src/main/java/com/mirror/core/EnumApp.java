package com.mirror.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

public class EnumApp {
    public static void main(String[] args) {
        User user = new User(1L,"mirror",MemberEnum.GOLD_MEMBER.getType());

        BigDecimal productPrice = new BigDecimal("1000");
        BigDecimal discountedPrice = calculateFinalPrice(productPrice, user.getMemberType());
        System.out.println(discountedPrice);

    }
    public static  BigDecimal calculateFinalPrice(BigDecimal originPrice,Integer type){
        return MemberEnum.getEnumByType(type).calculateFinalPrice(originPrice);
    }
}
@Data
@AllArgsConstructor
class User {
    private Long id;
    private String name;
    private Integer memberType;

}

@Getter
enum MemberEnum {
    //
    GOLD_MEMBER(1, "黄金会员") {
        @Override
        public BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.6")));
        }
    },
    SILVER_MEMBER(2, "白银会员") {
        @Override
        protected BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.6")));
        }
    },
    BRONZE_MEMBER(3, "青铜会员") {
        @Override
        protected BigDecimal calculateFinalPrice(BigDecimal originPrice) {
            return originPrice.multiply(new BigDecimal(("0.6")));

        }
    };

    private final Integer type;
    private final String desc;

    MemberEnum(Integer type, String desc) {
        this.desc = desc;
        this.type = type;
    }

    protected abstract BigDecimal calculateFinalPrice(BigDecimal originPrice);

    public static MemberEnum getEnumByType(Integer type) {
        MemberEnum[] values = MemberEnum.values();
        for (MemberEnum memberEnum : values) {
            if (memberEnum.getType().equals(type)) {
                return memberEnum;
            }
        }
        throw new IllegalArgumentException("");
    }
}