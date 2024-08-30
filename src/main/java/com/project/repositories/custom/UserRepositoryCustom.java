package com.project.repositories.custom;

import com.project.models.User;
import com.project.requests.UserSearchRequest;

import java.util.List;

public interface UserRepositoryCustom {
    List<User> getAllUsers(UserSearchRequest request);
}
