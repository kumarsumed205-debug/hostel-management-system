package com.smarthostel;

import com.smarthostel.model.Room;
import com.smarthostel.service.ComplaintPriorityService;
import com.smarthostel.service.RoomRecommendationService;

import java.util.List;

public class AlgorithmSelfTest {
    public static void main(String[] args) {
        testRoomRecommendation();
        testComplaintPriority();
        System.out.println("All algorithm tests passed.");
    }

    private static void testRoomRecommendation() {
        Room r1 = new Room(1, "A101", "A", 2, 0, "2-Seater", "AVAILABLE");
        Room r2 = new Room(2, "B201", "B", 3, 1, "3-Seater", "PARTIALLY_OCCUPIED");
        var service = new RoomRecommendationService();
        var result = service.recommend(List.of(r1, r2), "A", "2-Seater");
        assert result.getFirst().room().getRoomNumber().equals("A101");
        assert result.getFirst().score() == 100;
    }

    private static void testComplaintPriority() {
        var service = new ComplaintPriorityService();
        assert service.calculatePriority("Electrical", "Low").equals("HIGH");
        assert service.calculatePriority("Internet", "Medium").equals("MEDIUM");
        assert service.calculatePriority("Cleaning", "Low").equals("LOW");
        assert service.calculatePriority("Other", "High").equals("HIGH");
    }
}
