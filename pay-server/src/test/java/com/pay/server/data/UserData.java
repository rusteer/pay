/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.pay.server.data;
import org.springside.modules.test.data.RandomData;
import com.pay.server.entity.User;

public class UserData {
    public static User randomNewUser() {
        User user = new User();
        user.setLoginName(RandomData.randomName("user"));
        user.setName(RandomData.randomName("User"));
        user.setPlainPassword(RandomData.randomName("password"));
        return user;
    }
}
