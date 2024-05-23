import { Component, OnInit, ViewChild } from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDialog,
  MatSnackBar,
  MatTableDataSource,
} from '@angular/material';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { MatSort } from '@angular/material/sort';
import { CompanyPayingBankConfigService } from '@core/services/company-paying-bank-config/company-paying-bank-config.service';
import { MasterService } from '@core/services';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';

@Component({
  selector: 'app-company-paying-bank-config',
  templateUrl: './company-paying-bank-config.component.html',
  styleUrls: ['./company-paying-bank-config.component.scss'],
})
export class CompanyPayingBankConfigComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = [
    'houseBank',
    'paymentMethod',
    'currency',
    'accountCode',
    'glAccount',
  ];

  dataSource = new MatTableDataSource([]);

  companyPayingBankConfigForm: FormGroup;

  companyPayingCodeControl: FormControl; // รหัสหน่วยงาน

  glAccountControl: FormControl;
  houseBankKeyControl: FormControl;
  companyPayingIdControl: FormControl;
  currencyControl: FormControl;
  payMethodControl: FormControl;

  accountCodeControl: FormControl;
  companyPayingId: number;
  listPayingBankConfig = [{}] as any;

  companyPayingName = '';

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayingBankConfigService: CompanyPayingBankConfigService,
    private companyPayingService: CompanyPayingService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params) {
        this.companyPayingId = params.companyPayingId;
      }
    });
    this.createcompanyPayingBankConfigFormControl();
    this.createcompanyPayingBankConfigFormGroup();
    this.defaultcompanyPayingBankConfigForm();
    this.getCompanyPaying();
    // this.getAllByID();
  }

  createcompanyPayingBankConfigFormControl() {
    this.companyPayingCodeControl = this.formBuilder.control(''); // หน่วยงาน

    this.glAccountControl = this.formBuilder.control('');
    this.houseBankKeyControl = this.formBuilder.control('');
    this.companyPayingIdControl = this.formBuilder.control('');
    this.currencyControl = this.formBuilder.control('');
    this.payMethodControl = this.formBuilder.control('');
    this.accountCodeControl = this.formBuilder.control('');
  }

  createcompanyPayingBankConfigFormGroup() {
    this.companyPayingBankConfigForm = this.formBuilder.group({
      companyPayingCode: this.companyPayingCodeControl, // หน่วยงาน

      glAccount: this.glAccountControl,
      houseBankKey: this.houseBankKeyControl,
      companyPayingId: this.companyPayingIdControl,
      currency: this.currencyControl,
      payMethod: this.payMethodControl,
      accountCode: this.accountCodeControl,
    });
  }

  defaultcompanyPayingBankConfigForm() {
    this.companyPayingBankConfigForm.patchValue({
      glAccount: '',
      houseBankKey: '',
      currency: '',
      payMethod: '',
      accountCode: '',
    });
  }

  getCompanyPaying() {
    this.companyPayingService.searchByID(this.companyPayingId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayingBankConfigForm.patchValue({
            companyPayingCode: data.companyCode,
          });
          this.companyPayingName = data.companyName;
          this.getAllHouseBankPaymentMethodByCompanyCode(data.companyCode);
        }
      } else if (result.status === 404) {
      }
    });
  }

  getAllHouseBankPaymentMethodByCompanyCode(companyCode) {
    this.masterService.findHouseBankPaymentMethodWithParam(companyCode).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.listPayingBankConfig = data;
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
        }
      } else if (result.status === 404) {
        this.listPayingBankConfig = [];
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  // getAllByID() {
  //   this.companyPayingBankConfigService.getAllByID(this.companyPayingId).then(result => {
  //     console.log(result);
  //     if (result.status === 200) {
  //       const data = result.data;
  //       if (data) {
  //         this.listPayingBankConfig = data;
  //         this.dataSource = new MatTableDataSource(data);
  //
  //         this.dataSource.sort = this.sort;
  //       }
  //     } else if (result.status === 404) {
  //       this.listPayingBankConfig = [];
  //       this.dataSource = new MatTableDataSource([]);
  //     }
  //   });
  // }

  // clickSave(value) {
  //   if (!value.houseBankKey) {
  //     this.snackBar.open('กรุณากรอก ธนาคารตัวแทน', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.payMethod) {
  //     this.snackBar.open('กรุณาเลือกวิธีชำระเงิน', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.currency) {
  //     this.snackBar.open('กรุณาเลือกสกุลเงิน', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.accountCode) {
  //     this.snackBar.open('กรุณา รหัสบัญชี', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.glAccount) {
  //     this.snackBar.open('กรุณา เลขบัญชีย่อยธนาคาร', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   }
  //
  //   const payload = {
  //     glAccount: value.glAccount,
  //     houseBankKey: value.houseBankKey,
  //     companyPayingId: this.companyPayingId,
  //     currency: value.currency,
  //     payMethod: value.payMethod,
  //
  //     accountCode: value.accountCode,
  //   };
  //   this.companyPayingBankConfigService.create(payload).then(result => {
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
  //           panelClass: '_success',
  //         });
  //         this.getAllByID();
  //         this.defaultcompanyPayingBankConfigForm();
  //       }
  //     } else if (result.status === 403) {
  //       this.snackBar.open('ธนาคารนี้มีข้อมูลอยู่แล้ว', '', {
  //         panelClass: '_error',
  //       });
  //     }
  //   });
  // }

  // deleteItemCompanyPayingBankConfig(index) {
  //   const id = this.listPayingBankConfig[index].id;
  //   this.companyPayingBankConfigService.delete(id).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.getAllByID();
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (type === 'currency') {
          this.companyPayingBankConfigForm.patchValue({
            currency: result.value,
          });
        } else if (type === 'paymentMethod') {
          this.companyPayingBankConfigForm.patchValue({
            payMethod: result.value,
          });
        }
      }
    });
  }
}
