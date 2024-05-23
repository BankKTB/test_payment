import { Component, ElementRef, OnInit, QueryList, ViewChild, ViewChildren } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatSort } from '@angular/material';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { Observable } from 'rxjs';

import { map, startWith } from 'rxjs/operators';
import { SelectionModel } from '@angular/cdk/collections';
import { Utils } from '@shared/utils/utils';
import { Constant } from '@shared/utils/constant';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogOmColumnTableComponent } from '@shared/component/dialog-om-column-table/dialog-om-column-table.component';
import { LoaderService, LocalStorageService, SidebarService } from '@core/services';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { DateAdapter, MAT_DATE_FORMATS } from '@angular/material/core';
import { DialogOmSearchCriteriaComponent } from '@shared/component/dialog-om-search-criteria/dialog-om-search-criteria.component';
import { DialogOmSaveSearchCriteriaComponent } from '@shared/component/dialog-om-save-search-criteria/dialog-om-save-search-criteria.component';
import { DialogOmSearchColumnTableComponent } from '@shared/component/dialog-om-search-column-table/dialog-om-search-column-table.component';
import { DialogOmChangePaymentBlockComponent } from '@shared/component/dialog-om-change-payment-block/dialog-om-change-payment-block.component';
import { DialogOmSaveColumnTableComponent } from '@shared/component/dialog-om-save-column-table/dialog-om-save-column-table.component';
import { DialogOmCompanyCodeComponent } from '@shared/component/dialog-om-company-code/dialog-om-company-code.component';
import { DialogOmPaymentMethodComponent } from '@shared/component/dialog-om-payment-method/dialog-om-payment-method.component';
import { DialogOmDocTypeComponent } from '@shared/component/dialog-om-doc-type/dialog-om-doc-type.component';
import { UserProfile } from '@core/models/user-profile';
import { DialogResultComponent } from '@shared/component/dialog-result/dialog-result.component';
import { Om } from '@core/models/payment/om';
import { DialogOmVendorComponent } from '@shared/component/dialog-om-vendor/dialog-om-vendor.component';
import { DialogOmPaymentCenterComponent } from '@shared/component/dialog-om-payment-center/dialog-om-payment-center.component';
import { DialogOmSpecialTypeComponent } from '@shared/component/dialog-om-special-type/dialog-om-special-type.component';
import { DialogCriteriaComponent } from '@shared/component/dialog-criteria/dialog-criteria.component';
import { DialogOmDocumentNoComponent } from '@shared/component/dialog-om-document-no/dialog-om-document-no.component';
import { DialogOmHeaderReferenceComponent } from '@shared/component/dialog-om-header-reference/dialog-om-header-reference.component';
import { DialogOmInputReasonUnapprovedComponent } from '@shared/component/dialog-om-input-reason-unapproved/dialog-om-input-reason-unapproved.component';
import { DialogShowDocumentReferenceComponent } from '@shared/component/dialog-show-document-reference/dialog-show-document-reference.component';
import { FiService } from '@core/services/fi/fi.service';

@Component({
  selector: 'app-om-cgd',
  templateUrl: './om-cgd.component.html',
  styleUrls: ['./om-cgd.component.scss'],
})
export class OmCgdComponent implements OnInit {
  @ViewChildren('filterTable') filterTable; // accessing the reference element
  @ViewChildren('reasonInput') reasonInput: QueryList<ElementRef>;
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  @ViewChild(MatPaginator, { static: true }) paginator: MatPaginator;
  dataSource = new MatTableDataSource([]);
  selectionApprove = new SelectionModel<any>(true, []);
  selectionNotApprove = new SelectionModel<any>(true, []);

  isShowSearchData = true;

  filteredOptionsPaymentFrom: Observable<string[]>;
  filteredOptionsPaymentTo: Observable<string[]>;

  filteredOptionsSpecialFrom: Observable<string[]>;
  filteredOptionsSpecialTo: Observable<string[]>;

  filteredOptionsDocTypeFrom: Observable<string[]>;
  filteredOptionsDocTypeTo: Observable<string[]>;

  filteredOptionsDocumentNoFrom: Observable<string[]>;
  filteredOptionsDocumentNoTo: Observable<string[]>;

  button = 'Submit';

  tableForm: FormGroup;
  documentTypeControl: FormControl;
  compCodeControl: FormControl;
  dateDocControl: FormControl;
  dateAcctControl: FormControl;
  amountControl: FormControl;
  invoiceDocumentNoControl: FormControl;
  originalDocumentNoControl: FormControl;
  originalFiscalYearControl: FormControl;
  paymentMethodControl: FormControl;
  headerReferenceControl: FormControl;
  userPostControl: FormControl;
  fiAreaControl: FormControl;
  costCenterControl: FormControl;
  fundSourceControl: FormControl;
  bgCodeControl: FormControl;
  assignmentControl: FormControl;
  brLineControl: FormControl;
  subAccountControl: FormControl;
  subAccountOwnerControl: FormControl;
  depositAccountControl: FormControl;
  depositAccountOwnerControl: FormControl;
  lineItemTextControl: FormControl;
  specialGLControl: FormControl;
  dateBaseLineControl: FormControl;
  reference2Control: FormControl;
  poDocumentNoControl: FormControl;
  ////
  vendorNameControl: FormControl;
  vendorCodeControl: FormControl;
  bankKeyControl: FormControl;
  bankAccountNoControl: FormControl;
  bankAccountHolderNameControl: FormControl;
  fundTypeControl: FormControl;
  ///
  approveControl: FormControl;
  notApproveControl: FormControl;
  confirmSellerControl: FormControl;
  botControl: FormControl;
  reasonControl: FormControl;
  iconStatusControl: FormControl;
  frStatusControl: FormControl;
  textStatusControl: FormControl;
  errWtInfoControl: FormControl;
  deductControl: FormControl;
  partnerBankTypeControl: FormControl;
  depositMoneyControl: FormControl;
  wareHouseNoControl: FormControl;

  columnTable = [{ key: null, columnName: null, showColumn: null, seq: null }];

  listCompanyCode = [
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
  ];
  listDocType = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listPaymentMethod = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listVendor = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listDisbursementCode = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listSpecialType = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listPostDate = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listDocumentDate = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listDocumentCreateDate = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listDocumentNo = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listHeaderReference = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  optionExcludeCompanyCode = 'N';
  optionExcludeDocType = 'N';
  optionExcludePaymentMethod = 'N';
  optionExcludeVendor = 'N';
  optionExcludeDisbursementCode = 'N';
  optionExcludeSpecialType = 'N';
  optionExcludeDocumentNo = 'N';
  optionExcludeHeaderReference = 'N';

  panelExpanded = true;
  panelExpanded1 = true;
  panelExpanded2 = true;

  departmentCodeFromBindingName = ''; // รหัสหน่วยงาน
  departmentCodeToBindingName = ''; // รหัสหน่วยงาน
  provinceCodeFromBindingName = ''; // รหัสจังหวัด
  provinceCodeToBindingName = ''; // รหัสจังหวัด
  vendorTaxIdFromBindingName = ''; // ผุ้ขาย
  vendorTaxIdToBindingName = ''; // ผุ้ขาย
  disbursementCodeFromBindingName = ''; // รหัสหน่วยเบิกจ่าย
  disbursementCodeToBindingName = ''; // รหัสหน่วยเบิกจ่าย

  omFormCreate: FormGroup;
  departmentCodeFromControl: FormControl; // รหัสหน่วยงาน
  departmentCodeToControl: FormControl; // รหัสหน่วยงาน
  provinceCodeFromControl: FormControl; // รหัสจังหวัด
  provinceCodeToControl: FormControl; // รหัสจังหวัด
  yearAccountControl: FormControl; // ปีบัญชี
  postDateFromControl: FormControl; // วันผ่านรายการ
  postDateToControl: FormControl; // วันผ่านรายการ
  vendorTaxIdFromControl: FormControl; // ผุ้ขาย
  vendorTaxIdToControl: FormControl; // ผุ้ขาย
  disbursementCodeFromControl: FormControl; // รหัสหน่วยเบิกจ่าย
  disbursementCodeToControl: FormControl; // รหัสหน่วยเบิกจ่าย

  docTypeFromControl: FormControl; // ประเภทเอกสาร
  docTypeToControl: FormControl; // ประเภทเอกสาร
  payMethodFromControl: FormControl; // วิธีชำระเงิน
  payMethodToControl: FormControl; // วิธีชำระเงิน
  documentDateFromControl: FormControl; // วันที่เอกสาร
  documentDateToControl: FormControl; // วันที่เอกสาร
  documentCreateDateFromControl: FormControl; // วันที่บันทึก
  documentCreateDateToControl: FormControl; // วันที่บันทึก
  specialTypeFromControl: FormControl; // แยกประเภทพิเศษ
  specialTypeToControl: FormControl; // แยกประเภทพิเศษ
  documentNoFromControl: FormControl; // เลขที่เอกสาร
  documentNoToControl: FormControl; // เลขที่เอกสาร
  headerReferenceFromControl: FormControl; // อ้างอิง
  headerReferenceToControl: FormControl; // อ้างอิง

  outlineControl: FormControl; // โครงร่าง

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listDocumentForCheckRelation = [];
  listValidate = [];
  listSearchValidate = [];
  listMessageResponse = [];
  role = 'USER_CGD';
  dataCriteria = '';
  dataColumn = '';

  isDisablePayMethod = false;

  currentDate = new Date();

  userProfile: UserProfile = null;

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    private constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService,
    private localStorageService: LocalStorageService,
    private fiService: FiService
  ) {}

  ngOnInit() {
    this.currentDate.setHours(0);
    this.currentDate.setMinutes(0);
    this.currentDate.setSeconds(0);
    this.currentDate.setMilliseconds(0);

    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('omCgd');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

    this.userProfile = this.localStorageService.getUserProfile();

    this.createFormControl();
    this.createFormGroup();

    this.createTableFormControl();
    this.createTableFormGroup();

    if (this.role === 'USER_PTO') {
      this.defaultOmFormForArea();
    } else if (this.role === 'USER_CGD') {
      this.defaultOmFormForCGD();
    } else if (this.role === 'USER_PTO_H') {
      this.defaultOmFormForAreaTreatMent();
    }

    this.filteredOptionsPaymentFrom = this.payMethodFromControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter(value))
    );

    this.filteredOptionsPaymentTo = this.payMethodToControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter(value))
    );

    this.filteredOptionsSpecialFrom = this.specialTypeFromControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter2(value))
    );

    this.filteredOptionsSpecialTo = this.specialTypeToControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter2(value))
    );

    this.filteredOptionsDocTypeFrom = this.docTypeFromControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter3(value))
    );

    this.filteredOptionsDocumentNoFrom = this.documentNoFromControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter2(value))
    );

    this.filteredOptionsDocumentNoTo = this.documentNoToControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter2(value))
    );
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
    console.log(this.sort);
    console.log(this.paginator);
    console.log(this.dataSource);
  }

  getDisplayedColumns() {
    this.columnTable.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    return this.columnTable.filter((cd) => cd.showColumn).map((cd) => cd.key);
  }

  ngAfterViewInit() {}

  createFormControl() {
    console.log(this.utils.fiscYear);
    this.departmentCodeFromControl = this.formBuilder.control('', [Validators.required]); // รหัสหน่วยงาน
    this.departmentCodeToControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.provinceCodeFromControl = this.formBuilder.control('', [Validators.required]); // รหัสจังหวัด
    this.provinceCodeToControl = this.formBuilder.control(''); // รหัสจังหวัด
    this.yearAccountControl = this.formBuilder.control(this.utils.fiscYear, [Validators.required]); // ปีบัญชี
    this.postDateFromControl = this.formBuilder.control(''); // วันผ่านรายการ
    this.postDateToControl = this.formBuilder.control(''); // วันผ่านรายการ
    this.vendorTaxIdFromControl = this.formBuilder.control(''); // ผุ้ขาย
    this.vendorTaxIdToControl = this.formBuilder.control(''); // ผุ้ขาย
    this.disbursementCodeFromControl = this.formBuilder.control(''); // รหัสหน่วยเบิกจ่าย
    this.disbursementCodeToControl = this.formBuilder.control(''); // รหัสหน่วยเบิกจ่าย

    this.docTypeFromControl = this.formBuilder.control(''); // ประเภทเอกสาร
    this.docTypeToControl = this.formBuilder.control(''); // ประเภทเอกสาร
    this.payMethodFromControl = this.formBuilder.control(''); // วิธีชำระเงิน
    this.payMethodToControl = this.formBuilder.control(''); // วิธีชำระเงิน
    this.documentDateFromControl = this.formBuilder.control(''); // วันที่เอกสาร
    this.documentDateToControl = this.formBuilder.control(''); // วันที่เอกสาร
    this.documentCreateDateFromControl = this.formBuilder.control(''); // วันที่บันทึก
    this.documentCreateDateToControl = this.formBuilder.control(''); // วันที่บันทึก
    this.specialTypeFromControl = this.formBuilder.control(''); // แยกประเภทพิเศษ
    this.specialTypeToControl = this.formBuilder.control(''); // แยกประเภทพิเศษ
    this.documentNoFromControl = this.formBuilder.control(''); // เลขที่เอกสาร
    this.documentNoToControl = this.formBuilder.control(''); // เลขที่เอกสาร
    this.headerReferenceFromControl = this.formBuilder.control(''); // อ้างอิง
    this.headerReferenceToControl = this.formBuilder.control(''); // อ้างอิง
    this.outlineControl = this.formBuilder.control(''); // โครงร่าง
  }

  createFormGroup() {
    this.omFormCreate = this.formBuilder.group({
      departmentCodeFrom: this.departmentCodeFromControl, // รหัสหน่วยงาน
      departmentCodeTo: this.departmentCodeToControl, // รหัสหน่วยงาน
      provinceCodeFrom: this.provinceCodeFromControl, // รหัสจังหวัด
      provinceCodeTo: this.provinceCodeToControl, // รหัสจังหวัด
      yearAccount: this.yearAccountControl, // ปีบัญชี
      postDateFrom: this.postDateFromControl, // วันผ่านรายการ
      postDateTo: this.postDateToControl, // วันผ่านรายการ
      vendorTaxIdFrom: this.vendorTaxIdFromControl, // ผุ้ขาย
      vendorTaxIdTo: this.vendorTaxIdToControl, // ผุ้ขาย
      disbursementCodeFrom: this.disbursementCodeFromControl, // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: this.disbursementCodeToControl, // รหัสหน่วยเบิกจ่าย

      docTypeFrom: this.docTypeFromControl, // ประเภทเอกสาร
      docTypeTo: this.docTypeToControl, // ประเภทเอกสาร
      payMethodFrom: this.payMethodFromControl, // วิธีชำระเงิน
      payMethodTo: this.payMethodToControl, // วิธีชำระเงิน
      documentDateFrom: this.documentDateFromControl, // วันที่เอกสาร
      documentDateTo: this.documentDateToControl, // วันที่เอกสาร
      documentCreateDateFrom: this.documentCreateDateFromControl, // วันที่บันทึก
      documentCreateDateTo: this.documentCreateDateToControl, // วันที่บันทึก
      specialTypeFrom: this.specialTypeFromControl, // แยกประเภทพิเศษ
      specialTypeTo: this.specialTypeToControl, // แยกประเภทพิเศษ
      documentNoFrom: this.documentNoFromControl, // แยกประเภทพิเศษ
      documentNoTo: this.documentNoToControl, // แยกประเภทพิเศษ
      headerReferenceFrom: this.headerReferenceFromControl, // อ้างอิง
      headerReferenceTo: this.headerReferenceToControl, // อ้างอิง

      outline: this.outlineControl, // โครงร่าง
    });
  }

  createTableFormControl() {
    this.documentTypeControl = this.formBuilder.control(true);
    // this.compCodeControl = this.formBuilder.control(true);
    this.dateDocControl = this.formBuilder.control(true);
    this.dateAcctControl = this.formBuilder.control(true);
    this.amountControl = this.formBuilder.control(true);
    this.invoiceDocumentNoControl = this.formBuilder.control(true);
    this.originalDocumentNoControl = this.formBuilder.control(true);
    this.originalFiscalYearControl = this.formBuilder.control(true);
    this.paymentMethodControl = this.formBuilder.control(true);
    this.headerReferenceControl = this.formBuilder.control(true);
    this.userPostControl = this.formBuilder.control(true);
    this.fiAreaControl = this.formBuilder.control(true);
    this.costCenterControl = this.formBuilder.control(true);
    this.fundSourceControl = this.formBuilder.control(true);
    this.bgCodeControl = this.formBuilder.control(true);
    this.assignmentControl = this.formBuilder.control(true);
    this.brLineControl = this.formBuilder.control(false);
    this.subAccountControl = this.formBuilder.control(false);
    this.subAccountOwnerControl = this.formBuilder.control(false);
    this.depositAccountControl = this.formBuilder.control(false);
    this.depositAccountOwnerControl = this.formBuilder.control(false);
    this.lineItemTextControl = this.formBuilder.control(false);
    this.specialGLControl = this.formBuilder.control(false);
    this.dateBaseLineControl = this.formBuilder.control(false);
    this.reference2Control = this.formBuilder.control(false);
    this.poDocumentNoControl = this.formBuilder.control(false);
    // ////
    this.vendorNameControl = this.formBuilder.control(true);
    this.vendorCodeControl = this.formBuilder.control(true);
    this.bankKeyControl = this.formBuilder.control(true);
    this.bankAccountNoControl = this.formBuilder.control(true);
    this.bankAccountHolderNameControl = this.formBuilder.control(true);
    this.fundTypeControl = this.formBuilder.control(true);
    // ///
    this.approveControl = this.formBuilder.control(true);
    this.notApproveControl = this.formBuilder.control(true);
    this.confirmSellerControl = this.formBuilder.control(true);
    this.botControl = this.formBuilder.control(true);
    this.reasonControl = this.formBuilder.control(true);
    this.iconStatusControl = this.formBuilder.control(true);
    this.frStatusControl = this.formBuilder.control(true);

    this.textStatusControl = this.formBuilder.control(true);
    this.errWtInfoControl = this.formBuilder.control(true);
    this.deductControl = this.formBuilder.control(true);
    this.partnerBankTypeControl = this.formBuilder.control(true);
    this.depositMoneyControl = this.formBuilder.control(true);
    this.wareHouseNoControl = this.formBuilder.control(true);
  }

  createTableFormGroup() {
    this.tableForm = this.formBuilder.group({
      documentType: this.documentTypeControl,
      // compCode: this.compCodeControl,
      dateDoc: this.dateDocControl,
      dateAcct: this.dateAcctControl,
      amount: this.amountControl,
      invoiceDocumentNo: this.invoiceDocumentNoControl,
      originalDocumentNo: this.originalDocumentNoControl,
      originalFiscalYear: this.originalFiscalYearControl,
      paymentMethod: this.paymentMethodControl,
      headerReference: this.headerReferenceControl,
      userPost: this.userPostControl,
      fiArea: this.fiAreaControl,
      costCenter: this.costCenterControl,
      fundSource: this.fundSourceControl,
      bgCode: this.bgCodeControl,
      assignment: this.assignmentControl,
      brLine: this.brLineControl,
      subAccount: this.subAccountControl,
      subAccountOwner: this.subAccountOwnerControl,
      depositAccount: this.depositAccountControl,
      depositAccountOwner: this.depositAccountOwnerControl,
      lineItemText: this.lineItemTextControl,

      specialGL: this.specialGLControl,
      dateBaseLine: this.dateBaseLineControl,
      reference2: this.reference2Control,
      poDocumentNo: this.poDocumentNoControl,
      // ////
      vendorName: this.vendorNameControl,
      vendorCode: this.vendorCodeControl,
      bankKey: this.bankKeyControl,
      bankAccountNo: this.bankAccountNoControl,
      bankAccountHolderName: this.bankAccountHolderNameControl,
      fundType: this.fundTypeControl,
      // ///
      approve: this.approveControl,
      notApprove: this.notApproveControl,
      confirmSeller: this.confirmSellerControl,
      bot: this.botControl,
      reason: this.reasonControl,
      iconStatus: this.iconStatusControl,
      frStatus: this.frStatusControl,

      textStatus: this.textStatusControl,
      errWtInfo: this.errWtInfoControl,
      deduct: this.deductControl,
      partnerBankType: this.partnerBankTypeControl,
      depositMoney: this.depositMoneyControl,
      wareHouseNo: this.wareHouseNoControl,
    });
    setTimeout(() => {
      this.columnTable = [];
      this.columnTable.push({
        key: 'list',
        columnName: 'รายการ',
        showColumn: true,
        seq: 0,
      });
      for (const key in this.tableForm.controls) {
        const data = this.constant.LIST_TABLE_OM.get(key);
        console.log(data);
        this.columnTable.push({
          key,
          columnName: data.name,
          showColumn: this.tableForm.controls[key].value,
          seq: data.seq,
        });
      }
    }, 1000); // Execute something() 1 second later.
  }

  onSearch() {
    const form = this.omFormCreate.value;

    this.listSearchValidate = [];
    this.listValidate = [];
    if (!form.departmentCodeFrom) {
      this.listSearchValidate.push('กรุณาระบุรหัสหน่วยงาน');
      // this.omFormCreate.controls.departmentCodeFrom.setValidators([Validators.required]);
      // this.omFormCreate.controls.departmentCodeFrom.updateValueAndValidity();
    }
    if (!form.provinceCodeFrom) {
      this.listSearchValidate.push('กรุณาระบุรหัสจังหวัด');
      // this.omFormCreate.controls.provinceCodeFrom.setValidators([Validators.required]);
      // this.omFormCreate.controls.provinceCodeFrom.updateValueAndValidity();
    }
    if (!form.yearAccount) {
      this.listSearchValidate.push('กรุณาระบุปีบัญชี');
      // this.omFormCreate.controls.fiscalYear.setValidators([Validators.required]);
      // this.omFormCreate.controls.fiscalYear.updateValueAndValidity();
    }
    if (form.provinceCodeTo) {
      this.listSearchValidate.push('กรุณาระบุรหัสจังหวัด 1 จังหวัด');
      this.omFormCreate.controls.provinceCodeTo.setValidators([Validators.maxLength(0)]);
      this.omFormCreate.controls.provinceCodeTo.updateValueAndValidity();
    }
    this.isSubmitedForm = true;
    if (this.listSearchValidate.length <= 0) {
      this.isSubmitedForm = false;

      const payload = {
        compCodeFrom: '',
        compCodeTo: '',
        fiAreaFrom: '',
        fiAreaTo: '',
        fiscalYear: '',
        dateAcctFrom: '',
        dateAcctTo: '',
        bpartnerFrom: '',
        bpartnerTo: '',
        paymentCenterFrom: '',
        paymentCenterTo: '',
        docTypeFrom: '',
        docTypeTo: '',
        paymentMethodFrom: '',
        paymentMethodTo: '',
        dateDocFrom: '',
        dateDocTo: '',
        dateCreatedFrom: '',
        dateCreatedTo: '',
        specialGlFrom: '',
        specialGlTo: '',
        outline: '',
        documentNoFrom: '',
        documentNoTo: '',
        headerReferenceFrom: '',
        headerReferenceTo: '',
        listCompanyCode: this.listCompanyCode,
        listDocType: this.listDocType,
        listPaymentMethod: this.listPaymentMethod,
        vendor: this.listVendor,
        paymentCenter: this.listDisbursementCode,
        specialType: this.listSpecialType,
        listPostDate: this.listPostDate,
        listDocumentDate: this.listDocumentDate,
        listDocumentCreateDate: this.listDocumentCreateDate,
        listDocumentNo: this.listDocumentNo,
        listHeaderReference: this.listHeaderReference,
      };
      // วันผ่านรายการ
      let postDateFrom = '';
      let postDateTo = '';
      if (form.postDateFrom) {
        const dayPostDateFrom = form.postDateFrom.getDate();
        const monthPostDateFrom = form.postDateFrom.getMonth() + 1;
        const yearPostDateFrom = form.postDateFrom.getFullYear();
        postDateFrom = this.utils.parseDate(dayPostDateFrom, monthPostDateFrom, yearPostDateFrom);
        this.listPostDate[0].from = postDateFrom;
      }
      if (form.postDateTo) {
        const dayPostDateTo = form.postDateTo.getDate();
        const monthPostDateTo = form.postDateTo.getMonth() + 1;
        const yearPostDateTo = form.postDateTo.getFullYear();
        postDateTo = this.utils.parseDate(dayPostDateTo, monthPostDateTo, yearPostDateTo);
        this.listPostDate[0].to = postDateTo;
      }
      this.listPostDate = this.listPostDate.filter((e, index) => e.from || index === 0);
      payload.listPostDate = this.listPostDate;
      // วันที่เอกสาร
      let documentDateFrom = '';
      let documentDateTo = '';
      if (form.documentDateFrom) {
        const dayDocumentDateFrom = form.documentDateFrom.getDate();
        const monthDocumentDateFrom = form.documentDateFrom.getMonth() + 1;
        const yearDocumentDateFrom = form.documentDateFrom.getFullYear();
        documentDateFrom = this.utils.parseDate(
          dayDocumentDateFrom,
          monthDocumentDateFrom,
          yearDocumentDateFrom
        );
        this.listDocumentDate[0].from = documentDateFrom;
      }

      if (form.documentDateTo) {
        const dayDocumentDateTo = form.documentDateTo.getDate();
        const monthDocumentDateTo = form.documentDateTo.getMonth() + 1;
        const yearDocumentDateTo = form.documentDateTo.getFullYear();

        documentDateTo = this.utils.parseDate(
          dayDocumentDateTo,
          monthDocumentDateTo,
          yearDocumentDateTo
        );
        this.listDocumentDate[0].to = documentDateTo;
      }
      this.listDocumentDate = this.listDocumentDate.filter((e, index) => e.from || index === 0);
      payload.listDocumentDate = this.listDocumentDate;
      // วันที่บันทึก
      let documentCreateDateFrom = '';
      let documentCreateDateTo = '';
      if (form.documentCreateDateFrom) {
        const dayDocumentCreateDateFrom = form.documentCreateDateFrom.getDate();
        const monthDocumentCreateDateFrom = form.documentCreateDateFrom.getMonth() + 1;
        const yearDocumentCreateDateFrom = form.documentCreateDateFrom.getFullYear();
        documentCreateDateFrom = this.utils.parseDate(
          dayDocumentCreateDateFrom,
          monthDocumentCreateDateFrom,
          yearDocumentCreateDateFrom
        );
        this.listDocumentCreateDate[0].from = documentCreateDateFrom;
      }
      if (form.documentCreateDateTo) {
        const dayDocumentCreateDateTo = form.documentCreateDateTo.getDate();
        const monthDocumentCreateDateTo = form.documentCreateDateTo.getMonth() + 1;
        const yearDocumentCreateDateTo = form.documentCreateDateTo.getFullYear();
        documentCreateDateTo = this.utils.parseDate(
          dayDocumentCreateDateTo,
          monthDocumentCreateDateTo,
          yearDocumentCreateDateTo
        );
        this.listDocumentCreateDate[0].to = documentCreateDateTo;
      }
      this.listDocumentCreateDate = this.listDocumentCreateDate.filter(
        (e, index) => e.from || index === 0
      );
      payload.listDocumentCreateDate = this.listDocumentCreateDate;

      payload.compCodeFrom = form.departmentCodeFrom; // รหัสหน่วยงาน
      payload.compCodeTo = form.departmentCodeTo; // รหัสหน่วยงาน
      payload.fiAreaFrom = form.provinceCodeFrom; // รหัสจังหวัด
      payload.fiAreaTo = form.provinceCodeTo; // รหัสจังหวัด
      payload.fiscalYear = form.yearAccount ? this.utils.convertYearToAD(form.yearAccount) : ''; // ปีบัญชี
      payload.dateAcctFrom = postDateFrom; // วันผ่านรายการ
      payload.dateAcctTo = postDateTo; // วันผ่านรายการ
      payload.bpartnerFrom = form.vendorTaxIdFrom; // ผุ้ขาย
      payload.bpartnerTo = form.vendorTaxIdTo; // ผุ้ขาย
      payload.paymentCenterFrom = form.disbursementCodeFrom; // รหัสหน่วยเบิกจ่าย
      payload.paymentCenterTo = form.disbursementCodeTo; // รหัสหน่วยเบิกจ่าย

      payload.docTypeFrom = form.docTypeFrom; // ประเภทเอกสาร
      payload.docTypeTo = form.docTypeTo; // ประเภทเอกสาร
      payload.paymentMethodFrom = form.payMethodFrom; // วิธีชำระเงิน
      payload.paymentMethodTo = form.payMethodTo; // วิธีชำระเงิน
      payload.dateDocFrom = documentDateFrom; // วันที่เอกสาร
      payload.dateDocTo = documentDateTo; // วันที่เอกสาร
      payload.dateCreatedFrom = documentCreateDateFrom; // วันที่บันทึก
      payload.dateCreatedTo = documentCreateDateTo; // วันที่บันทึก
      payload.specialGlFrom = form.specialTypeFrom; // แยกประเภทพิเศษ
      payload.specialGlTo = form.specialTypeTo; // แยกประเภทพิเศษ
      payload.documentNoFrom = form.documentNoFrom; // เลขที่เอกสาร
      payload.documentNoTo = form.documentNoTo; // เลขที่เอกสาร
      payload.headerReferenceFrom = form.headerReferenceFrom; // อ้างอิง
      payload.headerReferenceTo = form.headerReferenceTo; // อ้างอิง
      payload.outline = form.outline; // โครงร่าง
      this.paymentBlockService
        .searchColumnByRoleAndName(this.role, form.outline)
        .then((data) => {
          const response = data as any;
          if (response.status === 200) {
            this.columnTable = JSON.parse(response.data.jsonText) as [];
          }
        })
        .then(() => {
          this.search(payload);
        });
    }
  }

  search(payload) {
    console.log(payload);
    this.listSearchValidate = [];
    // this.loadingScreenService.loadingToggleStatus(true)
    this.dataSource = new MatTableDataSource([]);
    this.paymentBlockService.search(payload).then((data) => {
      console.log(data);
      // this.loadingScreenService.loadingToggleStatus(false)
      // this.isDataSearchloaded = true;
      const response = data.data as any;

      console.log(response);
      if (response.documentForDisplay && response.documentForDisplay.length > 0) {
        this.dataSource = new MatTableDataSource(response.documentForDisplay);
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
        if (response.documentForCheckRelation && response.documentForCheckRelation.length > 0) {
          this.listDocumentForCheckRelation = response.documentForCheckRelation;
        }
        this.isShowSearchData = false;
        // this.selectedTabIndex = 1;
        let i = 1;
        this.dataSource.data.forEach((document) => {
          document.list = i++;
          document.approve = false;
          document.notApprove = false;
          document.reason = '';
          document.invoiceDocumentNo = document.lineInvoiceDocumentNo;
          document.lineItemText = document.lineDesc;

          const dateBaseLine = new Date(document.dateBaseLine);

          if (dateBaseLine.getTime() === this.currentDate.getTime()) {
            document.iconStatus = '1';
          } else if (dateBaseLine.getTime() > this.currentDate.getTime()) {
            document.iconStatus = '2';
          } else if (dateBaseLine.getTime() < this.currentDate.getTime()) {
            document.iconStatus = '3';
          }
          document.statusText = this.renderStatusDescription(document);

          if (document.paymentReference != null && document.paymentReference.startsWith('001')) {
            document.bot = true;
          } else {
            document.bot = false;
          }

          if (document.paymentReference != null) {
            document.bankKey = document.paymentReference.substr(0, 7);
          }
        });
      } else {
        this.isShowSearchData = true;
        this.listSearchValidate.push('ไม่พบรายการตามเงื่อนไข');
        // this.listMessageResponse.push('ไม่พบเอกสาร');
      }
      setTimeout(() => {
        document.getElementById('table-found').scrollIntoView({
          behavior: 'smooth',
        });
      }, 100);
    });
  }

  _filter(value: string): string[] {
    const filterValue = value && value.toLowerCase();

    return this.constant.PAYMENT_METHOD.filter((option) =>
      option.valueCode.toLowerCase().includes(filterValue)
    );
  }

  _filter2(value: string): string[] {
    console.log(value);
    const filterValue = value && value.toLowerCase();

    return this.constant.PAYMENT_SPECIAL_GL.filter((option) =>
      option.valueCode.toLowerCase().includes(filterValue)
    );
  }

  _filter3(value: string): string[] {
    console.log(value);
    const filterValue = value && value.toLowerCase();

    return this.constant.DOC_TYPE.filter((option) =>
      option.name.toLowerCase().includes(filterValue)
    );
  }

  openDialogDetailDocument(document) {
    const url =
      './detail-fi-kb?compCode=' +
      document.companyCode +
      '&docNo=' +
      document.originalDocumentNo +
      '&docYear=' +
      document.originalFiscalYear;
    // window.open(url, 'name', 'width=1200,height=700');
    window.open(url, '_blank');
    // const dialogRef = this.dialog.open(DialogDetailDocumentComponent, {
    //   width: '1200px',
    //   data: {
    //     document,
    //   },
    // });
    // dialogRef.afterClosed().subscribe((result) => {
    //   if (result) {
    //   }
    // });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.omFormCreate.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
          this.listCompanyCode[0].companyCodeFrom = result.value;
          this.departmentCodeFromBindingName = result.name;
        } else if (type === 'departmentCodeTo') {
          this.listCompanyCode[0].companyCodeTo = result.value;
          this.departmentCodeToBindingName = result.name;
        } else if (type === 'provinceCodeFrom') {
          this.provinceCodeFromBindingName = result.name;
        } else if (type === 'provinceCodeTo') {
          this.provinceCodeToBindingName = result.name;
        } else if (type === 'vendorTaxIdFrom') {
          this.listVendor[0].from = result.value;
          this.vendorTaxIdFromBindingName = result.name;
        } else if (type === 'vendorTaxIdTo') {
          this.listVendor[0].to = result.value;
          this.vendorTaxIdToBindingName = result.name;
        } else if (type === 'disbursementCodeFrom') {
          this.listDisbursementCode[0].from = result.value;
          this.disbursementCodeFromBindingName = result.name;
        } else if (type === 'disbursementCodeTo') {
          this.listDisbursementCode[0].to = result.value;
          this.disbursementCodeToBindingName = result.name;
        } else if (type === 'payMethodFrom') {
          this.listPaymentMethod[0].from = result.value;
        } else if (type === 'payMethodTo') {
          this.listPaymentMethod[0].to = result.value;
        } else if (type === 'specialTypeFrom') {
          this.listSpecialType[0].from = result.value;
        } else if (type === 'specialTypeTo') {
          this.listSpecialType[0].to = result.value;
        } else if (type === 'docTypeFrom') {
          this.listDocType[0].from = result.value;
        } else if (type === 'docTypeTo') {
          this.listDocType[0].to = result.value;
        }
      }
    });
  }

  openCollapseDetail() {
    this.isOpenCollapseDetail = !this.isOpenCollapseDetail;
  }

  checkboxLabelApproved(row?: any): string {
    return `${this.selectionApprove.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  checkboxLabelNotApproved(row?: any): string {
    return `${this.selectionNotApprove.isSelected(row) ? 'deselect' : 'select'} row ${
      row.position + 1
    }`;
  }

  setReasonTextArea(row, index, event) {
    console.log('setReasonTextArea', row, index, event.target.value);
    this.dataSource.data.find((res) => {
      if (
        res.originalDocumentNo === row.originalDocumentNo &&
        res.companyCode === row.companyCode &&
        res.originalFiscalYear === row.originalFiscalYear
      ) {
        res.reason = event.target.value;
      }
    });
  }

  setReason(row, index, event) {
    const reasonInput = this.reasonInput
      .toArray()
      .filter((value) => value.nativeElement.id === event.target.id)[0].nativeElement.value;

    console.log(
      this.dataSource.data.find((res) => {
        if (
          res.originalDocumentNo === row.originalDocumentNo &&
          res.companyCode === row.companyCode &&
          res.originalFiscalYear === row.originalFiscalYear
        ) {
          res.reason = reasonInput;
        }
      })
    );
  }

  onClickCheckedDocument(event, type, row, index) {
    console.log(index);
    console.log(type);
    console.log(row);
    event.stopPropagation();
    this.dataSource.data.find((res) => {
      if (
        res.originalDocumentNo === row.originalDocumentNo &&
        res.companyCode === row.companyCode &&
        res.originalFiscalYear === row.originalFiscalYear
      ) {
        res.reason = '';
      }
    });
    console.log(this.selectionApprove.selected);
    if (type === 'approve') {
      this.selectionNotApprove.deselect(row);
    } else {
      this.selectionApprove.deselect(row);
    }
  }

  // onPreSave() {
  //   const listItem = [];
  //   this.listValidate = [];
  //   const webInfo = this.localStorageService.getWebInfo();
  //   this.dataSource.data.forEach((item) => {
  //     item.approve = false;
  //     item.notApprove = false;
  //     const data = {
  //       valueOld: 'B',
  //       valueNew: '',
  //       unblockDate: new Date(),
  //       companyCode: item.companyCode,
  //       fiArea: item.fiArea,
  //       paymentCenter: item.paymentCenter,
  //       documentType: item.documentType,
  //       originalFiscalYear: item.originalFiscalYear,
  //       originalDocumentNo: item.originalDocumentNo,
  //       dateAcct: item.dateAcct,
  //       paymentMethod: item.paymentMethod,
  //       amount: item.amount,
  //       wtxType: item.wtxType,
  //       wtxCode: item.wtxCode,
  //       wtxBase: item.wtxBase,
  //       wtxAmount: item.wtxAmount,
  //       wtxTypeP: item.wtxTypeP,
  //       wtxCodeP: item.wtxCodeP,
  //       wtxBaseP: item.wtxBaseP,
  //       wtxAmountP: item.wtxAmountP,
  //       vendor: item.vendor,
  //       vendorName: item.vendorName,
  //       userPost: item.userPost,
  //       userName: this.userProfile.userdata.username,
  //       reason: item.reason,
  //       approve: null,
  //       confirmVendor: item.confirmVendor,
  //       webInfo: this.localStorageService.getWebInfo(),
  //       canUnblock: true,
  //     };
  //     if (this.selectionApprove.isSelected(item)) {
  //       item.approve = this.selectionApprove.isSelected(item);
  //       data.valueNew = '';
  //       data.reason = null;
  //     } else if (this.selectionNotApprove.isSelected(item)) {
  //       item.notApprove = this.selectionNotApprove.isSelected(item);
  //       data.valueNew = 'E';
  //     }
  //
  //     if (item.approve) {
  //       data.approve = true;
  //     } else if (item.notApprove) {
  //       data.approve = false;
  //     }
  //     if (data.approve !== null) {
  //       listItem.push(data);
  //     }
  //   });
  //
  //   const validateText = [];
  //   listItem.forEach((data) => {
  //     if (!data.approve && !data.reason) {
  //       validateText.push(
  //         'เลขที่เอกสาร ' +
  //           data.originalDocumentNo +
  //           ' ' +
  //           data.companyCode +
  //           ' ' +
  //           data.originalFiscalYear +
  //           ' ไม่ได้ระบุเหตุผล'
  //       );
  //       data.canUnblock = false;
  //     }
  //     if (data.approve && !data.confirmVendor) {
  //       validateText.push(
  //         'เลขที่เอกสาร ' +
  //           data.originalDocumentNo +
  //           ' ' +
  //           data.companyCode +
  //           ' ' +
  //           data.originalFiscalYear +
  //           ' ไม่ได้ยืนยันผู้ขาย'
  //       );
  //       data.canUnblock = false;
  //     }
  //   });
  //
  //   // console.log(listItem);
  //   let statusCheckParent = 0;
  //
  //   if (listItem.length > 0) {
  //     this.paymentBlockService
  //       .findParent(listItem)
  //       .then((data) => {
  //         console.log('listCheck');
  //         console.log(data);
  //         const response = data;
  //         statusCheckParent = response.status;
  //         if (response.status === 200) {
  //           const listChild = response.data as any;
  //           if (listChild && listChild.length > 0) {
  //             listChild.forEach((item) => {
  //               const parentDoc = listItem.filter(
  //                 (obj) =>
  //                   obj.companyCode === item.companyCode &&
  //                   obj.originalDocumentNo === item.originalDocumentNo &&
  //                   obj.originalFiscalYear === item.originalFiscalYear
  //               );
  //
  //               if (parentDoc.length === 0) {
  //                 listItem.find(
  //                   (obj) =>
  //                     obj.companyCode === item.companyCode &&
  //                     obj.originalDocumentNo === item.lineInvoiceDocumentNo &&
  //                     obj.originalFiscalYear === item.lineInvoiceFiscalYear
  //                 ).canUnblock = false;
  //
  //                 validateText.push(
  //                   'เลขที่เอกสาร ' +
  //                     item.lineInvoiceDocumentNo +
  //                     ' ' +
  //                     item.companyCode +
  //                     ' ' +
  //                     item.lineInvoiceFiscalYear +
  //                     ' ต้องอนุมัติพร้อมกับเอกสาร ' +
  //                     item.documentType +
  //                     ' ' +
  //                     item.originalDocumentNo +
  //                     ' ' +
  //                     item.companyCode +
  //                     ' ' +
  //                     item.originalFiscalYear
  //                 );
  //               }
  //             });
  //           }
  //         }
  //       })
  //       .then(() => {
  //         if (statusCheckParent === 200) {
  //           // if (validateText.length <= 0) {
  //           this.openDialogOmChangePaymentBlockComponent(listItem, validateText);
  //           // } else {
  //           //   const dialogRef = this.dialog.open(DialogResultComponent, {
  //           //     data: {
  //           //       type: 'LIST_VALIDATE_TEXT',
  //           //       listValidate: validateText,
  //           //     },
  //           //   });
  //           //   dialogRef.afterClosed().subscribe((result) => {
  //           //     if (result && result.event) {
  //           //       console.log(result);
  //           //     }
  //           //   });
  //           // }
  //         } else if (statusCheckParent === 404) {
  //           this.openDialogOmChangePaymentBlockComponent(listItem, validateText);
  //         }
  //       });
  //   } else {
  //     this.listValidate.push('กรุณา เลือกรายการ');
  //   }
  // }
  onPreSave() {
    const listItem = [];
    this.listValidate = [];
    this.dataSource.data.forEach((item) => {
      item.approve = false;
      item.notApprove = false;
      const data = {
        ...item,
        valueOld: 'B',
        valueNew: '',
        unblockDate: new Date(),
        companyCode: item.companyCode,
        fiArea: item.fiArea,
        paymentCenter: item.paymentCenter,
        documentType: item.documentType,
        originalFiscalYear: item.originalFiscalYear,
        originalDocumentNo: item.originalDocumentNo,
        dateAcct: item.dateAcct,
        paymentMethod: item.paymentMethod,
        amount: item.amount,
        wtxType: item.wtxType,
        wtxCode: item.wtxCode,
        wtxBase: item.wtxBase,
        wtxAmount: item.wtxAmount,
        wtxTypeP: item.wtxTypeP,
        wtxCodeP: item.wtxCodeP,
        wtxBaseP: item.wtxBaseP,
        wtxAmountP: item.wtxAmountP,
        vendor: item.vendor,
        vendorName: item.vendorName,
        userPost: item.userPost,
        userName: this.userProfile.userdata.username,
        reason: item.reason,
        approve: null,
        confirmVendor: item.confirmVendor,
        webInfo: this.localStorageService.getWebInfo(),
        canUnblock: true,
      };
      if (this.selectionApprove.isSelected(item)) {
        item.approve = this.selectionApprove.isSelected(item);
        data.valueNew = '';
        data.reason = null;
      } else if (this.selectionNotApprove.isSelected(item)) {
        item.notApprove = this.selectionNotApprove.isSelected(item);
        data.valueNew = 'E';
      }

      if (item.approve) {
        data.approve = true;
      } else if (item.notApprove) {
        data.approve = false;
      }
      if (data.approve !== null) {
        listItem.push(data);
      }
    });

    const validateText = [];
    listItem.forEach((data) => {
      if (!data.approve && !data.reason) {
        validateText.push(
          'เลขที่เอกสาร ' +
            data.originalDocumentNo +
            ' ' +
            data.companyCode +
            ' ' +
            data.originalFiscalYear +
            ' ไม่ได้ระบุเหตุผล'
        );
        data.canUnblock = false;
      }
      if (data.approve && !data.confirmVendor) {
        validateText.push(
          'เลขที่เอกสาร ' +
            data.originalDocumentNo +
            ' ' +
            data.companyCode +
            ' ' +
            data.originalFiscalYear +
            ' ไม่ได้ยืนยันผู้ขาย'
        );
        data.canUnblock = false;
      }
    });

    console.log('listItem');
    console.log(listItem);
    if (listItem.length > 0) {
      listItem.forEach((item) => {
        if (
          item.approve &&
          item.originalDocumentNo &&
          item.originalFiscalYear &&
          item.companyCode &&
          item.specialGL !== '3'
        ) {
          const checkRelation = this.listDocumentForCheckRelation.filter(
            (object) =>
              object.originalDocumentNo === item.originalDocumentNo &&
              object.originalFiscalYear === item.originalFiscalYear &&
              object.companyCode === item.companyCode &&
              object.specialGL !== '3'
          );
          console.log('=====================', item.originalDocumentNo);
          console.log('checkRelation', checkRelation);
          if (checkRelation.length > 0) {
            checkRelation.forEach((itemRelation) => {
              if (
                itemRelation.lineInvoiceDocumentNo &&
                itemRelation.lineInvoiceFiscalYear &&
                itemRelation.lineCompanyCode &&
                itemRelation.specialGL !== '3'
              ) {
                const filter = listItem.filter(
                  (obj) =>
                    obj.originalDocumentNo === itemRelation.lineInvoiceDocumentNo &&
                    obj.originalFiscalYear === itemRelation.lineInvoiceFiscalYear &&
                    obj.companyCode === itemRelation.lineCompanyCode
                );

                console.log('filter', filter);
                if (
                  filter.length === 0 &&
                  (itemRelation.linePaymentBlock === '0' ||
                    itemRelation.linePaymentBlock === 'A' ||
                    itemRelation.linePaymentBlock === 'B')
                ) {
                  item.canUnblock = false;

                  let textMessageNormal =
                    'เอกสาร ' +
                    itemRelation.documentType +
                    ' ' +
                    itemRelation.originalDocumentNo +
                    ' ' +
                    itemRelation.companyCode +
                    ' ' +
                    itemRelation.originalFiscalYear +
                    ' ต้องอนุมัติพร้อมกับเอกสาร ' +
                    itemRelation.lineDocumentType +
                    ' ' +
                    itemRelation.lineInvoiceDocumentNo +
                    ' ' +
                    itemRelation.lineCompanyCode +
                    ' ' +
                    itemRelation.lineInvoiceFiscalYear;

                  if (
                    itemRelation.linePaymentBlock === '0' ||
                    itemRelation.linePaymentBlock === 'A'
                  ) {
                    textMessageNormal =
                      textMessageNormal + ' ซึ่งเอกสารอยู่ในสถานะ ' + itemRelation.linePaymentBlock;
                  }

                  validateText.push(textMessageNormal);
                } else if (itemRelation.runStatus) {
                  item.canUnblock = false;
                  validateText.push(
                    'เอกสาร ' +
                      itemRelation.documentType +
                      ' ' +
                      itemRelation.originalDocumentNo +
                      ' ' +
                      itemRelation.companyCode +
                      ' ' +
                      itemRelation.originalFiscalYear +
                      ' ที่มีความสัมพันธ์ ' +
                      itemRelation.lineDocumentType +
                      ' ' +
                      itemRelation.lineInvoiceDocumentNo +
                      ' ' +
                      itemRelation.lineCompanyCode +
                      ' ' +
                      itemRelation.lineInvoiceFiscalYear +
                      ' ถูกรันจ่ายอยู่บน ' +
                      itemRelation.paymentName +
                      ' ' +
                      this.utils.format(new Date(itemRelation.paymentDate), 'input').toString()
                  );
                } else if (itemRelation.proposalStatus) {
                  item.canUnblock = false;
                  validateText.push(
                    'เอกสาร ' +
                      itemRelation.documentType +
                      ' ' +
                      itemRelation.originalDocumentNo +
                      ' ' +
                      itemRelation.companyCode +
                      ' ' +
                      itemRelation.originalFiscalYear +
                      ' ที่มีความสัมพันธ์ ' +
                      itemRelation.lineDocumentType +
                      ' ' +
                      itemRelation.lineInvoiceDocumentNo +
                      ' ' +
                      itemRelation.lineCompanyCode +
                      ' ' +
                      itemRelation.lineInvoiceFiscalYear +
                      ' ถูกรันข้อเสนออยู่บน ' +
                      itemRelation.paymentName +
                      ' ' +
                      this.utils.format(new Date(itemRelation.paymentDate), 'input').toString()
                  );
                } else {
                }
              }
            });
          }
        }
      });
      console.log('listLastItem', listItem);
      listItem.forEach((item) => {
        console.log('item-->', item);
        if (item.documentType === 'KX' || (item.documentType === 'K3' && item.specialGL !== '3')) {
          const findItem = listItem.find(
            (list) =>
              list.originalDocumentNo === item.lineInvoiceDocumentNo &&
              list.originalFiscalYear === item.lineInvoiceFiscalYear &&
              list.companyCode === item.companyCode
          );

          if (findItem) {
            if (!findItem.canUnblock) {
              console.log('aa');
              item.canUnblock = false;
              validateText.push(
                'เอกสาร ' +
                  item.documentType +
                  ' ' +
                  item.originalDocumentNo +
                  ' ' +
                  item.companyCode +
                  ' ' +
                  item.originalFiscalYear +
                  ' ต้องอนุมัติพร้อมกับเอกสาร ' +
                  findItem.documentType +
                  ' ' +
                  findItem.originalDocumentNo +
                  ' ' +
                  findItem.companyCode +
                  ' ' +
                  findItem.originalFiscalYear
              );
            }
          }
        }
      });
      console.log('listLastItem2', listItem);

      this.openDialogOmChangePaymentBlockComponent(listItem, validateText);
    } else {
      this.listValidate.push('กรุณา เลือกรายการ');
    }
    // if (listItem.length > 0) {
    //   this.paymentBlockService
    //     .findParent(listItem)
    //     .then((data) => {
    //       console.log('listCheck');
    //       console.log(data);
    //       const response = data;
    //       statusCheckParent = response.status;
    //       if (response.status === 200) {
    //         const listChild = response.data as any;
    //         if (listChild && listChild.length > 0) {
    //           listChild.forEach((item) => {
    //             const parentDoc = listItem.filter(
    //               (obj) =>
    //                 obj.companyCode === item.companyCode &&
    //                 obj.originalDocumentNo === item.originalDocumentNo &&
    //                 obj.originalFiscalYear === item.originalFiscalYear
    //             );
    //             console.log('first parentDoc');
    //             console.log(parentDoc);
    //             if (parentDoc.length === 0) {
    //               console.log('after parentDoc');
    //               console.log(parentDoc);
    //               const a = (listItem.find(
    //                 (obj) =>
    //                   obj.companyCode === item.companyCode &&
    //                   obj.originalDocumentNo === item.lineInvoiceDocumentNo &&
    //                   obj.originalFiscalYear === item.lineInvoiceFiscalYear
    //               ).canUnblock = false);
    //
    //               console.log('Item Child');
    //               console.log(a);
    //
    //               validateText.push(
    //                 'เลขที่เอกสาร ' +
    //                   item.lineInvoiceDocumentNo +
    //                   ' ' +
    //                   item.companyCode +
    //                   ' ' +
    //                   item.lineInvoiceFiscalYear +
    //                   ' ต้องอนุมัติพร้อมกับเอกสาร ' +
    //                   item.documentType +
    //                   ' ' +
    //                   item.originalDocumentNo +
    //                   ' ' +
    //                   item.companyCode +
    //                   ' ' +
    //                   item.originalFiscalYear
    //               );
    //             }
    //           });
    //         }
    //       }
    //     })
    //     .then(() => {
    //       if (statusCheckParent === 200) {
    //         // if (validateText.length <= 0) {
    //         this.openDialogOmChangePaymentBlockComponent(listItem, validateText);
    //         // } else {
    //         //   const dialogRef = this.dialog.open(DialogResultComponent, {
    //         //     data: {
    //         //       type: 'LIST_VALIDATE_TEXT',
    //         //       listValidate: validateText,
    //         //     },
    //         //   });
    //         //   dialogRef.afterClosed().subscribe((result) => {
    //         //     if (result && result.event) {
    //         //       console.log(result);
    //         //     }
    //         //   });
    //         // }
    //       } else if (statusCheckParent === 404) {
    //         this.openDialogOmChangePaymentBlockComponent(listItem, validateText);
    //       }
    //     });
    // } else {
    //   this.listValidate.push('กรุณา เลือกรายการ');
    // }
  }
  openDialogOmChangePaymentBlockComponent(listItem, listValidate) {
    if (this.listValidate.length <= 0) {
      // DialogOmChangePaymentBlockComponent
      if (listItem.length > 0) {
        const dialogRef = this.dialog.open(DialogOmChangePaymentBlockComponent, {
          disableClose: true,
          width: 'auto',
          data: {
            items: listItem,
            listValidate,
          },
        });
        dialogRef.afterClosed().subscribe((result) => {
          if (result && result.event === 'save') {
            this.onSearch();
          }
        });
      } else {
        this.listValidate.push('กรุณา เลือกรายการ');
      }
    }
  }

  selectAllApproved() {
    this.dataSource.data.forEach((item) => {
      this.selectionApprove.select(item);
      this.selectionNotApprove.deselect(item);
      item.approve = true;
      item.notApprove = false;
    });
  }

  unselectAllApproved() {
    this.dataSource.data.forEach((item) => {
      this.selectionApprove.deselect(item);
      item.approve = false;
    });
  }

  selectAllNotApproved() {
    this.dataSource.data.forEach((item) => {
      this.selectionNotApprove.deselect(item);
      item.notApprove = false;
      item.reason = '';
    });
  }

  openDialogOmColumnTable(): void {
    const dialogRef = this.dialog.open(DialogOmColumnTableComponent, {
      data: { listcolumn: this.columnTable },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Save') {
          this.columnTable = result.value;
          this.getDisplayedColumns();
        }
      }
      console.log(result);
    });
  }

  // for clear input
  clearInput(inputForm) {
    this.omFormCreate.controls[inputForm].setValue('');
  }

  openDialogOmSearchCriteria() {
    const dialogRef = this.dialog.open(DialogOmSearchCriteriaComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.dataCriteria = result.value;
        this.setDataToOmForm(result.value.jsonText);
      }
    });
  }

  openDialogOmSaveSearchCriteria(type) {
    this.omFormCreate.value.listCompanyCode = this.listCompanyCode;
    this.omFormCreate.value.listDocType = this.listDocType;
    this.omFormCreate.value.listPaymentMethod = this.listPaymentMethod;
    this.omFormCreate.value.listVendor = this.listVendor;
    this.omFormCreate.value.listDisbursementCode = this.listDisbursementCode;
    this.omFormCreate.value.listSpecialType = this.listSpecialType;

    this.omFormCreate.value.listDocumentCreateDate = this.listDocumentCreateDate;
    this.omFormCreate.value.listPostDate = this.listPostDate;
    this.omFormCreate.value.listDocumentDate = this.listDocumentDate;
    this.omFormCreate.value.listDocumentNo = this.listDocumentNo;
    this.omFormCreate.value.listHeaderReference = this.listHeaderReference;

    console.log(this.omFormCreate.value);

    const dialogRef = this.dialog.open(DialogOmSaveSearchCriteriaComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
        value: this.omFormCreate.value,
        type,
        dataCriteria: this.dataCriteria,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Success') {
          if (result.value) {
            this.dataCriteria = result.value;
          }
        }
      }
    });
  }

  openDialogOmSearchColumn() {
    const dialogRef = this.dialog.open(DialogOmSearchColumnTableComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.omFormCreate.patchValue({
          outline: result.value.name, // โครงร่าง
        });
        this.dataColumn = result.value;
        this.columnTable = JSON.parse(result.value.jsonText) as [];
        this.getDisplayedColumns();
      }
    });
  }

  changeRole(role) {
    this.role = role;
    this.isSubmitedForm = false;
    if (this.role === 'USER_PTO') {
      this.defaultOmFormForArea();
    } else if (this.role === 'USER_CGD') {
      this.defaultOmFormForCGD();
    } else if (this.role === 'USER_PTO_H') {
      this.defaultOmFormForAreaTreatMent();
    }
  }

  setDataToOmForm(data) {
    const jsonObject = JSON.parse(data);
    this.omFormCreate.patchValue({
      departmentCodeFrom: jsonObject.departmentCodeFrom, // รหัสหน่วยงาน
      departmentCodeTo: jsonObject.departmentCodeTo, // รหัสหน่วยงาน
      provinceCodeFrom: jsonObject.provinceCodeFrom, // รหัสจังหวัด
      provinceCodeTo: jsonObject.provinceCodeTo, // รหัสจังหวัด
      yearAccount: this.utils.convertYearToBuddhist(jsonObject.yearAccount), // ปีบัญชี
      postDateFrom: jsonObject.postDateFrom ? new Date(jsonObject.postDateFrom) : '', // วันผ่านรายการ
      postDateTo: jsonObject.postDateTo ? new Date(jsonObject.postDateTo) : '', // วันผ่านรายการ
      vendorTaxIdFrom: jsonObject.vendorTaxIdFrom, // ผุ้ขาย
      vendorTaxIdTo: jsonObject.vendorTaxIdTo, // ผุ้ขาย
      disbursementCodeFrom: jsonObject.disbursementCodeFrom, // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: jsonObject.disbursementCodeTo, // รหัสหน่วยเบิกจ่าย
      docTypeFrom: jsonObject.docTypeFrom, // ประเภทเอกสาร
      docTypeTo: jsonObject.docTypeTo, // ประเภทเอกสาร
      payMethodFrom: jsonObject.payMethodFrom, // วิธีชำระเงิน
      payMethodTo: jsonObject.payMethodTo, // วิธีชำระเงิน
      documentDateFrom: jsonObject.documentDateFrom ? new Date(jsonObject.documentDateFrom) : '', // วันที่เอกสาร
      documentDateTo: jsonObject.documentDateTo ? new Date(jsonObject.documentDateTo) : '', // วันที่เอกสาร
      documentCreateDateFrom: jsonObject.documentCreateDateFrom
        ? new Date(jsonObject.documentCreateDateFrom)
        : '', // วันที่บันทึก
      documentCreateDateTo: jsonObject.documentCreateDateTo
        ? new Date(jsonObject.documentCreateDateTo)
        : '', // วันที่บันทึก
      specialTypeFrom: jsonObject.specialTypeFrom, // แยกประเภทพิเศษ
      specialTypeTo: jsonObject.specialTypeTo, // แยกประเภทพิเศษ
      outline: jsonObject.outline, // โครงร่าง
      documentNoFrom: jsonObject.documentNoFrom, // เลขที่เอกสาร
      documentNoTo: jsonObject.documentNoTo, // เลขที่เอกสาร
      headerReferenceFrom: jsonObject.headerReferenceFrom, // เลขที่เอกสาร
      headerReferenceTo: jsonObject.headerReferenceTo, // เลขที่เอกสาร
    });
    this.listCompanyCode = jsonObject.listCompanyCode;
    this.listDocType = jsonObject.listDocType;
    this.listPaymentMethod = jsonObject.listPaymentMethod;
    this.listVendor = jsonObject.vendor;
    this.listDisbursementCode = jsonObject.paymentCenter;
    this.listSpecialType = jsonObject.specialType;

    this.listDocumentDate = jsonObject.listDocumentDate;
    this.listDocumentCreateDate = jsonObject.listDocumentCreateDate;
    this.listPostDate = jsonObject.listPostDate;
    this.listDocumentNo = jsonObject.listDocumentNo;
    this.listHeaderReference = jsonObject.listHeaderReference;
  }

  openDialogOmSaveColumnTable(type) {
    console.log(this.tableForm.value);
    const dialogRef = this.dialog.open(DialogOmSaveColumnTableComponent, {
      data: {
        role: this.role,
        createBy: this.userProfile.userdata.username,
        value: this.columnTable,
        type,
        dataColumn: this.dataColumn,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Success') {
          this.dataColumn = result.value;
        }
      }
    });
  }

  createColumeTable() {
    const test = this.tableForm.value;
    console.log(JSON.stringify(test));
    const payload = {
      name: 'ABC',
      role: this.role,
      jsonText: JSON.stringify(test),
    };

    this.paymentBlockService.createColumn(payload).then((result) => {
      console.log(result);
    });
  }

  defaultOmFormForArea() {
    this.isDisablePayMethod = false;
    this.listCompanyCode = [{ companyCodeFrom: null, companyCodeTo: null, optionExclude: false }];

    this.listDocType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listPaymentMethod = [
      { from: 'F', to: null, optionExclude: true },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listVendor = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];

    this.listDisbursementCode = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listSpecialType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listDocumentNo = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.omFormCreate.patchValue({
      departmentCodeFrom: '', // รหัสหน่วยงาน
      departmentCodeTo: '', // รหัสหน่วยงาน
      provinceCodeFrom: '', // รหัสจังหวัด
      provinceCodeTo: '', // รหัสจังหวัด
      yearAccount: this.utils.fiscYear, // ปีบัญชี
      postDateFrom: '', // วันผ่านรายการ
      postDateTo: '', // วันผ่านรายการ
      vendorTaxIdFrom: '', // ผุ้ขาย
      vendorTaxIdTo: '', // ผุ้ขาย
      disbursementCodeFrom: '', // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: '', // รหัสหน่วยเบิกจ่าย

      docTypeFrom: '', // ประเภทเอกสาร
      docTypeTo: '', // ประเภทเอกสาร
      payMethodFrom: 'F', // วิธีชำระเงิน
      payMethodTo: '', // วิธีชำระเงิน
      documentDateFrom: '', // วันที่เอกสาร
      documentDateTo: '', // วันที่เอกสาร
      documentCreateDateFrom: '', // วันที่บันทึก
      documentCreateDateTo: '', // วันที่บันทึก
      specialTypeFrom: '', // แยกประเภทพิเศษ
      specialTypeTo: '', // แยกประเภทพิเศษ
      documentNoFrom: '',
      documentNoTo: '',

      outline: '', // โครงร่าง
    });

    this.checkOptionExclude();
  }

  defaultOmFormForCGD() {
    this.isDisablePayMethod = false;
    this.listCompanyCode = [{ companyCodeFrom: null, companyCodeTo: null, optionExclude: false }];
    this.listDocType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listPaymentMethod = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listVendor = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];

    this.listDisbursementCode = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listSpecialType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listDocumentNo = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.omFormCreate.patchValue({
      departmentCodeFrom: '', // รหัสหน่วยงาน
      departmentCodeTo: '', // รหัสหน่วยงาน
      provinceCodeFrom: '', // รหัสจังหวัด
      provinceCodeTo: '', // รหัสจังหวัด
      yearAccount: this.utils.fiscYear, // ปีบัญชี
      postDateFrom: '', // วันผ่านรายการ
      postDateTo: '', // วันผ่านรายการ
      vendorTaxIdFrom: '', // ผุ้ขาย
      vendorTaxIdTo: '', // ผุ้ขาย
      disbursementCodeFrom: '', // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: '', // รหัสหน่วยเบิกจ่าย

      docTypeFrom: '', // ประเภทเอกสาร
      docTypeTo: '', // ประเภทเอกสาร
      payMethodFrom: '', // วิธีชำระเงิน
      payMethodTo: '', // วิธีชำระเงิน
      documentDateFrom: '', // วันที่เอกสาร
      documentDateTo: '', // วันที่เอกสาร
      documentCreateDateFrom: '', // วันที่บันทึก
      documentCreateDateTo: '', // วันที่บันทึก
      specialTypeFrom: '', // แยกประเภทพิเศษ
      specialTypeTo: '', // แยกประเภทพิเศษ
      documentNoFrom: '',
      documentNoTo: '',

      outline: '', // โครงร่าง
    });

    this.checkOptionExclude();
  }

  defaultOmFormForAreaTreatMent() {
    this.isDisablePayMethod = true;
    this.listCompanyCode = [
      { companyCodeFrom: '01001', companyCodeTo: '01012', optionExclude: false },
      { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
      { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
    ];
    this.listDocType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listPaymentMethod = [
      { from: 'F', to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listVendor = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];

    this.listDisbursementCode = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listSpecialType = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.listDocumentNo = [
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
    ];
    this.omFormCreate.patchValue({
      departmentCodeFrom: '01001', // รหัสหน่วยงาน
      departmentCodeTo: '01012', // รหัสหน่วยงาน
      provinceCodeFrom: '1000', // รหัสจังหวัด
      provinceCodeTo: '', // รหัสจังหวัด
      yearAccount: this.utils.fiscYear, // ปีบัญชี
      postDateFrom: '', // วันผ่านรายการ
      postDateTo: '', // วันผ่านรายการ
      vendorTaxIdFrom: '', // ผุ้ขาย
      vendorTaxIdTo: '', // ผุ้ขาย
      disbursementCodeFrom: '', // รหัสหน่วยเบิกจ่าย
      disbursementCodeTo: '', // รหัสหน่วยเบิกจ่าย

      docTypeFrom: '', // ประเภทเอกสาร
      docTypeTo: '', // ประเภทเอกสาร
      payMethodFrom: 'F', // วิธีชำระเงิน
      payMethodTo: '', // วิธีชำระเงิน
      documentDateFrom: '', // วันที่เอกสาร
      documentDateTo: '', // วันที่เอกสาร
      documentCreateDateFrom: '', // วันที่บันทึก
      documentCreateDateTo: '', // วันที่บันทึก
      specialTypeFrom: '', // แยกประเภทพิเศษ
      specialTypeTo: '', // แยกประเภทพิเศษ
      documentNoFrom: '',
      documentNoTo: '',

      outline: '', // โครงร่าง
    });

    this.checkOptionExclude();
  }

  defaultColumnTableForm() {
    this.tableForm.patchValue({
      documentType: true,
      // compCode: this.compCodeControl,
      dateDoc: true,
      dateAcct: true,
      amount: true,
      invoiceDocumentNo: true,
      originalDocumentNo: true,
      originalFiscalYear: true,
      paymentMethod: true,
      headerReference: true,
      userPost: true,
      fiArea: true,
      costCenter: true,
      fundSource: true,
      bgCode: true,
      assignment: true,
      brLine: false,
      subAccount: false,
      subAccountOwner: false,
      depositAccount: false,
      depositAccountOwner: false,
      lineItemText: false,

      specialGL: false,
      dateBaseLine: false,
      reference2: false,
      poDocumentNo: false,
      // ////
      vendorName: false,
      vendorCode: false,
      bankKey: false,
      bankAccountNo: false,
      bankAccountHolderName: false,
      fundType: false,
      // ///
      approve: false,
      notApprove: false,
      confirmSeller: false,
      bot: false,
      reason: false,
      iconStatus: false,
      frStatus: false,
      // textStatus: false,
      // errWtInfo: false,
      // deduct: false,
      // partnerBankType:false,
    });
    this.loaderService.loadingToggleStatus(true);
    setTimeout(() => {
      this.columnTable = [];
      this.columnTable.push({
        key: 'list',
        columnName: 'รายการ',
        showColumn: true,
        seq: 0,
      });
      for (const key in this.tableForm.controls) {
        const data = this.constant.LIST_TABLE_OM.get(key);
        // console.log(data);
        this.columnTable.push({
          key,
          columnName: data.name,
          showColumn: this.tableForm.controls[key].value,
          seq: data.seq,
        });
      }
      this.loaderService.loadingToggleStatus(false);
    }, 1000); // Execute something() 1 second later.
  }

  clearInputByRole() {
    this.dataCriteria = '';
    this.dataColumn = '';
    if (this.role === 'USER_PTO') {
      this.defaultOmFormForArea();
    } else if (this.role === 'USER_CGD') {
      this.defaultOmFormForCGD();
    } else if (this.role === 'USER_PTO_H') {
      this.defaultOmFormForAreaTreatMent();
    }
  }

  openDialogOmCompanyCode() {
    console.log(this.listCompanyCode);
    const dialogRef = this.dialog.open(DialogOmCompanyCodeComponent, {
      data: {
        listCompanyCode: this.listCompanyCode,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listCompanyCode = result.value;

          this.omFormCreate.patchValue({
            departmentCodeFrom: this.listCompanyCode[0].companyCodeFrom, // รหัสหน่วยงาน
            departmentCodeTo: this.listCompanyCode[0].companyCodeTo, // รหัสหน่วยงาน
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmDocType() {
    console.log(this.listDocType);
    const dialogRef = this.dialog.open(DialogOmDocTypeComponent, {
      data: {
        listDocType: this.listDocType,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listDocType = result.value;
          this.omFormCreate.patchValue({
            docTypeFrom: this.listDocType[0].from, // รหัสหน่วยงาน
            docTypeTo: this.listDocType[0].to, // รหัสหน่วยงาน
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmPaymentMethod() {
    console.log(this.listPaymentMethod);
    const dialogRef = this.dialog.open(DialogOmPaymentMethodComponent, {
      data: {
        listPaymentMethod: this.listPaymentMethod,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listPaymentMethod = result.value;

          this.omFormCreate.patchValue({
            payMethodFrom: this.listPaymentMethod[0].from, // รหัสหน่วยงาน
            payMethodTo: this.listPaymentMethod[0].to, // รหัสหน่วยงาน
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmVendor() {
    const dialogRef = this.dialog.open(DialogOmVendorComponent, {
      data: {
        listVendor: this.listVendor,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listVendor = result.value;
          console.log(this.listVendor);

          this.omFormCreate.patchValue({
            vendorTaxIdFrom: this.listVendor[0].from,
            vendorTaxIdTo: this.listVendor[0].to,
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmPaymentCenter() {
    console.log(this.listDisbursementCode);
    const dialogRef = this.dialog.open(DialogOmPaymentCenterComponent, {
      data: {
        listDisbursementCode: this.listDisbursementCode,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listDisbursementCode = result.value;
          console.log(this.listDisbursementCode);

          this.omFormCreate.patchValue({
            disbursementCodeFrom: this.listDisbursementCode[0].from,
            disbursementCodeTo: this.listDisbursementCode[0].to,
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmSpecialType() {
    console.log(this.listSpecialType);
    const dialogRef = this.dialog.open(DialogOmSpecialTypeComponent, {
      data: {
        listSpecialType: this.listSpecialType,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listSpecialType = result.value;
          console.log(this.listSpecialType);

          this.omFormCreate.patchValue({
            specialTypeFrom: this.listSpecialType[0].from,
            specialTypeTo: this.listSpecialType[0].to,
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  openDialogOmDocumentNo() {
    console.log(this.listDocumentNo);
    const dialogRef = this.dialog.open(DialogOmDocumentNoComponent, {
      data: {
        listDocumentNo: this.listDocumentNo,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listDocumentNo = result.value;
          console.log(this.listDocumentNo);

          this.omFormCreate.patchValue({
            documentNoFrom: this.listDocumentNo[0].from,
            documentNoTo: this.listDocumentNo[0].to,
          });
          this.checkOptionExclude();
        }
      }
    });
  }
  openDialogOmHeaderReference() {
    console.log(this.listHeaderReference);
    const dialogRef = this.dialog.open(DialogOmHeaderReferenceComponent, {
      data: {
        listHeaderReference: this.listHeaderReference,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listHeaderReference = result.value;
          console.log(this.listHeaderReference);

          this.omFormCreate.patchValue({
            headerReferenceFrom: this.listHeaderReference[0].from,
            headerReferenceTo: this.listHeaderReference[0].to,
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  setCompanyCode(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'departmentCodeFrom') {
      if (form.departmentCodeFrom) {
        this.listCompanyCode[0].companyCodeFrom = form.departmentCodeFrom;
      } else {
        if (this.listCompanyCode.length === 1) {
          this.listCompanyCode = [
            { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
          ];
        } else {
          this.listCompanyCode.splice(0, 1);
          this.omFormCreate.patchValue({
            departmentCodeFrom: this.listCompanyCode[0].companyCodeFrom,
          });
        }
      }
    } else if (type === 'departmentCodeTo') {
      if (form.departmentCodeTo) {
        this.listCompanyCode[0].companyCodeTo = form.departmentCodeTo;
      } else {
        this.listCompanyCode[0].companyCodeTo = '';
        this.omFormCreate.patchValue({
          departmentCodeTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  setDocType(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'docTypeFrom') {
      if (form.docTypeFrom) {
        this.listDocType[0].from = form.docTypeFrom;
      } else {
        if (this.listDocType.length === 1) {
          this.listDocType = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listDocType.splice(0, 1);
          this.omFormCreate.patchValue({
            docTypeFrom: this.listDocType[0].from,
          });
        }
      }
    } else if (type === 'docTypeTo') {
      if (form.docTypeTo) {
        this.listDocType[0].to = form.docTypeTo;
      } else {
        this.listDocType[0].to = '';
        this.omFormCreate.patchValue({
          docTypeTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  setPaymentMethod(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'payMethodFrom') {
      if (form.payMethodFrom) {
        this.listPaymentMethod[0].from = form.payMethodFrom;
      } else {
        if (this.listPaymentMethod.length === 1) {
          this.listPaymentMethod = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listPaymentMethod.splice(0, 1);
          this.omFormCreate.patchValue({
            payMethodFrom: this.listPaymentMethod[0].from,
          });
        }
      }
    } else if (type === 'payMethodTo') {
      if (form.payMethodTo) {
        this.listPaymentMethod[0].to = form.payMethodTo;
      } else {
        this.listPaymentMethod[0].to = '';
        this.omFormCreate.patchValue({
          payMethodTo: '',
        });
      }
    }

    this.checkOptionExclude();
  }

  setVendor(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'vendorFrom') {
      if (form.vendorTaxIdFrom) {
        this.listVendor[0].from = form.vendorTaxIdFrom;
      } else {
        if (this.listVendor.length === 1) {
          this.listVendor = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listVendor.splice(0, 1);
          this.omFormCreate.patchValue({
            vendorTaxIdFrom: this.listVendor[0].from,
          });
        }
      }
    } else if (type === 'vendorTo') {
      if (form.vendorTaxIdTo) {
        this.listVendor[0].to = form.vendorTaxIdTo;
      } else {
        this.listVendor[0].to = '';
        this.omFormCreate.patchValue({
          vendorTaxIdTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  setDisbursementCode(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'disbursementCodeFrom') {
      if (form.disbursementCodeFrom) {
        this.listDisbursementCode[0].from = form.disbursementCodeFrom;
      } else {
        if (this.listDisbursementCode.length === 1) {
          this.listDisbursementCode = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listDisbursementCode.splice(0, 1);
          this.omFormCreate.patchValue({
            disbursementCodeFrom: this.listDisbursementCode[0].from,
          });
        }
      }
    } else if (type === 'disbursementCodeTo') {
      if (form.disbursementCodeTo) {
        this.listDisbursementCode[0].to = form.to;
      } else {
        this.listDisbursementCode[0].to = '';
        this.omFormCreate.patchValue({
          disbursementCodeTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  setSpecialGL(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'specialTypeFrom') {
      if (form.specialTypeFrom) {
        this.listSpecialType[0].from = form.specialTypeFrom;
      } else {
        if (this.listSpecialType.length === 1) {
          this.listSpecialType = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listSpecialType.splice(0, 1);
          this.omFormCreate.patchValue({
            specialTypeFrom: this.listSpecialType[0].from,
          });
        }
      }
    } else if (type === 'specialTypeTo') {
      if (form.specialTypeTo) {
        this.listSpecialType[0].to = form.specialTypeTo;
      } else {
        this.listSpecialType[0].to = '';
        this.omFormCreate.patchValue({
          specialTypeTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  setDocumentNo(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'documentNoFrom') {
      if (form.documentNoFrom) {
        this.listDocumentNo[0].from = form.documentNoFrom;
      } else {
        if (this.listDocumentNo.length === 1) {
          this.listDocumentNo = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listDocumentNo.splice(0, 1);
          this.omFormCreate.patchValue({
            documentNoFrom: this.listDocumentNo[0].from,
          });
        }
      }
    } else if (type === 'documentNoTo') {
      if (form.documentNoTo) {
        this.listDocumentNo[0].to = form.documentNoTo;
      } else {
        this.listDocumentNo[0].to = '';
        this.omFormCreate.patchValue({
          documentNoTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }
  setHeaderReference(type) {
    console.log(type);

    const form = this.omFormCreate.value;
    if (type === 'headerReferenceFrom') {
      if (form.headerReferenceFrom) {
        this.listHeaderReference[0].from = form.headerReferenceFrom;
      } else {
        if (this.listHeaderReference.length === 1) {
          this.listHeaderReference = [{ from: null, to: null, optionExclude: false }];
        } else {
          this.listHeaderReference.splice(0, 1);
          this.omFormCreate.patchValue({
            headerReferenceFrom: this.listHeaderReference[0].from,
          });
        }
      }
    } else if (type === 'headerReferenceTo') {
      if (form.headerReferenceTo) {
        this.listHeaderReference[0].to = form.headerReferenceTo;
      } else {
        this.listHeaderReference[0].to = '';
        this.omFormCreate.patchValue({
          headerReferenceTo: '',
        });
      }
    }
    this.checkOptionExclude();
  }

  checkOptionExclude() {
    this.optionExcludeCompanyCode = this.listCompanyCode.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
    this.optionExcludeDocType = this.listDocType.find((data) => data.optionExclude) ? 'Y' : 'N';
    this.optionExcludePaymentMethod = this.listPaymentMethod.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
    this.optionExcludeVendor = this.listVendor.find((data) => data.optionExclude) ? 'Y' : 'N';
    this.optionExcludeDisbursementCode = this.listDisbursementCode.find(
      (data) => data.optionExclude
    )
      ? 'Y'
      : 'N';
    this.optionExcludeSpecialType = this.listSpecialType.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
    this.optionExcludeDocumentNo = this.listDocumentNo.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
    this.optionExcludeHeaderReference = this.listHeaderReference.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
  }

  getColumnOneByName() {
    const form = this.omFormCreate.value;
    this.paymentBlockService.searchColumnByRoleAndName(this.role, form.outline).then((data) => {
      console.log(data);
      const response = data as any;
      if (response.status === 200) {
        this.dataColumn = response.data;
        this.columnTable = JSON.parse(response.data.jsonText) as [];
      } else {
        this.dataColumn = '';
        this.columnTable = [];
        this.columnTable.push({
          key: 'list',
          columnName: 'รายการ',
          showColumn: true,
          seq: 0,
        });
        for (const key in this.tableForm.controls) {
          const data = this.constant.LIST_TABLE_OM.get(key);
          console.log(data);
          this.columnTable.push({
            key,
            columnName: data.name,
            showColumn: this.tableForm.controls[key].value,
            seq: data.seq,
          });
        }
      }
    });
  }

  openDialogDateParameter(listType) {
    let listCriteria = [];
    let thaiText = '';
    if (listType === 'postDate') {
      this.listPostDate[0] = {
        from: this.omFormCreate.controls.postDateFrom.value,
        to: this.omFormCreate.controls.postDateTo.value,
        optionExclude: this.listPostDate.length > 0 ? this.listPostDate[0].optionExclude : false,
      };
      listCriteria = this.listPostDate;
      thaiText = 'วันผ่านรายการ';
    } else if (listType === 'documentDate') {
      this.listDocumentDate[0] = {
        from: this.omFormCreate.controls.documentDateFrom.value,
        to: this.omFormCreate.controls.documentDateTo.value,
        optionExclude:
          this.listDocumentDate.length > 0 ? this.listDocumentDate[0].optionExclude : false,
      };
      listCriteria = this.listDocumentDate;
      thaiText = 'วันที่เอกสาร';
    } else if (listType === 'documentCreateDate') {
      this.listDocumentCreateDate[0] = {
        from: this.omFormCreate.controls.documentCreateDateFrom.value,
        to: this.omFormCreate.controls.documentCreateDateTo.value,
        optionExclude:
          this.listDocumentCreateDate.length > 0
            ? this.listDocumentCreateDate[0].optionExclude
            : false,
      };
      listCriteria = this.listDocumentCreateDate;
      thaiText = 'วันที่บันทึก';
    }
    const dialogRef = this.dialog.open(DialogCriteriaComponent, {
      width: '60vw',
      data: {
        listCriteria: listCriteria,
        typeParam: 'LIST',
        keyColumn: 'key',
        typeInput: 'DATE',
        thaiText,
        enableExclude: true,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          const values = result.value;
          if (listType === 'postDate') {
            this.listPostDate = values;
            this.omFormCreate.patchValue({
              postDateFrom: '',
              postDateTo: '',
            });
            if (values.length > 0) {
              this.omFormCreate.patchValue({
                postDateFrom: values[0].from ? new Date(values[0].from) : null,
                postDateTo: values[0].to ? new Date(values[0].to) : null,
              });
            }
          } else if (listType === 'documentDate') {
            this.listDocumentDate = values;
            this.omFormCreate.patchValue({
              documentDateFrom: '',
              documentDateTo: '',
            });
            if (values.length > 0) {
              this.omFormCreate.patchValue({
                documentDateFrom: values[0].from ? new Date(values[0].from) : null,
                documentDateTo: values[0].to ? new Date(values[0].to) : null,
              });
            }
          } else if (listType === 'documentCreateDate') {
            this.listDocumentCreateDate = values;
            this.omFormCreate.patchValue({
              documentCreateDateFrom: '',
              documentCreateDateTo: '',
            });
            if (values.length > 0) {
              this.omFormCreate.patchValue({
                documentCreateDateFrom: values[0].from ? new Date(values[0].from) : null,
                documentCreateDateTo: values[0].to ? new Date(values[0].to) : null,
              });
            }
          }
        }
      }
    });
  }

  isHaveOptionExclude(type) {
    if (type === 'postDate') {
      return this.listPostDate.some((e) => e.optionExclude);
    } else if (type === 'documentDate') {
      return this.listDocumentDate.some((e) => e.optionExclude);
    } else if (type === 'documentCreateDate') {
      return this.listDocumentCreateDate.some((e) => e.optionExclude);
    }
    return false;
  }

  renderStatusDescription(element) {
    if (!element) return '';
    const { iconStatus } = element;
    switch (iconStatus) {
      case this.constant.PAYMENT_BLOCK_STATUS.DUE:
        return 'ครบกำหนด';
      case this.constant.PAYMENT_BLOCK_STATUS.NOT_COMPLETE:
        return 'ไม่ครบ';
      case this.constant.PAYMENT_BLOCK_STATUS.OVER_DUE:
        return 'เกินกำหนด';
      default:
        return '';
    }
  }

  clearFilter() {
    this.filterTable.first.nativeElement.value = '';
  }

  unselectAllUnApproved() {
    const dialogRef = this.dialog.open(DialogOmInputReasonUnapprovedComponent, {});
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.event === 'save' && result.value) {
          this.dataSource.data.forEach((item) => {
            this.selectionApprove.deselect(item);
            this.selectionNotApprove.select(item);
            item.approve = false;
            item.notApprove = true;
            item.reason = result.value;
          });
        }
      }
    });
  }

  searchRef(document) {
    if (!document.invoiceDocumentNo) {
      // const payload = {
      //   compCode: '03003',
      //   paymentCenter: '0300300003',
      //   headerReferenceFrom: 'AA#IN65/0006',
      //   option: 1,
      //   webInfo: this.localStorageService.getWebInfo(),
      // };
      const payload = {
        compCode: document.companyCode,
        paymentCenter: document.paymentCenter,
        headerReferenceFrom: document.headerReference,
        option: 1,
        webInfo: this.localStorageService.getWebInfo(),
      };

      this.fiService.searchRef(payload).then((data) => {
        const response = data as any;
        console.log(response);
        if (response.status === 200) {
          const listItem = response.data.headers as any;

          if (listItem.length > 0) {
            const dialogRef = this.dialog.open(DialogShowDocumentReferenceComponent, {
              disableClose: true,
              width: 'auto',
              data: {
                items: listItem,
              },
            });
            dialogRef.afterClosed().subscribe((result) => {
              if (result && result.event === 'save') {
                this.onSearch();
              }
            });
          }
        }
      });
    }
  }
}
