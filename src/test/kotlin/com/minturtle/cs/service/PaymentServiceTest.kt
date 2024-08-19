package com.minturtle.cs.service

import org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.Test
import com.minturtle.cs.enums.PaymentMethods.*
import com.minturtle.cs.exceptions.TemporarilyUnavailablePaymentException
import com.minturtle.cs.exceptions.UnsupportedPaymentMethodException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class PaymentServiceTest{


    @Autowired
    private lateinit var paymentService: PaymentService

    @Test
    fun `결제 수단을 전달받아 특정 결제 수단은 TemporarilyUnavailablePaymentException를 throw할 수 있다`(){
        val temporarilyUnavailablePayments = listOf(
            BANK_TRANSFER,
            TOSS_PAY,
            KAKAO_PAY,
            NAVER_PAY,
            CREDIT_CARD,
            DEBIT_CARD,
            PAYPAL,
            APPLE_PAY,
            GOOGLE_PAY
        )

        temporarilyUnavailablePayments.forEach{
            assertThatThrownBy {
                paymentService.pay(it)
            }.isInstanceOf(TemporarilyUnavailablePaymentException::class.java)
        }

    }


    @Test
    fun `결제 수단을 전달받아 특정 결제 수단은 UnsupportedPaymentMethodException를 throw할 수 있다`(){
        val unSupportedPayments = listOf(
            SAMSUNG_PAY,
            ALIPAY,
            WECHAT_PAY,
            BITCOIN,
            ETHEREUM,
            MOBILE_CARRIER_BILLING,
            GIFT_CARD,

        )

        unSupportedPayments.forEach{
            assertThatThrownBy {
                paymentService.pay(it)
            }.isInstanceOf(UnsupportedPaymentMethodException::class.java)
        }

    }

    @Test
    fun `결제 수단을 전달받아 특정 결제 수단은 아무 오류를 출력하지 않는다(정상 처리한다)`(){
        val validPayments = listOf(
            PAYCO,
            LINE_PAY,
            AMAZON_PAY,
            STRIPE,
            SQUARE,
            PAYONEER,
            VENMO,
            ZELLE,
            WESTERN_UNION,
            CASH
        )

        validPayments.forEach{
            assertThatThrownBy {
                paymentService.pay(it)
            }.doesNotThrowAnyException()
        }


    }
}