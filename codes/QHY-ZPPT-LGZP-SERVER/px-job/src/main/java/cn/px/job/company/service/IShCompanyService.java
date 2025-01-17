package cn.px.job.company.service;

import cn.px.job.company.domain.ShCompany;

import java.util.List;

/**
 * 企业Service接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface IShCompanyService 
{
    /**
     * 查询企业
     * 
     * @param id 企业主键
     * @return 企业
     */
    public ShCompany selectShCompanyById(Long id);

    /**
     * 查询企业列表
     * 
     * @param shCompany 企业
     * @return 企业集合
     */
    public List<ShCompany> selectShCompanyList(ShCompany shCompany);

    /**
     * 新增企业
     * 
     * @param shCompany 企业
     * @return 结果
     */
    public int insertShCompany(ShCompany shCompany);

    /**
     * 修改企业
     * 
     * @param shCompany 企业
     * @return 结果
     */
    public int updateShCompany(ShCompany shCompany);

    /**
     * 修改企业 考虑到省市区id有可能为空，所以，创建了新的方法
     *
     * @param shCompany 企业
     * @return 结果
     */
    public int updateShCompanyNew(ShCompany shCompany);

    /**
     * 批量删除企业
     * 
     * @param ids 需要删除的企业主键集合
     * @return 结果
     */
    public int deleteShCompanyByIds(Long[] ids);

    /**
     * 删除企业信息
     * 
     * @param id 企业主键
     * @return 结果
     */
    public int deleteShCompanyById(Long id);

    /**
     * 删除企业信息
     *
     * @param id 企业主键
     * @return 结果
     */
    public int deleteShCompanyById1(Long id);
}
