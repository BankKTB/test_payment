import { Component, EventEmitter, Inject, OnInit, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

import { MatSnackBar } from '@angular/material/snack-bar';
import { Constant } from '@shared/utils/constant';
import { MasterService } from '@core/services';
import { Utils } from '@shared/utils/utils';

export interface DialogData {
  type: string;
  title: string;
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
  selector: 'app-dialog-search-master',
  templateUrl: './dialog-search-master.component.html',
  styleUrls: ['./dialog-search-master.component.scss'],
})
export class DialogSearchMasterComponent implements OnInit {
  @Output() submitClicked = new EventEmitter<any>();
  title = 'ค้นหา';
  errorMessage = '';
  dataSource = [];
  dataSourceHeader = [];
  searchType = '';
  isLoading = false;

  isConditionShowSourceBudgetCode = false;

  userProfile: any;

  listDocument = [
    { code: '07.11.2019', pay: 'H0001', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '07.11.2019', pay: 'H0002', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '29.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '29.10.2019', pay: 'Z-DI2', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '29.10.2019', pay: 'Z-IN1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '10.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '10.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '10.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '10.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
    { code: '10.10.2019', pay: 'Z-DI1', nextpay: 'ผ่านรายการการชำระเงินแล้ว' },
  ];

  checkTest = true;

  constructor(
    public dialogRef: MatDialogRef<DialogSearchMasterComponent>,
    public constant: Constant,
    private masterService: MasterService,
    private utils: Utils,
    private _snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.disableClose = false;
  }

  ngOnInit() {
    // this.userProfile = this.localStorageService.getUserProfile();

    this.setTitle();
    this.searchType = this.data.type;
  }

  openSnackBar() {
    this._snackBar.open('เกิดความผิดพลาดขึ้น', '', {
      panelClass: '_warning',
    });
  }

  openSnackBar2() {
    this._snackBar.open('บันทึกเรียบร้อย', '', {
      panelClass: '_success',
    });
  }

  setTitle() {
    if (this.data.type) {
      if (this.data.type === 'departmentCodeFrom' || this.data.type === 'departmentCodeTo') {
        this.title = 'รหัสหน่วยงาน';
      } else if (this.data.type === 'provinceCodeFrom' || this.data.type === 'provinceCodeTo') {
        this.title = 'รหัสจังหวัด';
      } else if (
        this.data.type === 'disbursementCodeFrom' ||
        this.data.type === 'disbursementCodeTo'
      ) {
        this.title = 'รหัสหน่วยเบิกจ่าย';
      } else if (this.data.type === 'vendorTaxIdFrom' || this.data.type === 'vendorTaxIdTo') {
        this.title = 'รหัสผู้ขาย';
      }
    }
  }

  search(e) {
    this.errorMessage = '';
    this.dataSource = [];
    if (this.data.type === 'departmentCodeFrom' || this.data.type === 'departmentCodeTo') {
      this.loadDepartment(e.value);
    } else if (this.data.type === 'provinceCodeFrom' || this.data.type === 'provinceCodeTo') {
      this.loadArea(e.value);
    } else if (
      this.data.type === 'disbursementCodeFrom' ||
      this.data.type === 'disbursementCodeTo'
    ) {
      this.loadPaymentCenter(e.value);
    } else if (this.data.type === 'vendorTaxIdFrom' || this.data.type === 'vendorTaxIdTo') {
      this.loadVendor(e.value);
    }
  }

  onNoClick(): void {
    this.errorMessage = '';
    this.dialogRef.close({
      event: false,
      type: this.data.type,
      value: '',
    });
  }

  loadDepartment(textSearch) {
    const percent = textSearch.split('').filter((value) => value === '%').length;
    for (let i = 0; i < percent; i++) {
      textSearch = textSearch.replace('%', '*');
    }
    const text = textSearch.split('').filter((value) => value === '*').length;
    if (text > 2) {
      this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
      return false;
    } else {
      const checkCondtion = textSearch.indexOf('**');

      if (checkCondtion === -1) {
        if (textSearch === '*' || textSearch === '**') {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return false;
        }
      } else {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return false;
      }
    }
    this.errorMessage = '';
    this.isLoading = true;
    this.masterService.findCompanyCodeWithParam(textSearch).then((result) => {
      const response = result as any;
      console.log(response);
      this.isLoading = false;

      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = response.data;
          this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.companyCode;
        } else {
          this.errorMessage = response.message;
        }
      } else {
        this.errorMessage = response.message;
      }
    });
  }

  loadArea(textSearch) {
    const percent = textSearch.split('').filter((value) => value === '%').length;
    for (let i = 0; i < percent; i++) {
      textSearch = textSearch.replace('%', '*');
    }
    const text = textSearch.split('').filter((value) => value === '*').length;
    if (text > 2) {
      this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
      return false;
    } else {
      const checkCondtion = textSearch.indexOf('**');

      if (checkCondtion === -1) {
        if (textSearch === '*' || textSearch === '**') {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return false;
        }
      } else {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return false;
      }
    }
    this.errorMessage = '';
    this.isLoading = true;
    this.masterService.findAreaCodeWithParam(textSearch).then((result) => {
      const response = result as any;
      this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = response.data;
          this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.areaCode;
        } else {
          this.errorMessage = response.message;
        }
      } else {
        this.errorMessage = response.message;
      }
    });
  }

  loadPaymentCenter(textSearch) {
    const percent = textSearch.split('').filter((value) => value === '%').length;
    for (let i = 0; i < percent; i++) {
      textSearch = textSearch.replace('%', '*');
    }
    const text = textSearch.split('').filter((value) => value === '*').length;
    if (text > 2) {
      this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
      return false;
    } else {
      const checkCondtion = textSearch.indexOf('**');

      if (checkCondtion === -1) {
        if (textSearch === '*' || textSearch === '**') {
          this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
          return false;
        }
      } else {
        this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
        return false;
      }
    }
    this.errorMessage = '';
    this.isLoading = true;
    this.masterService.findPaymentCenterCodeWithParam(textSearch).then((result) => {
      const response = result as any;
      this.isLoading = false;
      if (response.status === 200) {
        if (!response.message) {
          this.dataSource = response.data;
          this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.disbursementCode;
        } else {
          this.errorMessage = response.message;
        }
      } else {
        this.errorMessage = response.message;
      }
    });
  }

  loadVendor(textSearch) {
    this.dataSource = this.listDocument;
    this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.vendorTaxId;
    // const percent = textSearch.split('').filter(value => value === '%').length;
    // for (let i = 0; i < percent; i++) {
    //   textSearch = textSearch.replace('%', '*');
    // }
    // const text = textSearch.split('').filter(value => value === '*').length;
    // if (text > 2) {
    //   this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //   return false;
    // } else {
    //   const checkCondtion = textSearch.indexOf('**');

    //   if (checkCondtion === -1) {
    //     if (textSearch === '*' || textSearch === '**') {
    //       this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //       return false;
    //     }
    //   } else {
    //     this.errorMessage = 'เงื่อนไขไม่ถูกต้อง';
    //     return false;
    //   }
    // }
    // this.errorMessage = '';
    // this.isLoading = true;
    // this.masterService.findVendorCodeWithParam(textSearch).subscribe(data => {
    //   const response = data as any;
    //   this.isLoading = false;
    //   if (response.status === 'T') {
    //     this.dataSource = response.data;
    //     this.dataSourceHeader = this.constant.HEADER_DIALOG_SEARCH.vendorTaxId;
    //   } else {
    //     this.errorMessage = response.message;
    //   }
    // });
  }

  sortData(sortType) {
    this.checkTest = !sortType;

    if (sortType) {
      this.dataSource.sort((a, b) => (b.pay > a.pay ? 1 : -1));
    } else {
      this.dataSource.sort((a, b) => (a.pay > b.pay ? 1 : -1));
    }
    console.log(this.dataSource);
  }

  chooseDataSearch(value, name, optional, optionalName) {
    this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      type: this.data.type,
      value,
      name,
      optional,
      optionalName,
    });
  }

  chooseDataSearchBudgetReserve(value, name, optional1, optionalName1, optional2, optionalName2) {
    this.errorMessage = '';
    this.dialogRef.close({
      event: true,
      type: this.data.type,
      value,
      name,
      optional1,
      optionalName1,
      optional2,
      optionalName2,
    });
  }
}
