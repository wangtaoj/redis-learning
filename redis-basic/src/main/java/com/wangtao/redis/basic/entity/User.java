package com.wangtao.redis.basic.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author wangtao
 * Created at 2022/7/30 18:25
 */
@Data
public class User {

    private String username;

    private Integer age;

    private LocalDate birthday;

    private BigDecimal userCode;
}
