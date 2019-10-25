package uk.co.datadisk.photoapp.api.users.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import uk.co.datadisk.photoapp.api.users.shared.UserDto;

public interface UsersService extends UserDetailsService {
  UserDto createUser(UserDto userDetails);
  UserDto getUserDetailsByEmail(String email);
  UserDto getUserDetailsById(String userId);
}
