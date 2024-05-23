import { Component, OnInit, ViewChild } from '@angular/core';
import { UserProfile } from '@core/models/user-profile';
import Decimal from 'decimal.js';
import { WebInfo } from '@core/models/web-info';
import { Constant } from '@shared/utils/constant';
import { FormBuilder } from '@angular/forms';
import { Utils } from '@shared/utils/utils';
import { ActivatedRoute } from '@angular/router';
import { LoaderService, LocalStorageService, SidebarService } from '@core/services';
import { FiService } from '@core/services/fi/fi.service';
import { MatDialog } from '@angular/material/dialog';
import { PoService } from '@core/services/po/po.service';
import { DialogPoHistoryComponent } from '@shared/component/dialog-po-history/dialog-po-history.component';
import { DialogPoChangeHistoryComponent } from '@shared/component/dialog-po-change-history/dialog-po-change-history.component';
import { DialogWarningComponent } from '@shared/component/dialog-warning/dialog-warning.component';
import { DialogPoTaxComponent } from '@shared/component/dialog-po-tax/dialog-po-tax.component';
import { DialogPoAdvancePaymentComponent } from '@shared/component/dialog-po-advance-payment/dialog-po-advance-payment.component';
import { DialogPoHeaderDetailComponent } from '@shared/component/dialog-po-header-detail/dialog-po-header-detail.component';
import { DialogPoItemDetailComponent } from '@shared/component/dialog-po-item-detail/dialog-po-item-detail.component';

@Component({
  selector: 'app-detail-po4',
  templateUrl: './detail-po4.component.html',
  styleUrls: ['./detail-po4.component.scss'],
})
export class DetailPo4Component implements OnInit {
  @ViewChild('tabRef', { static: true }) tabRef: any;

  tabAmount = 0;
  p = 1;

  userProfile: UserProfile;

  companyCode = null;
  companyName = null;
  paymentCenter = null;
  paymentCenterName = null;
  poDocNo = null;
  userWebOnline = null;
  egporderNo = null;
  procureMethod = null;
  procureMethodName = null;
  egpdateContractStart = null;
  vendorCode = null;
  egpbpartnerTaxID = null;
  vendorCodeName = null;
  egpdateContractEnd = null;
  egpbpartnerBankAccountNo = null;

  listDocument = [];

  lineNo = null;
  poLineStatus = null;
  accType = null;
  accTypeName = null;
  gPSCCode = null;
  gPSCCodeName = null;
  qty = null;
  uom = null;
  uomName = null;
  price = null;
  priceLastPO = null;
  egpdateDelivery = null;
  downPayment = null;

  budgetCostCenter = null;
  budgetCostCenterName = null;
  glAccount = null;
  glAccountName = null;
  brDocNo = null;
  subBookGL = null;
  subBookGLName = null;
  budgetFundSource = null;
  budgetFundSourceName = null;
  budgetCode = null;
  budgetCodeName = null;
  budgetActivity = null;
  budgetActivityName = null;
  budgetCostActivity = null;
  budgetCostActivityName = null;
  depositAccountSub = null;
  depositAccountSubName = null;
  depositAccountSubOwner = null;
  depositAccount = null;
  depositAccountName = null;
  depositAccountOwner = null;
  depositAccountOwnerName = null;
  detailTexts = [];
  collectionPhase = null;
  isHistory = null;

  tax = null; // ประเภท ภาษี และมูลค่า
  isTaxInputApply = false; // เช็คสถานะ ภาษี
  checkTypeWtx = '';
  checkTypeVendorGroup = '';

  prepaidValue = null;
  isAdvancePaymentApply = null;

  listHeaderText = [];
  isCheckHeaderText = null;

  totalPrice = new Decimal(0);
  totalTax = new Decimal(0);
  netPrice = new Decimal(0);
  totalPrepaid = new Decimal(0);

  headerTemp = null;

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
    private poService: PoService
  ) {}

  ngOnInit() {
    this.sidebarService.toggleSidebarVisibility(false);

    this.userProfile = this.localStorageService.getUserProfile();
    this.webInfo = this.localStorageService.getWebInfo();
    console.log('call');
    // this.searchDetailDocument('03003', '4001004049', 'GWPO');
    this.route.queryParams.subscribe((params) => {
      console.log(params);
      if (params.compCode && params.docNo && params.docType) {
        this.searchDetailDocument(params.compCode, params.docNo, params.docType);
      }
    });
  }

  searchDetailDocument(companyCode, docNo, doctype) {
    const payload = {
      companyCode,
      docNo,
      doctype,
      formID: 'P04',
      webInfo: this.localStorageService.getWebInfo(),
    };
    console.log(payload);
    this.loaderService.loadingToggleStatus(true);
    this.poService.searchDetail(payload).then((result) => {
      if (result.status === 200) {
        console.log(result.data);
        this.loaderService.loadingToggleStatus(false);
        const header = result.data.header;
        this.headerTemp = header;
        const items = result.data.items;

        this.setDatatFromSearchDetail(header, items);
      }

      console.log(result);
    });
  }

  setDatatFromSearchDetail(header, items) {
    this.selectListOrder = 1;
    this.companyCode = header.companyCode;
    this.companyName = header.companyName;
    this.paymentCenter = header.paymentCenterCode;
    this.paymentCenterName = header.paymentCenterName;
    this.poDocNo = header.docNo;
    this.userWebOnline = header.webUserCode;

    this.egporderNo = header.egporderNo;
    this.procureMethod = header.procureMethod;
    this.procureMethodName = header.procureMethodName;
    this.egpdateContractStart = header.egpdateContractStart;
    this.vendorCode = header.vendorCode;
    this.egpbpartnerTaxID = header.egpbpartnerTaxID;
    this.vendorCodeName = header.vendorCodeName;
    this.egpdateContractEnd = header.egpdateContractEnd;
    this.egpbpartnerBankAccountNo = header.egpbpartnerBankAccountNo;

    this.listDocument = items;
    this.lineNo = items[0].lineNo;
    this.poLineStatus = items[0].poLineStatus;
    this.accType = items[0].accType;
    this.accTypeName = items[0].accTypeName;
    this.gPSCCode = items[0].gPSCCode;
    this.gPSCCodeName = items[0].gPSCCodeName;
    this.qty = items[0].qty;
    this.uom = items[0].uom;
    this.uomName = items[0].uomName;
    this.price = items[0].price;
    this.priceLastPO = items[0].priceLastPO;
    this.egpdateDelivery = items[0].egpdateDelivery;
    this.downPayment = header.isHeadAdvance === 'Y' ? new Decimal(0) : items[0].downPayment;
    this.budgetCostCenter = items[0].budgetCostCenter;
    this.budgetCostCenterName = items[0].budgetCostCenterName;
    this.glAccount = items[0].glAccount;
    this.glAccountName = items[0].glAccountName;
    this.brDocNo = items[0].brDocNo;
    this.subBookGL = items[0].subBookGL;
    this.subBookGLName = items[0].subBookGLName;
    this.budgetFundSource = items[0].budgetFundSource;
    this.budgetFundSourceName = items[0].budgetFundSourceName;
    this.budgetCode = items[0].budgetCode;
    this.budgetCodeName = items[0].budgetCodeName;
    this.budgetActivity = items[0].budgetActivity;
    this.budgetActivityName = items[0].budgetActivityName;
    this.budgetCostActivity = items[0].budgetCostActivity;
    this.budgetCostActivityName = items[0].budgetCostActivityName;
    this.depositAccountSub = items[0].depositAccountSub;
    this.depositAccountSubName = items[0].depositAccountSubName;
    this.depositAccountSubOwner = items[0].depositAccountSubOwner;
    this.depositAccount = items[0].depositAccount;
    this.depositAccountName = items[0].depositAccountName;
    this.depositAccountOwner = items[0].depositAccountOwner;
    this.depositAccountOwnerName = items[0].depositAccountOwnerName;

    this.detailTexts = items[0].detailTexts;
    this.isHistory = items[0].isHistory;

    if (header.headerConditions !== null && header.headerConditions.length > 0) {
      header.headerConditions.forEach((itemCondition) => {
        if (itemCondition.conditionType === 'ZVAT' || itemCondition.conditionType === 'ZVA2') {
          this.tax = {
            typeTax: itemCondition.conditionType,
            valueTax: itemCondition.conditionValue,
          };
        } else if (itemCondition.conditionType === 'ZDWP') {
          if (header.isHeadAdvance === 'Y') {
            if (itemCondition.conditionValue === '0') {
              this.prepaidValue = null;
            } else {
              this.prepaidValue = {
                prepaidValue: itemCondition.conditionValue,
              };
            }
          } else {
            this.prepaidValue = null;
          }
        }
      });
    }
    items.forEach((item) => {
      this.totalPrice = this.totalPrice.plus(item.qty);
    });

    if (this.tax !== null) {
      this.isTaxInputApply = true;
      if (this.tax.typeTax === 'ZVAT') {
        this.totalTax = new Decimal(this.tax.valueTax);
      } else if (this.tax.typeTax === 'ZVA2') {
        this.totalTax = this.totalPrice.mul(new Decimal(this.tax.valueTax)).div(100);
      }
    } else {
      this.totalTax = new Decimal(0);
      this.isTaxInputApply = false;
    }
    this.netPrice = this.totalPrice.plus(this.totalTax);
    if (this.prepaidValue !== null) {
      this.totalPrepaid = this.totalPrepaid.plus(this.prepaidValue.prepaidValue);
      this.isAdvancePaymentApply = true;
    } else {
      items.forEach((item) => {
        this.totalPrepaid = this.totalPrepaid.plus(item.downPayment);
      });
      this.isAdvancePaymentApply = false;
    }
    if (header.headerTextItems !== null && header.headerTextItems.length > 0) {
      header.headerTextItems.forEach((itemText) => {
        const text = {
          typeText: itemText.textCode,
          additionalText: itemText.textMsg,
        };
        this.listHeaderText.push(text);
      });
    }
    if (this.listHeaderText.length > 0) {
      this.isCheckHeaderText = true;
    } else {
      this.isCheckHeaderText = false;
    }
  }

  ngAfterViewInit(): void {
    this.tabAmount = this.tabRef._tabs.length;
  }

  selectOrder(document, i) {
    this.selectListOrder = i + 1;
    this.lineNo = document.lineNo;
    this.poLineStatus = document.poLineStatus;
    this.accType = document.accType;
    this.accTypeName = document.accTypeName;
    this.gPSCCode = document.gPSCCode;
    this.gPSCCodeName = document.gPSCCodeName;
    this.qty = document.qty;
    this.uom = document.uom;
    this.uomName = document.uomName;
    this.price = document.price;
    this.priceLastPO = document.priceLastPO;
    this.egpdateDelivery = document.egpdateDelivery;
    this.downPayment =
      this.headerTemp.isHeadAdvance === 'Y' ? new Decimal(0) : document.downPayment;
    this.budgetCostCenter = document.budgetCostCenter;
    this.budgetCostCenterName = document.budgetCostCenterName;
    this.glAccount = document.glAccount;
    this.glAccountName = document.glAccountName;
    this.brDocNo = document.brDocNo;
    this.subBookGL = document.subBookGL;
    this.subBookGLName = document.subBookGLName;
    this.budgetFundSource = document.budgetFundSource;
    this.budgetFundSourceName = document.budgetFundSourceName;
    this.budgetCode = document.budgetCode;
    this.budgetCodeName = document.budgetCodeName;
    this.budgetActivity = document.budgetActivity;
    this.budgetActivityName = document.budgetActivityName;
    this.budgetCostActivity = document.budgetCostActivity;
    this.budgetCostActivityName = document.budgetCostActivityName;
    this.depositAccountSub = document.depositAccountSub;
    this.depositAccountSubName = document.depositAccountSubName;
    this.depositAccountSubOwner = document.depositAccountSubOwner;
    this.depositAccount = document.depositAccount;
    this.depositAccountName = document.depositAccountName;
    this.depositAccountOwner = document.depositAccountOwner;
    this.depositAccountOwnerName = document.depositAccountOwnerName;

    this.detailTexts = document.detailTexts;
    this.isHistory = document.isHistory;
  }

  checkDownPay(value) {
    const downPay = new Decimal(value);
    if (this.headerTemp.isHeadAdvance === 'Y') {
      return false;
    } else {
      if (downPay.comparedTo(0) === 0) {
        return false;
      } else {
        return true;
      }
    }
  }

  showHistory(value) {
    console.log(value);
    const payload = {
      companyCode: this.companyCode,
      flag: 0,
      formID: 'P01',
      poDocumentNo: this.poDocNo,
      poLineNo: value.lineNo,
      webInfo: this.localStorageService.getWebInfo(),
    };
    this.poService.history(payload).then((res) => {
      if (res.data) {
        const dialogRef = this.dialog.open(DialogPoHistoryComponent, {
          width: '95vw',
          data: {
            item: res.data.history,
          },
        });
        dialogRef.afterClosed().subscribe((result) => {
          if (result && result.event === 'save') {
          } else {
          }
        });
      }
    });
  }

  changeHistory() {
    const payload = {
      companyCode: this.companyCode,
      flag: 0,
      formID: 'P01',
      poDocumentNo: this.poDocNo,
      webInfo: this.localStorageService.getWebInfo(),
    };

    this.poService.changeHistory(payload).then((res) => {
      if (res.data) {
        if (res.data.histories.length > 0) {
          const dialogRef = this.dialog.open(DialogPoChangeHistoryComponent, {
            width: '80vw',
            data: {
              item: res.data.histories,
            },
          });
          dialogRef.afterClosed().subscribe((result) => {
            if (result && result.event === 'save') {
            } else {
            }
          });
        } else {
          const dialogRef = this.dialog.open(DialogWarningComponent, {
            width: '35vw',
            disableClose: false /**** here na Boss */,
            data: {
              textWarning: 'ไม่มีประวัติการแก้ไขสำหรับเอกสารนี้ !',
            },
          });
        }
      }
    });
  }

  openDialogPoTax() {
    const dialogRef = this.dialog.open(DialogPoTaxComponent, {
      width: '95vw',
      data: {
        typeTax: this.tax,
        checkStatus: true,
        checkTypeWtx: this.checkTypeWtx,
        checkTypeVendorGroup: this.checkTypeVendorGroup,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event === 'save') {
        this.tax = result.typeTax;
        if (this.tax !== null) {
          this.isTaxInputApply = true;
          // this.calculateTax();
          // this.calculateNetPrice();
        } else {
          this.isTaxInputApply = false;
          // this.calculateTax();
          // this.calculateNetPrice();
        }
      }
    });
  }

  openDialogPoAdvancePayment() {
    const dialogRef = this.dialog.open(DialogPoAdvancePaymentComponent, {
      width: '60vw',
      data: {
        prepaidValue: this.prepaidValue,
        checkStatus: true,
        checkTypeWtx: this.checkTypeWtx,
        checkTypeVendorGroup: this.checkTypeVendorGroup,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event === 'save') {
        this.prepaidValue = result.prepaidValue;
        if (this.prepaidValue !== null) {
          this.isAdvancePaymentApply = true;
          // this.listFormControlValidate.controls.moneyPledge.disable();
          // this.checkMoneyPrepaid();
          // this.calculateTotalPrepaid();
        } else {
          this.isAdvancePaymentApply = false;
          // this.listFormControlValidate.controls.moneyPledge.enable();
          // this.calculateTotalPrepaid();
        }
      }
    });
  }

  openDialogPoHeaderDetail() {
    const dialogRef = this.dialog.open(DialogPoHeaderDetailComponent, {
      width: '60vw',
      data: {
        headerText: this.listHeaderText,
        checkStatus: true,
        // egpData: this.dataEgp,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  openDialogPoItemDetail() {
    const dialogRef = this.dialog.open(DialogPoItemDetailComponent, {
      width: '60vw',
      data: {
        itemText: this.detailTexts,
        checkStatus: true,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }
}
