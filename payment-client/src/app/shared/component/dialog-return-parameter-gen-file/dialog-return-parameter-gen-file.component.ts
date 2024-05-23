import { Component, ElementRef, Inject, OnInit, QueryList, ViewChildren } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { Constant } from '@shared/utils/constant';
import { Router } from '@angular/router';
import { PaymentService } from '@core/services/payment/payment.service';
import { LoaderService } from '@core/services';
import { DialogSearchParameterGenerateFileComponent } from '@shared/component/dialog-search-parameter-generate-file/dialog-search-parameter-generate-file.component';
import { Utils } from '@shared/utils/utils';
export interface DialogData {
  listGenFileParameter: [
    {
      from: null;
      to: null;
      optionExclude: false;
    }
  ];
  listGenFilePaymentDate: [
    {
      from: null;
      to: null;
      optionExclude: false;
    }
  ];
}
@Component({
  selector: 'app-dialog-return-parameter-gen-file',
  templateUrl: './dialog-return-parameter-gen-file.component.html',
  styleUrls: ['./dialog-return-parameter-gen-file.component.scss'],
})
export class DialogReturnParameterGenFileComponent implements OnInit {
  @ViewChildren('genFileParameterFrom') genFileParameterFrom: QueryList<ElementRef>;
  @ViewChildren('genFileParameterTo') genFileParameterTo: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;

  listGenFileParameter = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listGenFilePaymentDate = [
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
    { from: null, to: null, optionExclude: false },
  ];
  listMessageResponse = [];

  constructor(
    public dialogRef: MatDialogRef<DialogReturnParameterGenFileComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    public constant: Constant,
    private router: Router,
    private dialog: MatDialog,
    private utils: Utils,
    private paymentService: PaymentService,
    private loaderService: LoaderService
  ) {
    dialogRef.backdropClick().subscribe(() => {
      console.log('close dialog');
    });
  }

  ngOnInit() {
    if (!this.utils.isEmpty(this.data.listGenFileParameter)) {
      if (
        this.data.listGenFileParameter.length > 0 &&
        this.data.listGenFileParameter[0].from
      ) {
        this.listGenFileParameter = this.data.listGenFileParameter;
      }
    }
    if (!this.utils.isEmpty(this.data.listGenFilePaymentDate)) {
      if (
        this.data.listGenFilePaymentDate.length > 0 &&
        this.data.listGenFilePaymentDate[0].from
      ) {
        this.listGenFilePaymentDate = this.data.listGenFilePaymentDate;
      }
    }
  }

  addInputCompany() {
    this.listGenFileParameter.push({
      from: null,
      to: null,
      optionExclude: false,
    });
    this.listGenFilePaymentDate.push({
      from: null,
      to: null,
      optionExclude: false,
    });
  }

  setCompanyCode(index) {
    const genFileParameterFrom = this.genFileParameterFrom.toArray()[index].nativeElement.value;
    const genFileParameterTo = this.genFileParameterTo.toArray()[index].nativeElement.value;
    if (genFileParameterFrom) {
      this.listGenFileParameter[index].from = genFileParameterFrom;
    } else {
      this.listGenFileParameter[index].from = '';
    }
    if (genFileParameterTo) {
      this.listGenFileParameter[index].to = genFileParameterTo;
    } else {
      this.listGenFileParameter[index].to = '';
    }
  }

  setOptionInclude(index) {
    const valueIndex = this.optionExclude.toArray()[index] as any;

    const optionExclude = valueIndex._checked;
    if (optionExclude) {
      this.listGenFileParameter[index].optionExclude = optionExclude;
    } else {
      this.listGenFileParameter[index].optionExclude = false;
    }
  }

  deleteInputCompanyCode(index) {
    if (this.listGenFileParameter.length > 1) {
      this.listGenFileParameter.splice(index, 1);
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

      if (list.length > this.listGenFileParameter.length) {
        const need = list.length - this.listGenFileParameter.length;
        for (let i = 0; i < need; i++) {
          this.listGenFileParameter.push({
            from: null,
            to: null,
            optionExclude: false,
          });
          this.listGenFilePaymentDate.push({
            from: null,
            to: null,
            optionExclude: false,
          });
        }
      }
      console.log(this.listGenFileParameter);
      if (type === 'genFileParameterFrom') {
        for (let i = index; i < list.length; i++) {
          this.listGenFileParameter[i].from = list[i];
        }
      } else if (type === 'genFileParameterTo') {
        for (let i = index; i < list.length; i++) {
          this.listGenFileParameter[i].to = list[i];
        }
      }
    }
  }

  openDialogSearchGenFileParameter(index, type): void {
    const dialogRef = this.dialog.open(DialogSearchParameterGenerateFileComponent, {});
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const objValue = result.value;
        if (type === 'genFileParameterFrom') {
          this.listGenFileParameter[index].from = objValue.generateFileName;
          this.listGenFilePaymentDate[index].from = new Date(objValue.generateFileDate);
        } else if (type === 'genFileParameterTo') {
          this.listGenFileParameter[index].to = objValue.generateFileName;
          this.listGenFilePaymentDate[index].to = new Date(objValue.generateFileDate);
        }
        console.log(this.listGenFilePaymentDate);
      }
    });
  }

  clickSave() {
    this.dialogRef.close({
      event: true,
      status: 'save',
      value: this.listGenFileParameter,
      listGenFilePaymentDate: this.listGenFilePaymentDate,
    });
  }
}
