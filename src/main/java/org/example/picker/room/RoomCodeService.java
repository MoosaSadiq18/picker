package org.example.picker.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RoomCodeService {

    @Autowired
    RoomRepository roomRepository;

    public String generateCodeForRoom(){
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(new Random().nextInt(max - min + 1) + min);
    }

    public boolean isCodeOfRoom(Long roomId,String code){
        return code.matches(roomRepository.getCodeByRoomId(roomId));
    }
}
