package org.example.picker.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationRequest request){
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @PostMapping("/deleteRoom")
    public ResponseEntity<String> deleteRoom(@RequestBody RoomDeletionRequest request){
        return ResponseEntity.ok(roomService.deleteRoom(request));
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<String> joinRoom(@RequestBody RoomJoinRequest request){
        return ResponseEntity.ok(roomService.joinRoom(request));
    }

    @GetMapping("/getRooms")
    public List<RoomEntity> getRooms(){
        return roomService.getAllRooms();
    }
}
