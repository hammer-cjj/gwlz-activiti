package com.zsga.cf.gwlz.pojo;

/**
 * 用户以及对应的部门和角色
 * @author quadcopter
 *
 */
public class UserExt extends User {
	//角色ID
	private Integer roleId;
	//角色名称
	private String roleName;
	//部门ID
	private Integer deptId;
	//部门名称
	private String deptName;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}
	
}
