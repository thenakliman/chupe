package org.thenakliman.chupe.Services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.thenakliman.chupe.Models.User;
import org.thenakliman.chupe.Repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    UserService userService;

    @Test
    public void shouldReturnEmptyUser() {
        BDDMockito.given(userRepository.findAll()).willReturn(null);
        List<User> users = userService.getAllUsers();
        assertEquals(null, users);
    }

    @Test
    public void shouldReturnUser() {
        User test_user1 = new User("user1_fist_name", "user1_last_name", "user1_username", "user1_email", "user1_password", true);
        User test_user2 = new User("user2_fist_name", "user2_last_name", "user2_username", "user2_email", "user2_password", false);
        List<User> exp_users = new ArrayList<User>();
        exp_users.add(test_user1);
        exp_users.add(test_user2);
        BDDMockito.given(userRepository.findAll()).willReturn(exp_users);
        List<User> users = userService.getAllUsers();
        assertEquals(exp_users, users);
    }
}