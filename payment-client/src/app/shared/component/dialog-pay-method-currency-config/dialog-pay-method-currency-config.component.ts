import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MatSnackBar,
  MatSort,
  MatTableDataSource,
} from '@angular/material';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { APP_DATE_FORMATS, AppDateAdapter } from '@shared/utils/format-datepicker';
import { PayMethodCurrencyConfigService } from '@core/services/pay-method-currency-config/pay-method-currency-config.service';

export interface DialogData {
  item: {
    id: any;
    country: any;
    payMethod: any;
    payMethodName: any;
    countryNameEN: any;
  };
}

@Component({
  selector: 'app-dialog-pay-method-currency-config',
  templateUrl: './dialog-pay-method-currency-config.component.html',
  styleUrls: ['./dialog-pay-method-currency-config.component.scss'],
})
export class DialogPayMethodCurrencyConfigComponent implements OnInit {
  @ViewChild(MatSort, { static: true }) sort: MatSort;
  displayedColumns: string[] = ['currency', 'currencyName', 'edit'];

  dataSource = new MatTableDataSource([]);

  payMethodCurrencyConfigForm: FormGroup;

  countryControl: FormControl;
  payMethodControl: FormControl;
  payMethodNameControl: FormControl;

  currencyControl: FormControl;
  currencyNameControl: FormControl;
  payMethodConfigId: number;

  constructor(
    private dialogRef: MatDialogRef<DialogPayMethodCurrencyConfigComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private formBuilder: FormBuilder,
    private payMethodCurrencyConfigService: PayMethodCurrencyConfigService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    this.createpayMethodCurrencyConfigFormControl();
    this.createpayMethodCurrencyConfigFormGroup();
    this.defaultpayMethodCurrencyConfigForm();
    this.getAllbyId();
  }

  createpayMethodCurrencyConfigFormControl() {
    this.countryControl = this.formBuilder.control('');
    this.payMethodControl = this.formBuilder.control('');
    this.payMethodNameControl = this.formBuilder.control('');

    this.currencyControl = this.formBuilder.control('');
    this.currencyNameControl = this.formBuilder.control('');
  }

  createpayMethodCurrencyConfigFormGroup() {
    this.payMethodCurrencyConfigForm = this.formBuilder.group({
      country: this.countryControl,
      payMethod: this.payMethodControl,
      payMethodName: this.payMethodNameControl,

      currency: this.currencyControl,
      currencyName: this.currencyNameControl,
    });
  }

  defaultpayMethodCurrencyConfigForm() {
    this.payMethodCurrencyConfigForm.patchValue({
      country: this.data.item.country,
      payMethod: this.data.item.payMethod,
      payMethodName: this.data.item.payMethodName,

      currency: '',
      currencyName: '',
    });
  }

  getAllbyId() {
    this.payMethodConfigId = this.data.item.id;
    this.payMethodCurrencyConfigService.getAllByID(this.payMethodConfigId).then((result) => {
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

  clickSave(value) {
    if (!value.currency) {
      this.snackBar.open('กรุณาเลือกสกุลเงิน', '', {
        panelClass: '_warning',
      });
      return;
    }
    const payload = {
      currency: value.currency,
      currencyName: value.currencyName,
      payMethodConfigId: this.payMethodConfigId,
    };
    console.log(payload);
    this.payMethodCurrencyConfigService.create(payload).then((result) => {
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.snackBar.open('เพิ่มข้อมูลสำเร็จ', '', {
            panelClass: '_success',
          });
          this.getAllbyId();
          this.defaultpayMethodCurrencyConfigForm();
        }
      } else if (result.status === 404) {
        this.snackBar.open('ข้อมูลไม่ถูกต้อง', '', {
          panelClass: '_success',
        });
      } else if (result.status === 403) {
        this.snackBar.open('สกุลนี้มีอยู่แล้ว', '', {
          panelClass: '_success',
        });
      }
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      value: 'Cancel',
    });
  }

  clickConfirm() {
    this.dialogRef.close({
      event: true,
      value: this.payMethodCurrencyConfigForm.value,
    });
  }

  deleteInputPayMethodCurrencyConfig(item) {
    const id = item.id;
    this.payMethodCurrencyConfigService.delete(id).then((result) => {
      console.log(result);
      if (result.status === 201) {
        const data = result.data;
        if (data) {
          this.getAllbyId();
          this.openSnackBarDeleteSuccess();
        }
      } else if (result.status === 404) {
      }
    });
  }

  openSnackBarDeleteSuccess() {
    this.snackBar.open('ลบข้อมูลสำเร็จ', '', {
      panelClass: '_error',
    });
  }

  openDialogSearchMaster(type): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        this.payMethodCurrencyConfigForm.patchValue({
          currency: result.value,
          currencyName: result.name,
        });
      }
    });
  }

  openDialogPayMethodCurrencyConfig() {
    const dialogRef = this.dialog.open(DialogPayMethodCurrencyConfigComponent, {
      width: '50vw',
      data: {
        item: this.data.item,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {});
  }
}
