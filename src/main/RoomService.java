package com.smarthostel.service;

import com.smarthostel.dao.RoomDAO;
import com.smarthostel.model.Room;
import com.smarthostel.util.Validator;

import java.util.List;

public class RoomService {
    private final RoomDAO dao = new RoomDAO();
    private final RoomRecommendationService recommendationService = new RoomRecommendationService();

    public List<Room> getAll() throws Exception { return dao.findAll(); }

    public void add(String roomNumber, String block, String capacity, String roomType) throws Exception {
        Validator.room(roomNumber, block, capacity);
        dao.insert(new Room(0, roomNumber.trim(), block.trim().toUpperCase(), Integer.parseInt(capacity),
                0, roomType, "AVAILABLE"));
    }

    public void delete(int roomId) throws Exception { dao.delete(roomId); }

    public List<RoomRecommendationService.Recommendation> recommend(String preferredBlock, String preferredType) throws Exception {
        return recommendationService.recommend(dao.findAvailable(preferredBlock, preferredType), preferredBlock, preferredType);
    }
}
