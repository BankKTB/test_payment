import { Component, OnInit, ViewChild } from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDialog,
  MatSnackBar,
  MatSort,
  MatTableDataSource,
} from '@angular/material';
import { Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DialogCompanyPayeeConfigComponent } from '@shared/component/dialog-company-payee-config/dialog-company-payee-config.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogCopyCompanyPayeeComponent } from '@shared/component/dialog-copy-company-payee/dialog-copy-company-payee.component';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
import { CompanyPayeeService } from '@core/services/company-payee/company-payee.service';
import { PaymentAliasService } from '@core/services/payment-alias/payment-alias.service';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-company-payee',
  templateUrl: './company-payee.component.html',
  styleUrls: ['./company-payee.component.scss'],
})
export class CompanyPayeeComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['companyCode', 'companyName', 'edit'];

  dataSource = new MatTableDataSource([]);

  companyPayeeForm: FormGroup;

  companyCodeControl: FormControl; // รหัสหน่วยงาน
  companyNameControl: FormControl; // ชื่อหน่วยงาน

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private companyPayeeService: CompanyPayeeService,
    private snackBar: MatSnackBar,
    private paymentAliasService: PaymentAliasService,
    private sidebarService: SidebarService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('companyPayee');
    this.createcompanyPayeeFormControl();
    this.createcompanyPayeeFormGroup();
    this.clearInputAll();
    this.searchAllParameter();
  }

  searchAllParameter() {
    this.dataSource = new MatTableDataSource([]);
    this.companyPayeeService.searchAllParameter().then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.dataSource = new MatTableDataSource(data);

          this.dataSource.sort = this.sort;
        }
      } else if (result.status === 404) {
        this.dataSource = new MatTableDataSource([]);
      }
    });
  }

  createcompanyPayeeFormControl() {
    this.companyCodeControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.companyNameControl = this.formBuilder.control(''); // ชื่อหน่วยงาน
  }

  createcompanyPayeeFormGroup() {
    this.companyPayeeForm = this.formBuilder.group({
      companyCode: this.companyCodeControl, // รหัสหน่วยงาน
      companyName: this.companyNameControl, // ชื่อหน่วยงาน
    });
  }

  clearInputAll() {
    this.companyPayeeForm.patchValue({
      companyCode: '', // รหัสหน่วยงาน
      companyName: '', // ชื่อหน่วยงาน
    });
  }

  insertInputCompanyCode(value) {
    if (!value.companyCode) {
      this.snackBar.open('กรุณากรอก รหัสบริษัท', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.companyName) {
      this.snackBar.open('กรุณากรอก ชื่อบริษัท', '', {
        panelClass: '_warning',
      });
      return;
    }

    const payload = {
      companyCode: value.companyCode,
      companyName: value.companyName,
    };
    this.companyPayeeService.create(payload).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchAllParameter();
          this.clearInputAll();
          this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
            panelClass: '_success',
          });
        }
      } else if (result.status === 403) {
        this.snackBar.open('หน่วยงานนี้มีข้อมูลแล้ว', '', {
          panelClass: '_error',
        });
      }
    });
  }

  deleteInputCompanyCode(element) {
    const id = element.id;
    this.companyPayeeService.delete(id).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.searchAllParameter();
          this.clearInputAll();
        }
      } else if (result.status === 404) {
      }
    });
  }

  openDialogCompanyPayeeConfig(item) {
    const dialogRef = this.dialog.open(DialogCompanyPayeeConfigComponent, {
      width: '100vw',
      data: {
        item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  routeToCompanyPayingBankConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayeeHouseBankKeyConfig'], {
      queryParams: { companyPayeeId: id },
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
        this.companyPayeeForm.patchValue({
          companyCode: result.value,
          companyName: result.name,
        });
        if (result.value && result.name) {
          this.insertInputCompanyCode(this.companyPayeeForm.value);
        }
      }
    });
  }

  copy(data) {
    const dialogRef = this.dialog.open(DialogCopyCompanyPayeeComponent, {
      data: { config: data },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'Success') {
          this.searchAllParameter();
        }
      }
    });
  }
}
