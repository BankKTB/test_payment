import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { Utils } from '@shared/utils/utils';

export interface DialogData {
  listVendor: [{ from: null; to: null; optionExclude: false }];
  isShowIndex: boolean;
}

@Component({
  selector: 'app-dialog-om-vendor',
  templateUrl: './dialog-om-vendor.component.html',
  styleUrls: ['./dialog-om-vendor.component.scss'],
})
export class DialogOmVendorComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listVendor = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmVendorComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    public utils: Utils,
    private router: Router,
    private dialog: MatDialog,
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
    console.log(this.data.listVendor);
    if (this.data.listVendor[0].from) {
      this.listVendor = this.data.listVendor;
    }
  }

  addInputCompany() {
    this.listVendor.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setVendor(index) {
    const vendorFrom = this.from.toArray()[index].nativeElement.value;
    const vendorTo = this.to.toArray()[index].nativeElement.value;
    if (vendorFrom) {
      this.listVendor[index].from = vendorFrom;
    } else {
      this.listVendor[index].from = '';
    }
    if (vendorTo) {
      this.listVendor[index].to = vendorTo;
    } else {
      this.listVendor[index].to = '';
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
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listVendor);
      if (type === 'vendorFrom') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].from = list[i];
        }
      } else if (type === 'vendorTo') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].to = list[i];
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
          this.listVendor[index].from = result.value;
        } else if (type === 'vendorTo') {
          this.listVendor[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    console.log('save : ', this.listVendor);
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listVendor,
    });
  }

  copyToClipboard($event) {
    this.utils.copyToClipboard($event, this.listVendor).then((res) => {
      this.listVendor = this.listVendor.filter((data) => (data.from));
    });
  }

  deleteAllClipBoard() {
    this.listVendor = [{ from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false },
      { from: null, to: null, optionExclude: false }];
  }

  renderIndexRow(rowNumber: number) {
    if(this.data.isShowIndex) return rowNumber.toString();
  }

  renderCount() {
    return this.listVendor.filter((e) => e.from).length || 0
  }
}
