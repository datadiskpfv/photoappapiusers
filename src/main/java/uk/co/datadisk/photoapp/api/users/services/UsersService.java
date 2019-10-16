package uk.co.datadisk.photoapp.api.users.services;

import uk.co.datadisk.photoapp.api.users.shared.UserDto;

public interface UsersService {
  UserDto createUser(UserDto userDetails);
}
