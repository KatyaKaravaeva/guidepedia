package com.guidepedia.service;

import com.guidepedia.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private static final Integer ROLE_COUNT = 3;

    @Autowired


    public UserEntity findUser(Long userID) throws UsernameNotFoundException {
        return userDao.findById(userID).orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

    public UserEntity changeUser(Long userID, UserEntity newUser) throws UsernameNotFoundException{
        UserEntity user = userDao.findById(userID).orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
        user.setCommonname(newUser.getCommonname());
        user.setEmail(newUser.getEmail());
        userDao.save(user);
        return user;
    }

    public UserEntity createUser(String commonname, String email) {
        Optional<UserEntity> user = userDao.findByEmail(email);
        System.out.println(user);
        System.out.println(email);
        if (user.isPresent()) {
            return user.get();
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setCommonname(commonname);
        userEntity.setEmail(email);
        userEntity.setCreatedAt(LocalDateTime.now());
        userDao.save(userEntity);
        System.out.println(userEntity.getId());
        createProfile(userEntity.getId());
        return userEntity;
    }

    public List<UserEntity> getUsers(Integer courseID, Integer roleID, Boolean approved) {
        if (courseID > courseDao.count() || roleID > ROLE_COUNT) {
            throw new BusinessException(ExceptionMessage.INCORRECT_DATA);
        }
        return userDao.getListOfUsers(courseID, roleID, approved);
    }

    public RequestEntity getUserRequest(Long userID, Integer courseID) {
        userDao.findById(userID).orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
        courseDao.findById(courseID).orElseThrow(() -> new BusinessException(ExceptionMessage.INCORRECT_DATA));

        return requestDao.findByUserIDAndCourseID(userID, courseID)
                .orElseThrow(() -> new BusinessException(ExceptionMessage.OBJECT_NOT_FOUND));
    }

    public RequestEntity updateUserRequest(Integer courseID, Long userID, RequestEntity newRequestEntity) {
        RequestEntity request = requestDao.findByUserIDAndCourseID(userID, courseID).orElseThrow(() -> new BusinessException(ExceptionMessage.OBJECT_NOT_FOUND));
        Optional<UserCourseEntity> userCourse = userCourseDao.findByUserIDAndCourseID(userID, courseID);
        request.setApproved(newRequestEntity.getApproved());
        System.out.println(newRequestEntity.getApproved());
        if (newRequestEntity.getApproved()) {
            if (userCourse.isEmpty()){
                createUserCourse(courseID, userID, request.getRoleID());
            } else {
                userCourse.ifPresent(u -> u.setRoleID(request.getRoleID()));
            }
            if (request.getRoleID() == 1) {
                chatService.addMember(userID, courseID);
            }
        }
        request.setUpdatedAt(LocalDateTime.now());
        requestDao.save(request);
        return request;
    }

    public Map<String, Object> getProfile(){
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProfileEntity profile = userDao.getProfile(user.getId()).orElseThrow(() -> new BusinessException(ExceptionMessage.OBJECT_NOT_FOUND));
        Map<String, Object> json = new HashMap<String, Object>();
        json.put("userID", user.getId());
        json.put("avatar", profile.getAvatar());
        json.put("commonname", user.getUsername());
        json.put("email", user.getEmail());
        return json;
    }

    public RequestEntity createRequest(Integer courseID, Integer roleID) {
        if (roleID > 2) {
            throw new BusinessException(ExceptionMessage.ROLE_ERROR);
        }
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        requestDao.findByUserIDAndCourseID(user.getId(), courseID).ifPresent(r -> {throw new BusinessException(ExceptionMessage.OBJECT_ALREADY_EXISTS);});
        RequestEntity request = new RequestEntity();
        request.setCourseID(courseID);
        request.setUserID(user.getId());
        request.setRoleID(roleID);
        request.setApproved(false);
        request.setCreatedAt(LocalDateTime.now());
        requestDao.save(request);
        return request;
    }

    private void createProfile(Long id) {
        ProfileEntity profile = new ProfileEntity();
        profile.setUserID(id);
        profile.setCreatedAt(LocalDateTime.now());
        profileDao.save(profile);
    }

    public void createUserCourse(Integer courseID, Long userID, Integer roleID) {
        UserCourseEntity userCourse = new UserCourseEntity();
        userCourse.setUserID(userID);
        userCourse.setCourseID(courseID);
        userCourse.setRoleID(roleID);
        userCourseDao.save(userCourse);
    }

    public void updateUserCourse(Integer courseID, Long userID, Integer roleID) {
        Optional<UserCourseEntity> userCourse = userCourseDao.findByUserIDAndCourseID(userID, courseID);
        userCourse.ifPresent(u ->{
            u.setRoleID(roleID);
            userCourseDao.save(u);
        });
    }

    public UserEntity registerUser() {
        return userDao.findById(((UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
                .orElseThrow(() -> new BusinessException(ExceptionMessage.USER_NOT_FOUND));
    }

    public ProfileEntity updateProfile(ProfileEntity profileEntity) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ProfileEntity profile = profileDao.getByUserID(user.getId())
                .orElseThrow(() -> new BusinessException(ExceptionMessage.OBJECT_NOT_FOUND));
        profile.setAvatar(profileEntity.getAvatar());
        profileDao.save(profile);
        return profile;
    }

    public Optional<UserCourseEntity> getRole(Integer courseID) {
        UserAndRole user = (UserAndRole) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userCourseDao.findByUserIDAndCourseID(user.getId(), courseID);
    }
}
