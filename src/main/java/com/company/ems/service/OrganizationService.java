package com.company.ems.service;

import com.company.ems.entity.Organization;

import java.util.List;

public interface OrganizationService {

    /**
     * 获取机构树形结构
     */
    List<Organization> getOrganizationTree();

    /**
     * 根据ID获取机构详情
     */
    Organization getById(Long id);

    /**
     * 新增机构
     */
    Organization save(Organization organization);

    /**
     * 更新机构
     */
    Organization update(Organization organization);

    /**
     * 删除机构
     */
    void deleteById(Long id);

    /**
     * 根据父级ID获取子机构
     */
    List<Organization> getChildrenByParentId(Long parentId);
}

