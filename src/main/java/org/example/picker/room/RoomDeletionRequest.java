package org.example.picker.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDeletionRequest {

    private String creator;
    private String roomName;
}
