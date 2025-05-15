package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.gemini.model.type.SafetyCategory;
import spring.boot.gemini.model.type.SafetyThreshold;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SafetySettings {

    private SafetyCategory category;
    private SafetyThreshold threshold;
}
