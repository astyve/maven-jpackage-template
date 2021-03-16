package com.changenode.plugin;

import org.springframework.stereotype.Service;

@Service
public class HelloWorldService {
    public String now() {
        return "Hello World! " + java.util.Calendar.getInstance().getTime();
    }
}
