package cn.px.job.area.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import cn.px.common.core.domain.entity.SysMenu;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Treeselect树结构实体类
 *
 * @author 品讯科技
 */
public class TreeSelectArea implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 节点ID */
    private Long id;

    /** 节点名称 */
    private String label;

    /** 子节点 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<TreeSelectArea> children;

    public TreeSelectArea()
    {

    }

    public TreeSelectArea(ShArea shArea)
    {
        this.id = shArea.getId();
        this.label = shArea.getDescription();
        this.children = shArea.getChildren().stream().map(TreeSelectArea::new).collect(Collectors.toList());
    }

    public TreeSelectArea(SysMenu menu)
    {
        this.id = menu.getMenuId();
        this.label = menu.getMenuName();
        this.children = menu.getChildren().stream().map(TreeSelectArea::new).collect(Collectors.toList());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<TreeSelectArea> getChildren()
    {
        return children;
    }

    public void setChildren(List<TreeSelectArea> children)
    {
        this.children = children;
    }
}
