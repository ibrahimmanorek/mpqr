package com.paypro.qrcode.csvmodel;

import com.opencsv.bean.CsvBindByName;

public class Emvco {

    //Merchant
    @CsvBindByName(column = "*)merchantName", required = true)
    private String name;

    @CsvBindByName(column = "*)countryCode", required = true)
    private String countryCode;

    @CsvBindByName(column = "*)country", required = true)
    private String country;

    @CsvBindByName(column = "*)city", required = true)
    private String city;

    @CsvBindByName(column = "*)merchantCategoryCode", required = true)
    private String categoryCode;

    @CsvBindByName(column = "postalCode")
    private String postalCode;

    //Merchant Identifier
    @CsvBindByName(column = "*)merchantIdentifier")
    private String identifier;

    @CsvBindByName(column = "*)identifierValue")
    private String identifierValue;

    //Transaction Detail
    @CsvBindByName(column = "*)currencyCode", required = true)
    private String currencyCode;

    @CsvBindByName(column = "transactionAmount")
    private String amount;

    @CsvBindByName(column = "tipOrCovenienceIndicator")
    private String convenienceIndicator;

    @CsvBindByName(column = "billNumber")
    private String billNumber;

    @CsvBindByName(column = "mobileNumber")
    private String mobileNumber;

    //Additional Data
    @CsvBindByName(column = "storeId")
    private String storeId;

    @CsvBindByName(column = "loyaltyNumber")
    private String loyaltyNumber;

    @CsvBindByName(column = "referenceId")
    private String referenceId;

    @CsvBindByName(column = "cunsumerId")
    private String cunsumerId;

    @CsvBindByName(column = "terminalId")
    private String terminalId;

    @CsvBindByName(column = "purpose")
    private String purpose;

    @CsvBindByName(column = "bharatQrMaid")
    private String bharatQrMaid;

    //Alternate Language
    @CsvBindByName(column = "languagePreference")
    private String languagePreferenc;

    @CsvBindByName(column = "alternateLanguage")
    private String alternateLanguage;

    @CsvBindByName(column = "alternateMerchantName")
    private String alternateMerchantName;

    @CsvBindByName(column = "alternateMerchantCity")
    private String alternateMerchantCity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier.length() == 1 ? "0" + identifier : identifier;
    }

    public String getIdentifierValue() {
        return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
        String merchantIdentifierValue = identifierValue.replace("'", "");
        this.identifierValue = merchantIdentifierValue;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getConvenienceIndicator() {
        return convenienceIndicator;
    }

    public void setConvenienceIndicator(String convenienceIndicator) {
        this.convenienceIndicator = convenienceIndicator;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getLoyaltyNumber() {
        return loyaltyNumber;
    }

    public void setLoyaltyNumber(String loyaltyNumber) {
        this.loyaltyNumber = loyaltyNumber;
    }

    public String getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getCunsumerId() {
        return cunsumerId;
    }

    public void setCunsumerId(String cunsumerId) {
        this.cunsumerId = cunsumerId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getBharatQrMaid() {
        return bharatQrMaid;
    }

    public void setBharatQrMaid(String bharatQrMaid) {
        this.bharatQrMaid = bharatQrMaid;
    }

    public String getLanguagePreferenc() {
        return languagePreferenc;
    }

    public void setLanguagePreferenc(String languagePreferenc) {
        this.languagePreferenc = languagePreferenc;
    }

    public String getAlternateLanguage() {
        return alternateLanguage;
    }

    public void setAlternateLanguage(String alternateLanguage) {
        this.alternateLanguage = alternateLanguage;
    }

    public String getAlternateMerchantName() {
        return alternateMerchantName;
    }

    public void setAlternateMerchantName(String alternateMerchantName) {
        this.alternateMerchantName = alternateMerchantName;
    }

    public String getAlternateMerchantCity() {
        return alternateMerchantCity;
    }

    public void setAlternateMerchantCity(String alternateMerchantCity) {
        this.alternateMerchantCity = alternateMerchantCity;
    }
}
