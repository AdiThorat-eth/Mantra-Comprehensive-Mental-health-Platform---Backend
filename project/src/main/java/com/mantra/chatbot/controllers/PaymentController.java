package com.mantra.chatbot.controllers;

import com.mantra.chatbot.dto.OrderRequest;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin(origins = "http://localhost:5173") // Allow requests from your frontend
public class PaymentController {

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.secret.key}")
    private String razorpaySecretKey;

    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder(@RequestBody OrderRequest orderRequest) {
        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpaySecretKey);

            JSONObject orderRequestJson = new JSONObject();
            // Amount should be in the smallest currency unit (e.g., paise for INR)
            orderRequestJson.put("amount", orderRequest.getAmount() * 100);
            orderRequestJson.put("currency", orderRequest.getCurrency());
            orderRequestJson.put("receipt", "receipt_order_" + System.currentTimeMillis());

            Order order = razorpayClient.orders.create(orderRequestJson);

            // You can save order details to your database here if needed

            return ResponseEntity.ok(order.toString());

        } catch (RazorpayException e) {
            System.out.println("Exception while creating order: " + e.getMessage());
            return ResponseEntity.status(500).body("Error creating Razorpay order");
        }
    }
}