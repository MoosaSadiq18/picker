package org.example.picker.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomJoinRequest {

    private String creator;
    private String roomName;
    private String code;
}
