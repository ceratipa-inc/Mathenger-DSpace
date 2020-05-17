package com.example.mathengerapi.models.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MathFormula {
    @JsonProperty("formula")
    private String inputFormula;
    private String latex;
}
