import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listDocumentNo: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-document-no',
  templateUrl: './dialog-om-document-no.component.html',
  styleUrls: ['./dialog-om-document-no.component.scss'],
})
export class DialogOmDocumentNoComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listDocumentNo = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmDocumentNoComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
    });
  }

  ngOnInit() {
    console.log(this.data.listDocumentNo);
    if (this.data.listDocumentNo[0].from) {
      this.listDocumentNo = this.data.listDocumentNo;
    }
  }

  addInputCompany() {
    this.listDocumentNo.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setDocumentNo(index) {
    const documentNoFrom = this.from.toArray()[index].nativeElement.value;
    const documentNoTo = this.to.toArray()[index].nativeElement.value;
    if (documentNoFrom) {
      this.listDocumentNo[index].from = documentNoFrom;
    } else {
      this.listDocumentNo[index].from = '';
    }
    if (documentNoTo) {
      this.listDocumentNo[index].to = documentNoTo;
    } else {
      this.listDocumentNo[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listDocumentNo[index].optionExclude = optionExclude;
    } else {
      this.listDocumentNo[index].optionExclude = false;
    }
  }

  deleteInputDocumentNo(index) {
    if (this.listDocumentNo.length > 1) {
      this.listDocumentNo.splice(index, 1);
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

      if (list.length > this.listDocumentNo.length) {
        const need = list.length - this.listDocumentNo.length;
        for (let i = 0; i < need; i++) {
          this.listDocumentNo.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listDocumentNo);
      if (type === 'documentNoFrom') {
        for (let i = index; i < list.length; i++) {
          this.listDocumentNo[i].from = list[i];
        }
      } else if (type === 'documentNoTo') {
        for (let i = index; i < list.length; i++) {
          this.listDocumentNo[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchDocumentNo(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'documentNoFrom') {
          this.listDocumentNo[index].from = result.value;
        } else if (type === 'documentNoTo') {
          this.listDocumentNo[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listDocumentNo,
    });
  }
}
