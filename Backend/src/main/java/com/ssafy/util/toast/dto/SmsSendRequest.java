package com.ssafy.util.toast.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendRequest {

    private String body;
    private String sendNo;
    private List<Recipient> recipientList;

}
