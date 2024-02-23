package com.gymcrm.gymcrm.exception;

import lombok.Builder;

@Builder
public class ResourceNotFoundException extends RuntimeException{

    private String detailMessage;
}
