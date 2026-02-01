package uk.ac.ed.inf.acpAssignment.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SystemEnvironment {

    @Value( "${acp.ilp-endpoint:https://ilp-rest-2025-all-assigned.azurewebsites.net/}")
    private String ilpServiceEndpoint;

    @Bean(name = "ilpServiceEndpoint")
    public String getIlpServiceEndpoint(){
        return ilpServiceEndpoint;
    }
}
