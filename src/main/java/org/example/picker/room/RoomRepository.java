package org.example.picker.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

     @Query("select i.roomId from RoomEntity i where i.roomName =:roomName")
     Long getRoomIdByRoomName(@Param("roomName") String roomName);

     @Query("select i.userId from UserEntity i where i.username =:creator")
     Long getUserIdByUserName(@Param("creator") String creator);

     @Query("select i.code from RoomEntity i where i.roomId =:roomId")
     String getCodeByRoomId(@Param("roomId") Long roomId);

    @Query("select i.creator from RoomEntity i where i.roomId =:roomId")
    String getCreatorByRoom(@Param("roomId") Long roomId);

    @Query("select i.creatorId from RoomEntity i where i.roomName =:roomName")
    Long getCreatorIdByRoom(@Param("roomName") String roomName);

    Optional<RoomEntity> findByCreatorAndRoomName(String creator, String roomName);

}