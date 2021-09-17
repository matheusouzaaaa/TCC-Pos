package com.example.tcc.Common;

import com.example.tcc.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public interface ITimeSlotLoadListener {
    void onTimeSlotLoadSuccess(ArrayList<TimeSlot> timeSlotList);
    void onTimeSlotLoadFailed(String message);
    void onTimeSlotLoadEmpty();
}
