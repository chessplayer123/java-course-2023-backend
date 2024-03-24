package edu.java.scrapper.service.jpa;

import edu.java.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import edu.java.service.jpa.JpaLinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JpaLinkServiceTest extends IntegrationEnvironment {
    @Autowired
    private JpaLinkService linkService;
    @Autowired
    private JpaLinkRepository linkRepository;

    @Test
    public void test() {
    }
}
