package com.smarthostel.model;

public class Room {
    private int roomId;
    private String roomNumber;
    private String blockName;
    private int capacity;
    private int occupied;
    private String roomType;
    private String status;

    public Room() {}

    public Room(int roomId, String roomNumber, String blockName, int capacity, int occupied, String roomType, String status) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.blockName = blockName;
        this.capacity = capacity;
        this.occupied = occupied;
        this.roomType = roomType;
        this.status = status;
    }

    public int getRoomId() { return roomId; }
    public String getRoomNumber() { return roomNumber; }
    public String getBlockName() { return blockName; }
    public int getCapacity() { return capacity; }
    public int getOccupied() { return occupied; }
    public String getRoomType() { return roomType; }
    public String getStatus() { return status; }
}
