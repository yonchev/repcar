/*
 * Copyright RepCar AD 2017
 */

package com.repcar.cms.encrypt;

public interface EncryptDecryptService {

    String decrypt(String password);

    String encrypt(String password);

}