package com.wangtao.redis.springcache.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wangtao.redis.springcache.constant.CacheNameConstant;
import com.wangtao.redis.springcache.dao.SysDictItemMapper;
import com.wangtao.redis.springcache.entity.SysDictItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author wangtao
 * Created at 2023/7/29 16:38
 */
@CacheConfig(cacheNames = {CacheNameConstant.DICT})
@Service
public class SysDictItemService {

    @Autowired
    private SysDictItemMapper sysDictItemMapper;

    /**
     * 查询并添加缓存(缓存命中则直接返回)
     * 添加缓存肯定是目标方法执行成功之后再进行的
     *
     * @param dictCode      字典类型
     * @param dictItemValue 字典明细值
     * @return 字典明细实体
     */
    @Cacheable(key = "#dictCode.concat(':').concat(#dictItemValue)")
    public SysDictItem selectByDictCodeAndItemValue(String dictCode, String dictItemValue) {
        check(dictCode, dictItemValue);
        Wrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getDictCode, dictCode)
                .eq(SysDictItem::getDictItemValue, dictItemValue);
        return sysDictItemMapper.selectOne(queryWrapper);
    }

    public List<SysDictItem> selectByDictCode(String dictCode) {
        Wrapper<SysDictItem> queryWrapper = new LambdaQueryWrapper<SysDictItem>()
                .eq(SysDictItem::getDictCode, dictCode);
        return sysDictItemMapper.selectList(queryWrapper);
    }

    /**
     * 更新字典明细并删除缓存
     * 删除缓存的时机是通过beforeInvocation属性控制, 默认false
     * beforeInvocation = false, 目标方法执行成功之后进行
     * beforeInvocation = true， 目标方法执行之前进行
     * @param sysDictItem 字典实体
     */
    @CacheEvict(key = "#sysDictItem.dictCode.concat(':').concat(#sysDictItem.dictItemValue)")
    public void updateByDictCodeAndItemValue(SysDictItem sysDictItem) {
        check(sysDictItem.getDictCode(), sysDictItem.getDictItemValue());
        Wrapper<SysDictItem> updateWrapper = new LambdaUpdateWrapper<SysDictItem>()
                .set(SysDictItem::getDictItemName, sysDictItem.getDictItemName())
                .set(SysDictItem::getUpdateTime, sysDictItem.getUpdateTime())
                .eq(SysDictItem::getDictCode, sysDictItem.getDictCode())
                .eq(SysDictItem::getDictItemValue, sysDictItem.getDictItemValue());
        sysDictItem.setUpdateTime(LocalDateTime.now());
        sysDictItemMapper.update(null, updateWrapper);
    }

    private void check(String dictCode, String dictItemValue) {
        if (StringUtils.isBlank(dictCode)) {
            throw new IllegalArgumentException("字典类型不能为空");
        }
        if (StringUtils.isBlank(dictItemValue)) {
            throw new IllegalArgumentException("字典明细值不能为空");
        }
    }
}
