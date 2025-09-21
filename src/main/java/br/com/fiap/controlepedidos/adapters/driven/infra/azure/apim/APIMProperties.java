package br.com.fiap.controlepedidos.adapters.driven.infra.azure.apim;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "lambda")
public class APIMProperties {

    private String addcpfendpoint;
    private String authcpfendpoint;
    private String authkey;
    private String addcpfatuhvalue;
    private String authcpfauthvalue;

}