package org.example.picker.room;

import lombok.RequiredArgsConstructor;
import org.example.picker.auth.UserRepository;
import org.example.picker.auth.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    RoomRepository roomRepository;

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(@RequestBody RoomCreationRequest request){
        Long userId = userRepository.getUserIdByUsername(request.getCreator());

        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }
        return ResponseEntity.ok(roomService.createRoom(request));
    }

    @PostMapping("/deleteRoom")
    public ResponseEntity<String> deleteRoom(@RequestBody RoomDeletionRequest request){
        Long userId = userRepository.getUserIdByUsername(request.getCreator());

        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }
        return ResponseEntity.ok(roomService.deleteRoom(request));
    }

    @PostMapping("/joinRoom")
    public ResponseEntity<String> joinRoom(@RequestBody RoomJoinRequest request){
        Long userId = userRepository.getUserIdByUsername(request.getUsername());
        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }
        ResponseEntity<String> response = roomService.joinRoom(request);
        return ResponseEntity.ok(response.getBody());
    }

    @GetMapping("/getRooms")
    public List<RoomEntity> getRooms(){
        return roomService.getAllRooms();
    }
}
