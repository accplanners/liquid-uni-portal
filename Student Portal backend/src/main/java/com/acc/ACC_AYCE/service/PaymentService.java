package com.acc.ACC_AYCE.service;

import com.acc.ACC_AYCE.dto.OfflinePaymentRequest;
import com.acc.ACC_AYCE.dto.OnlinePaymentRequest;
import com.acc.ACC_AYCE.dto.PaymentResponse;
import org.springframework.lang.NonNull;

public interface PaymentService {
    
    PaymentResponse initiateOnlinePayment(OnlinePaymentRequest request);
    
    PaymentResponse createOfflinePayment(OfflinePaymentRequest request);
    
    PaymentResponse verifyOnlinePayment(String paymentId, String orderId, String signature);
    
    PaymentResponse processWebhook(String webhookData);
    
    boolean validatePaymentAmount(Double amount, @NonNull Long enrollmentId);
    
    PaymentResponse getPaymentById(@NonNull Long paymentId);
    
    PaymentResponse updatePaymentStatus(@NonNull Long paymentId, String status);
}
