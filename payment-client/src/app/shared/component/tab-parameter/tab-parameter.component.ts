import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnDestroy,
  OnInit,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { DialogSearchPaymentMethodComponent } from '@shared/component/dialog-param/dialog-search-payment-method/dialog-search-payment-method.component';
import { DialogCopyParameterComponent } from '@shared/component/dialog-copy-parameter/dialog-copy-parameter.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { Utils } from '@shared/utils/utils';
import { MasterService } from '@core/services';
import { DialogVendorForPaymentComponent } from '@shared/component/dialog-vendor-for-payment/dialog-vendor-for-payment.component';

@Component({
  selector: 'app-tab-parameter',
  templateUrl: './tab-parameter.component.html',
  styleUrls: ['./tab-parameter.component.scss'],
})
export class TabParameterComponent implements OnInit, OnDestroy, OnChanges {
  @ViewChildren('vendorTaxIdFrom') vendorTaxIdFrom: QueryList<ElementRef>;
  @ViewChildren('vendorTaxIdTo') vendorTaxIdTo: QueryList<ElementRef>;
  @ViewChildren('optionExclude') optionExclude: QueryList<ElementRef>;
  optionExcludeVendor = 'N';
  // Tab
  panelExpanded = true;
  panelExpanded1 = true;

  @Input() parameter;
  @Input() homeFormValue; //
  @Input() statusIden; // // receive array list search criteria
  @Output() messageFromParameter = new EventEmitter<any>();
  @Output() validateForm = new EventEmitter<any>();

  parameterForm: FormGroup;
  postDateControl: FormControl; // วันที่ผ่านรายการ
  saveDateControl: FormControl; // บันทึกเอกสาร
  paymentMethodControl: FormControl; // วิธีชำระเงิน
  paymentDateControl: FormControl; // วันชำระถัดไป
  companyControl: FormControl; // รหัสบริษัท

  listVendor = [
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
  ];

  checkVendorParameter = false;

  nonBusinessDays = [];

  editable = true;

  constructor(
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private utils: Utils,
    private masterService: MasterService
  ) {}

  ngOnInit() {
    this.getNonBusinessDay();
    this.createFormControl();
    this.createFormGroup();
    if (this.parameter) {
      this.setInputFromParameter();
    } else {
      this.defaultInput();
    }
  }

  ngOnChanges(changes) {
    if (this.homeFormValue.paymentDate) {
      if (
        changes.homeFormValue.previousValue.paymentDate !==
        changes.homeFormValue.currentValue.paymentDate
      ) {
        this.calculateDefaultDate('postDate', 1);
        this.calculateDefaultDate('paymentDate', 2);
      }

      if (this.statusIden) {
        this.editable = false;
        if (this.parameterForm) {
          this.parameterForm.disable();
        }
      } else {
        this.editable = true;
        if (this.parameterForm) {
          this.parameterForm.enable();
        }
      }
    }
  }

  ngOnDestroy() {
    console.log('ngOnDestroy');
    this.validateForm.emit(this.parameterForm.valid);
  }

  getParameterFromCopy(object) {
    this.listVendor = object.vendor;
    this.parameterForm.patchValue({
      postDate: object.postDate ? new Date(object.postDate) : '', // วันที่ผ่านรายการ
      saveDate: object.saveDate ? new Date(object.saveDate) : '', // บันทึกเอกสาร
      paymentMethod: object.paymentMethod, // วิธีชำระเงิน
      paymentDate: object.paymentDate ? new Date(object.paymentDate) : '', // วันชำระถัดไป
      company: this.utils.convertCompanyArrayToCompanyText(object.companyCondition), // รหัสบริษัท
    });

    this.onGetPostDate(object.postDate);
  }

  createFormControl() {
    this.postDateControl = this.formBuilder.control('', [Validators.required]); // วันที่ผ่านรายการ
    this.saveDateControl = this.formBuilder.control(''); // บันทึกเอกสาร
    this.paymentMethodControl = this.formBuilder.control('', [Validators.required]); // วิธีชำระเงิน
    this.paymentDateControl = this.formBuilder.control('', [Validators.required]); // วันชำระถัดไป
    this.companyControl = this.formBuilder.control('', [Validators.required]); // รหัสบริษัท
  }

  createFormGroup() {
    this.parameterForm = this.formBuilder.group({
      postDate: this.postDateControl, // วันที่ผ่านรายการ
      saveDate: this.saveDateControl, // บันทึกเอกสาร
      paymentMethod: this.paymentMethodControl, // วิธีชำระเงิน
      paymentDate: this.paymentDateControl, // วันชำระถัดไป
      company: this.companyControl, // รหัสบริษัท
    });
  }

  defaultInput() {
    this.listVendor = [
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false },
    ];
    this.parameterForm.patchValue({
      // postDate: '', // วันที่ผ่านรายการ
      saveDate: new Date(), // บันทึกเอกสาร
      paymentMethod: '', // วิธีชำระเงิน
      // paymentDate: '', // วันชำระถัดไป
      company: '', // รหัสบริษัท
    });
  }

  setInputFromParameter() {
    this.listVendor = this.parameter.vendor;
    this.parameterForm.patchValue({
      postDate: this.parameter.postDate ? new Date(this.parameter.postDate) : new Date(), // วันที่ผ่านรายการ
      saveDate: this.parameter.saveDate ? new Date(this.parameter.saveDate) : '', // บันทึกเอกสาร
      paymentMethod: this.parameter.paymentMethod, // วิธีชำระเงิน
      paymentDate: this.parameter.paymentDate ? new Date(this.parameter.paymentDate) : '', // วันชำระถัดไป
      company: this.utils.convertCompanyArrayToCompanyText(this.parameter.companyCondition), // รหัสบริษัท
    });
  }

  setVendor(index) {
    const vendorTaxIdFrom = this.vendorTaxIdFrom.toArray()[index].nativeElement.value;
    const vendorTaxIdTo = this.vendorTaxIdTo.toArray()[index].nativeElement.value;
    if (vendorTaxIdFrom) {
      this.listVendor[index].vendorTaxIdFrom = vendorTaxIdFrom;
    } else {
      this.listVendor[index].vendorTaxIdFrom = '';
    }
    if (vendorTaxIdTo) {
      this.listVendor[index].vendorTaxIdTo = vendorTaxIdTo;
    } else {
      this.listVendor[index].vendorTaxIdTo = '';
    }
    this.checkParameterVendor();
  }

  checkParameterVendor() {
    for (const item of this.listVendor) {
      if (item.vendorTaxIdFrom) {
        this.checkVendorParameter = false;
        break;
      } else {
        this.checkVendorParameter = true;
      }
    }
  }

  updateParameter(): void {
    this.parameterForm.value.vendor = this.listVendor;

    this.parameterForm.value.companyCondition = this.utils.convertCompanyTextToCompanyArray(
      this.parameterForm.value.company
    );

    const form = this.parameterForm.value;

    let postDate = '';
    let saveDate = '';
    let paymentDate = '';
    if (form.postDate) {
      const dayPostDate = new Date(form.postDate).getDate();
      const monthPostDate = new Date(form.postDate).getMonth() + 1;
      const yearPostDate = new Date(form.postDate).getFullYear();
      postDate = this.utils.parseDate(dayPostDate, monthPostDate, yearPostDate);
    }
    if (form.saveDate) {
      const daySaveDate = new Date(form.saveDate).getDate();
      const monthSaveDate = new Date(form.saveDate).getMonth() + 1;
      const yearSaveDate = new Date(form.saveDate).getFullYear();
      saveDate = this.utils.parseDate(daySaveDate, monthSaveDate, yearSaveDate);
    }
    if (form.paymentDate) {
      const dayPaymentDate = new Date(form.paymentDate).getDate();
      const monthPaymentDate = new Date(form.paymentDate).getMonth() + 1;
      const yearPaymentDate = new Date(form.paymentDate).getFullYear();
      paymentDate = this.utils.parseDate(dayPaymentDate, monthPaymentDate, yearPaymentDate);
    }
    this.parameterForm.value.postDate = postDate;
    this.parameterForm.value.saveDate = saveDate;
    this.parameterForm.value.paymentDate = paymentDate;
    this.parameter = this.parameterForm.value;

    this.messageFromParameter.emit(this.parameter);
  }

  openDialogSearchVendor(index, type): void {
    const dialog = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (type === 'vendorTaxIdFrom') {
          this.listVendor[index].vendorTaxIdFrom = result.value;
        } else if (type === 'vendorTaxIdTo') {
          this.listVendor[index].vendorTaxIdTo = result.value;
        }
      }
    });
  }

  openDialogSearchPaymentMethod(): void {
    const paymentMethod = this.parameterForm.controls.paymentMethod.value;
    const dialog = this.dialog.open(DialogSearchPaymentMethodComponent, {
      data: { paymentMethod },
    });
    dialog.afterClosed().subscribe((result) => {
      if (result && result.event) {
        this.parameterForm.patchValue({
          paymentMethod: result.value, // วิธีชำระเงิน
        });
      }
    });
  }

  openDialogCopyParameterComponent(type): void {
    const dialogRef = this.dialog.open(DialogCopyParameterComponent, {});
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const data = result.value;
        if (result.status === 'Save') {
          if (type === 'copy') {
            data.copy = true;
            this.messageFromParameter.emit(data);
          }
        }
      }
    });
  }

  addInputVendor() {
    this.listVendor.push({ vendorTaxIdFrom: null, vendorTaxIdTo: null, optionExclude: false });
  }

  deleteInputVendor(index) {
    this.listVendor.splice(index, 1);
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

  onChangePostDate(e) {
    const form = this.parameterForm.value;
    if (form.postDate > new Date(form.paymentDate)) {
      const payDate = new Date(e.value);
      payDate.setDate(payDate.getDate() + 1);

      this.parameterForm.patchValue({
        paymentDate: payDate,
      });
    }
  }

  onGetPostDate(value) {
    const form = this.parameterForm.value;
    if (new Date(form.postDate) > new Date(form.paymentDate)) {
      this.parameterForm.patchValue({
        paymentDate: new Date(value),
      });
    }
  }

  minFilter = (d: Date): boolean => {
    const from = new Date(this.parameterForm.get('postDate').value);
    if (d <= from) {
      return false;
    }
    return true;
  };

  copyFromExcel(event, index, type) {
    // console.log(event);

    const value = event.data;

    // console.log('value' + value);
    // console.log('index' + index);

    console.log(value);
    if (value && value.length > 10) {
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
      if (type === 'vendorTaxIdFrom') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].vendorTaxIdFrom = list[i];
        }
      } else if (type === 'vendorTaxIdTo') {
        for (let i = index; i < list.length; i++) {
          this.listVendor[i].vendorTaxIdTo = list[i];
        }
      }
    }
  }

  calculateDefaultDate(type, day) {
    const form = this.homeFormValue;

    const resultDate = new Date(form.paymentDate);
    const resultDateForCal = new Date(form.paymentDate);
    let newAmountDay = day;

    let giroDay = 0;
    let amountDay = 0;
    while (newAmountDay > 0) {
      resultDateForCal.setDate(resultDateForCal.getDate() + 1);
      const isNonBusinessDay =
        this.nonBusinessDays &&
        this.nonBusinessDays.some((item) => item === resultDateForCal.toDateString());
      if (isNonBusinessDay) {
        giroDay++;
        amountDay++;
      } else {
        giroDay++;
        newAmountDay--;
      }
    }
    resultDate.setDate(resultDate.getDate() + Number(giroDay));
    console.log('calculateDefaultDate');
    console.log(resultDate);
    this.parameterForm.patchValue({
      [type]: resultDate,
    });
  }

  getNonBusinessDay() {
    this.getWeekendDate();
    this.masterService.findAllNonBusinessDay().then((result) => {
      if (result.status === 200) {
        const items = result.data;
        items.forEach((item) => {
          const date = new Date(item.date);
          this.nonBusinessDays.push(date.toDateString());
        });
      }
    });
  }

  getWeekendDate() {
    const beginDate = new Date();
    const lastDate = new Date();
    beginDate.setFullYear(beginDate.getFullYear() - 1);
    lastDate.setFullYear(lastDate.getFullYear() + 1);
    for (const d = new Date(beginDate); d <= new Date(lastDate); d.setDate(d.getDate() + 1)) {
      if (d.getDay() === 6 || d.getDay() === 0) {
        const date = new Date(d);
        this.nonBusinessDays.push(d.toDateString());
      }
    }
  }

  openDialogOmVendor() {
    console.log(this.listVendor);
    const dialogRef = this.dialog.open(DialogVendorForPaymentComponent, {
      data: {
        listVendor: this.listVendor,
        editable: this.editable,
        isShowIndex: true,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listVendor = result.value;
          console.log(this.listVendor);

          this.parameterForm.patchValue({
            vendorTaxIdFrom: this.listVendor[0].vendorTaxIdFrom,
            vendorTaxIdTo: this.listVendor[0].vendorTaxIdTo,
          });
          this.checkOptionExclude();
        }
      }
    });
  }

  checkOptionExclude() {
    this.optionExcludeVendor = this.listVendor.find((data) => data.optionExclude) ? 'Y' : 'N';
  }
}
