import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { UserProfile } from '@core/models/user-profile';
import { WebInfo } from '@core/models/web-info';
import { LocalStorageService } from '@core/services/local-storage.service';
import { SidebarService } from '@core/services/sidebar.service';

import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { FiService } from '@core/services/fi/fi.service';
import { DialogShowAutodocComponent } from '@shared/component/dialog-show-autodoc/dialog-show-autodoc.component';
import { MatDialog } from '@angular/material/dialog';
import Decimal from 'decimal.js';
import { LoaderService, MasterService } from '@core/services';
import { DialogTaxFeeComponent } from '@shared/component/dialog-tax-fee/dialog-tax-fee.component';
import { DialogBankAccountDetailComponent } from '@shared/component/dialog-bank-account-detail/dialog-bank-account-detail.component';
import { PoService } from '@core/services/po/po.service';
import { interval } from 'rxjs';

@Component({
  selector: 'app-detail-fi-kb',
  templateUrl: './detail-fi-kb.component.html',
  styleUrls: ['./detail-fi-kb.component.scss'],
})
export class DetailFiKbComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('tabRef', { static: true }) tabRef: any;
  listPaymentBlock = [
    { id: 'R', name: 'R - CGD ไม่อนุมัติ' },
    { id: ' ', name: '  - ชำระเงินได้' },
    { id: 'V', name: 'V - หักล้างการชำระเงิน' },
    { id: '*', name: '* - ข้ามบัญชี' },
    { id: 'N', name: 'N - ไม่อนุมัติภายใน สรก.' },
    { id: 'E', name: 'E - PTO ไม่อนุมัติ' },
    { id: 'B', name: 'B - ระงับการชำระเงิน' },
    { id: 'P', name: 'P - รอ CGD อนุมัติ' },
    { id: '0', name: '0 - รออนุมัติขั้น1ในสรก.' },
    { id: 'A', name: 'A - รออนุมัติขั้น2ในสรก.' },
    { id: 'F', name: 'F - เลขที่บัญชีผิดพลาด' },
  ];

  tabAmount = 0;
  p = 1;

  userProfile: UserProfile;

  documentReverse = false;

  accDocNo = null;
  fiscalYear = null;

  revAccDocNo = null;
  revFiscalYear = null;

  userWebOnline = null;

  companyCode = null;
  companyName = null;
  documentDate = null;
  paymentCenter = null;
  paymentCenterName = null;
  postDate = null;
  areaCode = null;
  areaName = null;
  period = null;
  docType = null;
  docTypeName = null;
  headerReference = null;
  tabSelectedIndex = 0;
  listDocument = [];
  listBackupDocument = [];
  listChangeDocument = [];

  isTaxInputApply = false;
  totalPrice = new Decimal(0);
  totalTax = new Decimal(0);
  totalFee = new Decimal(0);
  netPrice = new Decimal(0);

  poDocNo = null;
  brDocNo = null;
  brTrxLineID = null;
  vendorTaxId = null;
  vendorName = null;
  vendorCode = null;

  payeeTaxId = null;
  payeeName = null;
  payeeCode = null;

  bankAccountNo = null;
  bankAccNoName = null;
  bankAccNoActive = null;
  taxFee = null;

  invDocNo = null;
  invFiscalYear = null;
  invLine = null;
  line = null;
  postingKeyName = null;
  postingKey = null;
  glAccount = null;
  glAccountName = null;
  glAccount2 = null;
  glAccountName2 = null;
  costCenter = null;
  costCenterName = null;
  fundSource = null;
  fundSourceName = null;
  bgCode = null;
  bgCodeName = null;
  bgActivity = null;
  bgActivityName = null;
  costActivity = null;
  costActivityName = null;
  tradingPartner = null;
  trandingPartnerName = null;
  gpsc = null;
  gpscName = null;
  gpscGroup = null;
  gpscGroupName = null;
  bgAccount = null;
  bgAccountName = null;
  depositAccount = null;
  depositAccountName = null;
  depositAccountOwner = null;
  depositAccountOwnerName = null;
  subAccount = null;
  subAccountName = null;
  subAccountOwner = null;
  subAccountOwnerName = null;
  bankBook = null;
  bankBookName = null;
  amount = null;
  ref1 = null;
  ref2 = null;
  ref3 = null;
  dateBaseLine = null;
  lineDesc = null;
  lineItemText = null;
  assignment = null;
  paymentBlock = null;
  clearingDocNo = null;
  clearingDateAcct = null;
  clearingFiscalYear = null;
  specialGL = null;
  dueDate = null;
  paymentReference = null;


  isDisabledButtonAutodocs = false;
  isDisabledButtonInvoiceDoc = false;
  listAutodoc = [];
  listInvoiceDoc = [];

  headerTemp = null;

  subscription = null;

  selectListOrder = null; // เลือกหน้ารายการบัญชีที่เลือกจากตาราง
  private webInfo: WebInfo;

  constructor(
    public constant: Constant,
    private formBuilder: FormBuilder,
    public utils: Utils,
    private route: ActivatedRoute,
    private localStorageService: LocalStorageService,
    private sidebarService: SidebarService,
    private fiService: FiService,
    private dialog: MatDialog,
    private loaderService: LoaderService,
    private masterService: MasterService,
    private poService: PoService,
  ) {
  }

  ngOnInit() {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
    this.sidebarService.toggleSidebarVisibility(false);

    this.userProfile = this.localStorageService.getUserProfile();
    this.webInfo = this.localStorageService.getWebInfo();

    this.route.queryParams.subscribe((params) => {
      console.log(params);
      if (params.compCode && params.docNo && params.docYear) {
        this.searchDetailDocument(params.compCode, params.docNo, params.docYear);
        if (params.autoRefresh) {
          this.subscription = interval(20000).subscribe(() => {
            this.searchDetailDocument(params.compCode, params.docNo, params.docYear);
          });
        }
      }
    });

  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  searchDocumentReverse() {
    this.searchDetailDocument(this.companyCode, this.revAccDocNo, this.fiscalYear);
  }

  searchDetailDocument(compCode, accountDocNo, fiscalYear) {
    const payload = {
      compCode,
      accountDocNo,
      fiscalYear,
      docNo: accountDocNo,
      webInfo: this.localStorageService.getWebInfo(),
    };

    console.log(payload);
    this.loaderService.loadingToggleStatus(true);
    this.fiService.searchDetail(payload).then((result) => {
      if (result.status === 200) {
        this.loaderService.loadingToggleStatus(false);
        const header = result.data.header;
        this.headerTemp = header;
        const items = result.data.lines;
        const autoDocs = result.data.autoDocs;
        const invoiceDocs = result.data.invDocs;
        if (autoDocs) {
          if (autoDocs.length > 0 && autoDocs !== null) {
            this.isDisabledButtonAutodocs = true;
            this.listAutodoc = autoDocs;
          } else {
            this.isDisabledButtonAutodocs = false;
          }
        }
        if (invoiceDocs) {
          if (invoiceDocs.length > 0 && invoiceDocs !== null) {
            this.isDisabledButtonInvoiceDoc = true;
            this.listInvoiceDoc = invoiceDocs;
          } else {
            this.isDisabledButtonInvoiceDoc = false;
          }
        }
        this.setDatatFromSearchDetail(header, items);
        this.loadDetail(payload);
      }

      console.log(result);
    });
  }

  setDatatFromSearchDetail(header, items) {
    this.selectListOrder = 1;
    this.accDocNo = header.accDocNo;
    this.fiscalYear = header.fiscalYear;

    this.revAccDocNo = header.revAccDocNo;
    this.revFiscalYear = header.revFiscalYear;
    this.userWebOnline = header.userWebOnline;

    this.companyCode = header.compCode;
    this.companyName = header.compCodeName;
    this.documentDate = header.dateDoc;
    this.paymentCenter = header.paymentCenter;
    this.paymentCenterName = header.paymentCenterName;
    this.postDate = header.dateAcct;
    this.areaCode = items[0].fiArea;
    this.areaName = items[0].fiAreaName;
    this.period = header.period;
    this.docType = header.docType;

    if (header.docType === 'KZ' || header.docType === 'PZ') {
      this.documentReverse = true;
    } else {
      this.documentReverse = false;
    }

    this.docTypeName = header.docTypeName;
    this.headerReference = header.headerReference;
    this.poDocNo = header.poDocNo;
    this.brDocNo = items.brDocNo;


    this.vendorTaxId = items[0].vendorTaxID;
    this.vendorCode = items[0].vendor;
    this.vendorName = items[0].vendorName;
    this.bankAccountNo = items[0].bankAccNo;

    this.bankAccNoName = items[0].bankAccNoName;
    this.bankAccNoActive = items[0].bankAccNoActive;

    this.payeeTaxId = header.payeeTaxID;
    this.payeeCode = header.payee;
    this.payeeName = header.payeeName;

    this.listDocument = items;

    this.line = items[0].line;
    this.postingKeyName = items[0].postingKeyName;
    this.postingKey = items[0].postingKey;
    this.brDocNo = items[0].brDocNo;

    if (items[0].wtxCode || items[0].wtxCodeP) {
      (this.totalTax = items[0].wtxAmount ? new Decimal(items[0].wtxAmount) : new Decimal(0)),
        (this.totalFee = items[0].wtxAmountP ? new Decimal(items[0].wtxAmountP) : new Decimal(0)),
        (this.isTaxInputApply = true);
      this.taxFee = {
        codeTax: items[0].wtxCode,
        typeTax: items[0].wtxType,
        taxBaseCalculate: items[0].wtxBase ? new Decimal(items[0].wtxBase) : new Decimal(0),
        valueTax: items[0].wtxAmount ? new Decimal(items[0].wtxAmount) : new Decimal(0),
        codeFee: items[0].wtxCodeP,
        typeFee: items[0].wtxTypeP,
        feeBaseCalculate: items[0].wtxBaseP ? new Decimal(items[0].wtxBaseP) : new Decimal(0),
        valueFee: items[0].wtxAmountP ? new Decimal(items[0].wtxAmountP) : new Decimal(0),
        lineDesc: items[0].descriptionDocument,
      };
    } else {
      this.totalFee = new Decimal(0);
      this.totalTax = new Decimal(0);
    }

    if (this.accDocNo.startsWith('4') && items[0].accountType === 'K') {
      this.glAccount = items[0].glAccount2;
      this.glAccountName = items[0].glAccount2Name;
    } else {
      this.glAccount = items[0].glAccount;
      this.glAccountName = items[0].glAccountName;
    }

    this.costCenter = items[0].costCenter;
    this.costCenterName = items[0].costCenterName;
    this.fundSource = items[0].fundSource;
    this.fundSourceName = items[0].fundSourceName;
    this.bgCode = items[0].bgCode;
    this.bgCodeName = items[0].bgCodeName;
    this.bgActivity = items[0].bgActivity;
    this.bgActivityName = items[0].bgActivityName;
    this.costActivity = items[0].costActivity;
    this.costActivityName = items[0].costActivityName;
    this.tradingPartner = items[0].tradingPartner;
    this.trandingPartnerName = items[0].trandingPartnerName;
    this.gpsc = items[0].gpsc;
    this.gpscName = items[0].gpscName;
    this.gpscGroup = items[0].gpscGroup;
    this.gpscGroupName = items[0].gpscGroupName;
    this.bgAccount = items[0].bgAccount;
    this.bgAccountName = items[0].bgAccountName;
    this.depositAccount = items[0].depositAccount;
    this.depositAccountName = items[0].depositAccountName;
    this.depositAccountOwner = items[0].depositAccountOwner;
    this.depositAccountOwnerName = items[0].depositAccountOwnerName;
    this.subAccount = items[0].subAccount;
    this.subAccountName = items[0].subAccountName;
    this.subAccountOwner = items[0].subAccountOwner;
    this.subAccountOwnerName = items[0].subAccountOwnerName;
    this.bankBook = items[0].bankBook;
    this.bankBookName = items[0].bankBookName;
    this.amount = items[0].amount;
    this.ref1 = items[0].reference1;

    this.ref2 = items[0].reference2;
    this.ref3 = items[0].reference3;
    this.dateBaseLine = items[0].dateBaseLine;
    this.lineDesc = items[0].lineDesc;
    this.lineItemText = items[0].lineItemText;
    this.assignment = items[0].assignment;
    this.paymentBlock = items[0].paymentBlock;
    this.clearingDocNo = items[0].clearingDocNo;
    this.clearingDateAcct = items[0].clearingDateAcct;
    this.clearingFiscalYear = items[0].clearingFiscalYear;
    this.specialGL = items[0].specialGL;
    this.dueDate = items[0].dueDate;

    this.paymentReference = items[0].paymentRef;



    this.invDocNo = items[0].invDocNo;
    this.invFiscalYear = items[0].invFiscalYear;
    this.invLine = items[0].invLine;

    // const payload = {
    //   companyCode: this.companyCode,
    //   docNo: this.poDocNo,
    //   doctype: 'GWPO',
    //   formID: 'P01',
    //   webInfo: this.localStorageService.getWebInfo(),
    // };
    // console.log(payload);
    // this.loaderService.loadingToggleStatus(true);
    // this.poService.searchDetail(payload).then((result) => {
    //   if (result.status === 200) {
    //     console.log(result.data);
    //     this.loaderService.loadingToggleStatus(false);
    //     const header = result.data.header;
    //     this.headerTemp = header;
    //     brTrxLineID
    //     // const items = result.data.items;
    //     // this.setDatatFromSearchDetail(header, items);
    //   }
    //
    //   console.log(result);
    // });
    this.calculateTotalPrice();
    this.calculateTax();
    this.calculateFee();
    this.calculateNetPrice();
  }

  ngAfterViewInit(): void {
    this.tabAmount = this.tabRef._tabs.length;
  }

  selectOrder(document, i) {
    this.selectListOrder = i + 1;
    this.line = document.line;
    this.postingKeyName = document.postingKeyName;
    this.postingKey = document.postingKey;
    this.brDocNo = document.brDocNo;
    if (this.accDocNo.startsWith('4') && document.accountType === 'K') {
      this.glAccount = document.glAccount2;
      this.glAccountName = document.glAccount2Name;
    } else {
      this.glAccount = document.glAccount;
      this.glAccountName = document.glAccountName;
    }

    this.costCenter = document.costCenter;
    this.costCenterName = document.costCenterName;
    this.fundSource = document.fundSource;
    this.fundSourceName = document.fundSourceName;
    this.bgCode = document.bgCode;
    this.bgCodeName = document.bgCodeName;
    this.bgActivity = document.bgActivity;
    this.bgActivityName = document.bgActivityName;
    this.costActivity = document.costActivity;
    this.costActivityName = document.costActivityName;

    this.tradingPartner = document.tradingPartner;
    this.trandingPartnerName = document.trandingPartnerName;
    this.gpsc = document.gpsc;
    this.gpscName = document.gpscName;
    this.gpscGroup = document.gpscGroup;
    this.gpscGroupName = document.gpscGroupName;
    this.bgAccount = document.bgAccount;
    this.bgAccountName = document.bgAccountName;
    this.depositAccount = document.depositAccount;
    this.depositAccountName = document.depositAccountName;
    this.depositAccountOwner = document.depositAccountOwner;
    this.depositAccountOwnerName = document.depositAccountOwnerName;
    this.subAccount = document.subAccount;
    this.subAccountName = document.subAccountName;
    this.subAccountOwner = document.subAccountOwner;
    this.subAccountOwnerName = document.subAccountOwnerName;
    this.bankBook = document.bankBook;
    this.bankBookName = document.bankBookName;
    this.amount = document.amount;
    this.ref1 = document.reference1;
    this.ref2 = document.reference2;
    this.ref3 = document.reference3;
    // this.dateBaseLine = document.dateBaseLine;
    // this.lineDesc = document.lineDesc;
    this.lineItemText = document.lineItemText;
    this.assignment = document.assignment;
    // this.paymentBlock = document.paymentBlock;
    this.clearingDocNo = document.clearingDocNo;
    this.clearingDateAcct = document.clearingDateAcct;
    this.clearingFiscalYear = document.clearingFiscalYear;

    // this.specialGL = document.specialGL;
    this.dueDate = document.dueDate;

    this.invDocNo = document.invDocNo;
    this.invFiscalYear = document.invFiscalYear;
    this.invLine = document.invLine;
  }

  onDisplayDocAutoGenarate(listObject) {

    const dialogRef = this.dialog.open(DialogShowAutodocComponent, {
      width: '70vw',
      data: {
        autodocs: listObject,
        type: 'kb',
        header: this.headerTemp,
        formID: '',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
      }
    });
  }

  openDialogTaxFee() {
    const dialogRef = this.dialog.open(DialogTaxFeeComponent, {
      width: '50vw',
      data: {
        taxFee: this.taxFee,
        type: '0',
        // checkStatus: this.isDisabledFromSearch,
        // checkTypeWtx: this.checkTypeWtx,
        // checkTypeVendorGroup: this.checkTypeVendorGroup,
        // checkDocType: this.documentType,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event === 'save') {
        this.taxFee = result.taxFee;
        if (this.taxFee !== null) {
          this.isTaxInputApply = true;
          // this.calculateTotalPrice();
          // this.calculateTax();
          // this.calculateFee();
          // this.calculateNetPrice();
        } else {
          this.isTaxInputApply = false;
          // this.calculateTotalPrice();
          // this.calculateTax();
          // this.calculateFee();
          // this.calculateNetPrice();
        }
      }
    });
  }

  calculateTotalPrice() {
    let totalPrice = new Decimal(0);
    this.totalPrice = new Decimal(0);
    this.listDocument.forEach((document) => {
      console.log(document);
      if (document.drCr === 'C') {
        totalPrice = totalPrice.plus(document.amount);
      }
    });
    this.totalPrice = totalPrice;
  }

  calculateTax() {
    if (this.taxFee !== null && this.taxFee !== '') {
      if (this.taxFee.valueTax !== null && this.taxFee.valueTax !== '') {
        if (this.taxFee.typeTax) {
          this.totalTax = new Decimal(this.taxFee.valueTax);
        }
      } else {
        this.totalTax = new Decimal(0);
      }
    } else {
      this.totalTax = new Decimal(0);
    }
  }

  calculateFee() {
    if (this.taxFee !== null && this.taxFee !== '') {
      if (this.taxFee.valueFee !== null && this.taxFee.valueFee !== '') {
        this.totalFee = new Decimal(this.taxFee.valueFee);
      } else {
        this.totalFee = new Decimal(0);
      }
    } else {
      this.totalFee = new Decimal(0);
    }
  }

  calculateNetPrice() {
    this.netPrice = new Decimal(0);

    if (this.totalPrice.comparedTo(this.totalTax) >= 0) {
      this.netPrice = this.totalPrice.minus(this.totalTax.plus(this.totalFee));
    }
  }

  openDetailPo() {
    if (this.poDocNo.toString().startsWith('4') || this.poDocNo.toString().startsWith('7') || this.poDocNo.toString().startsWith('6')) {
      const url =
        './detail-po?compCode=' +
        this.companyCode +
        '&docNo=' +
        this.poDocNo +
        '&docType=' +
        'GWPO';
      window.open(url, 'name', 'width=1200,height=700');
    } else if (this.poDocNo.toString().startsWith('5') || this.poDocNo.toString().startsWith('2')) {
      const url =
        './detail-po4?compCode=' +
        this.companyCode +
        '&docNo=' +
        this.poDocNo +
        '&docType=' +
        'GZPO';
      window.open(url, 'name', 'width=1200,height=700');
    }
  }

  openDetailBr() {
    const url =
      './detail-sng?docNo=' +
      this.brDocNo +
      '&companyCode=' +
      this.companyCode +
      '&paymentCenter=' +
      this.paymentCenter +
      '&formID=' +
      'F01';
    window.open(url, 'name', 'width=1200,height=700');
  }

  openChangeDocument() {
    const url =
      './change-document?compCode=' +
      this.companyCode +
      '&docNo=' +
      this.accDocNo +
      '&fiscalYear=' +
      this.fiscalYear;
    window.open(url, 'name', 'width=1200,height=700');

  }

  bankAccountDetail() {
    const vendorCode = this.payeeCode ? this.payeeCode : this.vendorCode;
    this.masterService.bankAccountDetail(vendorCode, this.bankAccountNo, this.paymentReference.substring(0, 7)).then((result) => {
      console.log(result);
      if (result.status === 200) {
        if (result.data.length > 0) {
          const dialogRef = this.dialog.open(DialogBankAccountDetailComponent, {
            width: '70vw',
            data: {
              payload: result.data[0],
            },
          });
          dialogRef.afterClosed().subscribe((res) => {
          });
        }
      }
    });
  }

  loadDetail(payload) {

    this.fiService.paymentBlockDetail(payload).then((data) => {
      console.log(data);
      const response = data as any;
      if (response.status === 200) {
        this.checkNull(response.data);
      }
    });
  }

  checkNull(value) {
    if (value.changeLog) {
      this.listChangeDocument = value.changeLog;
      this.listChangeDocument.forEach((element) => {
        const objectOld = this.listPaymentBlock.find((item) => item.id === element.valueOld);
        element.valueOldName = objectOld.name;
        const objectNew = this.listPaymentBlock.find((item) => item.id === element.valueNew);
        element.valueNewName = objectNew.name;
      });
    }
    if (value.followingDoc) {
      this.listBackupDocument = value.followingDoc;
    }
  }

  viewDocument(companyCode, documentNo, fiscalYear) {
    const url =
      './detail-fi-kb?compCode=' +
      companyCode +
      '&docNo=' +
      documentNo +
      '&docYear=' +
      fiscalYear;
    // window.open(url, 'name', 'width=1200,height=700');
    window.open(url, '_blank');

  }
}
