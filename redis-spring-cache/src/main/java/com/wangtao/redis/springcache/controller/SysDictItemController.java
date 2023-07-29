package com.wangtao.redis.springcache.controller;

import com.wangtao.redis.springcache.entity.SysDictItem;
import com.wangtao.redis.springcache.service.SysDictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wangtao
 * Created at 2023/7/29 18:26
 */
@RequestMapping("/dict")
@RestController
public class SysDictItemController {

    @Autowired
    private SysDictItemService sysDictItemService;

    @GetMapping("/selectByDictCode")
    public List<SysDictItem> selectByDictCode(String dictCode) {
        return sysDictItemService.selectByDictCode(dictCode);
    }

    @PostMapping("/selectByDictCodeAndItemValue")
    public SysDictItem selectByDictCodeAndItemValue(String dictCode, String dictItemValue) {
        return sysDictItemService.selectByDictCodeAndItemValue(dictCode, dictItemValue);
    }

    @PostMapping("/updateByDictCodeAndItemValue")
    public void updateByDictCodeAndItemValue(SysDictItem sysDictItem) {
        sysDictItemService.updateByDictCodeAndItemValue(sysDictItem);
    }
}
