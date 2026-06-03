package org.example.picker.room;
import org.example.picker.auth.AuthDetailer;
import org.example.picker.auth.UserEntity;
import org.example.picker.auth.UserRepository;
import org.example.picker.images.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomCodeService roomCodeService;

    @Autowired
    S3Service s3Service;

    @Autowired
    private AuthDetailer authDetailer;

    public String createRoom(RoomCreationRequest request){
        Optional<RoomEntity> existingRoom = roomRepository.findByCreatorAndRoomName(request.getCreator(),request.getRoomName());
        if(existingRoom.isPresent()){
            return "Room " + request.getRoomName() + " already exists";
        }
        else if(!authDetailer.getCurrentUsername().matches(request.getCreator())){
            return "Room creator " + request.getCreator() + " doesnot exist and authDetailer is "+ authDetailer.getCurrentUsername();
        }

        RoomEntity room = new RoomEntity();
        room.setRoomName(request.getRoomName());
        room.setCreator(request.getCreator());
        room.setCreatorId(roomRepository.getCreatorIdByRoom(request.getRoomName()));
        room.setCode(roomCodeService.generateCodeForRoom());
        roomRepository.save(room);
        return "Room " + request.getRoomName() + " created successfully by " + request.getCreator();
    }

    public String deleteRoom(RoomDeletionRequest request){
        Long roomId = roomRepository.getRoomIdByRoomName(request.getRoomName());
        String roomCreator = roomRepository.getCreatorByRoom(roomId);

        if(!(request.getCreator().equals(roomCreator))){
            return "Only creator can delete the room";
        }

        RoomEntity room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        roomRepository.delete(room);
        return "Room " + request.getRoomName() + " deleted successfully";
    }

    public ResponseEntity<byte[]> joinRoom(RoomJoinRequest request){
        Long roomId = roomRepository.getRoomIdByRoomName(request.getRoomName());
        String roomCreator = roomRepository.getCreatorByRoom(roomId);
        Long userId = userRepository.getUserIdByUsername(request.getUsername());


        if(roomCreator.equals(request.getUsername())){
            return ResponseEntity.badRequest().body("Creator cannot join".getBytes());
        }
        else if(!roomCodeService.isCodeOfRoom(roomId,request.getCode())){
            return ResponseEntity.badRequest().body("Wrong room code".getBytes());
        }

        UserEntity existingUser = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new RuntimeException("Joining User not found"));

        RoomEntity room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        RoomMemberEntity member = new RoomMemberEntity();

        member.setUsername(request.getUsername());
        room.getMembers().add(member);
        room.setMemberCount(room.getMemberCount()+1);
        roomRepository.save(room);
        byte[] r = {1,2};
        return ResponseEntity.ok(r);

    }

    public List<RoomEntity> getAllRooms(){
        return roomRepository.findAll();
    }

}
