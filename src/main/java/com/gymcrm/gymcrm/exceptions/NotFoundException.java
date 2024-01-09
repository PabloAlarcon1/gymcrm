package com.gymcrm.gymcrm.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NotFoundException extends RuntimeException{

    private String detailedMessage;



}
