package at.raphael.control;

import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import at.raphael.entity.OrderPosition;
import at.raphael.entity.Printer;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;

@ApplicationScoped
public class PrintService {

    @Inject
    Logger log;

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

        // Erzeuge Rechnung pro Buffet
        for (Map.Entry<Long, List<OrderPosition>> entry : orderPositions.entrySet()) {
            Long buffetId = entry.getKey();
            List<OrderPosition> positions = entry.getValue();

            Optional<Buffet> buffetOpt = buffets.stream()
                    .filter(b -> b.id.equals(buffetId))
                    .findAny();

            buffetOpt.ifPresent(buffet -> {
                StringBuilder bill = new StringBuilder();
                bill.append("Tisch: ").append(order.tableNr).append("\n")
                        .append("Kellner: ").append(order.waiter.firstName)
                        .append(" ").append(order.waiter.lastName).append("\n")
                        .append("Ausgabe: ").append(buffet.name).append("\n");

                for (OrderPosition position : positions) {
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
                    boolean gotGood = printString(bill.toString(), p.ipAddress, p.port);
                    if(!gotGood) {
                        log.info("Fehler beim Drucken!");
                    }
                }

            });

        }

    }
    boolean printString(String stringToPrint,String printerIp, String printerPort) {
        try (Socket socket = new Socket(printerIp, Integer.parseInt(printerPort));
             OutputStream os = socket.getOutputStream()) {

                os.write("\u001B@".getBytes(StandardCharsets.UTF_8)); // Reset Drucker

                os.write(stringToPrint.getBytes(StandardCharsets.UTF_8));
                os.write("\u001Bd\u0003".getBytes(StandardCharsets.UTF_8)); // 3 Zeilen vorschieben
                os.write("\u001Bm".getBytes(StandardCharsets.UTF_8)); // Cutter-Befehl für Papierabschneiden
                os.flush();

                return true;
            } catch (Exception e) {
                return false;
            }
    }
}