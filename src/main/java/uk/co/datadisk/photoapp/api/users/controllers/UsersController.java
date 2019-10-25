package uk.co.datadisk.photoapp.api.users.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.datadisk.photoapp.api.users.model.CreateUserRequestModel;
import uk.co.datadisk.photoapp.api.users.model.CreateUserResponseModel;
import uk.co.datadisk.photoapp.api.users.model.UserResponseModel;
import uk.co.datadisk.photoapp.api.users.services.UsersService;
import uk.co.datadisk.photoapp.api.users.shared.UserDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

  private final UsersService usersService;
  private Environment env;

  public UsersController(UsersService usersService, Environment env) {
    this.usersService = usersService;
    this.env = env;
  }

  @GetMapping("/status/check")
  public String status() {
    return "Working Users Service, using port " + env.getProperty("local.server.port") + ", with token = " +
        env.getProperty("token.secret");
  }

  @PostMapping(
          consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
          produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<CreateUserResponseModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = modelMapper.map(userDetails, UserDto.class);

    UserDto createdUser = usersService.createUser(userDto);
    CreateUserResponseModel returnValue = modelMapper.map(createdUser, CreateUserResponseModel.class);

    return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
  }

  @GetMapping("/{userId}",
      consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
      produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
  public ResponseEntity<UserResponseModel> getUser(@PathVariable("userId") String userId) {

    UserDto userDto = usersService.getUserDetailsById(userId);
    UserResponseModel returnValue = new ModelMapper().map(userDto, UserResponseModel.class);

    return ResponseEntity.status(HttpStatus.OK).body(returnValue);
  }
}