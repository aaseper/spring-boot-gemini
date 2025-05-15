package spring.boot.gemini.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenerationConfig {

    private Double temperature;
    private Double topP;
    private Integer topK;
    private Integer maxOutputTokens;
    private String[] stopSequences;
}
