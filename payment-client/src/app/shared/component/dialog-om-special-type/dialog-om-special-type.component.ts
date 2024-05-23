import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';

export interface DialogData {
  listSpecialType: [{ from: null; to: null; optionExclude: false }];
}

@Component({
  selector: 'app-dialog-om-special-type',
  templateUrl: './dialog-om-special-type.component.html',
  styleUrls: ['./dialog-om-special-type.component.scss'],
})
export class DialogOmSpecialTypeComponent implements OnInit {
  @ViewChildren('from') from: QueryList<ElementRef>;
  @ViewChildren('to') to: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listSpecialType = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogOmSpecialTypeComponent>,
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
    console.log(this.data.listSpecialType);
    if (this.data.listSpecialType[0].from) {
      this.listSpecialType = this.data.listSpecialType;
    }
  }

  addInputCompany() {
    this.listSpecialType.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setSpecialType(index) {
    const specialTypeFrom = this.from.toArray()[index].nativeElement.value;
    const specialTypeTo = this.to.toArray()[index].nativeElement.value;
    if (specialTypeFrom) {
      this.listSpecialType[index].from = specialTypeFrom;
    } else {
      this.listSpecialType[index].from = '';
    }
    if (specialTypeTo) {
      this.listSpecialType[index].to = specialTypeTo;
    } else {
      this.listSpecialType[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listSpecialType[index].optionExclude = optionExclude;
    } else {
      this.listSpecialType[index].optionExclude = false;
    }
  }

  deleteInputSpecialType(index) {
    if (this.listSpecialType.length > 1) {
      this.listSpecialType.splice(index, 1);
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

      if (list.length > this.listSpecialType.length) {
        const need = list.length - this.listSpecialType.length;
        for (let i = 0; i < need; i++) {
          this.listSpecialType.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listSpecialType);
      if (type === 'specialTypeFrom') {
        for (let i = index; i < list.length; i++) {
          this.listSpecialType[i].from = list[i];
        }
      } else if (type === 'specialTypeTo') {
        for (let i = index; i < list.length; i++) {
          this.listSpecialType[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchSpecialType(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'specialTypeFrom') {
          this.listSpecialType[index].from = result.value;
        } else if (type === 'specialTypeTo') {
          this.listSpecialType[index].to = result.value;
        }
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listSpecialType,
    });
  }
}
