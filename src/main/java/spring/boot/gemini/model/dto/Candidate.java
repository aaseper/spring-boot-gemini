package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.gemini.model.type.FinishReason;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Candidate {

    private Content content;
    private FinishReason finishReason;
    private SafetyRating[] safetyRatings;
}
