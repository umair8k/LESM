package ls.lesm.payload.request;

import lombok.Data;
import ls.lesm.model.Role;
import ls.lesm.model.User;

@Data
public class SignUpRequest {
	
	private User user;
	private Role role;

}
