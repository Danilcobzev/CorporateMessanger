package com.example.firstSpringProgect.service;

import com.example.firstSpringProgect.domen.User;
import com.example.firstSpringProgect.domen.dto.MessagePOJO;
import com.example.firstSpringProgect.domen.dto.UserPOJO;
import com.example.firstSpringProgect.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService, IUserService{
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username);
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
