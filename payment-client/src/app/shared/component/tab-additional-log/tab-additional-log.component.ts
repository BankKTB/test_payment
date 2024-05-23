import {
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnChanges,
  OnInit,
  Output,
  QueryList,
  ViewChildren,
} from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MatDialog } from '@angular/material';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { DialogVendorForPaymentComponent } from '@shared/component/dialog-vendor-for-payment/dialog-vendor-for-payment.component';

@Component({
  selector: 'app-tab-additional-log',
  templateUrl: './tab-additional-log.component.html',
  styleUrls: ['./tab-additional-log.component.scss'],
})
export class TabAdditionalLogComponent implements OnInit, OnChanges {
  @ViewChildren('vendorTaxIdFrom') vendorTaxIdFrom: QueryList<ElementRef>;
  @ViewChildren('vendorTaxIdTo') vendorTaxIdTo: QueryList<ElementRef>;
  // Tab
  panelExpanded = true;
  panelExpanded1 = true;

  @Input() additionLog: any;
  @Input() homeFormValue; //
  @Input() statusIden;
  @Output() messageFromAdditionLog = new EventEmitter<any>();

  additionLogForm: FormGroup;
  checkBoxDueDateControl: FormControl; // ตรวจสอบวันที่ครบกำหนด
  checkBoxPaymentMethodAllControl: FormControl; // เลือกวิธีชำระเงินในทุกกรณี
  checkBoxPaymentMethodUnSuccessControl: FormControl; // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
  checkBoxDisplayDetailControl: FormControl; // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
  listVendor = [
    { vendorTaxIdFrom: null, vendorTaxIdTo: null },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null },
    { vendorTaxIdFrom: null, vendorTaxIdTo: null },
  ];

  isDisabledCheckPaymentMethodUnSuccess = false; // for disabled checkbox
  isDisabledCheckBoxPaymentMethodAll = false; // for disabled checkbox

  checkVendorAddition = false;

  editable = true;

  constructor(private dialog: MatDialog, private formBuilder: FormBuilder) {}

  ngOnInit() {
    this.createAdditionLogFormControl();
    this.createAdditionLogFormGroup();
    if (this.additionLog) {
      this.setInputFromAddition();
    } else {
      this.defaultInputAdditionLogForm();
    }
  }

  ngOnChanges(changes) {
    if (this.homeFormValue.paymentDate) {
      if (
        changes.homeFormValue.previousValue.paymentDate !==
        changes.homeFormValue.currentValue.paymentDate
      ) {
      }

      if (this.statusIden) {
        this.editable = false;
      } else {
        this.editable = true;
      }
    }
  }

  getAdditionLogFromCopy(object) {
    this.listVendor = object.vendor;

    this.isDisabledCheckBoxPaymentMethodAll = false;
    this.isDisabledCheckPaymentMethodUnSuccess = false;

    if (object.checkBoxPaymentMethodUnSuccess) {
      this.isDisabledCheckBoxPaymentMethodAll = true;
      this.isDisabledCheckPaymentMethodUnSuccess = false;
    }
    if (object.checkBoxPaymentMethodAll) {
      this.isDisabledCheckBoxPaymentMethodAll = false;
      this.isDisabledCheckPaymentMethodUnSuccess = true;
    }

    this.additionLogForm.patchValue({
      checkBoxDueDate: object.checkBoxDueDate, // ตรวจสอบวันที่ครบกำหนด
      checkBoxPaymentMethodAll: object.checkBoxPaymentMethodAll, // เลือกวิธีชำระเงินในทุกกรณี
      checkBoxPaymentMethodUnSuccess: object.checkBoxPaymentMethodUnSuccess, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
      checkBoxDisplayDetail: object.checkBoxDisplayDetail, // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
    });
  }

  createAdditionLogFormControl() {
    this.checkBoxDueDateControl = this.formBuilder.control(''); // ตรวจสอบวันที่ครบกำหนด
    this.checkBoxPaymentMethodAllControl = this.formBuilder.control(''); // เลือกวิธีชำระเงินในทุกกรณี
    this.checkBoxPaymentMethodUnSuccessControl = this.formBuilder.control(''); // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
    this.checkBoxDisplayDetailControl = this.formBuilder.control(''); // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
  }

  createAdditionLogFormGroup() {
    this.additionLogForm = this.formBuilder.group({
      checkBoxDueDate: this.checkBoxDueDateControl, // ตรวจสอบวันที่ครบกำหนด
      checkBoxPaymentMethodAll: this.checkBoxPaymentMethodAllControl, // เลือกวิธีชำระเงินในทุกกรณี
      checkBoxPaymentMethodUnSuccess: this.checkBoxPaymentMethodUnSuccessControl, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
      checkBoxDisplayDetail: this.checkBoxDisplayDetailControl, // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
    });
  }

  defaultInputAdditionLogForm() {
    this.isDisabledCheckBoxPaymentMethodAll = false;
    this.isDisabledCheckPaymentMethodUnSuccess = false;
    this.listVendor = [
      { vendorTaxIdFrom: null, vendorTaxIdTo: null },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null },
      { vendorTaxIdFrom: null, vendorTaxIdTo: null },
    ];
    this.additionLogForm.patchValue({
      checkBoxDueDate: false, // ตรวจสอบวันที่ครบกำหนด
      checkBoxPaymentMethodAll: false, // เลือกวิธีชำระเงินในทุกกรณี
      checkBoxPaymentMethodUnSuccess: false, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
      checkBoxDisplayDetail: false, // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
    });
  }

  setInputFromAddition() {
    this.listVendor = this.additionLog.vendor;

    this.isDisabledCheckPaymentMethodUnSuccess = false;
    this.isDisabledCheckBoxPaymentMethodAll = false;

    if (this.additionLog.checkBoxPaymentMethodUnSuccess) {
      this.isDisabledCheckBoxPaymentMethodAll = true;
      this.isDisabledCheckPaymentMethodUnSuccess = false;
    }
    if (this.additionLog.checkBoxPaymentMethodAll) {
      this.isDisabledCheckBoxPaymentMethodAll = false;
      this.isDisabledCheckPaymentMethodUnSuccess = true;
    }
    this.additionLogForm.patchValue({
      checkBoxDueDate: this.additionLog.checkBoxDueDate, // ตรวจสอบวันที่ครบกำหนด
      checkBoxPaymentMethodAll: this.additionLog.checkBoxPaymentMethodAll, // เลือกวิธีชำระเงินในทุกกรณี
      checkBoxPaymentMethodUnSuccess: this.additionLog.checkBoxPaymentMethodUnSuccess, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
      checkBoxDisplayDetail: this.additionLog.checkBoxDisplayDetail, // แสดงรายการบรรทัดรายการเอกสารชำระเงิน
      vendorTaxIdOneFrom: this.additionLog.vendorTaxIdOneFrom, // vendor
      vendorTaxIdOneTo: this.additionLog.vendorTaxIdOneTo, // vendor
      vendorTaxIdTwoFrom: this.additionLog.vendorTaxIdTwoFrom, // vendor
      vendorTaxIdTwoTo: this.additionLog.vendorTaxIdTwoTo, // vendor
      vendorTaxIdThreeFrom: this.additionLog.vendorTaxIdThreeFrom, // vendor
      vendorTaxIdThreeTo: this.additionLog.venvendorTaxIdThreeTodorTax, // vendor
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

    this.checkAdditionVendor();
  }

  checkAdditionVendor() {
    for (const item of this.listVendor) {
      if (item.vendorTaxIdFrom) {
        this.checkVendorAddition = false;
        break;
      } else {
        this.checkVendorAddition = true;
      }
    }
  }

  checkValidationAdditionLog(event, type) {
    console.log('checkValidationAdditionLog');
    if (type === 'paymentMethodUnSuccess') {
      if (event.checked) {
        this.isDisabledCheckBoxPaymentMethodAll = true;
        this.additionLogForm.patchValue({
          checkBoxPaymentMethodUnSuccess: true, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
          checkBoxPaymentMethodAll: false, // เลือกวิธีชำระเงินในทุกกรณี
        });
      } else {
        this.isDisabledCheckBoxPaymentMethodAll = false;
        this.additionLogForm.patchValue({
          checkBoxPaymentMethodUnSuccess: false, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
          checkBoxPaymentMethodAll: false, // เลือกวิธีชำระเงินในทุกกรณี
        });
      }
    } else if (type === 'paymentMethodAll') {
      if (event.checked) {
        this.isDisabledCheckPaymentMethodUnSuccess = true;
        this.additionLogForm.patchValue({
          checkBoxPaymentMethodUnSuccess: false, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
          checkBoxPaymentMethodAll: true, // เลือกวิธีชำระเงินในทุกกรณี
        });
      } else {
        this.isDisabledCheckPaymentMethodUnSuccess = false;
        this.additionLogForm.patchValue({
          checkBoxPaymentMethodUnSuccess: false, // เลือกเฉพาะวิธีการชำระเงินไม่สำเร็จ
          checkBoxPaymentMethodAll: false, // เลือกวิธีชำระเงินในทุกกรณี
        });
      }
    }
  }

  updateParameter(): void {
    this.additionLogForm.value.vendor = this.listVendor;
    this.additionLog = this.additionLogForm.value;
    this.messageFromAdditionLog.emit(this.additionLog);
  }

  addInputVendor() {
    this.listVendor.push({ vendorTaxIdFrom: null, vendorTaxIdTo: null });
  }

  deleteInputVendor(index) {
    this.listVendor.splice(index, 1);
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
  openDialogOmVendor() {
    console.log(this.listVendor);
    const dialogRef = this.dialog.open(DialogVendorForPaymentComponent, {
      data: {
        listVendor: this.listVendor,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      console.log(result);
      if (result && result.event) {
        if (result.status === 'save') {
          this.listVendor = result.value;
          console.log(this.listVendor);

          this.additionLogForm.patchValue({
            vendorTaxIdFrom: this.listVendor[0].vendorTaxIdFrom,
            vendorTaxIdTo: this.listVendor[0].vendorTaxIdTo,
          });
          // this.checkOptionExclude();
        }
      }
    });
  }
}
