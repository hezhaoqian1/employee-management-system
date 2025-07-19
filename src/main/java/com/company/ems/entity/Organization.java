package com.company.ems.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel(description = "组织机构信息")
public class Organization {

    @ApiModelProperty(value = "组织ID", example = "1", position = 1)
    private Long id;
    
    @ApiModelProperty(value = "组织名称", example = "市场营销部", required = true, position = 2)
    private String name;
    
    @ApiModelProperty(value = "组织编码", example = "SCYXB_1234", position = 3)
    private String code;
    
    @ApiModelProperty(value = "父组织ID", example = "2", position = 4)
    private Long parentId;
    
    @ApiModelProperty(value = "组织层级", example = "3", position = 5)
    private Integer level;
    
    @ApiModelProperty(value = "完整路径", example = "/集团总部/杭州分公司/市场营销部", position = 6)
    private String fullPath;
    
    @ApiModelProperty(value = "地址", example = "杭州市拱墅区xx路xx号", position = 7)
    private String address;
    
    @ApiModelProperty(value = "联系电话", example = "0571-12345678", position = 8)
    private String phone;
    
    @ApiModelProperty(value = "组织描述", example = "负责市场推广、品牌建设和销售支持", position = 9)
    private String description;
    
    @ApiModelProperty(value = "状态", example = "1", notes = "1-正常，0-停用", position = 10)
    private Integer status;
    
    @ApiModelProperty(value = "排序号", example = "1", position = 11)
    private Integer sortOrder;
    
    @ApiModelProperty(value = "创建时间", example = "2024-07-19T10:30:00", position = 12)
    private LocalDateTime createdTime;
    
    @ApiModelProperty(value = "更新时间", example = "2024-07-19T10:30:00", position = 13)
    private LocalDateTime updatedTime;

    @ApiModelProperty(value = "子组织列表", position = 14)
    private List<Organization> children;
    
    @ApiModelProperty(value = "父组织信息", position = 15)
    private Organization parent;
    
    @ApiModelProperty(value = "员工数量", example = "5", position = 16)
    private Integer employeeCount;
}

