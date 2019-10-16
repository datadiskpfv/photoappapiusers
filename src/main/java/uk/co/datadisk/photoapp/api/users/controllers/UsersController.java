package uk.co.datadisk.photoapp.api.users.controllers;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;
import uk.co.datadisk.photoapp.api.users.model.CreateUserRequestModel;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

  private Environment env;

  public UsersController(Environment env) {
    this.env = env;
  }

  @GetMapping("/status/check")
  public String status() {
    return "Working Users Service, using port " + env.getProperty("local.server.port");
  }

  @PostMapping
  public String createUser(@Valid @RequestBody CreateUserRequestModel userDetails) {
    return "Create user method is called";
  }
}