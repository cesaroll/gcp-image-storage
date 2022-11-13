package io.ces.gdrive.images.gcpimagestorage.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import io.ces.gdrive.images.config.AuthorizationConfig;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomepageController {

  private final AuthorizationConfig authorizationConfig;

  @GetMapping(value = {"/"})
  public String showHomePage() throws IOException {
    return authorizationConfig.isUserAuthenticated() ? "dashboard.html" : "index.html";
  }

}
