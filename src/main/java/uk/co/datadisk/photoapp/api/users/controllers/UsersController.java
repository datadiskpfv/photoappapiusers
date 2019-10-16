package uk.co.datadisk.photoapp.api.users.controllers;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import uk.co.datadisk.photoapp.api.users.model.CreateUserRequestModel;
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
    return "Working Users Service, using port " + env.getProperty("local.server.port");
  }

  @PostMapping
  public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {

    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

    UserDto userDto = modelMapper.map(userDetails, UserDto.class);

    usersService.createUser(userDto);

    return "Create user method is called";
  }
}