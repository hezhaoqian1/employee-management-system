package com.company.ems.controller;

import com.company.ems.common.ApiResponse;
import com.company.ems.entity.Organization;
import com.company.ems.exception.BusinessException;
import com.company.ems.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "1. 组织机构管理")
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @ApiOperation(value = "获取组织架构树", notes = "获取完整的组织架构树形结构")
    @GetMapping("/tree")
    public ApiResponse<List<Organization>> getOrganizationTree() {
        List<Organization> tree = organizationService.getOrganizationTree();
        return ApiResponse.success("获取组织架构树成功", tree);
    }

    @ApiOperation(value = "获取组织详情", notes = "根据ID获取组织详细信息")
    @GetMapping("/{id}")
    public ApiResponse<Organization> getOrganizationById(@ApiParam(value = "组织ID", required = true) @PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "组织ID不能为空且必须大于0");
        }
        Organization organization = organizationService.getById(id);
        if (organization == null) {
            throw new BusinessException("ORGANIZATION_NOT_FOUND", "组织信息不存在");
        }
        return ApiResponse.success("获取组织详情成功", organization);
    }

    /**
     * 根据父级ID获取子组织
     */
    @GetMapping("/{parentId}/children")
    public ResponseEntity<List<Organization>> getChildrenByParentId(@PathVariable Long parentId) {
        try {
            List<Organization> children = organizationService.getChildrenByParentId(parentId);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @ApiOperation(value = "创建组织", notes = "新增组织信息")
    @PostMapping
    public ApiResponse<Organization> createOrganization(@RequestBody Organization organization) {
        if (organization == null) {
            throw new BusinessException("INVALID_PARAMETER", "组织信息不能为空");
        }
        if (organization.getName() == null || organization.getName().trim().isEmpty()) {
            throw new BusinessException("INVALID_PARAMETER", "组织名称不能为空");
        }
        Organization savedOrganization = organizationService.save(organization);
        return ApiResponse.success("创建组织成功", savedOrganization);
    }

    @ApiOperation(value = "更新组织", notes = "修改组织信息")
    @PutMapping("/{id}")
    public ApiResponse<Organization> updateOrganization(@ApiParam(value = "组织ID", required = true) @PathVariable Long id, @RequestBody Organization organization) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "组织ID不能为空且必须大于0");
        }
        if (organization == null) {
            throw new BusinessException("INVALID_PARAMETER", "组织信息不能为空");
        }
        organization.setId(id);
        Organization updatedOrganization = organizationService.update(organization);
        return ApiResponse.success("更新组织成功", updatedOrganization);
    }

    @ApiOperation(value = "删除组织", notes = "删除组织信息")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteOrganization(@ApiParam(value = "组织ID", required = true) @PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException("INVALID_PARAMETER", "组织ID不能为空且必须大于0");
        }
        Organization organization = organizationService.getById(id);
        if (organization == null) {
            throw new BusinessException("ORGANIZATION_NOT_FOUND", "组织信息不存在");
        }
        organizationService.deleteById(id);
        return ApiResponse.success("删除组织成功", null);
    }
}