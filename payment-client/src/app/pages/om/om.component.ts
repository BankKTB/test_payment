import {
  AfterViewInit,
  Component,
  ElementRef,
  OnInit,
  QueryList,
  ViewChild,
  ViewChildren,
} from '@angular/core';
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
import { DialogDetailDocumentComponent } from '@shared/component/dialog-detail-document/dialog-detail-document.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogOmColumnTableComponent } from '@shared/component/dialog-om-column-table/dialog-om-column-table.component';
import { LoaderService, SidebarService } from '@core/services';
import { DialogOmSearchCriteriaComponent } from '@shared/component/dialog-om-search-criteria/dialog-om-search-criteria.component';
import { DialogOmSaveSearchCriteriaComponent } from '@shared/component/dialog-om-save-search-criteria/dialog-om-save-search-criteria.component';
import { DialogOmSearchColumnTableComponent } from '@shared/component/dialog-om-search-column-table/dialog-om-search-column-table.component';
import { DialogOmChangePaymentBlockComponent } from '@shared/component/dialog-om-change-payment-block/dialog-om-change-payment-block.component';
import { DialogOmSaveColumnTableComponent } from '@shared/component/dialog-om-save-column-table/dialog-om-save-column-table.component';

@Component({
  selector: 'app-om',
  templateUrl: './om.component.html',
  styleUrls: ['./om.component.scss'],
  // changeDetection: ChangeDetectionStrategy.OnPush
})
export class OmComponent implements OnInit, AfterViewInit {
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

  button = 'Submit';

  tableForm: FormGroup;
  docTypeControl: FormControl;
  compCodeControl: FormControl;
  dateDocControl: FormControl;
  dateAcctControl: FormControl;
  amountControl: FormControl;
  invDocNoControl: FormControl;
  accDocNoControl: FormControl;
  fiscalYearControl: FormControl;
  paymentMethodControl: FormControl;
  hdReferenceControl: FormControl;
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
  poDocNoControl: FormControl;
  ////
  vendorNameControl: FormControl;
  vendorCodeControl: FormControl;
  bankKeyControl: FormControl;
  bankAccountNoControl: FormControl;
  accountHolderControl: FormControl;
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

  columnTable = [{ key: null, columnName: null, showColumn: null, seq: null }];

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

  outlineControl: FormControl; // โครงร่าง

  isOpenCollapseDetail = true; // เปิดปิด collpase

  isSubmitedForm = false;
  listValidate = [];
  listSearchValidate = [];
  listMessageResponse = [];
  role = 'USER_PTO';

  isDisablePayMethod = false;

  currentDate = new Date();

  constructor(
    private formBuilder: FormBuilder,
    private dialog: MatDialog,
    public utils: Utils,
    private constant: Constant,
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private loaderService: LoaderService
  ) {}

  ngOnInit() {
    this.currentDate.setHours(0);
    this.currentDate.setMinutes(0);
    this.currentDate.setSeconds(0);
    this.currentDate.setMilliseconds(0);

    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('om');
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());

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
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  getDisplayedColumns() {
    this.columnTable.sort((a, b) => (a.seq > b.seq ? 1 : -1));
    return this.columnTable.filter((cd) => cd.showColumn).map((cd) => cd.key);
  }

  ngAfterViewInit() {}

  createFormControl() {
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

      outline: this.outlineControl, // โครงร่าง
    });
  }

  createTableFormControl() {
    this.docTypeControl = this.formBuilder.control(true);
    // this.compCodeControl = this.formBuilder.control(true);
    this.dateDocControl = this.formBuilder.control(true);
    this.dateAcctControl = this.formBuilder.control(true);
    this.amountControl = this.formBuilder.control(true);
    this.invDocNoControl = this.formBuilder.control(true);
    this.accDocNoControl = this.formBuilder.control(true);
    this.fiscalYearControl = this.formBuilder.control(true);
    this.paymentMethodControl = this.formBuilder.control(true);
    this.hdReferenceControl = this.formBuilder.control(true);
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
    this.poDocNoControl = this.formBuilder.control(false);
    // ////
    this.vendorNameControl = this.formBuilder.control(true);
    this.vendorCodeControl = this.formBuilder.control(true);
    this.bankKeyControl = this.formBuilder.control(true);
    this.bankAccountNoControl = this.formBuilder.control(true);
    this.accountHolderControl = this.formBuilder.control(true);
    this.fundTypeControl = this.formBuilder.control(true);
    // ///
    this.approveControl = this.formBuilder.control(true);
    this.notApproveControl = this.formBuilder.control(true);
    this.confirmSellerControl = this.formBuilder.control(true);
    this.botControl = this.formBuilder.control(true);
    this.reasonControl = this.formBuilder.control(true);
    this.iconStatusControl = this.formBuilder.control(true);
    this.frStatusControl = this.formBuilder.control(true);
    // this.textStatusControl = this.formBuilder.control(true);
    // this.errWtInfoControl = this.formBuilder.control(true);
    // this.deductControl = this.formBuilder.control(true);
    // this.partnerBankTypeControl = this.formBuilder.control(true);
  }

  createTableFormGroup() {
    this.tableForm = this.formBuilder.group({
      docType: this.docTypeControl,
      // compCode: this.compCodeControl,
      dateDoc: this.dateDocControl,
      dateAcct: this.dateAcctControl,
      amount: this.amountControl,
      invDocNo: this.invDocNoControl,
      accDocNo: this.accDocNoControl,
      fiscalYear: this.fiscalYearControl,
      paymentMethod: this.paymentMethodControl,
      hdReference: this.hdReferenceControl,
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
      poDocNo: this.poDocNoControl,
      // ////
      vendorName: this.vendorNameControl,
      vendorCode: this.vendorCodeControl,
      bankKey: this.bankKeyControl,
      bankAccountNo: this.bankAccountNoControl,
      accountHolder: this.accountHolderControl,
      fundType: this.fundTypeControl,
      // ///
      approve: this.approveControl,
      notApprove: this.notApproveControl,
      confirmSeller: this.confirmSellerControl,
      bot: this.botControl,
      reason: this.reasonControl,
      iconStatus: this.iconStatusControl,
      frStatus: this.frStatusControl,
      // textStatus: this.textStatusControl,
      // errWtInfo: this.errWtInfoControl,
      // deduct: this.deductControl,
      // partnerBankType: this.partnerBankTypeControl,
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
    if (!form.departmentCodeFrom) {
      this.listSearchValidate.push('กรุณา กรอกรหัสหน่วยงาน');
      // this.omFormCreate.controls.departmentCodeFrom.setValidators([Validators.required]);
      // this.omFormCreate.controls.departmentCodeFrom.updateValueAndValidity();
    }
    if (!form.provinceCodeFrom) {
      this.listSearchValidate.push('กรุณา กรอกรหัสจังหวัด');
      // this.omFormCreate.controls.provinceCodeFrom.setValidators([Validators.required]);
      // this.omFormCreate.controls.provinceCodeFrom.updateValueAndValidity();
    }
    if (!form.yearAccount) {
      this.listSearchValidate.push('กรุณา เลือกปีบัญชี');
      // this.omFormCreate.controls.fiscalYear.setValidators([Validators.required]);
      // this.omFormCreate.controls.fiscalYear.updateValueAndValidity();
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
      };
      // วันผ่านรายการ
      let postDateFrom = '';
      let postDateTo = '';
      if (form.postDateFrom) {
        const dayPostDateFrom = form.postDateFrom.getDate();
        const monthPostDateFrom = form.postDateFrom.getMonth() + 1;
        const yearPostDateFrom = form.postDateFrom.getFullYear();
        postDateFrom = this.utils.parseDate(dayPostDateFrom, monthPostDateFrom, yearPostDateFrom);
      }
      if (form.postDateTo) {
        const dayPostDateTo = form.postDateTo.getDate();
        const monthPostDateTo = form.postDateTo.getMonth() + 1;
        const yearPostDateTo = form.postDateTo.getFullYear();
        postDateTo = this.utils.parseDate(dayPostDateTo, monthPostDateTo, yearPostDateTo);
      }
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
      }
      // วันที่บันทึก
      let documentCreateDateFrom = '';
      let documentCreateDateTo = '';
      if (form.documentCreateDateFrom && form.documentCreateDateTo) {
        const dayDocumentCreateDateFrom = form.documentCreateDateFrom.getDate();
        const monthDocumentCreateDateFrom = form.documentCreateDateFrom.getMonth() + 1;
        const yearDocumentCreateDateFrom = form.documentCreateDateFrom.getFullYear();
        documentCreateDateFrom = this.utils.parseDate(
          dayDocumentCreateDateFrom,
          monthDocumentCreateDateFrom,
          yearDocumentCreateDateFrom
        );
      }
      if (form.documentCreateDateFrom && form.documentCreateDateTo) {
        const dayDocumentCreateDateTo = form.documentCreateDateTo.getDate();
        const monthDocumentCreateDateTo = form.documentCreateDateTo.getMonth() + 1;
        const yearDocumentCreateDateTo = form.documentCreateDateTo.getFullYear();
        documentCreateDateTo = this.utils.parseDate(
          dayDocumentCreateDateTo,
          monthDocumentCreateDateTo,
          yearDocumentCreateDateTo
        );
      }

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

      payload.outline = form.outline; // โครงร่าง

      this.paymentBlockService.searchColumnByRoleAndName(this.role, form.outline).then((data) => {
        const response = data as any;
        if (response.status === 200) {
          this.columnTable = JSON.parse(response.data.jsonText) as [];
        }
      });

      this.search(payload);
    }
  }

  search(payload) {
    // this.loadingScreenService.loadingToggleStatus(true)
    this.dataSource = new MatTableDataSource([]);
    this.paymentBlockService.search(payload).then((data) => {
      console.log(data);
      // this.loadingScreenService.loadingToggleStatus(false)
      // this.isDataSearchloaded = true;
      const response = data as any;
      const result = response.data;

      if (result) {
        if (result.length > 0) {
          this.dataSource = new MatTableDataSource(result);
          this.dataSource.sort = this.sort;
          this.dataSource.paginator = this.paginator;
          this.isShowSearchData = false;
          // this.selectedTabIndex = 1;
          let i = 1;
          this.dataSource.data.forEach((document) => {
            document.no = i++;
            document.approve = false;
            document.notApprove = false;
            document.reason = '';

            const dateBaseline = new Date(document.dateBaseline);

            if (dateBaseline.getTime() === this.currentDate.getTime()) {
              document.iconStatus = '1';
            } else if (dateBaseline.getTime() > this.currentDate.getTime()) {
              document.iconStatus = '2';
            } else if (dateBaseline.getTime() < this.currentDate.getTime()) {
              document.iconStatus = '3';
            }

            if (document.paymentRef != null && document.paymentRef.startsWith('001')) {
              document.bot = true;
            } else {
              document.bot = false;
            }

            if (document.paymentRef != null) {
              document.bankKey = document.paymentRef.substr(0, 6);
            }
          });
        } else if (result.length > 1000) {
          this.isShowSearchData = true;
          this.listMessageResponse.push(
            'ไม่สามารถแสดงผลการค้นหาเกิน 500 รายการได้ กรุณาเปลี่ยนเงื่อนไขการค้นหาใหม่'
          );
        } else {
          this.isShowSearchData = true;
          this.listMessageResponse.push('ไม่พบเอกสาร');
        }
      } else {
        this.isShowSearchData = true;
        this.listMessageResponse.push('ไม่พบเอกสาร');
      }
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

  openDialogDetailDocument(document) {
    const dialogRef = this.dialog.open(DialogDetailDocumentComponent, {
      width: '1200px',
      data: {
        document,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
      }
    });
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
          this.departmentCodeFromBindingName = result.name;
        } else if (type === 'departmentCodeTo') {
          this.departmentCodeToBindingName = result.name;
        } else if (type === 'provinceCodeFrom') {
          this.provinceCodeFromBindingName = result.name;
        } else if (type === 'provinceCodeTo') {
          this.provinceCodeToBindingName = result.name;
        } else if (type === 'vendorTaxIdFrom') {
          this.vendorTaxIdFromBindingName = result.name;
        } else if (type === 'vendorTaxIdTo') {
          this.vendorTaxIdToBindingName = result.name;
        } else if (type === 'disbursementCodeFrom') {
          this.departmentCodeFromBindingName = result.name;
        } else if (type === 'disbursementCodeTo') {
          this.disbursementCodeToBindingName = result.name;
        } else if (type === 'payMethodFrom') {
        } else if (type === 'payMethodTo') {
        } else if (type === 'specialTypeFrom') {
        } else if (type === 'specialTypeTo') {
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

  setReason(row, index) {
    this.dataSource.data[row.no - 1].reason = this.reasonInput.toArray()[index].nativeElement.value;
  }

  onClickCheckedDocument(event, type, row, index) {
    console.log(index);
    console.log(type);
    console.log(row);
    event.stopPropagation();
    this.reasonInput.toArray()[index].nativeElement.value = '';
    console.log(this.selectionApprove.selected);
    if (type === 'approve') {
      this.selectionNotApprove.deselect(row);
    } else {
      this.selectionApprove.deselect(row);
    }
  }

  onPreSave() {
    const listItem = [];
    this.listValidate = [];
    this.dataSource.data.forEach((item) => {
      item.approve = false;
      item.notApprove = false;
      const webInfo = {
        fiArea: '1000',
        compCode: '03003',
        ipAddress: '127.0.0.1',
        paymentCenter: '0300300003',
        userWebOnline: 'A030030000310',
        authPaymentCenter: [],
        authCostCenter: [],
        authFIArea: [],
        authCompanyCode: [],
      };
      const data = {
        docType: item.docType,
        compCode: item.compCode,
        accDocNo: item.accDocNo,
        fiscalYear: item.fiscalYear,
        approve: null,
        reason: item.reason,
        webInfo,
        postDate: new Date(),
        valueOld: 'B',
        valueNew: '',
        userPost: item.userPost,
        username: 'A1231234124',
      };

      if (this.selectionApprove.isSelected(item)) {
        item.approve = this.selectionApprove.isSelected(item);
        data.valueNew = '';
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

    listItem.forEach((data) => {
      if (!data.approve && !data.reason) {
        this.listValidate.push('เลขที่เอกสาร ' + data.accDocNo + ' ไม่ได้ระบุเหตุผล');
      }
    });

    console.log(listItem);
    if (this.listValidate.length <= 0) {
      // DialogOmChangePaymentBlockComponent
      if (listItem.length > 0) {
        const dialogRef = this.dialog.open(DialogOmChangePaymentBlockComponent, {
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
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.setDataToOmForm(result.value.jsonText);
      }
    });
  }

  openDialogOmSaveSearchCriteria() {
    const dialogRef = this.dialog.open(DialogOmSaveSearchCriteriaComponent, {
      data: {
        role: this.role,
        value: this.omFormCreate.value,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Success') {
        }
      }
    });
  }

  openDialogOmSearchColumn() {
    const dialogRef = this.dialog.open(DialogOmSearchColumnTableComponent, {
      data: {
        role: this.role,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.omFormCreate.patchValue({
          outline: result.value.name, // โครงร่าง
        });
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
    });
  }

  // setDataToColumnTableForm(data) {
  //   const jsonObject = JSON.parse(data);
  //
  //
  //   this.tableForm.patchValue({
  //     docType: jsonObject.docType,
  //     // compCode: this.compCodeControl,
  //     dateDoc: jsonObject.dateDoc,
  //     dateAcct: jsonObject.dateAcct,
  //     amount: jsonObject.amount,
  //     invDocNo: jsonObject.invDocNo,
  //     accDocNo: jsonObject.accDocNo,
  //     fiscalYear: jsonObject.fiscalYear,
  //     paymentMethod: jsonObject.paymentMethod,
  //     hdReference: jsonObject.hdReference,
  //     userPost: jsonObject.userPost,
  //     fiArea: jsonObject.fiArea,
  //     costCenter: jsonObject.costCenter,
  //     fundSource: jsonObject.fundSource,
  //     bgCode: jsonObject.bgCode,
  //     assignment: jsonObject.assignment,
  //     brLine: jsonObject.brLine,
  //     subAccount: jsonObject.subAccount,
  //     subAccountOwner: jsonObject.subAccountOwner,
  //     depositAccount: jsonObject.depositAccount,
  //     depositAccountOwner: jsonObject.depositAccountOwner,
  //     lineItemText: jsonObject.lineItemText,
  //
  //     specialGL: jsonObject.specialGL,
  //     dateBaseLine: jsonObject.dateBaseLine,
  //     reference2: jsonObject.reference2,
  //     poDocNo: jsonObject.poDocNo,
  //     // ////
  //     vendorName: jsonObject.vendorName,
  //     vendorCode: jsonObject.vendorCode,
  //     // bankKey: jsonObject.bankKey,
  //     bankAccountNo: jsonObject.bankAccountNo,
  //     accountHolder: jsonObject.accountHolder,
  //     // fundType: jsonObject.fundType,
  //     // ///
  //     approve: jsonObject.approve,
  //     notApprove: jsonObject.notApprove,
  //     // confirmSeller: jsonObject.confirmSeller,
  //     bot: jsonObject.bot,
  //     reason: jsonObject.reason,
  //     iconStatus: jsonObject.iconStatus,
  //     frStatus: jsonObject.frStatus,
  //     // textStatus: jsonObject.textStatus,
  //     // errWtInfo: jsonObject.errWtInfo,
  //     // deduct: jsonObject.deduct,
  //     // partnerBankType: jsonObject.partnerBankType,
  //   });
  //
  //   this.loaderService.loadingToggleStatus(true);
  //   setTimeout(() => {
  //     this.columnTable = [];
  //     this.columnTable.push({
  //       key: 'list',
  //       columnName: 'รายการ',
  //       showColumn: true,
  //       seq: 0,
  //     });
  //     for (const key in this.tableForm.controls) {
  //       const data = this.constant.LIST_TABLE_OM.get(key);
  //       // console.log(data);
  //       this.columnTable.push({
  //         key,
  //         columnName: data.name,
  //         showColumn: this.tableForm.controls[key].value,
  //         seq: data.seq,
  //       });
  //     }
  //     this.loaderService.loadingToggleStatus(false);
  //   }, 1000); // Execute something() 1 second later.
  // }

  openDialogOmSaveColumnTable() {
    console.log(this.tableForm.value);
    const dialogRef = this.dialog.open(DialogOmSaveColumnTableComponent, {
      data: {
        role: this.role,
        value: this.columnTable,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Success') {
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
    this.omFormCreate.patchValue({
      departmentCodeFrom: '', // รหัสหน่วยงาน
      departmentCodeTo: '', // รหัสหน่วยงาน
      provinceCodeFrom: '', // รหัสจังหวัด
      provinceCodeTo: '', // รหัสจังหวัด
      yearAccount: '', // ปีบัญชี
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

      outline: '', // โครงร่าง
    });
  }

  defaultOmFormForCGD() {
    this.isDisablePayMethod = false;
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

      outline: '', // โครงร่าง
    });
  }

  defaultOmFormForAreaTreatMent() {
    this.isDisablePayMethod = true;
    this.omFormCreate.patchValue({
      departmentCodeFrom: '01001', // รหัสหน่วยงาน
      departmentCodeTo: '01012', // รหัสหน่วยงาน
      provinceCodeFrom: '1000', // รหัสจังหวัด
      provinceCodeTo: '9999', // รหัสจังหวัด
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

      outline: '', // โครงร่าง
    });
  }

  defaultColumnTableForm() {
    this.tableForm.patchValue({
      docType: true,
      // compCode: this.compCodeControl,
      dateDoc: true,
      dateAcct: true,
      amount: true,
      invDocNo: true,
      accDocNo: true,
      fiscalYear: true,
      paymentMethod: true,
      hdReference: true,
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
      poDocNo: false,
      // ////
      vendorName: false,
      vendorCode: false,
      bankKey: false,
      bankAccountNo: false,
      accountHolder: false,
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
    if (this.role === 'USER_PTO') {
      this.defaultOmFormForArea();
    } else if (this.role === 'USER_CGD') {
      this.defaultOmFormForCGD();
    } else if (this.role === 'USER_PTO_H') {
      this.defaultOmFormForAreaTreatMent();
    }
  }
}
