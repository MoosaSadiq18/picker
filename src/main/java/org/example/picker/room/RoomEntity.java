package org.example.picker.room;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.picker.auth.UserEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Entity
@Table(name = "Rooms")
@Component
@AllArgsConstructor
@Data
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String roomName;
    private String creator;
    private String code;
    private int memberCount;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private List<RoomMemberEntity> members;

    public RoomEntity(){
        this.memberCount = 0;
    }
}