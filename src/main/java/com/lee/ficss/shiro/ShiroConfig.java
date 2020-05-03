package com.lee.ficss.shiro;

import com.lee.ficss.util.Encryption;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public CustomizeRealm customizeRealm(){
        CustomizeRealm realm = new CustomizeRealm();
        realm.setCredentialsMatcher(hashedCredentialsMatcher());
        return realm;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(customizeRealm());
        return manager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();

        definition.addPathDefinition("/user/login-form", "anon");
        definition.addPathDefinition("/user/logout", "logout");
        definition.addPathDefinition("/admin/**", "authc");
        definition.addPathDefinition("/agenda/**", "authc");
        definition.addPathDefinition("/candidate/**", "authc");
        definition.addPathDefinition("/conference/**", "authc");
        definition.addPathDefinition("/event/**", "authc");
        definition.addPathDefinition("/file/**", "authc");
        definition.addPathDefinition("/overview/**", "authc");
        definition.addPathDefinition("/paper/**", "authc");
        definition.addPathDefinition("/session/**", "authc");
        definition.addPathDefinition("/submission/**", "authc");
        return definition;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        Encryption encryption = new Encryption();
        hashedCredentialsMatcher.setHashAlgorithmName(encryption.getAlgorithmName());
        hashedCredentialsMatcher.setHashIterations(encryption.getIterationTime());
        return hashedCredentialsMatcher;
    }
}
