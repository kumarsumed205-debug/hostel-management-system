package com.smarthostel.service;

import com.smarthostel.dao.AllocationDAO;
import com.smarthostel.model.Room;

public class AllocationService {
    private final AllocationDAO dao = new AllocationDAO();

    public Room getStudentRoom(String studentId) throws Exception { return dao.findStudentRoom(studentId); }
    public void allocate(String studentId, int roomId) throws Exception { dao.allocate(studentId, roomId); }
}
