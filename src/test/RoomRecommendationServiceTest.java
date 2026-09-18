package com.smarthostel;

import com.smarthostel.model.Room;
import com.smarthostel.service.RoomRecommendationService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoomRecommendationServiceTest {
    @Test
    void preferredRoomGetsHighestScore() {
        Room preferred = new Room(1, "A101", "A", 2, 0, "2-Seater", "AVAILABLE");
        Room other = new Room(2, "B201", "B", 3, 1, "3-Seater", "PARTIALLY_OCCUPIED");
        var service = new RoomRecommendationService();

        var result = service.recommend(List.of(other, preferred), "A", "2-Seater");

        assertEquals("A101", result.getFirst().room().getRoomNumber());
        assertEquals(100, result.getFirst().score());
    }
}
