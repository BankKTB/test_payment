import { Component, OnInit, ViewChild } from '@angular/core';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatDialog,
  MatSort,
  MatTableDataSource,
} from '@angular/material';
import { Router } from '@angular/router';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { DialogPayMethodConfigComponent } from '@shared/component/dialog-pay-method-config/dialog-pay-method-config.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { MatSnackBar } from '@angular/material/snack-bar';
import { DialogCopyPaymentMethodCountryConfigComponent } from '@shared/component/dialog-copy-payment-method-country-config/dialog-copy-payment-method-country-config.component';
import { Constant } from '@shared/utils/constant';
import { Utils } from '@shared/utils/utils';
import { PayMethodConfigService } from '@core/services/pay-method-config/pay-method-config.service';
import { SidebarService } from '@core/services';

@Component({
  selector: 'app-pay-method',
  templateUrl: './pay-method.component.html',
  styleUrls: ['./pay-method.component.scss'],
})
export class PayMethodComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['country', 'payMethod', 'payMethodName', 'edit'];

  dataSource = new MatTableDataSource([]);

  payMethodForm: FormGroup;

  countryControl: FormControl; // วิธีการชำระเงิน
  countryNameEnControl: FormControl; // วิธีการชำระเงิน
  payMethodControl: FormControl; // ประเทศ
  payMethodNameControl: FormControl; // ประเทศ

  listPayMethod = [];

  constructor(
    public constant: Constant,
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private payMethodConfigService: PayMethodConfigService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('config');
    this.sidebarService.updateNowPage('payMethod');
    this.createPayMethodFormControl();
    this.createPayMethodFormGroup();
    this.clearInputAll();
    this.searchAllParameter();
  }

  searchAllParameter() {
    this.dataSource = new MatTableDataSource([]);
    this.payMethodConfigService.searchAllParameter().then((result) => {
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

  createPayMethodFormControl() {
    this.payMethodControl = this.formBuilder.control(''); // ประเทศ
    this.payMethodNameControl = this.formBuilder.control(''); // ประเทศ
    this.countryControl = this.formBuilder.control(''); // วิธีการชำระเงิน
    this.countryNameEnControl = this.formBuilder.control(''); // วิธีการชำระเงิน
  }

  createPayMethodFormGroup() {
    this.payMethodForm = this.formBuilder.group({
      payMethod: this.payMethodControl, // ประเทศ
      payMethodName: this.payMethodNameControl, // ประเทศ
      country: this.countryControl, // วิธีการชำระเงิน
      countryNameEN: this.countryNameEnControl, // ประเทศ
    });
  }

  clearInputAll() {
    this.payMethodForm.patchValue({
      payMethod: '', // ประเทศ
      payMethodName: '',
      country: '', // วิธีการชำระเงิน
    });
  }

  insertInputPayMethod(value) {
    if (!value.country) {
      this.snackBar.open('กรุณาเลือกประเทศ', '', {
        panelClass: '_warning',
      });
      return;
    } else if (!value.payMethod) {
      this.snackBar.open('กรุณาเลือกวิธีชำระเงิน', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      payMethod: value.payMethod,
      payMethodName: value.payMethodName,
      country: value.country,
      countryNameEn: value.countryNameEn,
    };
    this.payMethodConfigService.create(payload).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          console.log(data);
          this.searchAllParameter();
          this.clearInputAll();
        }
      } else if (result.status === 404) {
      }
    });
  }

  deleteInputPayMethod(item) {
    const id = item.id;
    this.payMethodConfigService.delete(id).then((result) => {
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

  openDialogPayMethodConfig(item) {
    const dialogRef = this.dialog.open(DialogPayMethodConfigComponent, {
      width: '100vw',
      data: {
        item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      width: '65vw',
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.payMethodForm.patchValue({ [result.type]: result.value });
        if (type === 'paymentMethod') {
          this.payMethodForm.patchValue({
            payMethod: result.value, // ประเทศ
            payMethodName: result.name,
          });
        } else if (type === 'country') {
          this.payMethodForm.patchValue({
            countryNameEn: result.name,
          });
        }
      }
    });
  }

  copy(data) {
    const dialogRef = this.dialog.open(DialogCopyPaymentMethodCountryConfigComponent, {
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
