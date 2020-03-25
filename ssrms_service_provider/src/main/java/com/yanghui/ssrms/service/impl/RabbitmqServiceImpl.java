package com.yanghui.ssrms.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.io.UnsupportedEncodingException;


@Slf4j
public class RabbitmqServiceImpl implements MessageListener {


    public void onMessage(Message message)  {
        String messages = null;
        try {
            messages = new String(message.getBody(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if ("已发送".equals(messages)) {
            messages = "已接收";
        }
        log.info(">>>>>>>>>message:{}", messages);
        log.info(">>>>>>>>>message:{}", messages);
        log.info(">>>>>>>>>message:{}", messages);
    }
}
