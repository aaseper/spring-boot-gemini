package spring.boot.gemini.model;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import spring.boot.gemini.model.dto.ModelResponse;

@AllArgsConstructor
@Controller
public class ModelWebController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ModelService modelService;

    @GetMapping("/model")
    public String getModel() {
        return "index";
    }

    @PostMapping("/model/post")
    public String postModel(@RequestParam("file") MultipartFile file) {
        ModelResponse modelResponse = modelService.uploadFile(file);
        logger.info(modelResponse.toString());
        return "redirect:/model";
    }
}
