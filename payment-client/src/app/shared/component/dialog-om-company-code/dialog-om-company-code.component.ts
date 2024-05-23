import {
  Component,
  ElementRef,
  Inject,
  OnInit,
  QueryList,
  ViewChild,
  ViewChildren,
} from '@angular/core';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { SelectionModel } from '@angular/cdk/collections';
import { MatTableDataSource } from '@angular/material/table';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listCompanyCode: [{ companyCodeFrom: null; companyCodeTo: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-company-code',
  templateUrl: './dialog-om-company-code.component.html',
  styleUrls: ['./dialog-om-company-code.component.scss'],
})
export class DialogOmCompanyCodeComponent implements OnInit {
  @ViewChildren('companyCodeFrom') companyCodeFrom: QueryList<ElementRef>;
  @ViewChildren('companyCodeTo') companyCodeTo: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listCompanyCode = [
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
    { companyCodeFrom: null, companyCodeTo: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmCompanyCodeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private paymentService: PaymentService,
    private loaderService: LoaderService
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
      // ส่ง event param ตาม ปกติเลย
      // if (this.isSaveSuccess && this.resultSave.accDocNo) {
      //   this.dialogRef.close({event: 'savesucess'});
      // } else {
      //   this.dialogRef.close({event: 'close'});
      // }
    });
  }

  ngOnInit() {
    console.log(this.data.listCompanyCode);
    if (this.data.listCompanyCode[0].companyCodeFrom) {
      this.listCompanyCode = this.data.listCompanyCode;
    }
  }

  addInputCompany() {
    this.listCompanyCode.push({ companyCodeFrom: null, companyCodeTo: null, optionExclude: false });
  }

  setCompanyCode(index) {
    const companyCodeFrom = this.companyCodeFrom.toArray()[index].nativeElement.value;
    const companyCodeTo = this.companyCodeTo.toArray()[index].nativeElement.value;
    if (companyCodeFrom) {
      this.listCompanyCode[index].companyCodeFrom = companyCodeFrom;
    } else {
      this.listCompanyCode[index].companyCodeFrom = '';
    }
    if (companyCodeTo) {
      this.listCompanyCode[index].companyCodeTo = companyCodeTo;
    } else {
      this.listCompanyCode[index].companyCodeTo = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listCompanyCode[index].optionExclude = optionExclude;
    } else {
      this.listCompanyCode[index].optionExclude = false;
    }
  }

  deleteInputCompanyCode(index) {
    if (this.listCompanyCode.length > 1) {
      this.listCompanyCode.splice(index, 1);
    }
  }

  onNoClick() {
    this.dialogRef.close({
      event: true,
      status: 'Close',
      value: '',
    });
  }

  copyFromExcel(event, index, type) {
    // console.log(event);

    const value = event.data;

    // console.log('value' + value);

    if (value.length > 10) {
      // console.log('index' + index);

      console.log(value.split('\n'));
      const list = value.split('\n');

      if (list.length > this.listCompanyCode.length) {
        const need = list.length - this.listCompanyCode.length;
        for (let i = 0; i < need; i++) {
          this.listCompanyCode.push({
            companyCodeFrom: null,
            companyCodeTo: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listCompanyCode);
      if (type === 'companyCodeFrom') {
        for (let i = index; i < list.length; i++) {
          this.listCompanyCode[i].companyCodeFrom = list[i];
        }
      } else if (type === 'companyCodeTo') {
        for (let i = index; i < list.length; i++) {
          this.listCompanyCode[i].companyCodeTo = list[i];
        }
      }
    }
  }

  openDialogSearchCompanyCode(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'companyCodeFrom') {
          this.listCompanyCode[index].companyCodeFrom = result.value;
        } else if (type === 'companyCodeTo') {
          this.listCompanyCode[index].companyCodeTo = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listCompanyCode,
    });
  }
}
