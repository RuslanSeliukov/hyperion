package com.hyperion.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SelectOption {

    public SelectOption(String value, String label) {
        this.value = value;
        this.label = label;
    }

    private String value;
    private String label;

}
