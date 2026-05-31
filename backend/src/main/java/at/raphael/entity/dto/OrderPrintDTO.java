package at.raphael.entity.dto;

import at.raphael.entity.Buffet;
import at.raphael.entity.Order;

import java.util.List;

public record OrderPrintDTO(Long id, int tableNr, String waiterName, String buffetName, double price, List<PositionDTO> positions, List<PrinterDTO> printers) {
}
