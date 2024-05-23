import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialog, MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { Utils } from '@shared/utils/utils';

export interface DialogData {
  role: any;
  createBy: any;
  value: any;
  type: any;
  dataCriteria: any;
}

@Component({
  selector: 'app-dialog-om-save-search-criteria',
  templateUrl: './dialog-om-save-search-criteria.component.html',
  styleUrls: ['./dialog-om-save-search-criteria.component.scss'],
})
export class DialogOmSaveSearchCriteriaComponent implements OnInit {
  searchCriteriaForm: FormGroup;

  nameControl: FormControl;
  userOnlyControl: FormControl;

  constructor(
    private dialogRef: MatDialogRef<DialogOmSaveSearchCriteriaComponent>,
    private dialog: MatDialog,
    private formBuilder: FormBuilder,
    private paymentBlockService: PaymentBlockService,
    private utils: Utils,
    private snackBar: MatSnackBar,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {
    dialogRef.backdropClick().subscribe(() => {
      this.dialogRef.close({ event: false });
    });
  }

  ngOnInit() {
    this.createFormControl();
    this.createFormGroup();
  }

  createFormControl() {
    console.log(this.data);
    if (this.data.type === 'edit') {
      this.nameControl = this.formBuilder.control(this.data.dataCriteria.name);
      this.userOnlyControl = this.formBuilder.control(this.data.dataCriteria.isUserOnly);
    } else {
      this.nameControl = this.formBuilder.control('');
      this.userOnlyControl = this.formBuilder.control(true);
    }
  }

  createFormGroup() {
    this.searchCriteriaForm = this.formBuilder.group({
      name: this.nameControl,
      userOnly: this.userOnlyControl,
    });
  }

  clickCancel() {
    this.dialogRef.close({
      event: true,
      status: 'Cancel',
      value: '',
    });
  }

  clickSave() {
    const value = this.searchCriteriaForm.value;
    if (!value.name) {
      this.snackBar.open('กรุณา กรอก ชื่อ', '', {
        panelClass: '_warning',
      });
      return;
    }
    let payload = {};
    if (this.data.role === 'GENERATE_JU') {
      payload = this.saveGenerateJu();
    } else {
      payload = this.saveOm();
    }
    if (this.data.type === 'save') {
      this.paymentBlockService.createCriteria(payload).then((result) => {
        if (result.status === 201) {
          const data = result.data;
          if (data) {
            this.snackBar.open('บันทึกข้อมูลสำเร็จ', '', {
              panelClass: '_success',
            });
            this.dialogRef.close({
              event: true,
              status: 'Success',
              value: data,
            });
          }
        } else if (result.status === 403) {
          this.snackBar.open('ชื่อตัวเลือกนี้มีอยู่ในระบบแล้ว', '', {
            panelClass: '_error',
          });
        }
      });
    } else if (this.data.type === 'edit') {
      payload['id'] = this.data.dataCriteria.id;
      payload['createDate'] = this.data.dataCriteria.createDate;
      this.paymentBlockService.updateCriteria(payload).then((result) => {
        if (result.status === 201) {
          const data = result.data;
          if (data) {
            this.snackBar.open('อัพเดทข้อมูลสำเร็จ', '', {
              panelClass: '_success',
            });
            this.dialogRef.close({
              event: true,
              status: 'Success',
              value: data,
            });
          }
        } else if (result.status === 403) {
          this.snackBar.open('ชื่อตัวเลือกนี้มีอยู่ในระบบแล้ว', '', {
            panelClass: '_error',
          });
        }
      });
    }
  }

  saveOm(): any {
    const value = this.searchCriteriaForm.value;
    const form = this.data.value as any;

    // วันผ่านรายการ
    let postDateFrom = '';
    let postDateTo = '';
    if (form.postDateFrom) {
      const dayPostDateFrom = form.postDateFrom.getDate();
      const monthPostDateFrom = form.postDateFrom.getMonth() + 1;
      const yearPostDateFrom = form.postDateFrom.getFullYear();
      postDateFrom = this.utils.parseDate(dayPostDateFrom, monthPostDateFrom, yearPostDateFrom);
    }
    if (form.postDateTo) {
      const dayPostDateTo = form.postDateTo.getDate();
      const monthPostDateTo = form.postDateTo.getMonth() + 1;
      const yearPostDateTo = form.postDateTo.getFullYear();
      postDateTo = this.utils.parseDate(dayPostDateTo, monthPostDateTo, yearPostDateTo);
    }
    // วันที่เอกสาร
    let documentDateFrom = '';
    let documentDateTo = '';
    if (form.documentDateFrom) {
      const dayDocumentDateFrom = form.documentDateFrom.getDate();
      const monthDocumentDateFrom = form.documentDateFrom.getMonth() + 1;
      const yearDocumentDateFrom = form.documentDateFrom.getFullYear();
      documentDateFrom = this.utils.parseDate(
        dayDocumentDateFrom,
        monthDocumentDateFrom,
        yearDocumentDateFrom
      );
    }

    if (form.documentDateTo) {
      const dayDocumentDateTo = form.documentDateTo.getDate();
      const monthDocumentDateTo = form.documentDateTo.getMonth() + 1;
      const yearDocumentDateTo = form.documentDateTo.getFullYear();

      documentDateTo = this.utils.parseDate(
        dayDocumentDateTo,
        monthDocumentDateTo,
        yearDocumentDateTo
      );
    }
    // วันที่บันทึก
    let documentCreateDateFrom = '';
    let documentCreateDateTo = '';
    if (form.documentCreateDateFrom) {
      const dayDocumentCreateDateFrom = form.documentCreateDateFrom.getDate();
      const monthDocumentCreateDateFrom = form.documentCreateDateFrom.getMonth() + 1;
      const yearDocumentCreateDateFrom = form.documentCreateDateFrom.getFullYear();
      documentCreateDateFrom = this.utils.parseDate(
        dayDocumentCreateDateFrom,
        monthDocumentCreateDateFrom,
        yearDocumentCreateDateFrom
      );
    }
    if (form.documentCreateDateTo) {
      const dayDocumentCreateDateTo = form.documentCreateDateTo.getDate();
      const monthDocumentCreateDateTo = form.documentCreateDateTo.getMonth() + 1;
      const yearDocumentCreateDateTo = form.documentCreateDateTo.getFullYear();
      documentCreateDateTo = this.utils.parseDate(
        dayDocumentCreateDateTo,
        monthDocumentCreateDateTo,
        yearDocumentCreateDateTo
      );
    }
    const jsonObject = {
      departmentCodeFrom: form.departmentCodeFrom,
      departmentCodeTo: form.departmentCodeTo,
      provinceCodeFrom: form.provinceCodeFrom,
      provinceCodeTo: form.provinceCodeTo,
      yearAccount: this.utils.convertYearToAD(form.yearAccount),
      postDateFrom,
      postDateTo,
      vendorTaxIdFrom: form.vendorTaxIdFrom,
      vendorTaxIdTo: form.vendorTaxIdTo,
      disbursementCodeFrom: form.disbursementCodeFrom,
      disbursementCodeTo: form.disbursementCodeTo,
      docTypeFrom: form.docTypeFrom,
      docTypeTo: form.docTypeTo,
      payMethodFrom: form.payMethodFrom,
      payMethodTo: form.payMethodTo,
      documentDateFrom,
      documentDateTo,
      documentCreateDateFrom,
      documentCreateDateTo,
      specialTypeFrom: form.specialTypeFrom,
      specialTypeTo: form.specialTypeTo,
      outline: form.outline,
      listCompanyCode: form.listCompanyCode,
      listDocType: form.listDocType,
      listPaymentMethod: form.listPaymentMethod,
      vendor: form.listVendor,
      paymentCenter: form.listDisbursementCode,
      specialType: form.listSpecialType,

      listDocumentCreateDate: form.listDocumentCreateDate,
      listPostDate: form.listPostDate,
      listDocumentDate: form.listDocumentDate,
    };
    return {
      name: value.name,
      userOnly: value.userOnly ? value.userOnly : false,
      createBy: this.data.createBy,
      role: this.data.role,
      jsonText: JSON.stringify(jsonObject),
      id: null,
      createDate: '',
    };
  }

  saveGenerateJu(): any {
    const criteriaForm = this.searchCriteriaForm.value;
    const formValue = this.data.value as any;
    return {
      name: criteriaForm.name,
      userOnly: criteriaForm.userOnly ? criteriaForm.userOnly : false,
      createBy: this.data.createBy,
      role: this.data.role,
      jsonText: JSON.stringify(formValue),
      id: null,
      createDate: '',
    };
  }
}
