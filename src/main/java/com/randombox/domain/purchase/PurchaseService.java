package com.randombox.domain.purchase;

import com.randombox.domain.coupon.Coupon;
import com.randombox.domain.coupon.UserCoupon;
import com.randombox.domain.coupon.UserCouponRepository;
import com.randombox.domain.randombox.RandomBox;
import com.randombox.domain.randombox.RandomBoxItem;
import com.randombox.domain.randombox.RandomBoxRepository;
import com.randombox.domain.randombox.RandomBoxService;
import com.randombox.domain.user.User;
import com.randombox.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseResultRepository purchaseResultRepository;
    private final RandomBoxRepository randomBoxRepository;
    private final UserRepository userRepository;
    private final RandomBoxService randomBoxService;
    private final UserCouponRepository userCouponRepository;

    @Transactional
    public Purchase purchaseRandomBox(Long userId, Long randomBoxId, int quantity, Long userCouponId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        RandomBox randomBox = randomBoxRepository.findById(randomBoxId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 랜덤박스입니다."));

        if (!randomBox.isOnSale()) {
            throw new IllegalStateException("현재 판매 중인 랜덤박스가 아닙니다.");
        }

        int totalPrice = randomBox.getPrice() * quantity;
        
        // 쿠폰 적용
        if (userCouponId != null) {
            UserCoupon userCoupon = userCouponRepository.findById(userCouponId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
            
            if (userCoupon.isUsed()) {
                throw new IllegalStateException("이미 사용된 쿠폰입니다.");
            }
            
            if (!userCoupon.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("해당 쿠폰을 사용할 권한이 없습니다.");
            }
            
            Coupon coupon = userCoupon.getCoupon();
            if (!coupon.isValid()) {
                throw new IllegalStateException("유효하지 않은 쿠폰입니다.");
            }
            
            if (coupon.getMinPurchase() != null && totalPrice < coupon.getMinPurchase()) {
                throw new IllegalStateException("최소 구매 금액을 만족하지 않습니다.");
            }
            
            int discountAmount = coupon.calculateDiscount(totalPrice);
            totalPrice -= discountAmount;
            
            userCoupon.use();
        }

        randomBox.decreaseQuantity(quantity);

        Purchase purchase = Purchase.builder()
                .user(user)
                .randomBox(randomBox)
                .quantity(quantity)
                .totalPrice(totalPrice)
                .build();

        Purchase savedPurchase = purchaseRepository.save(purchase);
        
        // 랜덤 아이템 추첨 및 결과 저장
        List<PurchaseResult> results = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            RandomBoxItem randomItem = randomBoxService.drawRandomItem(randomBoxId);
            PurchaseResult result = PurchaseResult.builder()
                    .purchase(savedPurchase)
                    .randomBoxItem(randomItem)
                    .build();
            results.add(purchaseResultRepository.save(result));
        }

        return savedPurchase;
    }

    public Purchase getPurchase(Long purchaseId) {
        return purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 내역입니다."));
    }

    public List<Purchase> getUserPurchases(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        
        return purchaseRepository.findByUserOrderByPurchaseDateTimeDesc(user);
    }

    public List<PurchaseResult> getPurchaseResults(Long purchaseId) {
        return purchaseResultRepository.findByPurchaseId(purchaseId);
    }

    @Transactional
    public Purchase cancelPurchase(Long userId, Long purchaseId) {
        Purchase purchase = purchaseRepository.findById(purchaseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매 내역입니다."));

        if (!purchase.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("해당 구매를 취소할 권한이 없습니다.");
        }

        if (purchase.getStatus() == Purchase.PurchaseStatus.CANCELLED) {
            throw new IllegalStateException("이미 취소된 구매입니다.");
        }

        // 구매 시간으로부터 24시간이 지났는지 확인
        if (purchase.getPurchaseDateTime().plusHours(24).isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("구매 후 24시간이 지나 취소할 수 없습니다.");
        }

        purchase.cancel();
        
        // 랜덤박스 수량 복구
        RandomBox randomBox = purchase.getRandomBox();
        randomBox.update(
                randomBox.getName(),
                randomBox.getDescription(),
                randomBox.getPrice(),
                randomBox.getQuantity() + purchase.getQuantity(),
                randomBox.getSalesStartTime(),
                randomBox.getSalesEndTime()
        );

        return purchase;
    }
}
