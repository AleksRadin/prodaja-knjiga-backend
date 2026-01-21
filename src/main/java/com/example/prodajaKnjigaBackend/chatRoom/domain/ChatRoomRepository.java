package com.example.prodajaKnjigaBackend.chatRoom.domain;

import com.example.prodajaKnjigaBackend.user.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE (cr.user1 = :userA AND cr.user2 = :userB) OR (cr.user1 = :userB AND cr.user2 = :userA)")
    Optional<ChatRoomEntity> findByParticipants(@Param("userA") UserEntity userA, @Param("userB") UserEntity userB);

    @Query("SELECT cr FROM ChatRoomEntity cr WHERE cr.user1.id = :userId OR cr.user2.id = :userId")
    List<ChatRoomEntity> findAllByUserId(@Param("userId") Long userId);

    //List<ChatRoomEntity> findByUser1Id(Long user1Id);
}

