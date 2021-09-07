package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.Role;
import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;
import com.example.firstSpringProgect.domen.dto.UserPOJO;
import com.example.firstSpringProgect.providers.JwtConfirmProvider;
import com.example.firstSpringProgect.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, IUserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private JwtConfirmProvider jwtConfirmProvider;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepo.findByUsername(user.getUsername());

        if (userFromDb != null) {
            return false;
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());

        userRepo.save(user);

        sendMessage(user);

        return true;
    }

    private void sendMessage(User user) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to CorporateMessenger. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    private void sendMessage(User user, String newEmail) {
        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to CorporateMessenger. Please, visit next link: http://localhost:8081/user/changeEmail/%s",
                    user.getUsername(),
                    jwtConfirmProvider.generateToken(user.getId(), newEmail)
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);

        userRepo.save(user);

        return true;
    }

    @Override
    public List<UserPOJO> findAll() {
        ArrayList<UserPOJO> list = new ArrayList<UserPOJO>();
        for (User item : userRepo.findAll()) {
            UserPOJO userPOJO = new UserPOJO(
                    item.getId(),
                    item.getUsername(),
                    item.isActive(),
                    item.getRoles(),
                    item.getEmail()
            );
            list.add(userPOJO);

        }
        return list;
    }

    @Override
    public UserPOJO save(UserPOJO userPOJO, String password) {
        User user = new User();
        user.setRoles(userPOJO.getRoles());
        user.setId(userPOJO.getId());
        user.setUsername(userPOJO.getUsername());
        user.setActive(userPOJO.isActive());
        user.setPassword(password);
        user.setEmail(userPOJO.getEmail());

        User item = userRepo.save(user);

        UserPOJO res = new UserPOJO(
                item.getId(),
                item.getUsername(),
                item.isActive(),
                item.getRoles(),
                item.getEmail()
        );
        return res;
    }

    @Override
    public UserPOJO getById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        User u = optionalUser.get();
        UserPOJO up = new UserPOJO(
                u.getId(),
                u.getUsername(),
                u.isActive(),
                u.getRoles(),
                u.getEmail()
        );
        return up;

    }

    @Override
    public UserPOJO change(User user, String newEmail) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (newEmail != null && !newEmail.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(newEmail));
        if (isEmailChanged) {
            user.setEmail(newEmail);
        }
        User savedUser = userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
        UserPOJO savedUserPOJO = new UserPOJO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.isActive(),
                savedUser.getRoles(),
                savedUser.getEmail()
        );
        return savedUserPOJO;
    }

    @Override
    public void userWannaChangeEmail(User user, String newEmail) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = (newEmail != null && !newEmail.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(newEmail));
        if (isEmailChanged) {
            // user.setEmail(email);

            if (!StringUtils.isEmpty(newEmail)) {
                sendMessage(user, newEmail);
                //   user.setActivationCode(UUID.randomUUID().toString());
            }
        }
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    @Override
    public UserPOJO changeRoles(Long userId, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        Set<Role> roles1 = new HashSet<>();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                roles1.add(Role.valueOf(key));
            }
        }

        Optional<User> optionalUser = userRepo.findById(userId);
        User user = optionalUser.get();
        user.setRoles(roles1);
        User item = userRepo.save(user);
        UserPOJO res = new UserPOJO(
                item.getId(),
                item.getUsername(),
                item.isActive(),
                item.getRoles(),
                item.getEmail()
        );

        return res;
    }
}
