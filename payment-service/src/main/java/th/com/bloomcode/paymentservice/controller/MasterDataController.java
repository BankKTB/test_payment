package th.com.bloomcode.paymentservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import th.com.bloomcode.paymentservice.model.Result;
import th.com.bloomcode.paymentservice.model.idem.*;
import th.com.bloomcode.paymentservice.model.request.CheckAPUserBankAccNoRequest;
import th.com.bloomcode.paymentservice.service.idem.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/master")
public class MasterDataController {
    @Autowired
    private AreaService areaService;

    @Autowired
    private DocumentTypeService documentTypeService;

    @Autowired
    private BankBranchService bankBranchService;

    @Autowired
    private SpecialGLService specialGLService;

    @Autowired
    private CompanyCodeService companyCodeService;

    @Autowired
    private PaymentCenterService paymentCenterService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private PaymentMethodService paymentMethodService;

    @Autowired
    private CurrencyService currencyService;

    @Autowired
    private HouseBankPaymentMethodService houseBankPaymentMethodService;

    @Autowired
    private HouseBankService houseBankService;

    @Autowired
    private HouseBankAccountService houseBankAccountService;

    @Autowired
    private NonBusinessDayService nonBusinessDayService;

    @Autowired
    private MMVendorBankAccountService mmVendorBankAccountService;

    @Autowired
    private APUserChgBankAccNoService apUserChgBankAccNoService;

    @Autowired
    private BankAccountDetailService bankAccountDetailService;

    // รหัสพื้นที่หรือจังหวัด
    @RequestMapping(path = "/area/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<Area>>> getAllArea(@PathVariable("value") String value)
            throws Exception {
        return areaService.findAllByValue(value);
    }

    @RequestMapping(path = "/area/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<Area>> getOneArea(@PathVariable("value") String value) throws Exception {
        return areaService.findOneByValue(value);
    }

    // ประเภทเอกสาร
    @RequestMapping(path = "/docType/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<DocumentType>>> getAllDocType(@PathVariable("value") String value)
            throws Exception {
        return documentTypeService.findAllByValue(value);
    }

    @RequestMapping(path = "/docType/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<DocumentType>> getOneDocType(@PathVariable("value") String value) throws Exception {
        return documentTypeService.findOneByValue(value);
    }

    // Bank Branch รหัสสาขาธนาคาร
    @RequestMapping(path = "/bank/branch/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<BankBranch>>> getBankBranchByBankAndValue(
            @PathVariable("value") String value) throws Exception {
        return bankBranchService.findAllByValue(value);
    }

    @RequestMapping(path = "/bank/branch/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<BankBranch>> getOneRontingNo(
            @PathVariable("value") String value) throws Exception {
        return bankBranchService.findOneByValue(value);
    }

    // รหัส SpecialGL
    @RequestMapping(path = "/specialGL/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<SpecialGL>>> getAllSpecialGL(@PathVariable("value") String value)
            throws Exception {
        return specialGLService.findAllByValue(value);
    }

    @RequestMapping(path = "/specialGL/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<SpecialGL>> getOneSpecialGL(@PathVariable("value") String value) throws Exception {
        return specialGLService.findOneByValue(value);
    }

    // รหัสหน่วยงาน
    @RequestMapping(path = "/compCode/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<CompanyCode>>> getCompCodeValue(@PathVariable("value") String value)
            throws Exception {
        return companyCodeService.findAllByValue(value);
    }

    @RequestMapping(path = "/compCode/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<CompanyCode>> getOneCompCode(@PathVariable("value") String value) throws Exception {
        return companyCodeService.findOneByValue(value);
    }

    // รหัสหน่วยเบิกจ่าย
    @RequestMapping(path = "/paymentCenter/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentCenter>>> getPaymentCenterValue(@PathVariable("value") String value)
            throws Exception {
        return paymentCenterService.findAllByValue(value);
    }

    @RequestMapping(path = "/paymentCenter/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentCenter>> getOnePaymentCenter(@PathVariable("value") String value)
            throws Exception {
        return paymentCenterService.findOneByValue(value);
    }

    // รหัสผู้ขาย
    @RequestMapping(path = "/vendor/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<Vendor>>> getVendorByValue(@PathVariable("value") String value) throws Exception {
        return vendorService.findAllByValue(value);
    }

    @RequestMapping(path = "/vendor/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<Vendor>> getOneVendor(@PathVariable("value") String value) throws Exception {
        return vendorService.findOneByValueCode2(value);
    }

    // ประเทศ
    @RequestMapping(path = "/country/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<Country>>> getAllCountry(@PathVariable("value") String value)
            throws Exception {
        return countryService.findAllByValueCode(value);
    }

    @RequestMapping(path = "/country/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<Country>> getOneCountry(@PathVariable("value") String value) throws Exception {
        return countryService.findOneByValueCode(value);
    }

    // วิธีชำระเงิน
    @RequestMapping(path = "/paymentMethod/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<PaymentMethod>>> getAllPaymentMethod(@PathVariable("value") String value)
            throws Exception {
        return paymentMethodService.findAllByValue(value);
    }

    @RequestMapping(path = "/paymentMethod/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<PaymentMethod>> getOnePaymentMethod(@PathVariable("value") String value) throws Exception {
        return paymentMethodService.findOneByValue(value);
    }

    // สกุลเงิน
    @RequestMapping(path = "/currency/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<Currency>>> getAllCurrency(@PathVariable("value") String value)
            throws Exception {
        return currencyService.findAllByValueCode(value);
    }

    @RequestMapping(path = "/currency/getOne/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<Currency>> getOneCurrency(@PathVariable("value") String value) throws Exception {
        return currencyService.findOneByValueCode(value);
    }

    // รหัส HouseBank
    @RequestMapping(path = "/houseBank/paymentMethod/getByValue/{client}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<HouseBankPaymentMethod>>> getAllHouseBankPaymentMethod(@PathVariable("client") String client)
            throws Exception {
        return houseBankPaymentMethodService.findAllByValueCode(client);
    }

    @RequestMapping(path = "/houseBank/paymentMethod/getOne/{client}/{houseBankKey}/{paymentMethod}", method = RequestMethod.GET)
    public ResponseEntity<Result<HouseBankPaymentMethod>> getOneHouseBankPaymentMethod(@PathVariable("client") String client,
                                                                                       @PathVariable("houseBankKey") String houseBankKey,
                                                                                       @PathVariable("paymentMethod") String paymentMethod) throws Exception {
        return houseBankPaymentMethodService.findOneByValueCode(client, houseBankKey, paymentMethod);
    }

    // รหัส houseBank
    @RequestMapping(path = "/houseBank/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<HouseBank>>> getAllHouseBank(@PathVariable("value") String value)
            throws Exception {
        return houseBankService.findAllByValueCode(value);
    }

    @RequestMapping(path = "/houseBank/getOne/{compCode}/{valueCode}/{bankBranch}", method = RequestMethod.GET)
    public ResponseEntity<Result<HouseBank>> getOneHouseBank(
            @PathVariable("compCode") String compCode,
            @PathVariable("valueCode") String valueCode,
            @PathVariable("bankBranch") String bankBranch) throws Exception {
        return houseBankService.findOneByValueCode(valueCode, compCode, bankBranch);
    }

    @RequestMapping(path = "/houseBank/account/getByValue/{value}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<HouseBankAccount>>> getAllHouseBankAccount(@PathVariable("value") String value)
            throws Exception {
        return houseBankAccountService.findAllByValueCode(value);
    }

    @RequestMapping(path = "/houseBank/account/getOne/{compCode}/{valueCode}/{accountCode}", method = RequestMethod.GET)
    public ResponseEntity<Result<HouseBankAccount>> getOneHouseBankAccount(
            @PathVariable("compCode") String compCode,
            @PathVariable("valueCode") String valueCode,
            @PathVariable("accountCode") String accountCode) throws Exception {
        return houseBankAccountService.findOneByValueCode(valueCode, compCode, accountCode);
    }

    @RequestMapping(path = "/nonBusiness/findByDateAndRangeDay/{dateBegin}/{rangeDay}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<NonBusinessDay>>> getByDateAndRangeDay(
            @PathVariable("dateBegin") String dateBegin,
            @PathVariable("rangeDay") String rangeDay) throws Exception {
        return nonBusinessDayService.findByDateAndRangeDay(dateBegin, rangeDay);
    }

    @RequestMapping(path = "/nonBusiness/getAll", method = RequestMethod.GET)
    public ResponseEntity<Result<List<NonBusinessDay>>> getAllNonBusinessDay() throws Exception {
        return nonBusinessDayService.findAll();
    }


    @RequestMapping(path = "/mmVendorBankAccount/getByValue/{value}/{type}/{paymentMethodType}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<VendorBankAccount>>> mmVendorBankAccountFindByKey(@PathVariable("value") String value,
                                                                                        @PathVariable("type") String type,
                                                                                        @PathVariable("paymentMethodType") String paymentMethodType)
            throws Exception {
        return mmVendorBankAccountService.findByCondition(value, type, paymentMethodType);
    }


    //
    @RequestMapping(path = "/checkApUserChgBankAccNo", method = RequestMethod.POST)
    public ResponseEntity<Result<List<APUserChgBankAccNo>>> checkApUserChgBankAccNo(@RequestBody CheckAPUserBankAccNoRequest checkAPUserBankAccNoRequest)
            throws Exception {
        return apUserChgBankAccNoService.findAllByValue(checkAPUserBankAccNoRequest);
    }

    @RequestMapping(path = "/bankAccountDetail/getByValue/{vendorCode}/{value}/{routingNo}", method = RequestMethod.GET)
    public ResponseEntity<Result<List<BankAccountDetail>>> bankAccountDetail(
            @PathVariable("vendorCode") String vendorCode, @PathVariable("value") String value
            , @PathVariable("routingNo") String routingNo
    )
            throws Exception {
        return bankAccountDetailService.findByCondition(vendorCode, value,routingNo);
    }

}
