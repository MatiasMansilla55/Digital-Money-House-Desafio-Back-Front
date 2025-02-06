package com.example.api_activity.service;

import com.example.api_activity.dto.entry.ActivityFilterEntryDTO;
import com.example.api_activity.dto.exit.ActivityOutDTO;
import com.example.api_activity.entities.Activity;
import com.example.api_activity.exceptions.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface IActivityService {
    List<ActivityOutDTO> getAllActivitiesByAccountId(Long accountId, String token) throws ResourceNotFoundException;
    ActivityOutDTO getActivityDetail(Long accountId, Long id) throws ResourceNotFoundException;
    List<ActivityOutDTO> filterActivities(ActivityFilterEntryDTO filter);
    Activity getActivityById(Long activityId) throws ResourceNotFoundException;
    ByteArrayOutputStream generateActivityReceipt(Activity activity);
}
