package com.afikur.healthcare.service;

import java.util.Map;

public interface AdminService {
    long getTotalUsers();

    long getTotalPatients();

    long getTotalPrescriptions();

    Map<String, Object> getDashboardAnalytics();
}
