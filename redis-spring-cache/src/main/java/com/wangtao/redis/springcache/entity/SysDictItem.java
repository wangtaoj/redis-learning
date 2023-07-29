package com.wangtao.redis.springcache.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author wangtao
 * Created at 2023/7/29 16:30
 */
@Data
public class SysDictItem {

    @TableId
    private Long id;

    private String dictCode;

    private String dictItemValue;

    private String dictItemName;

    private Integer delFlg;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
