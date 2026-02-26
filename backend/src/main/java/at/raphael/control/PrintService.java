package at.raphael.control;

import at.raphael.entity.*;
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
        executor.execute(() -> createStringForPrint(o, orderPositions, buffet));
        return orderPositions;
    }

    void createStringForPrint(Order order, List<OrderPosition> orderPositions, Buffet buffet) {
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
        for(Printer p : buffet.printers) {
            boolean gotGood = sendOrderToPrinter(bill.toString(), p.ipAddress, p.port);
            if(!gotGood) {
                log.info("Fehler beim Drucken!");
            }else{
                orderPositions.forEach(orderPosition -> {
                    OrderPosition tmp = OrderPosition.findById(orderPosition.id);
                    if(tmp != null) {
                        tmp.dispached = true;
                    }
                });

            }
        }
    }

    boolean sendOrderToPrinter(String stringToPrint, String printerIp, String printerPort) {

        log.info(printerIp);
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
        return true;
    }


}