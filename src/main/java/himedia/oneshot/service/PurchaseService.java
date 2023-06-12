package himedia.oneshot.service;

import himedia.oneshot.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchaseService {
    private final PurchaseRepository purchaseRepository;
    public PurchaseService(PurchaseRepository purchaseRepository){
        this.purchaseRepository = purchaseRepository;
    }

    public void placeOrder(Long memberId, List<Map<String,Object>> cartItems){
        purchaseRepository.placeOrder(memberId,cartItems);
    }

}
