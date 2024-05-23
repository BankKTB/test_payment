import { MatTableDataSource } from '@angular/material/table';
import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatSort } from '@angular/material';
import { MasterService } from '@core/services';
import { Constant } from '@shared/utils/constant';

export interface DialogData {
  type: string;
  title: string;
  alternativeVendor: boolean;
  paymentMethodType: string;
  taxId: string;
  specialCase: {
    departmentCode: '';
    disbursementCode: '';
    areaCode: '';
    bankCode: '';
    vendorTaxId: '';
    year: '';
    condition: '';
    formId: '';
  };
}

@Component({
  selector: 'app-dialog-search-master-data',
  templateUrl: './dialog-search-master-data.component.html',
  styleUrls: ['./dialog-search-master-data.component.scss'],
})
export class DialogSearchMasterDataComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  title = 'ค้นหา';
  displayedColumns: string[] = [];
  dataSource = new MatTableDataSource([]);

  errorMessage = '';

  isShowTable = false;
  button = 'Submit';
  isLoading = false;

  constructor(
    private dialogRef: MatDialogRef<DialogSearchMasterDataComponent>,
    private masterService: MasterService,
    private constant: Constant,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  closeDialog(): void {
    const dialogRef = this.dialogRef.close();
  }

  click() {
    this.isLoading = true;
    this.button = 'Processing';

    setTimeout(() => {
      this.isLoading = false;
      this.button = 'Submit';
    }, 2400);
  }

  ngOnInit() {
    // this.loadVendor('')
    this.setTitle();
  }

  loadDepartment(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    this.masterService.findCompanyCodeWithParam(text).then((result) => {
      const response = result as any;
      console.log(response);

      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadArea(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findAreaCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'fiArea', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadPaymentCenter(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findPaymentCenterCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'description'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadVendor(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findVendorCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          console.log(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'taxId', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadSpecialGL(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findSpecialGLCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          console.log(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadDocType(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findDocTypeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          console.log(response.data);
          this.displayedColumns = ['choose', 'name', 'description'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadCountry(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findCountryCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadVendorBankAccount(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService
      .findVendorBankAccountWithParam(
        text,
        this.data.alternativeVendor,
        this.data.paymentMethodType
      )
      .then((result) => {
        const response = result as any;
        // this.isLoading = false;
        if (response.status === 200) {
          if (!response.message) {
            this.dataSource = new MatTableDataSource(response.data);
            console.log(response.data);

            this.displayedColumns = [
              'choose',
              'bankAccountNo',
              'routingNo',
              'vendor',
              'vendorName',
              'active',
            ];
            this.dataSource.sort = this.sort;
            this.isShowTable = true;
          } else {
            this.errorMessage = response.message;
          }
        } else if (response.status === 413) {
          const error = response.error;
          this.errorMessage = error.message;
        } else if (response.status === 404) {
          const error = response.error;
          this.errorMessage = error.message;
        } else if (response.status === 500) {
          const error = response.error;
          this.errorMessage = error.message;
        }
      });
  }

  loadBankBranch(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findBankBranchCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'description'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadPaymentMethod(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findPaymentMethodCodeWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'name'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  loadCurrency(textSearch) {
    const text = this.checkStarAndPercent(textSearch);
    if (text === 'error') {
      return;
    }
    this.errorMessage = '';
    this.isShowTable = false;
    // this.isLoading = true;
    this.masterService.findCurrencyWithParam(text).then((result) => {
      const response = result as any;
      // this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = new MatTableDataSource(response.data);
          this.displayedColumns = ['choose', 'valueCode', 'description'];
          this.dataSource.sort = this.sort;
          this.isShowTable = true;
        } else {
          this.errorMessage = response.message;
        }
      } else if (response.status === 413) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 404) {
        const error = response.error;
        this.errorMessage = error.message;
      } else if (response.status === 500) {
        const error = response.error;
        this.errorMessage = error.message;
      }
    });
  }

  setTitle() {
    console.log(this.data);
    if (this.data.type) {
      if (
        this.data.type === 'departmentCodeFrom' ||
        this.data.type === 'departmentCodeTo' ||
        this.data.type === 'companyCode' ||
        this.data.type === 'companyCodeFrom' ||
        this.data.type === 'companyCodeFromTo'
      ) {
        this.title = 'รหัสหน่วยงาน';
      } else if (
        this.data.type === 'provinceCodeFrom' ||
        this.data.type === 'provinceCodeTo' ||
        this.data.type === 'fiAreaFrom' ||
        this.data.type === 'fiAreaTo'
      ) {
        this.title = 'รหัสจังหวัด';
      } else if (
        this.data.type === 'disbursementCodeFrom' ||
        this.data.type === 'disbursementCodeTo'
      ) {
        this.title = 'รหัสหน่วยเบิกจ่าย';
      } else if (
        this.data.type === 'vendorTaxIdFrom' ||
        this.data.type === 'vendorTaxIdTo' ||
        this.data.type === 'vendorFrom' ||
        this.data.type === 'vendorTo'
      ) {
        this.title = 'รหัสผู้ขาย';
      } else if (this.data.type === 'country') {
        this.title = 'ประเทศ';
      } else if (this.data.type === 'bankBranch') {
        this.title = 'คีย์ธนาคาร';
      } else if (
        this.data.type === 'paymentMethodFrom' ||
        this.data.type === 'paymentMethodTo' ||
        this.data.type === 'paymentMethod' ||
        this.data.type === 'payMethodFrom' ||
        this.data.type === 'payMethodTo'
      ) {
        this.title = 'วิธีชำระเงิน';
      } else if (this.data.type === 'currency') {
        this.title = 'สกุลเงิน';
      } else if (this.data.type === 'specialTypeFrom' || this.data.type === 'specialTypeTo') {
        this.title = 'แยกประเภทพิเศษ';
      } else if (this.data.type === 'docTypeFrom' || this.data.type === 'docTypeTo') {
        this.title = 'ประเภทเอกสาร';
      } else if (this.data.type === 'bankAccountNo') {
        this.title = 'เลขที่บัญชีธนาคาร';
        this.loadVendorBankAccount(this.data.taxId);
      }
    }
  }

  search(e) {
    this.errorMessage = '';
    this.dataSource = new MatTableDataSource([]);
    if (
      this.data.type === 'departmentCodeFrom' ||
      this.data.type === 'departmentCodeTo' ||
      this.data.type === 'companyCode' ||
      this.data.type === 'companyCodeFrom' ||
      this.data.type === 'companyCodeFromTo'
    ) {
      this.loadDepartment(e.value);
    } else if (
      this.data.type === 'provinceCodeFrom' ||
      this.data.type === 'provinceCodeTo' ||
      this.data.type === 'fiAreaFrom' ||
      this.data.type === 'fiAreaTo'
    ) {
      this.loadArea(e.value);
    } else if (
      this.data.type === 'disbursementCodeFrom' ||
      this.data.type === 'disbursementCodeTo'
    ) {
      this.loadPaymentCenter(e.value);
    } else if (
      this.data.type === 'vendorTaxIdFrom' ||
      this.data.type === 'vendorTaxIdTo' ||
      this.data.type === 'vendorFrom' ||
      this.data.type === 'vendorTo'
    ) {
      this.loadVendor(e.value);
    } else if (this.data.type === 'country') {
      this.loadCountry(e.value);
    } else if (this.data.type === 'bankBranch') {
      this.loadBankBranch(e.value);
    } else if (
      this.data.type === 'paymentMethodFrom' ||
      this.data.type === 'paymentMethodTo' ||
      this.data.type === 'paymentMethod' ||
      this.data.type === 'payMethodFrom' ||
      this.data.type === 'payMethodTo'
    ) {
      this.loadPaymentMethod(e.value);
    } else if (this.data.type === 'currency') {
      this.loadCurrency(e.value);
    } else if (this.data.type === 'specialTypeFrom' || this.data.type === 'specialTypeTo') {
      this.loadSpecialGL(e.value);
    } else if (this.data.type === 'docTypeFrom' || this.data.type === 'docTypeTo') {
      this.loadDocType(e.value);
    } else if (this.data.type === 'bankAccountNo') {
      this.loadVendorBankAccount(e.value);
    }
  }

  chooseDataSearch(value, name) {
    // this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      type: this.data.type,
      value,
      name,
    });
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  checkStarAndPercent(textSearch) {
    console.log(textSearch);
    const slash = textSearch.split('').filter((value) => value === '/').length;
    if (slash > 0) {
      for (let i = 0; i < slash; i++) {
        textSearch = textSearch.replace('/', '');
      }
    }
    console.log(textSearch);

    const percent = textSearch.split('').filter((value) => value === '%').length;
    if (percent > 0) {
      for (let i = 0; i < percent; i++) {
        textSearch = textSearch.replace('%', '*');
      }
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar > 2) {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return 'error';
      } else {
        const checkCondtion = textSearch.indexOf('**');
        if (checkCondtion === -1) {
          if (textSearch === '*' || textSearch === '**') {
            this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
            return 'error';
          }
        } else {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return 'error';
        }
      }
    } else {
      const textStar = textSearch.split('').filter((value) => value === '*').length;
      if (textStar === 0) {
        textSearch = '*' + textSearch + '*';
      }
    }
    return textSearch;
  }
}
