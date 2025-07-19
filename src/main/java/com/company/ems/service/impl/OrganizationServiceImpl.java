package com.company.ems.service.impl;

import com.company.ems.entity.Organization;
import com.company.ems.mapper.OrganizationMapper;
import com.company.ems.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<Organization> getOrganizationTree() {
        List<Organization> allOrgs = organizationMapper.selectAll();
        return buildTree(allOrgs);
    }

    private List<Organization> buildTree(List<Organization> allOrgs) {
        List<Organization> roots = new ArrayList<>();
        
        for (Organization org : allOrgs) {
            if (org.getParentId() == null) {
                roots.add(org);
            }
        }
        
        for (Organization root : roots) {
            setChildren(root, allOrgs);
        }
        
        return roots;
    }

    private void setChildren(Organization parent, List<Organization> allOrgs) {
        List<Organization> children = new ArrayList<>();
        
        for (Organization org : allOrgs) {
            if (org.getParentId() != null && org.getParentId().equals(parent.getId())) {
                org.setEmployeeCount(organizationMapper.countEmployeesByOrganizationId(org.getId()));
                children.add(org);
            }
        }
        
        parent.setChildren(children);
        
        for (Organization child : children) {
            setChildren(child, allOrgs);
        }
    }

    @Override
    public Organization getById(Long id) {
        return organizationMapper.selectById(id);
    }

    @Override
    public Organization save(Organization organization) {
        if (organization.getParentId() != null) {
            Organization parent = organizationMapper.selectById(organization.getParentId());
            if (parent != null) {
                organization.setLevel(parent.getLevel() + 1);
                organization.setFullPath(parent.getFullPath() + "/" + organization.getName());
            }
        } else {
            organization.setLevel(1);
            organization.setFullPath("/" + organization.getName());
        }

        if (organization.getCode() == null || organization.getCode().trim().isEmpty()) {
            organization.setCode(generateOrgCode(organization.getName()));
        }
        if (organization.getStatus() == null) {
            organization.setStatus(1);
        }
        if (organization.getSortOrder() == null) {
            organization.setSortOrder(0);
        }

        organizationMapper.insert(organization);
        return organization;
    }

    @Override
    public Organization update(Organization organization) {
        organizationMapper.updateById(organization);
        return organization;
    }

    @Override
    public void deleteById(Long id) {
        List<Organization> children = organizationMapper.selectByParentId(id);
        if (!children.isEmpty()) {
            throw new RuntimeException("存在子机构，无法删除");
        }

        int employeeCount = organizationMapper.countEmployeesByOrganizationId(id);
        if (employeeCount > 0) {
            throw new RuntimeException("机构下存在员工，无法删除");
        }

        organizationMapper.deleteById(id);
    }

    @Override
    public List<Organization> getChildrenByParentId(Long parentId) {
        return organizationMapper.selectByParentId(parentId);
    }
    
    private String generateOrgCode(String orgName) {
        if (orgName == null || orgName.trim().isEmpty()) {
            return "ORG_" + System.currentTimeMillis();
        }
        
        String code = "";
        for (char c : orgName.toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                code += c;
            } else if (c >= 'a' && c <= 'z') {
                code += Character.toUpperCase(c);
            } else if (c >= '\u4e00' && c <= '\u9fff') {
                // 中文转拼音首字母，这里简化处理
                switch (c) {
                    case '市': code += "S"; break;
                    case '场': code += "C"; break;
                    case '营': code += "Y"; break;
                    case '销': code += "X"; break;
                    case '部': code += "B"; break;
                    case '门': code += "M"; break;
                    case '技': code += "J"; break;
                    case '术': code += "S"; break;
                    case '财': code += "C"; break;
                    case '务': code += "W"; break;
                    case '人': code += "R"; break;
                    case '事': code += "S"; break;
                    default: code += "X"; break;
                }
            }
        }
        
        if (code.isEmpty()) {
            code = "ORG";
        }
        
        return code + "_" + System.currentTimeMillis() % 10000;
    }
}
