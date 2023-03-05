package com.tripscanner.TripScanner.utils;

import com.tripscanner.TripScanner.model.User;

public interface AbstractEmailService {

    void sendRegistrationEmail(User user);

}