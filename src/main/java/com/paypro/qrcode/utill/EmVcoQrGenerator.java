package com.paypro.qrcode.utill;

import com.mastercard.mpqr.pushpayment.exception.UnknownTagException;
import com.mastercard.mpqr.pushpayment.model.PushPaymentData;
import com.paypro.qrcode.csvmodel.Emvco;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.List;

public class EmVcoQrGenerator {

    private final static Logger log = Logger.getLogger(EmVcoQrGenerator.class);

    private PushPaymentData pushPaymentData;

    public EmVcoQrGenerator(PushPaymentData pushPaymentData) {
        this.pushPaymentData = pushPaymentData;
    }

    public void generateQrCode(List<Emvco> emvcos, File rootPath) throws UnknownTagException {
        log.info("=== Generate QR Code ===");
        pushPaymentData = new PushPaymentData();

        int i = 0;
        for (Emvco emvco : emvcos) {
            setPayloadFormatIndicator();
            setPointOfInitiationMethod();
            setMerchantIdentifier(emvco);
            setMerchant(emvco);
            setTransactionDetail(emvco);
            try {
                i++;
                pushPaymentData.validate();
                String qrContent = pushPaymentData.generatePushPaymentString();
                MpqrUtill.encodeAsBitmap(qrContent, 600, 600, i, rootPath.toString(), pushPaymentData.getMerchantName(),
                        MpqrUtill.getQrFilePth(i + "_" + pushPaymentData.getMerchantName(), rootPath), emvco.getIdentifierValue());
            } catch (Exception e) {
                log.debug(e.getMessage());
            }
        }
    }

    private void setPayloadFormatIndicator() {
        log.info("=== Set Payload Format Indicator ===");
        pushPaymentData.setPayloadFormatIndicator("01");
    }

    private void setPointOfInitiationMethod() {
        log.info("=== Set Point Of Initiation Method ===");
        try {
            pushPaymentData.setValue("01", "11");
        } catch (UnknownTagException e) {
            log.debug(e.getMessage());
        }
    }

    private void setMerchantIdentifier(Emvco emvco) throws UnknownTagException {
        log.info("=== Set Merchant Identifier ===");
        if (emvco.getIdentifier() != null) {
            pushPaymentData.setValue(emvco.getIdentifier(), emvco.getIdentifierValue());
        }
    }

    private void setMerchant(Emvco emvco) {
        log.info("=== Set Merchant ===");
        pushPaymentData.setMerchantName(emvco.getName());
        pushPaymentData.setCountryCode(emvco.getCountryCode());
        pushPaymentData.setMerchantCity(emvco.getCity());
        if (emvco.getPostalCode() != null) {
            pushPaymentData.setPostalCode(emvco.getPostalCode());
        }
        pushPaymentData.setMerchantCategoryCode(emvco.getCategoryCode());
    }

    private void setTransactionDetail(Emvco emvco) {
        log.info("=== Set Transaction Detail ===");
        pushPaymentData.setTransactionCurrencyCode(emvco.getCurrencyCode());
        if (PushPaymentData.TipConvenienceIndicator.PROMTED_TO_ENTER_TIP.equals(emvco.getConvenienceIndicator())) {
            pushPaymentData.setTipOrConvenienceIndicator("01");

        } else if (PushPaymentData.TipConvenienceIndicator.FLAT_CONVENIENCE_FEE.equals(emvco.getConvenienceIndicator())) {
            pushPaymentData.setTipOrConvenienceIndicator("02");
            pushPaymentData.setValueOfConvenienceFeeFixed(10);

        } else if (PushPaymentData.TipConvenienceIndicator.PERCENTAGE_CONVENIENCE_FEE.equals(emvco.getConvenienceIndicator())) {
            pushPaymentData.setTipOrConvenienceIndicator("03");
            pushPaymentData.setValueOfConvenienceFeePercentage(5);
        }
    }
}
