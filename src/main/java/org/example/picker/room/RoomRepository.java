package org.example.picker.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<RoomEntity,Long> {

     @Query("select i.id from RoomEntity i where i.roomName =:roomName")
     Long getRoomIdByRoomName(@Param("roomName") String roomName);

     @Query("select i.code from RoomEntity i where i.id =:id")
     String getCodeByRoomId(@Param("id") Long id);

    Optional<RoomEntity> findByCreatorAndRoomName(String creator, String roomName);

}