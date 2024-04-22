package com.digitallending.userservice.service.def;


public interface RSAService {


    String getPublicKey();

    String decodeMessage(String encodedString);

    String encodeMessage(String string);


}
