package cn.px.job.banner.service.impl;

import java.util.List;
import cn.px.common.utils.DateUtils;
import cn.px.job.banner.domain.ShBanner;
import cn.px.job.banner.mapper.ShBannerMapper;
import cn.px.job.banner.service.IShBannerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 个人端首页轮播图Service业务层处理
 * 
 * @author 品讯科技
 * @date 2024-08
 */
@Service
public class ShBannerServiceImpl implements IShBannerService
{
    @Autowired
    private ShBannerMapper shBannerMapper;

    /**
     * 查询个人端首页轮播图
     * 
     * @param id 个人端首页轮播图主键
     * @return 个人端首页轮播图
     */
    @Override
    public ShBanner selectShBannerById(Long id)
    {
        return shBannerMapper.selectShBannerById(id);
    }

    /**
     * 查询个人端首页轮播图列表
     * 
     * @param shBanner 个人端首页轮播图
     * @return 个人端首页轮播图
     */
    @Override
    public List<ShBanner> selectShBannerList(ShBanner shBanner)
    {
        return shBannerMapper.selectShBannerList(shBanner);
    }

    /**
     * 新增个人端首页轮播图
     * 
     * @param shBanner 个人端首页轮播图
     * @return 结果
     */
    @Override
    public int insertShBanner(ShBanner shBanner)
    {
        shBanner.setCreateTime(DateUtils.getNowDate());
        return shBannerMapper.insertShBanner(shBanner);
    }

    /**
     * 修改个人端首页轮播图
     * 
     * @param shBanner 个人端首页轮播图
     * @return 结果
     */
    @Override
    public int updateShBanner(ShBanner shBanner)
    {
        shBanner.setUpdateTime(DateUtils.getNowDate());
        return shBannerMapper.updateShBanner(shBanner);
    }

    /**
     * 批量删除个人端首页轮播图
     * 
     * @param ids 需要删除的个人端首页轮播图主键
     * @return 结果
     */
    @Override
    public int deleteShBannerByIds(Long[] ids)
    {
        return shBannerMapper.deleteShBannerByIds(ids);
    }

    /**
     * 删除个人端首页轮播图信息
     * 
     * @param id 个人端首页轮播图主键
     * @return 结果
     */
    @Override
    public int deleteShBannerById(Long id)
    {
        return shBannerMapper.deleteShBannerById(id);
    }
}
