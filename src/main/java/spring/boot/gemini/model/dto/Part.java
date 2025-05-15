package spring.boot.gemini.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import spring.boot.gemini.model.InlineData;

@NoArgsConstructor
@Data
public class Part {

    private String text;
    private InlineData inlineData;

    public Part(String text) {
        this.text = text;
    }

    public Part(InlineData inlineData) {
        this.inlineData = inlineData;
    }
}
