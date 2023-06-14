package himedia.oneshot.service;

import himedia.oneshot.entity.Purchase;
import himedia.oneshot.entity.PurchaseDetail;
import himedia.oneshot.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    public PurchaseService(PurchaseRepository purchaseRepository){
        this.purchaseRepository = purchaseRepository;
    }

    public void placeOrder(Long memberId, List<Map<String,Object>> cartItems){
        purchaseRepository.placeOrder(memberId,cartItems);
    }

    public List<Purchase> showPurchase(Long memberId){
        return purchaseRepository.showPurchase(memberId);
    }

    public List<PurchaseDetail> showPurchaseDetail(Long purchaseId){
        return purchaseRepository.showPurchaseDetail(purchaseId);
    }
}
