package cn.px.job.underhrwelfare.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 客户企业福利中间对象 sh_company_under_hr_welfare
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public class ShCompanyUnderHrWelfare extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 客户企业id */
    @Excel(name = "客户企业id")
    private Long underHrId;

    /** 福利表id */
    @Excel(name = "福利表id")
    private Long welfareId;

    /** 删除状态（0代表存在 2代表删除） */
    private String delFlag;

    private String welfareName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUnderHrId(Long underHrId) 
    {
        this.underHrId = underHrId;
    }

    public Long getUnderHrId() 
    {
        return underHrId;
    }
    public void setWelfareId(Long welfareId) 
    {
        this.welfareId = welfareId;
    }

    public Long getWelfareId() 
    {
        return welfareId;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getWelfareName() {
        return welfareName;
    }

    public void setWelfareName(String welfareName) {
        this.welfareName = welfareName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("underHrId", getUnderHrId())
            .append("welfareId", getWelfareId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("delFlag", getDelFlag())
            .append("welfareName", getWelfareName())
            .toString();
    }
}
