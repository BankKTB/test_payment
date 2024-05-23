import { Component, OnInit, ViewChild } from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDialog,
  MatSnackBar,
  MatSort,
  MatTableDataSource,
} from '@angular/material';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { CompanyPayeeBankAccountNoConfigService } from '@core/services/company-payee-bank-account-no-config/company-payee-bank-account-no-config.service';
import { MasterService } from '@core/services';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';

@Component({
  selector: 'app-company-payee-bank-account-no-config',
  templateUrl: './company-payee-bank-account-no-config.component.html',
  styleUrls: ['./company-payee-bank-account-no-config.component.scss'],
})
export class CompanyPayeeBankAccountNoConfigComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['valueCode', 'accountCode', 'bankAccountNo', 'edit'];
  dataSource = new MatTableDataSource([]);
  companyPayeeBankAccountNoConfigForm: FormGroup;

  companyPayeeCodeControl: FormControl;
  houseBankKeyControl: FormControl;
  accountCodeControl: FormControl;
  bankAccountNoControl: FormControl;
  houseBankKeyId: number;
  houseBankKey: number;
  companyPayeeId: number;
  listPayeeBankAccountNoKeyConfig = [{}] as any;

  companyPayeeName = '';

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayeeBankAccountNoConfigService: CompanyPayeeBankAccountNoConfigService,
    private companyPayeeService: CompanyPayeeService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      if (params) {
        this.houseBankKeyId = params.houseBankKeyId;
        this.houseBankKey = params.houseBankKey;
        this.companyPayeeId = params.companyPayeeId;
      }
      console.log(params);
    });

    this.createCompanyPayeeBankAccountNoConfigFormControl();
    this.createCompanyPayeeBankAccountNoConfigFormGroup();
    this.defaultCompanyPayeeBankAccountNoConfigForm();
    this.getCompanyPayee();
    // this.getAllByID();
  }

  createCompanyPayeeBankAccountNoConfigFormControl() {
    this.companyPayeeCodeControl = this.formBuilder.control('');
    this.houseBankKeyControl = this.formBuilder.control('');
    this.accountCodeControl = this.formBuilder.control('');
    this.bankAccountNoControl = this.formBuilder.control('');
  }

  createCompanyPayeeBankAccountNoConfigFormGroup() {
    this.companyPayeeBankAccountNoConfigForm = this.formBuilder.group({
      companyPayeeCode: this.companyPayeeCodeControl,
      houseBankKey: this.houseBankKeyControl,
      accountCode: this.accountCodeControl,
      bankAccountNo: this.bankAccountNoControl,
    });
  }

  defaultCompanyPayeeBankAccountNoConfigForm() {
    this.companyPayeeBankAccountNoConfigForm.patchValue({
      houseBankKey: this.houseBankKey,
      accountCode: '',
      bankAccountNo: '',
    });
  }

  getCompanyPayee() {
    this.companyPayeeService.searchByID(this.companyPayeeId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeBankAccountNoConfigForm.patchValue({
            companyPayeeCode: data.companyCode,
          });
          this.companyPayeeName = data.companyName;
          this.getAllHouseBankByCompanyCode(data.companyCode);
        }
      } else if (result.status === 404) {
      }
    });
  }

  getAllHouseBankByCompanyCode(companyCode) {
    this.masterService.findHouseBankAccountWithParam(companyCode).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
          this.listPayeeBankAccountNoKeyConfig = data;
        }
      } else if (result.status === 404) {
        this.listPayeeBankAccountNoKeyConfig = [];
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  // getAllByID() {
  //   this.dataSource = new MatTableDataSource([]);
  //
  //   this.companyPayeeBankAccountNoConfigService
  //     .searchAllParameter(this.houseBankKeyId)
  //     .then(result => {
  //       console.log(result);
  //       if (result.status === 200) {
  //         const data = result.data;
  //         if (data) {
  //           this.dataSource = new MatTableDataSource(data);
  //           this.dataSource.sort = this.sort;
  //           this.listPayeeBankAccountNoKeyConfig = data;
  //         }
  //       } else if (result.status === 404) {
  //         this.listPayeeBankAccountNoKeyConfig = [];
  //         this.dataSource = new MatTableDataSource([]);
  //       }
  //     });
  // }

  // clickSave(value) {
  //
  //   if (!value.houseBankKey || value.houseBankKey.length !== 5) {
  //     this.snackBar.open('กรุณากรอก ธนาคารตัวแทน ให้ครบ 5 หลัก', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.accountCode || value.accountCode.length !== 5) {
  //     this.snackBar.open('กรุณากรอก รหัสบัญชี ให้ครบ 5 หลัก', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.bankAccountNo || value.bankAccountNo.length !== 10) {
  //     this.snackBar.open('กรุณากรอก เลขบัญชีธนาคาร ให้ครบ 10 หลัก', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   }
  //
  //   const payload = {
  //     accountCode: value.accountCode,
  //     bankAccountNo: value.bankAccountNo,
  //     houseBankKey: value.houseBankKey,
  //     houseBankKeyId: this.houseBankKeyId,
  //   };
  //   console.log(payload);
  //   this.companyPayeeBankAccountNoConfigService.create(payload).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //
  //         this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
  //           panelClass: '_success',
  //         });
  //         this.getAllByID();
  //         this.defaultCompanyPayeeBankAccountNoConfigForm();
  //       }
  //     } else if (result.status === 400) {
  //       this.snackBar.open('กรุณากรอกข้อมูลให้ครบถ้วน', '', {
  //         panelClass: '_error',
  //       });
  //     } else if (result.status === 403) {
  //       this.snackBar.open('บัญชีธนาคารนี้มีข้อมูลอยู่แล้ว', '', {
  //         panelClass: '_error',
  //       });
  //     }
  //   });
  // }

  // deleteItemCompanyPayeeBankAccountNoConfig(index) {
  //   const id = this.listPayeeBankAccountNoKeyConfig[index].id;
  //   this.companyPayeeBankAccountNoConfigService.delete(id).then(result => {
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

  routeToCompanyPayingBankAccountNoConfig(element) {
    console.log(element);

    this.router.navigate(['/companyPayeeBankAccountNoDetailConfig'], {
      queryParams: {
        companyPayeeId: this.companyPayeeId,
        houseBankKey: element.valueCode,
        accountCode: element.accountCode,
      },
    });
  }

  // deleteBankAccountNo(element) {
  //   const id = element.id;
  //   this.companyPayeeBankAccountNoConfigService.delete(id).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.snackBar.open('ลบข้อมูลสำเร็จ', '', {
  //           panelClass: '_success',
  //         });
  //         this.getAllByID();
  //         this.defaultCompanyPayeeBankAccountNoConfigForm();
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }
}
