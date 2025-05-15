package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.gemini.model.type.BlockReason;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PromptFeedback {

    private BlockReason blockReason;
    private SafetyRating[] safetyRatings;
}
