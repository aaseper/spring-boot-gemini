package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.gemini.model.type.SafetyCategory;
import spring.boot.gemini.model.type.SafetyProbability;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetyRating {

    private SafetyCategory category;
    private SafetyProbability probability;
}
