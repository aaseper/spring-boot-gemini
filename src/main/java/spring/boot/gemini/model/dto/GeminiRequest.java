package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GeminiRequest {

    private Content[] contents;
    private GenerationConfig generationConfig;
    private SafetySettings[] safetySettings;
}
