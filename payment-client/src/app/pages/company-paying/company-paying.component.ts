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
import { DialogCompanyPayingConfigComponent } from '@shared/component/dialog-company-paying-config/dialog-company-paying-config.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-company-paying',
  templateUrl: './company-paying.component.html',
  styleUrls: ['./company-paying.component.scss'],
})
export class CompanyPayingComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['companyCode', 'companyName', 'edit'];

  dataSource = new MatTableDataSource([]);

  companyPayingForm: FormGroup;

  companyCodeControl: FormControl; // รหัสหน่วยงาน
  companyNameControl: FormControl; // ชื่อหน่วยงาน

  listCompany = [];

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private companyPayingService: CompanyPayingService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('companyPaying');
    this.createcompanyPayingFormControl();
    this.createcompanyPayingFormGroup();
    this.clearInputAll();
    this.searchAllParameter();
  }

  searchAllParameter() {
    this.dataSource = new MatTableDataSource([]);
    this.companyPayingService.searchAllParameter().then((result) => {
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

  createcompanyPayingFormControl() {
    this.companyCodeControl = this.formBuilder.control(''); // รหัสหน่วยงาน
    this.companyNameControl = this.formBuilder.control(''); // ชื่อหน่วยงาน
  }

  createcompanyPayingFormGroup() {
    this.companyPayingForm = this.formBuilder.group({
      companyCode: this.companyCodeControl, // รหัสหน่วยงาน
      companyName: this.companyNameControl, // ชื่อหน่วยงาน
    });
  }

  clearInputAll() {
    this.companyPayingForm.patchValue({
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
    this.companyPayingService.create(payload).then((result) => {
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
    this.companyPayingService.delete(id).then((result) => {
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

  openDialogCompanyPayingConfig(item) {
    const dialogRef = this.dialog.open(DialogCompanyPayingConfigComponent, {
      width: '65vw',
      data: {
        item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  routeToCompanyPayingBankConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayingBankConfig'], {
      queryParams: { companyPayingId: id },
    });
  }

  routeToCompanyPayingPayMethodConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayingPayMethod'], {
      queryParams: { companyPayingId: id },
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.companyPayingForm.patchValue({
          companyCode: result.value,
          companyName: result.name,
        });
      }
    });
  }
}
