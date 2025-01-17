package cn.px.job.recruit.service;

import cn.px.job.recruit.domain.ShCompanyRecruit;
import cn.px.job.recruit.domain.ShCompanyRecruitSystemSkill;

import java.util.List;

/**
 * 招聘纪录与系统技能中间Service接口
 *
 * @author 品讯科技
 * @date 2024-08
 */
public interface IShCompanyRecruitSystemSkillService
{
    /**
     * 查询招聘纪录与系统技能中间
     *
     * @param id 招聘纪录与系统技能中间主键
     * @return 招聘纪录与系统技能中间
     */
    public ShCompanyRecruitSystemSkill selectShCompanyRecruitSystemSkillById(Long id);

    /**
     * 查询招聘纪录与系统技能中间列表
     *
     * @param shCompanyRecruitSystemSkill 招聘纪录与系统技能中间
     * @return 招聘纪录与系统技能中间集合
     */
    public List<ShCompanyRecruitSystemSkill> selectShCompanyRecruitSystemSkillList(ShCompanyRecruitSystemSkill shCompanyRecruitSystemSkill);


    public List<Long> selectSystemSkillIdsByRecruitId(ShCompanyRecruitSystemSkill shCompanyRecruitSystemSkill);

    /**
     * 新增招聘纪录与系统技能中间
     *
     * @param shCompanyRecruitSystemSkill 招聘纪录与系统技能中间
     * @return 结果
     */
    public int insertShCompanyRecruitSystemSkill(ShCompanyRecruitSystemSkill shCompanyRecruitSystemSkill);

    /**
     * 修改招聘纪录与系统技能中间
     *
     * @param shCompanyRecruitSystemSkill 招聘纪录与系统技能中间
     * @return 结果
     */
    public int updateShCompanyRecruitSystemSkill(ShCompanyRecruitSystemSkill shCompanyRecruitSystemSkill);

    /**
     * 批量删除招聘纪录与系统技能中间
     *
     * @param ids 需要删除的招聘纪录与系统技能中间主键集合
     * @return 结果
     */
    public int deleteShCompanyRecruitSystemSkillByIds(Long[] ids);

    /**
     * 删除招聘纪录与系统技能中间信息
     *
     * @param id 招聘纪录与系统技能中间主键
     * @return 结果
     */
    public int deleteShCompanyRecruitSystemSkillById(Long id);
    public int deleteShCompanyRecruitSystemSkillById2(Long id);

    /**
     * 删除招聘纪录与系统技能中间信息
     *
     * @param id 招聘纪录与系统技能中间主键
     * @return 结果
     */
    public int deleteShCompanyRecruitSystemSkillByRecruitId(Long id);

    public int saveSkills(ShCompanyRecruit companyRecruit);
}
