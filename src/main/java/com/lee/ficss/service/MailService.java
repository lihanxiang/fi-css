package com.lee.ficss.service;

import com.lee.ficss.util.DataMap;

public interface MailService {

    DataMap sendRandomPassword(String email);

    DataMap sentAuthCode(String email);
}
