package spring.boot.gemini.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InlineData {

  private String mimeType;
  private String data;

  public MultipartFile toMultipartFile() {
    return new MockMultipartFile("file", ".jpeg", mimeType, Base64.getDecoder().decode(data));
  }
}
