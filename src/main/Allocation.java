package com.smarthostel.model;

import java.time.LocalDateTime;

public class Allocation {
    private final int allocationId;
    private final String studentId;
    private final int roomId;
    private final LocalDateTime allocationDate;

    public Allocation(int allocationId, String studentId, int roomId, LocalDateTime allocationDate) {
        this.allocationId = allocationId;
        this.studentId = studentId;
        this.roomId = roomId;
        this.allocationDate = allocationDate;
    }

    public int getAllocationId() { return allocationId; }
    public String getStudentId() { return studentId; }
    public int getRoomId() { return roomId; }
    public LocalDateTime getAllocationDate() { return allocationDate; }
}
