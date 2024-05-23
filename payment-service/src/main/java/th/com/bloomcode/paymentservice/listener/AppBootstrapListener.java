package th.com.bloomcode.paymentservice.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import th.com.bloomcode.paymentservice.context.Context;
import th.com.bloomcode.paymentservice.idem.entity.CompCode;
import th.com.bloomcode.paymentservice.idem.entity.PaymentMethod;
import th.com.bloomcode.paymentservice.model.idem.CAClient;
import th.com.bloomcode.paymentservice.model.idem.HouseBankPaymentMethod;
import th.com.bloomcode.paymentservice.model.payment.PayMethodConfig;
import th.com.bloomcode.paymentservice.service.SmartFeeService;
import th.com.bloomcode.paymentservice.service.SwiftFeeService;
import th.com.bloomcode.paymentservice.service.idem.CAClientService;
import th.com.bloomcode.paymentservice.service.idem.HouseBankPaymentMethodService;
import th.com.bloomcode.paymentservice.service.idem.IdemConfigService;
import th.com.bloomcode.paymentservice.service.master.CompanyCodeService;
import th.com.bloomcode.paymentservice.service.master.PaymentMethodService;
import th.com.bloomcode.paymentservice.service.master.VendorBankAccountService;
import th.com.bloomcode.paymentservice.service.master.WtxService;
import th.com.bloomcode.paymentservice.service.payment.BankCodeService;
import th.com.bloomcode.paymentservice.service.payment.ConfigWebOnlineService;
import th.com.bloomcode.paymentservice.service.payment.GLAccountService;
import th.com.bloomcode.paymentservice.service.payment.PayMethodConfigService;

import java.util.List;

@Slf4j
@Component
public class AppBootstrapListener implements ApplicationListener<ApplicationReadyEvent> {

//    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final PaymentMethodService paymentMethodService;
//    private final VendorService vendorService;
    private final BankCodeService bankCodeService;
    private final SmartFeeService smartFeeService;
    private final SwiftFeeService swiftFeeService;
    private final ConfigWebOnlineService configWebOnlineService;
    private final IdemConfigService idemConfigService;
    private final CompanyCodeService companyCodeService;
    private final WtxService wtxService;
    private final VendorBankAccountService vendorBankAccountService;
    private final HouseBankPaymentMethodService houseBankPaymentMethodService;
    private final GLAccountService glAccountService;
    private final PayMethodConfigService payMethodConfigService;
    private final CAClientService caClientService;



    @Autowired
    public AppBootstrapListener(PaymentMethodService paymentMethodService, BankCodeService bankCodeService, SmartFeeService smartFeeService, SwiftFeeService swiftFeeService, ConfigWebOnlineService configWebOnlineService, IdemConfigService idemConfigService, CompanyCodeService companyCodeService
            , WtxService wtxService, VendorBankAccountService vendorBankAccountService, HouseBankPaymentMethodService houseBankPaymentMethodService, GLAccountService glAccountService, PayMethodConfigService payMethodConfigService, CAClientService caClientService) {
        this.paymentMethodService = paymentMethodService;
        this.bankCodeService = bankCodeService;
        this.smartFeeService = smartFeeService;
        this.swiftFeeService = swiftFeeService;
        this.configWebOnlineService = configWebOnlineService;
        this.idemConfigService = idemConfigService;
        this.companyCodeService = companyCodeService;
        this.wtxService = wtxService;
        this.vendorBankAccountService = vendorBankAccountService;
        this.houseBankPaymentMethodService = houseBankPaymentMethodService;
        this.glAccountService = glAccountService;
        this.payMethodConfigService = payMethodConfigService;
        this.caClientService = caClientService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll("%%");
        for (PaymentMethod paymentMethod : paymentMethods) {
            Context.sessionPayment.add(paymentMethod.getValueCode());
            Context.sessionPaymentMethod.put(paymentMethod.getValueCode(), paymentMethod);
        }

        List<CompCode> compCodes = companyCodeService.findAll("%%");
//        Context.sessionCompCode.put("99999", new CompCode());
        for (CompCode compCode : compCodes) {
            Context.sessionCompCode.put(compCode.getValueCode(), compCode);
        }
//        List<Wtx> wtxs = wtxService.findAll();
//        for (Wtx wtx : wtxs) {
//            Context.sessionWtx.put(wtx.getWtxCode(), wtx);
//        }
//        List<ConfigWebOnline> configWebOnlines = configWebOnlineService.findAll();
//        for (ConfigWebOnline configWebOnline : configWebOnlines) {
//            Context.sessionConfigWebOnline.put(configWebOnline.getValueCode().substring(0, 2), configWebOnline);
//        }
//
//        List<IdemConfig> idemConfigs = idemConfigService.findAll();
//
//        for (IdemConfig idemConfig : idemConfigs) {
//
//            Context.sessionIdemConfig.put(idemConfig.getClientValue().substring(0, 2), idemConfig);
//        }
//
        List<HouseBankPaymentMethod> houseBankPaymentMethodList = houseBankPaymentMethodService.findAllByCompanyCodePay("99999");

        for (HouseBankPaymentMethod houseBankPaymentMethod : houseBankPaymentMethodList) {

            Context.sessionHouseBankPaymentMethod.put(houseBankPaymentMethod.getPaymentMethod(), houseBankPaymentMethod);
        }
//
//        List<GLAccount> glAccountList = glAccountService.findAll();
//
//        for (GLAccount glAccount : glAccountList) {
//
//            Context.sessionGlAccountForPayment.put(glAccount.getDocType() + "-" + glAccount.getFundSource(), glAccount);
//        }
//        List<PayMethodConfig> payMethodConfigList = payMethodConfigService.findAllOrderByCountryAscPayMethodAsc();
        List<PayMethodConfig> payMethodConfigList = payMethodConfigService.findAllOrderByCountryAscPayMethodAscNew();
        for (PayMethodConfig payMethodConfig : payMethodConfigList) {

            Context.sessionPayMethodConfig.put(payMethodConfig.getCountry() + "-" + payMethodConfig.getPayMethod(), payMethodConfig);
        }

        List<CAClient> caClients = caClientService.findAll();
        for (CAClient caClient : caClients) {

            Context.sessionCAClient.put(caClient.getClientValue().substring(0, 2), caClient);
        }
//
//
//        Context.sessionBankCodes = bankCodeService.findAllBank();
//        for (BankCode bankCode : Context.sessionBankCodes) {
//            Context.sessionBankCode.put(bankCode.getBankKey(), bankCode);
//        }
        Context.sessionSmartFees = smartFeeService.findAll();
        Context.sessionSwiftFee = swiftFeeService.findAll();
//
//        List<Vendor> vendors = vendorService.findAll();
//        Context.sessionVendors = vendors;
//        for (Vendor vendor : Context.sessionVendors) {
//            Context.sessionVendor.put(vendor.getValueCode(), vendor);
//        }

//        Context.sessionVendor = vendors.stream().collect(Collectors.toMap(this::getGroupingByKey,
//                Function.identity()));

//        List<Vendor> alternatePayees = vendorService.findAllAlternativeVendor();
//        for (Vendor vendor : alternatePayees) {
//            Context.sessionAlternativeVendor.put(vendor.getValueCode(), vendor);
//        }
//        List<VendorBankAccount> vendorBankAccounts = vendorBankAccountService.findAll();
//        for (VendorBankAccount bankAccount : vendorBankAccounts) {
//            Context.sessionVendorBankAccount.put(bankAccount.getRoutingNo(), bankAccount);
//        }

    }

//    private String getGroupingByKey(Vendor vendor) {
//        return vendor.getCompCode() + "-" + vendor.getValueCode();
//    }
}
