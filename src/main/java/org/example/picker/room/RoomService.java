package org.example.picker.room;
import org.example.picker.auth.AuthDetailer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomCodeService roomCodeService;

    @Autowired
    private AuthDetailer authDetailer;


    public String createRoom(RoomCreationRequest request){
        Optional<RoomEntity> existingRoom = roomRepository.findByCreatorAndRoomName(request.getCreator(),request.getRoomName());
        if(existingRoom.isPresent()){
            return "Room " + request.getRoomName() + " already exists";
        }

        RoomEntity room = new RoomEntity();
        room.setRoomName(request.getRoomName());
        room.setCreator(request.getCreator());
        room.setCode(roomCodeService.generateCodeForRoom());
        roomRepository.save(room);
        return "Room " + request.getRoomName() + " created successfully by " + request.getCreator();
    }

    public String joinRoom(RoomJoinRequest request){
        Long roomId = roomRepository.getRoomIdByRoomName(request.getRoomName());
        String email = authDetailer.getCurrentUserEmail();

        if(!roomCodeService.isCodeOfRoom(roomId,request.getCode())){
            return "Wrong room code";
        }

        RoomEntity room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        RoomMemberEntity member = new RoomMemberEntity();

        member.setUsername(email);
        room.getMembers().add(member);
        room.setMemberCount(room.getMemberCount()+1);
        roomRepository.save(room);
        return "Member " + email + " joined " + room.getRoomName() + " Successfully";
    }

}
