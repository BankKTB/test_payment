import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { Utils } from '@shared/utils/utils';

export interface DialogData {
  listVendor: [{ vendorTaxIdFrom: null; vendorTaxIdTo: null; optionExclude: false }];
  editable: boolean;
  isShowIndex: boolean;
}

@Component({
  selector: 'app-dialog-vendor-for-payment',
  templateUrl: './dialog-vendor-for-payment.component.html',
  styleUrls: ['./dialog-vendor-for-payment.component.scss'],
})
export class DialogVendorForPaymentComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listVendor = [
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogVendorForPaymentComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private utils: Utils
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
    console.log(this.data.editable);
    if (this.data.listVendor[0].vendorTaxIdFrom) {
      this.listVendor = this.data.listVendor;
    }
  }

  addInputCompany() {
    this.listVendor.push({
      vendorTaxIdFrom: null,
      vendorTaxIdTo: null,
      optionExclude: false,
    });
  }

  setVendor(index) {
    const vendorFrom = this.from.toArray()[index].nativeElement.value;
    const vendorTo = this.to.toArray()[index].nativeElement.value;
    if (vendorFrom) {
      this.listVendor[index].vendorTaxIdFrom = vendorFrom;
    } else {
      this.listVendor[index].vendorTaxIdFrom = '';
    }
    if (vendorTo) {
      this.listVendor[index].vendorTaxIdTo = vendorTo;
    } else {
      this.listVendor[index].vendorTaxIdTo = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listVendor[index].optionExclude = optionExclude;
    } else {
      this.listVendor[index].optionExclude = false;
    }
  }

  deleteInputVendor(index) {
    if (this.listVendor.length > 1) {
      this.listVendor.splice(index, 1);
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

      if (list.length > this.listVendor.length) {
        const need = list.length - this.listVendor.length;
        for (let i = 0; i < need; i++) {
          this.listVendor.push({
            vendorTaxIdFrom: null,
            vendorTaxIdTo: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listVendor);
      if (type === 'vendorFrom') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].vendorTaxIdFrom = list[i];
        }
      } else if (type === 'vendorTo') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].vendorTaxIdTo = list[i];
        }
      }
    }
  }

  openDialogSearchVendor(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'vendorFrom') {
          this.listVendor[index].vendorTaxIdFrom = result.value;
        } else if (type === 'vendorTo') {
          this.listVendor[index].vendorTaxIdTo = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listVendor,
    });
  }
  renderIndexRow(rowNumber: number) {
    if (this.data.isShowIndex) return rowNumber.toString();
  }

  renderCount() {
    return this.listVendor.filter((e) => e.vendorTaxIdFrom).length || 0;
  }
  copyToClipboard($event) {
    const tmpListVendor: any[] = [...this.listVendor];
    this.utils.copyToClipboard($event, tmpListVendor).then((res) => {
      tmpListVendor.forEach((v) => {
        const y = {
          vendorTaxIdFrom: v.from,
          vendorTaxIdTo: v.to,
          optionExclude: v.optionExclude,
        };
        this.listVendor.push(y);
      });
      this.listVendor = this.listVendor.filter((data) => data.vendorTaxIdFrom);
      if (this.listVendor.length === 0) {
        this.listVendor = [
          { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
          { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
          { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
        ];
      }
    });
  }

  deleteAllClipBoard() {
    this.listVendor = [
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    ];
  }
}
