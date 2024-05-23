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
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { CompanyPayeeHouseBankKeyConfigService } from '@core/services/company-payee-house-bank-key-config/company-payee-house-bank-key-config.service';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';
import { MasterService } from '@core/services';

@Component({
  selector: 'app-company-payee-house-bank-key-config',
  templateUrl: './company-payee-house-bank-key-config.component.html',
  styleUrls: ['./company-payee-house-bank-key-config.component.scss'],
})
export class CompanyPayeeHouseBankKeyConfigComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['valueCode', 'countryCode', 'bankBranch', 'bankName', 'edit'];
  companyPayeeHouseBankKeyConfigForm: FormGroup;

  companyPayeeCodeControl: FormControl;
  houseBankKeyControl: FormControl;
  countryControl: FormControl;
  countryNameEnControl: FormControl;
  bankBranchControl: FormControl;
  bankNameControl: FormControl;
  companyPayeeId: number;
  listPayeeHouseBankKeyConfig = [{}] as any;
  dataSource = new MatTableDataSource([]);

  companyPayeeName = '';

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private companyPayeeHouseBankKeyConfigService: CompanyPayeeHouseBankKeyConfigService,
    private companyPayeeService: CompanyPayeeService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      console.log('house config');
      console.log(params);
      if (params) {
        this.companyPayeeId = params.companyPayeeId;
      }
    });
    this.createcompanyPayeeHouseBankKeyConfigFormControl();
    this.createcompanyPayeeHouseBankKeyConfigFormGroup();
    this.defaultcompanyPayeeHouseBankKeyConfigForm();
    this.getCompanyPayee();
    // this.getAllByID();
  }

  createcompanyPayeeHouseBankKeyConfigFormControl() {
    this.companyPayeeCodeControl = this.formBuilder.control('');
    this.houseBankKeyControl = this.formBuilder.control('');
    this.countryControl = this.formBuilder.control('');
    this.countryNameEnControl = this.formBuilder.control('');
    this.bankBranchControl = this.formBuilder.control('');
    this.bankNameControl = this.formBuilder.control('');
  }

  createcompanyPayeeHouseBankKeyConfigFormGroup() {
    this.companyPayeeHouseBankKeyConfigForm = this.formBuilder.group({
      companyPayeeCode: this.companyPayeeCodeControl,
      houseBankKey: this.houseBankKeyControl,
      country: this.countryControl,
      countryNameEn: this.countryNameEnControl,
      bankBranch: this.bankBranchControl,
      bankName: this.bankNameControl,
    });
  }

  defaultcompanyPayeeHouseBankKeyConfigForm() {
    this.companyPayeeHouseBankKeyConfigForm.patchValue({
      houseBankKey: '',
      country: '',
      countryNameEn: '',
      bankBranch: '',
      bankName: '',
    });
  }

  getCompanyPayee() {
    this.companyPayeeService.searchByID(this.companyPayeeId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayeeHouseBankKeyConfigForm.patchValue({
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
    this.masterService.findHouseBankWithParam(companyCode).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.listPayeeHouseBankKeyConfig = data;
          this.dataSource = new MatTableDataSource(data);
          this.dataSource.sort = this.sort;
        }
      } else if (result.status === 404) {
        this.listPayeeHouseBankKeyConfig = [];
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  // getAllByID() {
  //   this.dataSource = new MatTableDataSource([]);
  //   this.companyPayeeHouseBankKeyConfigService.searchAllParameter(this.companyPayeeId).then(result => {
  //     console.log(result);
  //     if (result.status === 200) {
  //       const data = result.data;
  //       if (data) {
  //         this.listPayeeHouseBankKeyConfig = data;
  //         this.dataSource = new MatTableDataSource(data);
  //         this.dataSource.sort = this.sort;
  //       }
  //     } else if (result.status === 404) {
  //       this.listPayeeHouseBankKeyConfig = [];
  //       this.dataSource = new MatTableDataSource([]);
  //
  //     }
  //   });
  // }

  // clickSave(value) {
  //
  //
  //   if (!value.houseBankKey || value.houseBankKey.length !== 5) {
  //     this.snackBar.open('กรุณากรอก ธนาคารตัวแทน ให้ครบ 5 หลัก', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.country) {
  //     this.snackBar.open('กรุณากรอก รหัสประเทศ', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   } else if (!value.bankBranch) {
  //     this.snackBar.open('กรุณากรอก คีย์ธนาคาร', '', {
  //       panelClass: '_warning',
  //     });
  //     return;
  //   }
  //
  //   const payload = {
  //     houseBankKey: value.houseBankKey,
  //     country: value.country,
  //     countryNameEn: value.countryNameEn,
  //     bankBranch: value.bankBranch,
  //     bankName: value.bankName,
  //     companyPayeeId: this.companyPayeeId,
  //   };
  //   this.companyPayeeHouseBankKeyConfigService.create(payload).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
  //           panelClass: '_success',
  //         });
  //         this.getAllByID();
  //         this.defaultcompanyPayeeHouseBankKeyConfigForm();
  //       }
  //     } else if (result.status === 400) {
  //       this.snackBar.open('กรุณากรอกข้อมูลให้ครบถ้วน', '', {
  //         panelClass: '_error',
  //       });
  //     } else if (result.status === 403) {
  //       this.snackBar.open('ธนาคารตัวแทนมีข้อมูลอยู่แล้ว', '', {
  //         panelClass: '_error',
  //       });
  //     }
  //   });
  // }

  // deleteItemCompanyPayeeHouseBankKeyConfig(index) {
  //   const id = this.listPayeeHouseBankKeyConfig[index].id;
  //   this.companyPayeeHouseBankKeyConfigService.delete(id).then(result => {
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

  routeToCompanyPayeeHouseBankKeyDetailConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayeeHouseBankKeyDetailConfig'], {
      queryParams: {
        companyPayeeId: this.companyPayeeId,
        houseBankKey: element.valueCode,
        bankBranch: element.bankBranch,
      },
    });
  }

  routeToCompanyPayeeBankAccountNoConfig(element) {
    const id = element.id;
    const houseBankKey = element.houseBankKey;
    this.router.navigate(['/companyPayeeBankAccountNoConfig'], {
      queryParams: { companyPayeeId: this.companyPayeeId },
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      width: '65vw',
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.companyPayeeHouseBankKeyConfigForm.patchValue({ [result.type]: result.value });
        if (type === 'bankBranch') {
          this.companyPayeeHouseBankKeyConfigForm.patchValue({
            bankName: result.name,
          });
        } else if (type === 'country') {
          this.companyPayeeHouseBankKeyConfigForm.patchValue({
            countryNameEn: result.name,
          });
        }
      }
    });
  }

  // deleteHouseBankKey(element) {
  //   const id = element.id;
  //   this.companyPayeeHouseBankKeyConfigService.delete(id).then(result => {
  //     console.log(result);
  //     if (result.status === 201) {
  //       const data = result.data;
  //       if (data) {
  //         this.snackBar.open('ลบข้อมูลสำเร็จ', '', {
  //           panelClass: '_success',
  //         });
  //         this.getAllByID();
  //         this.defaultcompanyPayeeHouseBankKeyConfigForm();
  //       }
  //     } else if (result.status === 404) {
  //     }
  //   });
  // }
}
