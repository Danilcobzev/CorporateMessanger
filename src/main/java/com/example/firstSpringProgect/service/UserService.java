package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.Role;
import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;
import com.example.firstSpringProgect.domen.dto.UserPOJO;
import com.example.firstSpringProgect.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserService implements UserDetailsService, IUserService{
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

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

        if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to CorporateMessenger. Please, visit next link: http://localhost:8081/activate/%s",
                    user.getUsername(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation code", message);
        }

        return true;
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
        for (User item : userRepo.findAll()){
            UserPOJO userPOJO = new UserPOJO(
                    item.getId(),
                    item.getUsername(),
                    item.getPassword(),
                    item.isActive(),
                    item.getRoles()
            );
            list.add(userPOJO);

        }
        return list;
    }

    @Override
    public UserPOJO save(UserPOJO userPOJO) {
        User user = new User();
        user.setRoles(userPOJO.getRoles());
        user.setId(userPOJO.getId());
        user.setUsername(userPOJO.getUsername());
        user.setActive(userPOJO.isActive());
        user.setPassword(userPOJO.getPassword());

        User item =userRepo.save(user);

        UserPOJO res = new UserPOJO(
                item.getId(),
                item.getUsername(),
                item.getPassword(),
                item.isActive(),
                item.getRoles()
        );
        return res ;
    }

    @Override
    public UserPOJO getById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        User u = optionalUser.get();
        UserPOJO up = new UserPOJO(
                u.getId(),
                u.getUsername(),
                u.getPassword(),
                u.isActive(),
                u.getRoles()
        );
        return up;
    }
}
