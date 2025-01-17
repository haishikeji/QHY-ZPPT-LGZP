package cn.px.job.feedback.mapper;

import java.util.List;

import cn.px.job.feedback.domain.ShFeedbackImage;

/**
 * 反馈文件Mapper接口
 * 
 * @author 品讯科技
 * @date 2024-08
 */
public interface ShFeedbackImageMapper 
{
    /**
     * 查询反馈文件
     * 
     * @param id 反馈文件主键
     * @return 反馈文件
     */
    public ShFeedbackImage selectShFeedbackImageById(Long id);

    /**
     * 查询反馈文件列表
     * 
     * @param shFeedbackImage 反馈文件
     * @return 反馈文件集合
     */
    public List<ShFeedbackImage> selectShFeedbackImageList(ShFeedbackImage shFeedbackImage);

    /**
     * 新增反馈文件
     * 
     * @param shFeedbackImage 反馈文件
     * @return 结果
     */
    public int insertShFeedbackImage(ShFeedbackImage shFeedbackImage);

    /**
     * 修改反馈文件
     * 
     * @param shFeedbackImage 反馈文件
     * @return 结果
     */
    public int updateShFeedbackImage(ShFeedbackImage shFeedbackImage);

    /**
     * 删除反馈文件
     * 
     * @param id 反馈文件主键
     * @return 结果
     */
    public int deleteShFeedbackImageById(Long id);

    /**
     * 批量删除反馈文件
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteShFeedbackImageByIds(Long[] ids);
}
