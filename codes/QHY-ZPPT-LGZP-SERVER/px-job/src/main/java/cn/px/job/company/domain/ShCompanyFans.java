package cn.px.job.company.domain;

import cn.px.job.appuser.domain.ShAppUser;
import cn.px.job.company.domain.ShCompany;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import cn.px.common.annotation.Excel;
import cn.px.common.core.domain.BaseEntity;

/**
 * 企业粉丝对象 sh_company_fans
 *
 * @author 品讯科技
 * @date 2024-08
 */
public class ShCompanyFans extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 被关注的企业id */
    @Excel(name = "被关注的企业id")
    private Long companyId;

    /** 发起关注的人（id） */
    @Excel(name = "发起关注的人", readConverterExp = "i=d")
    private Long fansUserId;

    /** 被关注的人资企业id(如果是人资下的客户企业，则填该信息)*/
    @Excel(name = "被关注的人资企业id(如果是人资企业，则填该信息)")
    private Long hrId;

    /** 发起关注的企业id*/
    @Excel(name = "发起关注的企业id")
    private Long fansCompanyId;

    /** 发起关注的客户企业id(如果是人资企业下的客户企业，则填该字段)*/
    @Excel(name = "发起关注的客户企业id(如果是人资企业下的客户企业，则填该字段)")
    private Long fanHrId;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCompanyId(Long companyId)
    {
        this.companyId = companyId;
    }

    public Long getCompanyId()
    {
        return companyId;
    }
    public void setFansUserId(Long fansUserId)
    {
        this.fansUserId = fansUserId;
    }

    public Long getFansUserId()
    {
        return fansUserId;
    }

    public Long getHrId() {
        return hrId;
    }

    public void setHrId(Long hrId) {
        this.hrId = hrId;
    }

    public Long getFansCompanyId() {
        return fansCompanyId;
    }

    public void setFansCompanyId(Long fansCompanyId) {
        this.fansCompanyId = fansCompanyId;
    }

    public Long getFanHrId() {
        return fanHrId;
    }

    public void setFanHrId(Long fanHrId) {
        this.fanHrId = fanHrId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("comanyId", getCompanyId())
            .append("fansUserId", getFansUserId())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("hrId",getHrId())
            .append("fansCompanyId",getFansCompanyId())
            .append("fanHrId",getFanHrId())
            .toString();
    }
    /***************************/
    @Getter
    @Setter
    private ShAppUser shAppUser;

    @Getter
    @Setter
    private ShCompany company;
}
