package uk.co.datadisk.photoapp.api.users.services;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;
import uk.co.datadisk.photoapp.api.users.data.UserEntity;
import uk.co.datadisk.photoapp.api.users.repositories.UserRepository;
import uk.co.datadisk.photoapp.api.users.shared.UserDto;

import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

  private final UserRepository userRepository;

  public UsersServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDto createUser(UserDto userDetails) {

    userDetails.setUserId(UUID.randomUUID().toString());

    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
    userEntity.setEncryptedPassword("test");

    userRepository.save(userEntity);

    UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

    return returnValue;
  }

}
