package org.example.picker.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findByUsername(String username);

    @Query("select i.userId from UserEntity i where i.username =:username")
    Long getUserIdByUsername(@Param("username") String username);

    @Query("select i.username from UserEntity i where i.userId =:userId")
    String getUsernameByUserId(@Param("userId") Long userId);

}
