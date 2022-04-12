package example.micronaut;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.cognito.AutoVerifiedAttrs;
import software.amazon.awscdk.services.cognito.CognitoDomainOptions;
import software.amazon.awscdk.services.cognito.OAuthFlows;
import software.amazon.awscdk.services.cognito.OAuthScope;
import software.amazon.awscdk.services.cognito.OAuthSettings;
import software.amazon.awscdk.services.cognito.SignInAliases;
import software.amazon.awscdk.services.cognito.UserPool;
import software.amazon.awscdk.services.cognito.UserPoolClientOptions;
import software.amazon.awscdk.services.cognito.UserPoolDomainOptions;
import software.constructs.Construct;

import java.util.Arrays;
import java.util.Collections;

public class AppStack extends Stack {
    public static final String OAUTH_CALLBACK_COGNITO = "/oauth/callback/cognito";
    public static final String LOCALHOST = "http://localhost:8080";
    public static final String PATH_LOGOUT = "/logout";

    public AppStack(final Construct parent, final String id) {
        this(parent, id, null);
    }

    public AppStack(final Construct parent, final String id, final StackProps props) {
        super(parent, id, props);
        UserPool userPool = UserPool.Builder.create(this, "micronautguidepool")
                .userPoolName("micronautguidepool-name")
                .signInAliases(SignInAliases.builder()
                        .phone(false)
                        .username(false)
                        .email(true)
                        .build())
                .autoVerify(AutoVerifiedAttrs.builder()
                        .email(true)
                        .build())
                .selfSignUpEnabled(true)
                .build();
        userPool.addDomain("micronautguidepool-domain", UserPoolDomainOptions.builder()
                        .cognitoDomain(CognitoDomainOptions.builder()
                                .domainPrefix("micronautguidepool")
                                .build())
                .build());
        userPool.addClient("micronautguidepool-client", UserPoolClientOptions.builder()
                .generateSecret(true)
                .userPoolClientName("micronautguidepool-client")
                .oAuth(OAuthSettings.builder()
                        .scopes(Arrays.asList(OAuthScope.PROFILE,
                                OAuthScope.EMAIL,
                                OAuthScope.OPENID))
                        .flows(OAuthFlows.builder()
                                .authorizationCodeGrant(true)
                                .build())
                        .callbackUrls(Collections.singletonList(LOCALHOST + OAUTH_CALLBACK_COGNITO))
                        .logoutUrls(Collections.singletonList(LOCALHOST + PATH_LOGOUT))
                        .build())
                .build());
    }

}
