package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeminiResponse {

    private Candidate[] candidates;
    private PromptFeedback promptFeedback;
}
