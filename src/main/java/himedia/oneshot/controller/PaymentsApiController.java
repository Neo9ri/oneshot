package himedia.oneshot.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
//import himedia.oneshot.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

@RestController
public class PaymentsApiController {

    //토큰 발급을 위해 아임포트에서 제공해주는 rest api 사용.(gradle로 의존성 추가)
    private final IamportClient iamportClientApi;

    //생성자로 rest api key와 secret을 입력해서 토큰 바로생성.
    public PaymentsApiController() {
        this.iamportClientApi = new IamportClient("2745172666828753",
                "XHlHPQ3iqdolskuBEpqc3GjlVB9NdB6clOjXlz1SOawsel2lF2oaT7U7VTABpwB9j05zQnZDCkkE1MAF");
    }


//    @Autowired
//    private PaymentService paymentService;

    public IamportResponse<Payment> paymentLookup(String impUid) throws IamportResponseException, IOException {
        return iamportClientApi.paymentByImpUid(impUid);
    }

    @PostMapping("/payment/validate/{imp_uid}")
    public IamportResponse<Payment> verifyIamport(@PathVariable(value="imp_uid") String impUid) throws IamportResponseException, IOException
    {
        return iamportClientApi.paymentByImpUid(impUid);
    }
}
