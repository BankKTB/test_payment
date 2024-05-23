import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { SelectGroupDocumentService } from '@core/services/select-group-document/select-group-document.service';
import { LoaderService, LocalStorageService, MasterService, SidebarService } from '@core/services';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { MatTabChangeEvent } from '@angular/material/tabs';
import Decimal from 'decimal.js';
import { DialogTaxFeeComponent } from '@shared/component/dialog-tax-fee/dialog-tax-fee.component';
import { UserProfile } from '@core/models/user-profile';
import { Constant } from '@shared/utils/constant';
import { FiService } from '@core/services/fi/fi.service';
import { DialogConfirmChangeDocumentComponent } from '@shared/component/dialog-confirm-change-document/dialog-confirm-change-document.component';
import { DialogUploadChangeDocumentComponent } from '@shared/component/dialog-upload-change-document/dialog-upload-change-document.component';
import { ActivatedRoute } from '@angular/router';
import { DialogResultComponent } from '@shared/component/dialog-result/dialog-result.component';

@Component({
  selector: 'app-change-document',
  templateUrl: './change-document.component.html',
  styleUrls: ['./change-document.component.scss'],
})
export class ChangeDocumentComponent implements OnInit {
  changeDocumentForm: FormGroup;
  tabSelectedIndex = 0;
  p = 1;

  isSubmitedForm = false;

  userProfile: UserProfile;

  checkChangeDocument = true;

  documentReverse = false;

  listValidate = [];

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

  listDocument = [];
  routingNo = '';

  isTaxInputApply = false;
  totalPrice = new Decimal(0);
  totalTax = new Decimal(0);
  totalFee = new Decimal(0);
  netPrice = new Decimal(0);

  poDocNo = null;
  vendorTaxId = null;
  vendorName = null;
  vendorCode = null;

  payeeTaxId = null;
  payeeName = null;
  payeeCode = null;

  bankAccountNo = null;
  taxFee = null;

  line = null;
  postingKeyName = null;
  postingKey = null;
  brDocNo = null;
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
  specialGL = null;

  isDisabledButtonAutodocs = false;
  listAutodoc = [];
  headerTemp = null;
  oldValueTemp = null;

  selectListOrder = null; // เลือกหน้ารายการบัญชีที่เลือกจากตาราง

  constructor(
    private formBuilder: FormBuilder,
    private selectGroupDocumentService: SelectGroupDocumentService,
    private sidebarService: SidebarService,
    public utils: Utils,
    private snackBar: MatSnackBar,
    public constant: Constant,
    private fiService: FiService,
    private dialog: MatDialog,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService,
    private masterService: MasterService,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('change');
    this.sidebarService.updateNowPage('change-document');

    this.createFormGroup();
    this.route.queryParams.subscribe((params) => {
      console.log(params);
      if (params.compCode && params.docNo && params.fiscalYear) {
        this.changeDocumentForm.patchValue({
          departmentCodeFrom: params.compCode,
          documentNo: params.docNo,
          fiscalYear: this.utils.convertYearToBuddhist(params.fiscalYear),
        });
        this.searchDetailDocument();
      }
    });
  }

  createFormGroup() {
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

    this.changeDocumentForm = this.formBuilder.group({
      documentNo: this.formBuilder.control('', [Validators.required]),
      departmentCodeFrom: this.formBuilder.control('', [Validators.required]),
      fiscalYear: this.formBuilder.control(this.utils.fiscYear, [Validators.required]),
      dateBaseLine: this.formBuilder.control(''),
      paymentBlock: this.formBuilder.control(''),
      paymentMethod: this.formBuilder.control(''),
      define: this.formBuilder.control(''),
      text: this.formBuilder.control(''),
      reference1: this.formBuilder.control(''),
      bankAccountNo: this.formBuilder.control(''),
    });
    // this.changeDocumentForm.controls.dateBaseLine.disable();
    this.changeDocumentForm.controls.paymentMethod.disable();
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent): void {
    this.tabSelectedIndex = tabChangeEvent.index;
  }

  openDialogSearchMaster(type): void {
    let taxId = '';
    let alternativeVendor = '0';
    if (this.payeeTaxId) {
      taxId = this.payeeTaxId;
      alternativeVendor = '1';
    } else {
      taxId = this.vendorTaxId;
      alternativeVendor = '0';
    }

    const form = this.changeDocumentForm.getRawValue();
    let paymentMethodType = '';
    if (
      form.paymentMethod === '1' ||
      form.paymentMethod === '3' ||
      form.paymentMethod === 'I' ||
      form.paymentMethod === 'J' ||
      form.paymentMethod === '6' ||
      form.paymentMethod === 'G' ||
      form.paymentMethod === 'H' ||
      form.paymentMethod === 'F' ||
      form.paymentMethod === 'A' ||
      form.paymentMethod === 'K' ||
      form.paymentMethod === 'B'
    ) {
      paymentMethodType = 'direct';
    } else {
      paymentMethodType = 'indirect';
    }

    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type, taxId, alternativeVendor, paymentMethodType },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.changeDocumentForm.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
        } else if (type === 'departmentCodeTo') {
        } else if (type === 'provinceCodeFrom') {
        } else if (type === 'provinceCodeTo') {
        } else if (type === 'vendorTaxIdFrom') {
        } else if (type === 'vendorTaxIdTo') {
        } else if (type === 'disbursementCodeFrom') {
        } else if (type === 'disbursementCodeTo') {
        } else if (type === 'payMethodFrom') {
        } else if (type === 'payMethodTo') {
        } else if (type === 'specialTypeFrom') {
        } else if (type === 'specialTypeTo') {
        } else if (type === 'docTypeFrom') {
        } else if (type === 'docTypeTo') {
        } else if (type === 'bankAccountNo') {
          this.routingNo = result.name;
        }
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
  }

  searchDetailDocument() {
    const form = this.changeDocumentForm.getRawValue();
    this.listValidate = [];
    this.listDocument = [];
    if (!form.documentNo) {
      this.listValidate.push('กรุณากรอก เลขที่เอกสาร');
    }
    if (!form.departmentCodeFrom) {
      this.listValidate.push('กรุณากรอก รหัสหน่วยงาน');
    }
    if (!form.fiscalYear) {
      this.listValidate.push('กรุณากรอก ปีบัญชี');
    }
    this.isSubmitedForm = true;

    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const payload = {
        compCode: form.departmentCodeFrom,
        accountDocNo: form.documentNo,
        fiscalYear: this.utils.convertYearToAD(form.fiscalYear),
        webInfo: this.localStorageService.getWebInfo(),
      };
      this.oldValueTemp = null;
      this.loaderService.loadingToggleStatus(true);
      this.fiService.searchDetail(payload).then((result) => {
        console.log(result);
        if (result.status === 200) {
          this.loaderService.loadingToggleStatus(false);
          const header = result.data.header;
          this.headerTemp = header;
          const items = result.data.lines;
          const autoDocs = result.data.autoDocs;
          if (autoDocs) {
            if (autoDocs.length > 0 && autoDocs !== null) {
              this.isDisabledButtonAutodocs = true;
              this.listAutodoc = autoDocs;
            } else {
              this.isDisabledButtonAutodocs = false;
            }
          }
          this.setDatatFromSearchDetail(header, items);
        } else {
          this.listValidate.push('ไม่พบเอกสาร');
        }
      });
    }
  }

  setDatatFromSearchDetail(header, items) {
    this.taxFee = null;
    this.isTaxInputApply= false;
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

    this.vendorTaxId = items[0].vendorTaxID;
    this.vendorName = items[0].vendorName;
    this.bankAccountNo = items[0].bankAccNo;

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
    this.specialGL = items[0].specialGL;
    this.routingNo = items[0].branchNo;

    this.changeDocumentForm.patchValue({
      dateBaseLine: items[0].dateBaseLine ? new Date(items[0].dateBaseLine) : '',
      paymentBlock: items[0].paymentBlock,
      paymentMethod: items[0].paymentMethod,
      define: items[0].assignment,
      text: items[0].lineDesc,
      reference1: items[0].reference1,
      bankAccountNo: items[0].bankAccNo,
    });
    let stringDate = null;
    if (items[0].dateBaseLine) {
      const date = new Date(items[0].dateBaseLine);
      const dayDate = date.getDate();
      const monthDate = date.getMonth() + 1;
      const yearDate = date.getFullYear();
      stringDate = this.utils.parseDate(dayDate, monthDate, yearDate);
    }
    this.oldValueTemp = {
      dateBaseLine: stringDate,
      paymentBlock: items[0].paymentBlock,
      paymentMethod: items[0].paymentMethod,
      assignment: items[0].assignment,
      lineDesc: items[0].lineDesc,
      reference1: items[0].reference1,
      bankAccountNo: items[0].bankAccNo,
    };

    if (this.accDocNo.startsWith('4') || this.revAccDocNo || items[0].clearingDocNo) {
      this.checkChangeDocument = true;
      this.changeDocumentForm.controls.dateBaseLine.disable();
      this.changeDocumentForm.controls.paymentBlock.disable();
      this.changeDocumentForm.controls.define.disable();
      this.changeDocumentForm.controls.text.disable();
      this.changeDocumentForm.controls.reference1.disable();
      this.changeDocumentForm.controls.bankAccountNo.disable();
    } else {
      this.checkChangeDocument = false;
      this.changeDocumentForm.controls.dateBaseLine.enable();
      this.changeDocumentForm.controls.paymentBlock.enable();
      this.changeDocumentForm.controls.define.enable();
      this.changeDocumentForm.controls.text.enable();
      this.changeDocumentForm.controls.reference1.enable();

      this.checkApUserChgBankAccNo(this.docType);
    }
    this.calculateTotalPrice();
    this.calculateTax();
    this.calculateFee();
    this.calculateNetPrice();
  }

  checkApUserChgBankAccNo(documentType) {
    const data = {
      documentType,
      webInfo: this.localStorageService.getWebInfo(),
    };
    this.masterService.checkApUserChgBankAccNo(data).then((result) => {
      console.log(data);
      if (result.status === 200) {
        if (result.data.length > 0) {
          this.changeDocumentForm.controls.bankAccountNo.enable();
        } else {
          this.changeDocumentForm.controls.bankAccountNo.disable();
        }
      } else {
        this.changeDocumentForm.controls.bankAccountNo.disable();
        // this.listValidate.push('ไม่พบเอกสาร');
      }
    });
  }

  calculateTotalPrice() {
    let totalPrice = new Decimal(0);
    this.totalPrice = new Decimal(0);
    this.listDocument.forEach((document) => {
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

  updateLineVendor() {
    const form = this.changeDocumentForm.getRawValue();
    console.log(form);
    let stringDate = null;
    if (form.dateBaseLine) {
      const dayDate = form.dateBaseLine.getDate();
      const monthDate = form.dateBaseLine.getMonth() + 1;
      const yearDate = form.dateBaseLine.getFullYear();
      stringDate = this.utils.parseDate(dayDate, monthDate, yearDate);
    }
    const payload = {
      compCode: form.departmentCodeFrom,
      accountDocNo: form.documentNo,
      fiscalYear: this.utils.convertYearToAD(form.fiscalYear),
      paymentBlock: form.paymentBlock,
      assignment: form.define,
      lineDesc: form.text,
      reference1: form.reference1,
      bankAccNo: form.bankAccountNo,
      dateBaseLine: stringDate,
      bankBranchNo: this.routingNo,
      flag: 1,
      webInfo: this.localStorageService.getWebInfo(),
    };
    console.log(payload);

    this.fiService.validateUpdateLineVendor(payload).then((result) => {
      console.log(result);
      if (result.status === 200) {
        if (result.data) {
          const dialogRefValidate = this.dialog.open(DialogResultComponent, {
            data: {
              textError: result.data,
              type: 'ERROR_DIALOG',
            },
          });
        } else {
          const dialogRef = this.dialog.open(DialogConfirmChangeDocumentComponent, {
            disableClose: true,
            width: 'auto',
            data: {
              payload,
              oldValueTemp: this.oldValueTemp,
            },
          });
          dialogRef.afterClosed().subscribe((result) => {
            if (result.event === 'save') {
              this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
                panelClass: '_success',
              });
              this.searchDetailDocument();
            }
          });
        }
      } else if (result.status === 422) {
        this.listValidate = result.error.data.errors;
      }
    });
  }

  openDialogUploadFile() {
    const dialogRef = this.dialog.open(DialogUploadChangeDocumentComponent, {
      disableClose: true,
      width: 'auto',
      data: {},
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event === 'save') {
        this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
          panelClass: '_success',
        });
      }
    });
  }
}
