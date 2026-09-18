package com.smarthostel.service;

import com.smarthostel.model.Room;

import java.util.Comparator;
import java.util.List;

public class RoomRecommendationService {
    public record Recommendation(Room room, int score) {}

    public List<Recommendation> recommend(List<Room> rooms, String preferredBlock, String preferredType) {
        return rooms.stream()
                .filter(r -> r.getOccupied() < r.getCapacity())
                .map(r -> new Recommendation(r, score(r, preferredBlock, preferredType)))
                .sorted(Comparator.comparingInt(Recommendation::score).reversed()
                        .thenComparing(r -> r.room().getRoomNumber()))
                .toList();
    }

    public int score(Room room, String preferredBlock, String preferredType) {
        int score = 0;
        if (preferredBlock != null && !preferredBlock.isBlank() &&
                room.getBlockName().equalsIgnoreCase(preferredBlock.trim())) score += 40;
        if (preferredType != null && !preferredType.isBlank() &&
                room.getRoomType().equalsIgnoreCase(preferredType.trim())) score += 30;
        if (room.getOccupied() < room.getCapacity()) score += 30;
        return score;
    }
}
