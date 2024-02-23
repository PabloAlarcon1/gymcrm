package com.gymcrm.gymcrm.exception;

import lombok.Builder;

@Builder
public class DuplicatedResourceException extends RuntimeException{

    private String detailMessage;
}
