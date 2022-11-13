package com.kitaplik.libraryservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.authentication.ClientAuthentication;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.config.AbstractVaultConfiguration;
import org.springframework.vault.support.VaultToken;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@RefreshScope
public class VaultConfig {

    @Value("${count}")
    private Integer count;

    public Integer getCount() {
        return count;
    }

}
