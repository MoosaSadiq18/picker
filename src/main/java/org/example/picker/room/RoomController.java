package org.example.picker.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationRequest request){
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<String> joinRoom(@RequestBody RoomJoinRequest request){
        return ResponseEntity.ok(roomService.joinRoom(request));
    }
}
