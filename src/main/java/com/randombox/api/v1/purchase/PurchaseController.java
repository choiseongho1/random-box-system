package com.randombox.api.v1.purchase;

import com.randombox.api.v1.purchase.dto.PurchaseRequest;
import com.randombox.api.v1.purchase.dto.PurchaseResponse;
import com.randombox.domain.purchase.Purchase;
import com.randombox.domain.purchase.PurchaseResult;
import com.randombox.domain.purchase.PurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<PurchaseResponse> purchaseRandomBox(@RequestBody PurchaseRequest request) {
        Purchase purchase = purchaseService.purchaseRandomBox(
                request.getUserId(),
                request.getRandomBoxId(),
                request.getQuantity(),
                request.getUserCouponId()
        );
        
        // PurchaseResponse 생성
        PurchaseResponse response = PurchaseResponse.from(purchase);
        
        // 구매 결과 목록 설정
        List<PurchaseResult> results = purchaseService.getPurchaseResults(purchase.getId());
        List<PurchaseResponse.PurchaseResultResponse> resultResponses = results.stream()
                .map(PurchaseResponse.PurchaseResultResponse::from)
                .collect(Collectors.toList());
        response.setResults(resultResponses);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseResponse> getPurchase(@PathVariable Long id) {
        Purchase purchase = purchaseService.getPurchase(id);
        
        // PurchaseResponse 생성
        PurchaseResponse response = PurchaseResponse.from(purchase);
        
        // 구매 결과 목록 설정
        List<PurchaseResult> results = purchaseService.getPurchaseResults(purchase.getId());
        List<PurchaseResponse.PurchaseResultResponse> resultResponses = results.stream()
                .map(PurchaseResponse.PurchaseResultResponse::from)
                .collect(Collectors.toList());
        response.setResults(resultResponses);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PurchaseResponse>> getUserPurchases(@PathVariable Long userId) {
        List<Purchase> purchases = purchaseService.getUserPurchases(userId);
        List<PurchaseResponse> responses = purchases.stream()
                .map(purchase -> {
                    // PurchaseResponse 생성
                    PurchaseResponse response = PurchaseResponse.from(purchase);
                    
                    // 구매 결과 목록 설정
                    List<PurchaseResult> results = purchaseService.getPurchaseResults(purchase.getId());
                    List<PurchaseResponse.PurchaseResultResponse> resultResponses = results.stream()
                            .map(PurchaseResponse.PurchaseResultResponse::from)
                            .collect(Collectors.toList());
                    response.setResults(resultResponses);
                    
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<PurchaseResponse> cancelPurchase(
            @PathVariable Long userId,
            @PathVariable Long id) {
        Purchase canceledPurchase = purchaseService.cancelPurchase(userId, id);
        
        // PurchaseResponse 생성
        PurchaseResponse response = PurchaseResponse.from(canceledPurchase);
        
        // 구매 결과 목록 설정
        List<PurchaseResult> results = purchaseService.getPurchaseResults(canceledPurchase.getId());
        List<PurchaseResponse.PurchaseResultResponse> resultResponses = results.stream()
                .map(PurchaseResponse.PurchaseResultResponse::from)
                .collect(Collectors.toList());
        response.setResults(resultResponses);
        
        return ResponseEntity.ok(response);
    }
}
