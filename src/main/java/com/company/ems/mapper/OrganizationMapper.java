package com.company.ems.mapper;

import com.company.ems.entity.Organization;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface OrganizationMapper {

    // 查询所有机构
    List<Organization> selectAll();

    // 根据父级ID查询子机构
    List<Organization> selectByParentId(@Param("parentId") Long parentId);

    // 根据ID查询机构
    Organization selectById(@Param("id") Long id);

    // 查询根机构（parentId为null的）
    List<Organization> selectRootOrganizations();

    // 新增机构
    int insert(Organization organization);

    // 更新机构
    int updateById(Organization organization);

    // 删除机构（软删除）
    int deleteById(@Param("id") Long id);

    // 统计机构下员工数量
    int countEmployeesByOrganizationId(@Param("organizationId") Long
                                               organizationId);
}
