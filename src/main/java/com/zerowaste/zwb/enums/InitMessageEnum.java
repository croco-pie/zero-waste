package com.zerowaste.zwb.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InitMessageEnum {

    @JsonProperty("/start")
    START,
    @JsonProperty("/search")
    SEARCH
}
