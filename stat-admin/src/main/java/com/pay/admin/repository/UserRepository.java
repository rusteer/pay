package com.pay.admin.repository;
import com.pay.admin.entity.UserEntity;
import com.pay.admin.repository.framework.MyJpaRepository;

public interface UserRepository extends MyJpaRepository<UserEntity> {
    UserEntity findByLoginName(String loginName);
}
