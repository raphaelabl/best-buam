package at.raphael.control;

import at.raphael.entity.*;
import at.raphael.entity.dto.OrderPrintDTO;
import at.raphael.entity.dto.PositionDTO;
import at.raphael.entity.dto.PrinterDTO;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.jboss.logging.Logger;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

@ApplicationScoped
public class PrintService {

    @Inject
    ManagedExecutor executor;

    @Inject
    Logger log;

    // Alt konnte nicht unterscheiden zwischen websocket buffet und nicht ws buffet darum muss jetzt separiert werden
    /** deprecated **/
    void printOrder(Order order) {
        List<Buffet> buffets = Buffet.listAll();
        Map<Long, List<OrderPosition>> orderPositions = new HashMap<>();

        // Mappe OrderPositionen zu Buffets
        for (OrderPosition position : order.positions) {
            for (Buffet buffet : buffets) {
                boolean itemInBuffet = buffet.items.stream()
                        .anyMatch(item -> Objects.equals(item.id, position.item.id));
                if (itemInBuffet) {
                    orderPositions
                            .computeIfAbsent(buffet.id, id -> new ArrayList<>())
                            .add(position);
                }
            }
        }

    }

    public List<OrderPosition> createBillAndPrint(Order o, Buffet buffet){
        // Filter all Order Positions from Order with the same buffet
        List<OrderPosition> orderPositions = o.positions
                .stream()
                .filter(orderPosition -> buffet.items.stream().map(item -> item.id).toList().contains(orderPosition.item.id)).toList();

        String bill = createStringForPrint(o, orderPositions, buffet);
        sendToPrintersFromBuffet(bill, buffet);
        return orderPositions;
    }


    String createStringForPrint(Order order, List<OrderPosition> orderPositions, Buffet buffet) {
        // Erzeuge Rechnung für Buffet
        StringBuilder bill = new StringBuilder();
        bill.append("Tisch: ").append(order.tableNr).append("\n")
                .append("Kellner: ").append(order.waiter.firstName)
                .append(" ").append(order.waiter.lastName).append("\n")
                .append("Ausgabe: ").append(buffet.name).append("\n");

        for (OrderPosition position : orderPositions) {
            bill.append(position.amount)
                    .append("  ")
                    .append(position.item.name)
                    .append("\n");

            if (position.isSpezial) {
                bill.append("    Extra: ").append(position.spezialText).append("\n");
            }
        }
        bill.append("\n\n\n");

        return bill.toString();
    }

    String createStringForPrintFromHardcopy(OrderPrintDTO printDTO){
        // Erzeuge Rechnung für Buffet
        StringBuilder bill = new StringBuilder();
        bill.append("Tisch: ").append(printDTO.tableNr()).append("\n")
                .append("Kellner: ").append(printDTO.waiterName())
                .append("Ausgabe: ").append(printDTO.buffetName()).append("\n");

        for (PositionDTO position : printDTO.positions()) {
            bill.append(position.amount())
                    .append("  ")
                    .append(position.itemName())
                    .append("\n");

            if (position.isSpezial()) {
                bill.append("    Extra: ").append(position.spezialText()).append("\n");
            }
        }
        bill.append("\n\n\n");

        return bill.toString();
    }

    void sendToPrintersFromBuffet(String stringToPrint, Buffet buffet) {
        for(Printer p : buffet.printers) {
            String printerIp = p.ipAddress.toString();
            int printerPort = Integer.parseInt(p.port);
            executor.execute(() -> {
                log.info(printerIp + ":" + printerPort);
                log.info(stringToPrint);

                /*try (Socket socket = new Socket(printerIp, Integer.parseInt(printerPort));
                     OutputStream os = socket.getOutputStream()) {

                    os.write("\u001B@".getBytes(StandardCharsets.UTF_8)); // Reset Drucker

                    os.write(stringToPrint.getBytes(StandardCharsets.UTF_8));
                    os.write("\u001Bd\u0003".getBytes(StandardCharsets.UTF_8)); // 3 Zeilen vorschieben
                    os.write("\u001Bm".getBytes(StandardCharsets.UTF_8)); // Cutter-Befehl für Papierabschneiden
                    os.flush();

                    return true;
                } catch (Exception e) {
                    return false;
                }*/

            });
        }
    }

    void sendToPrintersFromBuffetFromDTO(String stringToPrint, List<PrinterDTO> printers) {
        for(PrinterDTO p : printers) {
            String printerIp = p.ipAddress();
            int printerPort = p.port();
            executor.execute(() -> {
                log.info(printerIp + ":" + printerPort);
                log.info(stringToPrint);

                /*try (Socket socket = new Socket(printerIp, Integer.parseInt(printerPort));
                     OutputStream os = socket.getOutputStream()) {

                    os.write("\u001B@".getBytes(StandardCharsets.UTF_8)); // Reset Drucker

                    os.write(stringToPrint.getBytes(StandardCharsets.UTF_8));
                    os.write("\u001Bd\u0003".getBytes(StandardCharsets.UTF_8)); // 3 Zeilen vorschieben
                    os.write("\u001Bm".getBytes(StandardCharsets.UTF_8)); // Cutter-Befehl für Papierabschneiden
                    os.flush();

                    return true;
                } catch (Exception e) {
                    return false;
                }*/

            });
        }
    }


}