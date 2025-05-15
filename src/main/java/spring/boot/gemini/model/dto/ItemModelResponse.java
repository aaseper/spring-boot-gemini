package spring.boot.gemini.model.dto;

public record ItemModelResponse(String itemDescription, String itemSupplierRef, Double quantity,
                                Double pricePerUnitWithoutTaxes, Double totalAmountWithoutTaxes,
                                Double pricePerUnitWithTaxes, Double totalAmountWithTaxes) {

}
