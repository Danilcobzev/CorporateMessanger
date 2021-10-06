package com.example.corporatemessenger.service;

import com.example.corporatemessenger.domen.Role;
import com.example.corporatemessenger.domen.User;
import com.example.corporatemessenger.domen.dto.UserPOJO;
import com.example.corporatemessenger.providers.JwtConfirmProvider;
import com.example.corporatemessenger.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService, IUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepo;

    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    private final JwtConfirmProvider jwtConfirmProvider;

    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, MailSender mailSender, JwtConfirmProvider jwtConfirmProvider) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.jwtConfirmProvider = jwtConfirmProvider;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
    }

    public boolean addUser(User user) {
        LOGGER.info("Method addUser called");
        User userFromDb = userRepo.findByUsername(user.getUsername());
        boolean exists = true;
        if (userFromDb == null) {
            user.setActive(true);
            user.setRoles(Collections.singleton(Role.USER));
            user.setActivationCode(UUID.randomUUID().toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepo.save(user);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("User saved:" + savedUser.getId());
            }
            sendMessage(user);
        } else {
            LOGGER.info("User already exists");
            exists = false;
        }
        return exists;
    }

    private void sendMessage(User user) {
        if (StringUtils.isEmpty(user.getEmail())) {
            LOGGER.info("User " + user.getUsername() + " [" + user.getId() + "] not have email");
        } else {
            LOGGER.info("Sending an email to confirm the user  " + user.getUsername() + " [" + user.getId() + "]");
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to CorporateMessenger. Please, visit next link: http://localhost:8081/registration/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }
    }

    private void sendMessage(User user, String newEmail) {
        if (StringUtils.isEmpty(user.getEmail())) {
            change(user, newEmail);
            LOGGER.info("User " + user.getUsername() + " added email: " + newEmail);
        } else {
            LOGGER.info("Sending an email to update the email to the user " + user.getUsername() + " [" + user.getId() + "]");
            String message = String.format(
                    "Hello, %s! \n" +
                            "Please, visit next link: http://localhost:8081/user/changeEmail/%s",
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
    public UserPOJO getById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("Can not find user by user id"));
        return new UserPOJO(
                user.getId(),
                user.getUsername(),
                user.isActive(),
                user.getRoles(),
                user.getEmail()
        );

    }

    @Override
    public UserPOJO change(User user, String newEmail) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = isEmailChanged(newEmail, userEmail);
        if (isEmailChanged) {
            user.setEmail(newEmail);
        }
        User savedUser = userRepo.save(user);
        if (isEmailChanged) {
            sendMessage(user);
        }
        return new UserPOJO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.isActive(),
                savedUser.getRoles(),
                savedUser.getEmail()
        );
    }

    private boolean isEmailChanged(String newEmail, String userEmail) {
        return newEmail != null && !newEmail.equals(userEmail) ||
                userEmail != null && !userEmail.equals(newEmail);
    }

    @Override
    public void userWannaChangeEmail(User user, String newEmail) {
        String userEmail = user.getEmail();
        boolean isEmailChanged = isEmailChanged(newEmail, userEmail);
        if (!StringUtils.isEmpty(newEmail) && isEmailChanged) {
            sendMessage(user, newEmail);
        }
        if (isEmailChanged) {
            sendMessage(user);
        }
    }

    @Override
    public UserPOJO changeRoles(Long userId, Map<String, String> form) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("Can not find user by user id"));
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        Set<Role> roles1 = new HashSet<>();
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                roles1.add(Role.valueOf(key));
            }
        }

        user.setRoles(roles1);
        User item = userRepo.save(user);

        return new UserPOJO(
                item.getId(),
                item.getUsername(),
                item.isActive(),
                item.getRoles(),
                item.getEmail()
        );
    }
}
