package uk.co.datadisk.photoapp.api.users.services;

import feign.FeignException;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import uk.co.datadisk.photoapp.api.users.data.AlbumsServiceClient;
import uk.co.datadisk.photoapp.api.users.data.UserEntity;
import uk.co.datadisk.photoapp.api.users.model.AlbumResponseModel;
import uk.co.datadisk.photoapp.api.users.repositories.UserRepository;
import uk.co.datadisk.photoapp.api.users.shared.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  //private final RestTemplate restTemplate;
  private final Environment env;
  private final AlbumsServiceClient albumsServiceClient;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  public UsersServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, Environment env, AlbumsServiceClient albumsServiceClient) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.env = env;
    this.albumsServiceClient = albumsServiceClient;
  }

  @Override
  public UserDto createUser(UserDto userDetails) {

    userDetails.setUserId(UUID.randomUUID().toString());
    userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

    userRepository.save(userEntity);

    UserDto returnValue = modelMapper.map(userEntity, UserDto.class);

    return returnValue;
  }

  @Override
  public UserDto getUserDetailsByEmail(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);

    if(userEntity == null) throw new UsernameNotFoundException(email);

    return new ModelMapper().map(userEntity, UserDto.class);
  }

  @Override
  public UserDto getUserDetailsById(String userId) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    if(userEntity == null) throw new UsernameNotFoundException("User not found");

    UserDto userDto =  new ModelMapper().map(userEntity, UserDto.class);
    
//    String albumsUrl = String.format(env.getProperty("albums.url"), userId);
//
//    ResponseEntity<List<AlbumResponseModel>> albumListResponse = restTemplate.exchange(albumsUrl, HttpMethod.GET, null,
//        new ParameterizedTypeReference<>() {});
//
//    List<AlbumResponseModel> albumsList = albumListResponse.getBody();

    // If Feign exception occurs, still send details but albums will be null
    List<AlbumResponseModel> albumsList = null;

    try {
      logger.info("Before calling albums Microservice");
      albumsList = albumsServiceClient.getAlbums(userId);
      logger.info("After calling albums Microservice");
    } catch (FeignException e) {
      logger.error(e.getLocalizedMessage());
    }

    userDto.setAlbums(albumsList);

    return userDto;
  }

  @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);

        if(userEntity == null) throw new UsernameNotFoundException(username);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), true, true,
                true, true, new ArrayList<>());
    }
}
