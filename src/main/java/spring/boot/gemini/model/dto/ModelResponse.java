package spring.boot.gemini.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record ModelResponse(@SerializedName("invoice") InvoiceModelResponse invoiceModelResponse,
                            @SerializedName("invoiceItems") List<ItemModelResponse> itemModelResponseList) {

}
