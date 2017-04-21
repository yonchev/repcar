/*
 * Copyright RepCar AD 2017
 */
package com.repcar.notification.encryption;

public interface EncryptDecryptService {

    String encrypt(String password);

    String decrypt(String password);
}
