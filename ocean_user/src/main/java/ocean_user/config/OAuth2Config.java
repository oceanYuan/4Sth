package ocean_user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 配置OAuth2的授权服务
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2Config extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private DataSource dataSource;

    //配置Client
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //client JDBC模式
        clients.withClientDetails(clientDetails());
        /*clients.inMemory()
                .withClient(JWTClient.clientId)
                .secret(JWTClient.secret)
                .scopes(JWTClient.scopes)
                .autoApprove(true)
                .authorizedGrantTypes("implicit","refresh_token", "password", "authorization_code")
                .accessTokenValiditySeconds(24*3600);//24小时过期*/
    }
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

        security.checkTokenAccess("permitAll()").allowFormAuthenticationForClients();

    }
    //Token管理：使用JWT Tokens
    //授权类型：密码类型，需要设置authenticationManager
    //refresh tokens :由于使用了UserDetailsService，此处需要设置userDetailsService，验证用户的有效性
    //tokenEnhancer :拓展，此处主要用来添加自定义属性
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //添加自定义属性
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
//        tokenEnhancerChain.setTokenEnhancers(
//                Arrays.asList(tokenEnhancer(), jwtTokenEnhancer())
//        );

//        endpoints.userDetailsService(refreshUserDetails());
        endpoints.tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager);

    }


    /////////////////////////////以下为注入的Bean///////////////////////////////////////////////
//    @Bean
//    public UserDetailsService refreshUserDetails() {
//        return new RefreshUserDetailsService();
//    }
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    //用来对JWT Token进行加解密的类，Authorization Server和Resource Server公用
    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("fzp-jwt.jks"), "fzp123".toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("fzp-jwt"));
        return converter;
    }
//    @Bean
//    public TokenEnhancer tokenEnhancer() {
//        return new CustomJwtTokenEnhancer();
//    }

    @Bean // 声明 ClientDetails实现
    public ClientDetailsService clientDetails() {
        return new JdbcClientDetailsService(dataSource);
    }
}
