import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listDocType: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-doc-type',
  templateUrl: './dialog-om-doc-type.component.html',
  styleUrls: ['./dialog-om-doc-type.component.scss'],
})
export class DialogOmDocTypeComponent implements OnInit {
  @ViewChildren('docTypeFrom') docTypeFrom: QueryList<ElementRef>;
  @ViewChildren('docTypeTo') docTypeTo: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listDocType = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmDocTypeComponent>,
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
    console.log(this.data.listDocType);
    if (this.data.listDocType[0].from) {
      this.listDocType = this.data.listDocType;
    }
  }

  addInputCompany() {
    this.listDocType.push({ from: null, to: null, optionExclude: false });
  }

  setDocType(index) {
    const docTypeFrom = this.docTypeFrom.toArray()[index].nativeElement.value;
    const docTypeTo = this.docTypeTo.toArray()[index].nativeElement.value;
    if (docTypeFrom) {
      this.listDocType[index].from = docTypeFrom;
    } else {
      this.listDocType[index].from = '';
    }
    if (docTypeTo) {
      this.listDocType[index].to = docTypeTo;
    } else {
      this.listDocType[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listDocType[index].optionExclude = optionExclude;
    } else {
      this.listDocType[index].optionExclude = false;
    }
  }

  deleteInputDocType(index) {
    if (this.listDocType.length > 1) {
      this.listDocType.splice(index, 1);
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

      if (list.length > this.listDocType.length) {
        const need = list.length - this.listDocType.length;
        for (let i = 0; i < need; i++) {
          this.listDocType.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listDocType);
      if (type === 'docTypeFrom') {
        for (let i = index; i < list.length; i++) {
          this.listDocType[i].from = list[i];
        }
      } else if (type === 'docTypeTo') {
        for (let i = index; i < list.length; i++) {
          this.listDocType[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchDocType(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'docTypeFrom') {
          this.listDocType[index].from = result.value;
        } else if (type === 'docTypeTo') {
          this.listDocType[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listDocType,
    });
  }
}
