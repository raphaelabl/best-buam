package at.raphael.entity.dto;

import at.raphael.entity.Buffet;
import at.raphael.entity.Order;
import jakarta.websocket.Session;

import java.util.ArrayList;
import java.util.List;

public class OrderSessions {
    public Session session;
    public Buffet buffet;
    public List<BuffetOrderDTO> ordersToDo;

    public OrderSessions() {
        ordersToDo = new ArrayList<>();
    }
}
