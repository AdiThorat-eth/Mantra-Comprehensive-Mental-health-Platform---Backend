package com.mantra.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminStatsDTO {
    private long totalUsers;
    private long totalTherapists;
    private long totalBookings;
    private long totalMeditations;
    private long totalPosts;
    private long totalReplies;
    private long totalContactMessages;
}