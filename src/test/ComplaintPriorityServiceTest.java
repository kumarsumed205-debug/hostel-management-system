package com.smarthostel;

import com.smarthostel.service.ComplaintPriorityService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ComplaintPriorityServiceTest {
    private final ComplaintPriorityService service = new ComplaintPriorityService();

    @Test
    void electricalIsHigh() {
        assertEquals("HIGH", service.calculatePriority("Electrical", "Low"));
    }

    @Test
    void cleaningIsLowWhenSeverityIsLow() {
        assertEquals("LOW", service.calculatePriority("Cleaning", "Low"));
    }

    @Test
    void highSeverityOverridesCategory() {
        assertEquals("HIGH", service.calculatePriority("Cleaning", "High"));
    }
}
