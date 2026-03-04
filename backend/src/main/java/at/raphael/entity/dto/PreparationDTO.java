package at.raphael.entity.dto;

public class PreparationDTO {
    public String itemName;
    public int dispatchedAmount;
    public int preparationAmount;

    public PreparationDTO(String itemName, int dispatchedAmount, int preparationAmount) {
        this.itemName = itemName;
        this.dispatchedAmount = dispatchedAmount;
        this.preparationAmount = preparationAmount;
    }

    public PreparationDTO() {
    }
}
