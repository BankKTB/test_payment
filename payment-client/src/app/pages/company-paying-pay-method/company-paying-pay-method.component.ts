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
import { DialogCompanyPayingConfigComponent } from '@shared/component/dialog-company-paying-config/dialog-company-paying-config.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogCopyPaymentMethodConfigComponent } from '@shared/component/dialog-copy-payment-method-config/dialog-copy-payment-method-config.component';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
import { CompanyPayingPayMethodConfigService } from '@core/services/company-paying-pay-method-config/company-paying-pay-method-config.service';
import { CompanyPayingService } from '@core/services/company-paying/company-paying.service';

@Component({
  selector: 'app-company-paying-pay-method',
  templateUrl: './company-paying-pay-method.component.html',
  styleUrls: ['./company-paying-pay-method.component.scss'],
})
export class CompanyPayingPayMethodComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['payMethod', 'payMethodName', 'edit'];

  dataSource = new MatTableDataSource([]);

  companyPayingPayMethodForm: FormGroup;

  companyPayingCodeControl: FormControl; // รหัสหน่วยงาน
  payMethodControl: FormControl;
  payMethodNameControl: FormControl;

  companyPayingName = '';

  listCompany = [];
  companyPayingId = 0;

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private companyPayingPayMethodConfigService: CompanyPayingPayMethodConfigService,
    private companyPayingService: CompanyPayingService,
    private snackBar: MatSnackBar,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    this.route.queryParams.subscribe((params) => {
      console.log(params);
      if (params) {
        this.companyPayingId = params.companyPayingId;
      }
    });
    this.createcompanyPayingPayMethodFormControl();
    this.createcompanyPayingPayMethodFormGroup();
    this.getCompanyPaying();
    this.searchAllParameter();
  }

  getCompanyPaying() {
    this.companyPayingService.searchByID(this.companyPayingId).then((result) => {
      console.log(result);
      if (result.status === 200) {
        const data = result.data;
        if (data) {
          this.companyPayingPayMethodForm.patchValue({
            companyPayingCode: data.companyCode,
          });
          this.companyPayingName = data.companyName;
        }
      } else if (result.status === 404) {
      }
    });
  }

  searchAllParameter() {
    this.dataSource = new MatTableDataSource([]);
    this.companyPayingPayMethodConfigService
      .searchAllParameter(this.companyPayingId)
      .then((result) => {
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

  createcompanyPayingPayMethodFormControl() {
    this.companyPayingCodeControl = this.formBuilder.control(''); // หน่วยงาน
    this.payMethodControl = this.formBuilder.control(''); // วิธีชำระเงิน
    this.payMethodNameControl = this.formBuilder.control(''); // วิธีชำระเงิน
  }

  createcompanyPayingPayMethodFormGroup() {
    this.companyPayingPayMethodForm = this.formBuilder.group({
      companyPayingCode: this.companyPayingCodeControl, // หน่วยงาน
      payMethod: this.payMethodControl, // วิธีชำระเงิน
      payMethodName: this.payMethodNameControl,
    });
  }

  clearInputAll() {
    this.companyPayingPayMethodForm.patchValue({
      payMethod: '', // ชื่อหน่วยงาน
      payMethodName: '',
    });
  }

  insertCompanyPayingPayMethodConfig(value) {
    console.log(value);

    if (!value.payMethod) {
      this.snackBar.open('กรุณากรอก วิธีชำระเงิน', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      companyPayingId: this.companyPayingId,
      payMethod: value.payMethod,
      payMethodName: value.payMethodName,
    };
    console.log(payload);
    this.companyPayingPayMethodConfigService.create(payload).then((result) => {
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
        this.snackBar.open('วิธีชำระเงินนี้มีข้อมูลแล้ว', '', {
          panelClass: '_error',
        });
      }
    });
  }

  deleteInputCompanyCode(element) {
    const id = element.id;
    this.companyPayingPayMethodConfigService.delete(id).then((result) => {
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

  openDialogCompanyPayingPayMethodConfig(item) {
    const dialogRef = this.dialog.open(DialogCompanyPayingConfigComponent, {
      width: '50vw',
      data: {
        item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  routeToCompanyPayingPayMethodConfig(element) {
    const id = element.id;
    this.router.navigate(['/companyPayingPayMethodConfig'], {
      queryParams: { companyPayingPayMethodId: id, companyPayingId: this.companyPayingId },
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
        this.companyPayingPayMethodForm.patchValue({
          payMethod: result.value,
          payMethodName: result.name,
        });
        if (result.value && result.name) {
          this.insertCompanyPayingPayMethodConfig(this.companyPayingPayMethodForm.value);
        }
      }
    });
  }

  copy(data) {
    const dialogRef = this.dialog.open(DialogCopyPaymentMethodConfigComponent, {
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
