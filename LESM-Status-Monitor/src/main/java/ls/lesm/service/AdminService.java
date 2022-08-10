package ls.lesm.service;

import java.util.List;

import ls.lesm.model.Role;
import ls.lesm.payload.request.RoleRequest;

public interface AdminService {
	
	public void deleteRoles(String roleName);
	
	public Role createNewRole(Role role);
	
	public List<Role> getAllRole();

}
