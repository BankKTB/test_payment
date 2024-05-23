import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { LocalStorageService, SidebarService } from '@core/services';
import { MatDialog } from '@angular/material/dialog';
import { DialogOmLogComponent } from '@shared/component/dialog-om-log/dialog-om-log.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Utils } from '@shared/utils/utils';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { map, startWith } from 'rxjs/operators';
import { Constant } from '@shared/utils/constant';
import { Observable } from 'rxjs';
import { DialogOmDocTypeComponent } from '@shared/component/dialog-om-doc-type/dialog-om-doc-type.component';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { UserProfile } from '@core/models/user-profile';
import { DialogOmSearchDetailComponent } from '@shared/component/dialog-om-search-detail/dialog-om-search-detail.component';
import { FiService } from '@core/services/fi/fi.service';
@Component({
  selector: 'app-om-log',
  templateUrl: './om-log.component.html',
  styleUrls: ['./om-log.component.scss'],
})
export class OmLogComponent implements OnInit, AfterViewInit {
  @ViewChild(MatSort, { static: false }) sort: MatSort;
  @ViewChild(MatPaginator, { static: false }) paginator: MatPaginator;

  dataSource = new MatTableDataSource([]);
  panelExpanded = true;
  omForm: FormGroup;
  departmentCodeFromControl: FormControl; // รหัสหน่วยงาน
  departmentCodeToControl: FormControl; // รหัสหน่วยงาน
  docTypeFromControl: FormControl; // ประเภทเอกสาร
  docTypeToControl: FormControl; // ประเภทเอกสาร
  accDocNoFromControl: FormControl;
  accDocNoToControl: FormControl;
  postDateFromControl: FormControl;
  postDateToControl: FormControl;
  approveDateFromControl: FormControl;
  approveDateToControl: FormControl;
  yearAccountFromControl: FormControl; // ปีบัญชี
  yearAccountToControl: FormControl; // ปีบัญชี
  valueOldControl: FormControl;
  valueNewControl: FormControl;
  displayedColumns: string[] = [
    'chooseOmDetail',
    'docType',
    'compCode',
    'accDocNo',
    'fiscalYear',
    'valueOld',
    'valueNew',
    'reason',
    'userPost',
    'postDate',
    'approveDate',
    'username',
  ];
  isSubmittedForm = false;
  report = null;
  isDisableTable = false;
  isDisableValue = true;
  role = '';
  listSearchValidate = [];
  filteredOptionsDocTypeFrom: Observable<string[]>;
  filteredOptionsDocTypeTo: Observable<string[]>;
  listDocType = [{ docTypeFrom: null, docTypeTo: null, optionExclude: false }];
  listCompanyCode = [{ companyCodeFrom: null, companyCodeTo: null, optionExclude: false }];
  listPaymentMethod = [{ paymentMethodFrom: null, paymentMethodTo: null, optionExclude: false }];
  optionExcludeCompanyCode = 'N';
  optionExcludeDocType = 'N';
  optionExcludePaymentMethod = 'N';
  userProfile: UserProfile = null;
  constructor(
    private paymentBlockService: PaymentBlockService,
    private sidebarService: SidebarService,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    public utils: Utils,
    private constant: Constant,
    private localStorageService: LocalStorageService,
    private fiService: FiService
  ) {}

  ngOnInit() {
    this.dataSource = new MatTableDataSource([]);
    this.sidebarService.updatePageType('om');
    this.sidebarService.updateNowPage('omLog');
    this.userProfile = this.localStorageService.getUserProfile();
    this.utils.listYear = this.utils.CalculateYear();
    this.utils.fiscYear = this.utils.CalculateFiscYear(new Date());
    this.createFormControl();
    this.createFormGroup();
    this.defaultInput();
    this.filteredOptionsDocTypeFrom = this.docTypeFromControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter3(value))
    );
    this.filteredOptionsDocTypeTo = this.docTypeToControl.valueChanges.pipe(
      startWith(''),
      map((value) => this._filter3(value))
    );
  }

  ngAfterViewInit() {
    this.dataSource = new MatTableDataSource([]);
    this.dataSource.sort = this.sort;
    this.dataSource.paginator = this.paginator;
  }

  createFormControl() {
    this.departmentCodeFromControl = this.formBuilder.control('', [Validators.required]); // รหัสหน่วยงาน
    this.departmentCodeToControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.docTypeFromControl = this.formBuilder.control('');
    this.docTypeToControl = this.formBuilder.control('');
    this.accDocNoFromControl = this.formBuilder.control('');
    this.accDocNoToControl = this.formBuilder.control('');
    this.postDateFromControl = this.formBuilder.control('');
    this.postDateToControl = this.formBuilder.control('');
    this.approveDateFromControl = this.formBuilder.control('');
    this.approveDateToControl = this.formBuilder.control('');
    this.yearAccountFromControl = this.formBuilder.control('');
    this.yearAccountToControl = this.formBuilder.control('');
    this.valueOldControl = this.formBuilder.control('');
    this.valueNewControl = this.formBuilder.control('');
  }

  createFormGroup() {
    this.omForm = this.formBuilder.group({
      departmentCodeFrom: this.departmentCodeFromControl,
      departmentCodeTo: this.departmentCodeToControl,
      docTypeFrom: this.docTypeFromControl,
      docTypeTo: this.docTypeToControl,
      accDocNoFrom: this.accDocNoFromControl,
      accDocNoTo: this.accDocNoToControl,
      postDateFrom: this.postDateFromControl,
      postDateTo: this.postDateToControl,
      approveDateFrom: this.approveDateFromControl,
      approveDateTo: this.approveDateToControl,
      yearAccountFrom: this.yearAccountFromControl,
      yearAccountTo: this.yearAccountToControl,
      valueOld: this.valueOldControl,
      valueNew: this.valueNewControl,
    });
  }

  defaultInput() {
    this.isSubmittedForm = false;
    this.listSearchValidate = [];
    this.isDisableTable = false;
    this.omForm.patchValue({
      departmentCodeFrom: '',
      departmentCodeTo: '',
      docTypeFrom: '',
      docTypeTo: '',
      accDocNoFrom: '',
      accDocNoTo: '',
      yearAccountFrom: this.utils.fiscYear,
      yearAccountTo: this.utils.fiscYear,
      valueOld: 'B',
      valueNew: '',
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.omForm.patchValue({ [result.type]: result.value });
        if (type === 'departmentCodeFrom') {
          // this.departmentCodeFromBindingName = result.name;
        } else if (type === 'departmentCodeTo') {
          // this.departmentCodeToBindingName = result.name;
        } else if (type === 'docTypeFrom') {
          // this.provinceCodeFromBindingName = result.name;
        } else if (type === 'docTypeTo') {
          // this.provinceCodeToBindingName = result.name;
        } else if (type === 'accDocNoFrom') {
          // this.vendorTaxIdFromBindingName = result.name;
        } else if (type === 'accDocNoTo') {
          // this.vendorTaxIdToBindingName = result.name;
        } else if (type === 'yearAccountFrom') {
          // this.departmentCodeFromBindingName = result.name;
        } else if (type === 'yearAccountTo') {
          // this.disbursementCodeToBindingName = result.name;
        } else if (type === 'payMethodFrom') {
        } else if (type === 'payMethodTo') {
        } else if (type === 'specialTypeFrom') {
        } else if (type === 'specialTypeTo') {
        }
      }
    });
  }

  onSearch() {
    const form = this.omForm.value;
    this.listSearchValidate = [];
    this.report = null;
    if (!form.departmentCodeFrom) {
      this.listSearchValidate.push('กรุณา กรอก รหัสหน่วยงาน');
    }
    this.isSubmittedForm = true;
    if (this.listSearchValidate.length <= 0) {
      this.isSubmittedForm = false;
      const payload = {
        companyCodeFrom: '',
        companyCodeTo: '',
        originalDocumentNoFrom: '',
        originalDocumentNoTo: '',
        originalFiscalYearFrom: '',
        originalFiscalYearTo: '',
        documentTypeFrom: '',
        documentTypeTo: '',
        valueOld: '',
        valueNew: '',
        dateAcctFrom: '',
        dateAcctTo: '',
        unblockDateFrom: '',
        unblockDateDateTo: '',
        // listCompanyCode: this.listCompanyCode,
        // listDocType: this.listDocType,
        // listPaymentMethod: this.listPaymentMethod,
      };
      payload.companyCodeFrom = form.departmentCodeFrom;
      payload.companyCodeTo = form.departmentCodeTo;
      payload.originalDocumentNoFrom = form.accDocNoFrom;
      payload.originalDocumentNoTo = form.accDocNoTo;
      payload.originalFiscalYearFrom = this.utils.convertYearToAD(form.yearAccountFrom);
      payload.originalFiscalYearTo = this.utils.convertYearToAD(form.yearAccountTo);
      payload.documentTypeFrom = form.docTypeFrom;
      payload.documentTypeTo = form.docTypeTo;
      payload.valueNew = form.valueNew;
      payload.valueOld = form.valueOld;

      if (form.postDateFrom) {
        const dayPostDateFrom = form.postDateFrom.getDate();
        const monthPostDateFrom = form.postDateFrom.getMonth() + 1;
        const yearPostDateFrom = form.postDateFrom.getFullYear();
        payload.dateAcctFrom = this.utils.parseDate(
          dayPostDateFrom,
          monthPostDateFrom,
          yearPostDateFrom
        );
      }
      if (form.postDateTo) {
        const dayPostDateTo = form.postDateTo.getDate();
        const monthPostDateTo = form.postDateTo.getMonth() + 1;
        const yearPostDateTo = form.postDateTo.getFullYear();
        payload.dateAcctTo = this.utils.parseDate(dayPostDateTo, monthPostDateTo, yearPostDateTo);
      }
      if (form.approveDateFrom) {
        const dayApproveDateFrom = form.approveDateFrom.getDate();
        const monthApproveDateFrom = form.approveDateFrom.getMonth() + 1;
        const yearApproveDateFrom = form.approveDateFrom.getFullYear();
        payload.unblockDateFrom = this.utils.parseDate(
          dayApproveDateFrom,
          monthApproveDateFrom,
          yearApproveDateFrom
        );
      }
      if (form.approveDateTo) {
        const dayApproveDateTo = form.approveDateTo.getDate();
        const monthApproveDateTo = form.approveDateTo.getMonth() + 1;
        const yearApproveDateTo = form.approveDateTo.getFullYear();
        payload.unblockDateDateTo = this.utils.parseDate(
          dayApproveDateTo,
          monthApproveDateTo,
          yearApproveDateTo
        );
      }
      this.search(payload);
    }
  }

  search(payload) {
    this.paymentBlockService.getPaymentBlockLog(payload).then((result) => {
      if (result.status === 200) {
        const response = result.data.filter(
          (e) =>
            e.companyCode !== null && e.originalDocumentNo !== null && e.originalFiscalYear !== null
        );
        this.dataSource = new MatTableDataSource(response);
        this.isDisableTable = true;
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      } else {
        this.listSearchValidate.push('ไม่พบข้อมูลที่ต้องการค้นหา');
        this.isDisableTable = false;
      }
    });
  }

  clearInputByRole() {
    if (this.role === '') {
      this.defaultInput();
      // } else if (this.role === 'USER_CGD') {
      //   this.defaultOmFormForCGD();
      // } else if (this.role === 'USER_PTO_H') {
      //   this.defaultOmFormForAreaTreatMent();
      // }
    }
  }

  openDialogOmLogDocument(document) {
    if (document.valueNew === 'E') {
      const dialogRef = this.dialog.open(DialogOmLogComponent, {
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
  }

  _filter3(value: string): string[] {
    const filterValue = value.toLowerCase();
    return this.constant.DOC_TYPE.filter((option) =>
      option.name.toLowerCase().includes(filterValue)
    );
  }

  setDocType(type) {
    const form = this.omForm.value;
    if (type === 'docTypeFrom') {
      if (form.docTypeFrom) {
        this.listDocType[0].docTypeFrom = form.docTypeFrom;
      } else {
        if (this.listDocType.length === 1) {
          this.listDocType = [{ docTypeFrom: null, docTypeTo: null, optionExclude: false }];
        } else {
          this.listDocType.splice(0, 1);
          this.omForm.patchValue({
            docTypeFrom: this.listDocType[0].docTypeFrom,
          });
        }
      }
    } else if (type === 'docTypeTo') {
      if (form.docTypeTo) {
        this.listDocType[0].docTypeTo = form.docTypeTo;
      }
    }
    this.checkOptionExclude();
  }

  openDialogOmDocType() {
    const dialogRef = this.dialog.open(DialogOmDocTypeComponent, {
      data: {
        listDocType: this.listDocType,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          this.listDocType = result.value;
          this.omForm.patchValue({
            docTypeFrom: this.listDocType[0].docTypeFrom, // รหัสหน่วยงาน
            docTypeTo: this.listDocType[0].docTypeTo, // รหัสหน่วยงาน
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  checkOptionExclude() {
    this.optionExcludeCompanyCode = this.listCompanyCode.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
    this.optionExcludeDocType = this.listDocType.find((data) => data.optionExclude) ? 'Y' : 'N';
    this.optionExcludePaymentMethod = this.listPaymentMethod.find((data) => data.optionExclude)
      ? 'Y'
      : 'N';
  }

  clearInput(inputForm) {
    this.omForm.controls[inputForm].setValue('');
  }

  chooseDataSearchDetail(element) {
    const webInfo = this.localStorageService.getWebInfo();
    const payload = {
      compCode: element.companyCode,
      docNo: element.originalDocumentNo,
      fiscalYear: element.originalFiscalYear,
      webInfo: webInfo,
    };
    this.fiService.paymentBlockDetail(payload).then((data) => {
      const response = data as any;
      if (response.status === 200) {
        const dialogRef = this.dialog.open(DialogOmSearchDetailComponent, {
          width: '80vw',
          data: {
            item: response.data,
          },
        });
      }
    });
  }
}
