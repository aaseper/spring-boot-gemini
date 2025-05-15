package spring.boot.gemini.model;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.gemini.model.dto.*;
import spring.boot.gemini.model.type.SafetyCategory;
import spring.boot.gemini.model.type.SafetyThreshold;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@Service
public class ModelService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestClient restClient;
    private final String TEXT_PROMPT = """
            Populates the fields of the attached invoice with its items. All date- fields must follow ISO 8601 format as String: YYYY-MM-DDThh:mm:ssTZD (eg 1997-07-16T19:20:30+01:00), assume Spain TZD. If a String value isn't found return null, if a Number value isn't found infers numerically the number: try to find the tax per item and multiply the tax by the -WithoutTaxes to obtain the -WithTaxes, and so on. Number must be always round up to the nearest 0.01. Item description must be only the item name (eg Camera, Laptop or Shovel). Return valid JSON Schema:
            ```json
            {
            "invoice": {
            "dateInvoice": String,
            "supplier": String,
            "totalAmount": Number
            },
            "invoiceItems": [{ // Assuming multiple items are possible, using an array
            "itemDescription": String,
            "itemSupplierRef": String,
            "quantity": Number,
            "pricePerUnitWithoutTaxes": Number,
            "totalAmountWithoutTaxes": Number,
            "pricePerUnitWithTaxes": Number,
            "totalAmountWithTaxes": Number
            }]
            }
            ```
            """;
    private final GenerationConfig generationConfig = new GenerationConfig(0.0, 1.0, 40, 2048,
            new String[]{});
    private final SafetySettings[] safetySettings = new SafetySettings[]{
            new SafetySettings(SafetyCategory.HARM_CATEGORY_HARASSMENT, SafetyThreshold.BLOCK_NONE),
            new SafetySettings(SafetyCategory.HARM_CATEGORY_HATE_SPEECH, SafetyThreshold.BLOCK_NONE),
            new SafetySettings(SafetyCategory.HARM_CATEGORY_SEXUALLY_EXPLICIT,
                    SafetyThreshold.BLOCK_NONE),
            new SafetySettings(SafetyCategory.HARM_CATEGORY_DANGEROUS_CONTENT, SafetyThreshold.BLOCK_NONE)
    };
    private final String MODEL_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";
    final Gson gson = new Gson();
    @Value("${spring.ai.openai.api-key}")
    private String GEMINI_API_KEY;

    public ModelService(RestClient.Builder builder) {
        this.restClient = builder.baseUrl("https://generativelanguage.googleapis.com").build();
    }

    public ModelResponse uploadFile(MultipartFile file) {
        if (file == null) {
            throw new IllegalArgumentException("File is null");
        }

        if (!isIn(file.getContentType(), new String[]{"image/jpg", "image/jpeg", "application/pdf"})) {
            throw new IllegalArgumentException("Unsupported content type: " + file.getContentType());
        }

        if (file.getSize() > 16777216) {
            throw new IllegalArgumentException("Too large file size: " + file.getSize());
        }

        Part textPart = new Part(TEXT_PROMPT);
        Part imagePart = null;
        try {
            byte[] fileContent = file.getBytes();
            String base64Data = Base64.getEncoder().encodeToString(fileContent);
            String mimeType = file.getContentType();
            if (mimeType == null || mimeType.isEmpty()) {
                throw new IllegalArgumentException("Could not determine MIME type for the file");
            }
            InlineData inlineData = new InlineData(mimeType, base64Data);
            imagePart = new Part(inlineData);
        } catch (IOException e) {
            logger.error("Error while parsing file", e);
        }

        List<Part> partsList = new ArrayList<>();
        partsList.add(textPart);
        if (imagePart != null) {
            partsList.add(imagePart);
        }
        Content content = new Content(partsList.toArray(new Part[0]));

        GeminiRequest requestPayload = new GeminiRequest(new Content[]{content}, generationConfig,
                safetySettings);
        String urlWithKey = MODEL_API_URL + "?key=" + GEMINI_API_KEY;
        try {
            GeminiResponse response = restClient.post().uri(urlWithKey)
                    .contentType(MediaType.APPLICATION_JSON).body(requestPayload).retrieve()
                    .body(GeminiResponse.class);
            if (response != null) {
                if (response.getCandidates() != null && response.getCandidates().length > 0) {
                    Candidate firstCandidate = response.getCandidates()[0];
                    if (firstCandidate.getContent() != null && firstCandidate.getContent().getParts() != null
                            && firstCandidate.getContent().getParts().length > 0) {
                        StringBuilder responseText = new StringBuilder();
                        for (Part part : firstCandidate.getContent().getParts()) {
                            if (part.getText() != null) {
                                responseText.append(part.getText());
                            }
                        }
                        String rawModelOutput = responseText.toString();
                        String jsonSubstring = rawModelOutput.substring(rawModelOutput.indexOf('{'),
                                rawModelOutput.lastIndexOf('}') + 1).trim();
                        return gson.fromJson(jsonSubstring, ModelResponse.class);
                    }
                }
            } else {
                throw new Exception("Gemini API returned no valid content: {}");
            }
        } catch (Exception e) {
            logger.error("Error calling Gemini API", e);
        }
        return null;
    }

    public static <T> boolean isIn(T element, T[] array) {
        if (element == null) {
            return false;
        }
        if (array == null) {
            return false;
        }
        for (T item : array) {
            if (Objects.equals(element, item)) {
                return true;
            }
        }
        return false;
    }
}