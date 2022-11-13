package io.ces.gdrive.images.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Value;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class AuthorizationConfig {

  private static final String USER_IDENTIFIER_KEY = "MY_DUMMY_USER";

  private final HttpTransport httpTransport;
  private final JsonFactory jsonFactory;

  private final List<String> scopes = Collections.singletonList(DriveScopes.DRIVE);

  @Value("${google.secret.key.path}")
  private Resource gdSecretKeys;

  @Value("${google.credentials.folder.path}")
  private Resource credentialFolder;

  @Value("${google.oauth.callback.uri}")
  private String CALLBACK_URI;

  private GoogleAuthorizationCodeFlow flow;

  @PostConstruct
  public void init() throws Exception {
    this.flow = getAuthorizationCodeFlow();
  }

  public boolean isUserAuthenticated() throws IOException {
    return isUserAuthenticated(USER_IDENTIFIER_KEY);
  }

  public boolean isUserAuthenticated(String userIdeKey) throws IOException {
    var credential = flow.loadCredential(userIdeKey);

    if (credential != null) {
      return credential.refreshToken();
    }

    return false;
  }

  private GoogleAuthorizationCodeFlow getAuthorizationCodeFlow() throws IOException {
    var secrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(gdSecretKeys.getInputStream()));
    return new GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, secrets, scopes)
    .setDataStoreFactory(new FileDataStoreFactory(credentialFolder.getFile()))
    .build();
  }
}
