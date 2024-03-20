package edu.java.scrapper.database.jooq;

import edu.java.repository.jooq.JooqLinkRepository;
import edu.java.scrapper.database.IntegrationEnvironment;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class JooqLinkRepositoryTest extends IntegrationEnvironment {
    @Autowired
    private JooqLinkRepository linkRepository;
    @Autowired
    private DSLContext dslContext;


    @Test
    @Transactional
    @Rollback
    void addLinkShouldInsertRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void removeLinkShouldDeleteRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void updateShouldChangeRecord() {
    }

    @Test
    @Transactional
    @Rollback
    void findByUrlShouldReturnExpectedLink() {
    }

    @Test
    @Transactional
    @Rollback
    void pruneShouldDeleteAllOrphansLinks() {
    }

    @Test
    @Transactional
    @Rollback
    void findLastCheckTimeExceedLimitShouldReturnAllOldLinks() {
    }
}
