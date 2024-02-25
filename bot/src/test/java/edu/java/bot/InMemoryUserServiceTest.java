package edu.java.bot;

import edu.java.bot.user.InMemoryUserRepository;
import edu.java.bot.user.InMemoryUserService;
import edu.java.bot.exceptions.UserIsNotRegisteredException;
import edu.java.bot.user.UserRepository;
import edu.java.bot.user.UserService;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class InMemoryUserServiceTest {
    @Test
    public void listLinksOfUnregisteredUserShouldThrowException() {
        UserService userService = new InMemoryUserService(new InMemoryUserRepository());

        assertThatThrownBy(() -> userService.getTrackedLinks(0L))
            .isInstanceOf(UserIsNotRegisteredException.class);
    }
}
