import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SidebarService } from '@core/services';
import { MatDialog } from '@angular/material/dialog';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
// tslint:disable-next-line:max-line-length
// tslint:disable-next-line:max-line-length
import { DialogReturnParameterGenFileComponent } from '@shared/component/dialog-return-parameter-gen-file/dialog-return-parameter-gen-file.component';
import { ReturnService } from '@core/services/return/return.service';
import { DialogReturnLogComponent } from '@shared/component/dialog-return-log/dialog-return-log.component';
import { DialogSearchMasterDataComponent } from '@shared/component/dialog-search-master-data/dialog-search-master-data.component';
import { PaymentBlockService } from '@core/services/om/payment-block.service';
import { environment } from '@env/environment.prod';
import { DialogSearchParameterReturnFileComponent } from '@shared/component/dialog-search-parameter-return-file/dialog-search-parameter-return-file.component';
import { DialogOmVendorComponent } from '@shared/component/dialog-om-vendor/dialog-om-vendor.component';

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.scss'],
})
export class ReturnComponent implements OnInit {
  returnFileForm: FormGroup;
  // STEP: 1
  stepControl: FormControl;
  directoryParameterControl: FormControl;
  pathControl: FormControl;
  // STEP: 2
  paymentDateStep2FromControl: FormControl; // วันที่ประมวลผล
  paymentDateStep2ToControl: FormControl;
  generateFileNameStep2FromControl: FormControl; // การกำหนด
  generateFileNameStep2ToControl: FormControl;
  transferDateFromControl: FormControl; // วันที่สั่งโอน
  transferDateToControl: FormControl;
  vendorFromControl: FormControl; // ผู้ขาย
  vendorToControl: FormControl;
  fileTypeSmartControl: FormControl; // ประเภทไฟล์ SMART
  fileTypeSwiftControl: FormControl; // ประเภทไฟล์ SWIFT
  fileTypeGiroControl: FormControl; // ประเภทไฟล์ GIRO
  fileTypeInHouseControl: FormControl; // ประเภทไฟล์ INHOUSE
  fileStatusCompleteControl: FormControl; // สถานะไฟล์ สมบูรณ์
  fileStatusReturnControl: FormControl; // สถานะไฟล์ คืนกลับ
  fileStatusNotSetControl: FormControl; // สถานะไฟล์ ยังไม่ได้กำหนด
  // STEP: 3
  paymentDateStep3FromControl: FormControl; // วันที่ประมวลผล
  paymentDateStep3ToControl: FormControl;
  generateFileNameStep3FromControl: FormControl; // การกำหนด
  generateFileNameStep3ToControl: FormControl;
  // STEP: 4
  paymentDateStep4FromControl: FormControl; // วันที่ประมวลผล
  paymentDateStep4ToControl: FormControl;
  generateFileNameStep4FromControl: FormControl; // การกำหนด
  generateFileNameStep4ToControl: FormControl;
  isSubmitedForm = false;
  isReadonly = false;

  listGenFileStep02Parameter = [];
  listPaymentDateStep02 = [];
  listTransferDate = [];
  listVendorStep02 = [];

  listGenFileStep03Parameter = [];
  listPaymentDateStep03 = [];

  listGenFileStep04Parameter = [];
  listPaymentDateStep04 = [];

  step = '01';
  currentPageType = 'CRITERIA';
  nameTableReturnUpdateStatus = 'table-return-update-status';
  nameTableReturnReversePayment = 'table-return-reverse-payment';
  nameTableReturnReverseInvoice = 'table-return-reverse-invoice';
  searchCriteria: any = {};
  listValidate = [];
  isDisableStep1 = false;
  isDisableStep2 = true;
  isDisableStep3 = true;
  isDisableStep4 = true;

  constructor(
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private generateFileService: GenerateFileService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService,
    private returnService: ReturnService,
    private paymentBlockService: PaymentBlockService
  ) {}

  ngOnInit() {
    this.sidebarService.updatePageType('generate');
    this.sidebarService.updateNowPage('return');
    this.createHomeFormControl();
    this.createHomeFormGroup();
    this.default();
    this.setValidateStep1();
  }

  createHomeFormControl() {
    this.stepControl = this.formBuilder.control('01');
    this.directoryParameterControl = this.formBuilder.control(environment.sftp);
    this.pathControl = this.formBuilder.control(environment.path);

    this.paymentDateStep2FromControl = this.formBuilder.control('');
    this.paymentDateStep2ToControl = this.formBuilder.control('');
    this.generateFileNameStep2FromControl = this.formBuilder.control('');
    this.generateFileNameStep2ToControl = this.formBuilder.control('');
    this.transferDateFromControl = this.formBuilder.control('');
    this.transferDateToControl = this.formBuilder.control('');
    this.vendorFromControl = this.formBuilder.control('');
    this.vendorToControl = this.formBuilder.control('');
    this.fileTypeSmartControl = this.formBuilder.control(true);
    this.fileTypeSwiftControl = this.formBuilder.control(true);
    this.fileTypeGiroControl = this.formBuilder.control(true);
    this.fileTypeInHouseControl = this.formBuilder.control(true);
    this.fileStatusCompleteControl = this.formBuilder.control(true);
    this.fileStatusReturnControl = this.formBuilder.control(true);
    this.fileStatusNotSetControl = this.formBuilder.control(true);

    this.paymentDateStep3FromControl = this.formBuilder.control('');
    this.paymentDateStep3ToControl = this.formBuilder.control('');
    this.generateFileNameStep3FromControl = this.formBuilder.control('');
    this.generateFileNameStep3ToControl = this.formBuilder.control('');

    this.paymentDateStep4FromControl = this.formBuilder.control('');
    this.paymentDateStep4ToControl = this.formBuilder.control('');
    this.generateFileNameStep4FromControl = this.formBuilder.control('');
    this.generateFileNameStep4ToControl = this.formBuilder.control('');
  }

  createHomeFormGroup() {
    this.returnFileForm = this.formBuilder.group({
      // step 1
      step: this.stepControl,
      directoryParameter: this.directoryParameterControl,
      path: this.pathControl,
      // step 2
      paymentDateStep2From: this.paymentDateStep2FromControl,
      paymentDateStep2To: this.paymentDateStep2ToControl,
      generateFileNameStep2From: this.generateFileNameStep2FromControl,
      generateFileNameStep2To: this.generateFileNameStep2ToControl,
      transferDateFrom: this.transferDateFromControl,
      transferDateTo: this.transferDateToControl,
      vendorFrom: this.vendorFromControl,
      vendorTo: this.vendorToControl,
      fileTypeSmart: this.fileTypeSmartControl,
      fileTypeSwift: this.fileTypeSwiftControl,
      fileTypeGiro: this.fileTypeGiroControl,
      fileTypeInHouse: this.fileTypeInHouseControl,
      fileStatusComplete: this.fileStatusCompleteControl,
      fileStatusReturn: this.fileStatusReturnControl,
      fileStatusNotSet: this.fileStatusNotSetControl,
      // step 3
      paymentDateStep3From: this.paymentDateStep3FromControl,
      paymentDateStep3To: this.paymentDateStep3ToControl,
      generateFileNameStep3From: this.generateFileNameStep3FromControl,
      generateFileNameStep3To: this.generateFileNameStep3ToControl,
      // step 4
      paymentDateStep4From: this.paymentDateStep4FromControl,
      paymentDateStep4To: this.paymentDateStep4ToControl,
      generateFileNameStep4From: this.generateFileNameStep4FromControl,
      generateFileNameStep4To: this.generateFileNameStep4ToControl,
    });
  }

  defaultInput() {
    // step 2
    this.returnFileForm.patchValue({
      paymentDateStep2From: '',
      paymentDateStep2To: '',
      generateFileNameStep2From: '',
      generateFileNameStep2To: '',
      transferDateFrom: '',
      transferDateTo: '',
      vendorFrom: '',
      vendorTo: '',
      fileTypeSmart: true,
      fileTypeSwift: true,
      fileTypeGiro: true,
      fileTypeInHouse: true,
      fileStatusComplete: true,
      fileStatusReturn: true,
      fileStatusNotSet: true,
      // step 3
      paymentDateStep3From: '',
      paymentDateStep3To: '',
      generateFileNameStep3From: '',
      generateFileNameStep3To: '',
      // step 4
      paymentDateStep4From: '',
      paymentDateStep4To: '',
      generateFileNameStep4From: '',
      generateFileNameStep4To: '',
    });
  }

  default() {
    this.isSubmitedForm = false;
    this.returnFileForm.patchValue({
      step: this.step,
    });
  }

  defaultBack() {
    this.isSubmitedForm = false;
    this.returnFileForm.patchValue({
      step: this.step,
    });
    if (this.step === '01') {
      this.setValidateStep1();
    } else if (this.step === '02') {
      this.setValidateStep2();
    } else if (this.step === '03') {
      this.setValidateStep3();
    } else if (this.step === '04') {
      this.setValidateStep4();
    }
  }

  clearAllValidate() {
    Object.keys(this.returnFileForm.controls).forEach((key) => {
      this.returnFileForm.get(key).clearValidators();
      this.returnFileForm.get(key).updateValueAndValidity();
    });
  }

  setValidateStep1() {
    this.clearAllValidate();
    this.returnFileForm.controls.directoryParameter.setValidators([Validators.required]);
    this.returnFileForm.controls.path.setValidators([Validators.required]);
    this.returnFileForm.controls.directoryParameter.updateValueAndValidity();
    this.returnFileForm.controls.path.updateValueAndValidity();
    this.returnFileForm.disable();
    this.returnFileForm.controls.step.enable();
    this.returnFileForm.controls.directoryParameter.enable();
    this.returnFileForm.controls.path.enable();
    // this.defaultInput();
  }

  setValidateStep2() {
    this.clearAllValidate();
    // this.returnFileForm.controls.generateFileNameStep2From.setValidators([Validators.required]);
    // this.returnFileForm.controls.generateFileNameStep2From.updateValueAndValidity();
    this.returnFileForm.disable();
    this.returnFileForm.controls.step.enable();
    this.returnFileForm.controls.generateFileNameStep2From.enable();
    this.returnFileForm.controls.generateFileNameStep2To.enable();
    this.returnFileForm.controls.paymentDateStep2From.enable();
    this.returnFileForm.controls.paymentDateStep2To.enable();
    this.returnFileForm.controls.generateFileNameStep2To.enable();
    this.returnFileForm.controls.transferDateFrom.enable();
    this.returnFileForm.controls.transferDateTo.enable();
    this.returnFileForm.controls.vendorFrom.enable();
    this.returnFileForm.controls.vendorTo.enable();
    this.returnFileForm.controls.fileTypeSmart.enable();
    this.returnFileForm.controls.fileTypeSwift.enable();
    this.returnFileForm.controls.fileTypeGiro.enable();
    this.returnFileForm.controls.fileTypeInHouse.enable();
    this.returnFileForm.controls.fileStatusComplete.enable();
    this.returnFileForm.controls.fileStatusReturn.enable();
    this.returnFileForm.controls.fileStatusNotSet.enable();
    // this.defaultInput();
  }

  setValidateStep3() {
    this.clearAllValidate();
    // this.returnFileForm.controls.generateFileNameStep3From.setValidators([Validators.required]);
    // this.returnFileForm.controls.generateFileNameStep3From.updateValueAndValidity();
    this.returnFileForm.disable();
    this.returnFileForm.controls.step.enable();
    this.returnFileForm.controls.generateFileNameStep3From.enable();
    this.returnFileForm.controls.generateFileNameStep3To.enable();
    this.returnFileForm.controls.paymentDateStep3From.enable();
    this.returnFileForm.controls.paymentDateStep3To.enable();
    // this.defaultInput();
  }

  setValidateStep4() {
    this.clearAllValidate();
    // this.returnFileForm.controls.generateFileNameStep4From.setValidators([Validators.required]);
    // this.returnFileForm.controls.generateFileNameStep4From.updateValueAndValidity();
    this.returnFileForm.disable();
    this.returnFileForm.controls.step.enable();
    this.returnFileForm.controls.generateFileNameStep4From.enable();
    this.returnFileForm.controls.generateFileNameStep4To.enable();
    this.returnFileForm.controls.paymentDateStep4From.enable();
    this.returnFileForm.controls.paymentDateStep4To.enable();
    // this.defaultInput();
  }

  validateForm() {
    Object.keys(this.returnFileForm.controls).forEach((key) => {
      const controlErrors = this.returnFileForm.get(key).errors;
      if (controlErrors) {
        if (key === 'directoryParameter') {
          this.listValidate.push('กรุณาระบุ Directory parameter');
        }
        if (key === 'path') {
          this.listValidate.push('กรุณาระบุ Path');
        }
        if (key === 'generateFileNameStep2From') {
          this.listValidate.push('กรุณาระบุ การกำหนด');
        }
        if (key === 'generateFileNameStep3From') {
          this.listValidate.push('กรุณาระบุ การกำหนด');
        }
        if (key === 'generateFileNameStep4From') {
          this.listValidate.push('กรุณาระบุ การกำหนด');
        }
      }
    });
  }

  openDialogParameterGenerateFile(type, step): void {
    const dialogRef = this.dialog.open(DialogSearchParameterReturnFileComponent, {
      data: {
        title: 'รายการการชำระเงินอัตโนมัติ : ภาพรวม',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        const objValue = result.value;
        if (type === 'from') {
          if (step === '01') {
          } else if (step === '02') {
            this.returnFileForm.patchValue({
              generateFileNameStep2From: objValue.generateFileName,
              paymentDateStep2From: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep02Parameter.length === 0) {
              this.listGenFileStep02Parameter.push({ from: objValue.generateFileName, to: null, optionExclude: false });
            }
            // this.listGenFileStep02Parameter[0].from = objValue.generateFileName;
            if (this.listPaymentDateStep02.length === 0) {
              this.listPaymentDateStep02.push({
                from: this.utils.parseDate(
                  new Date(objValue.generateFileDate).getDate(),
                  new Date(objValue.generateFileDate).getMonth() + 1,
                  new Date(objValue.generateFileDate).getFullYear()
                ),
                to: null,
                optionExclude: false,
              });
            }
            // this.listPaymentDateStep02[0].from = this.utils.parseDate(
            //   new Date(objValue.generateFileDate).getDate(),
            //   new Date(objValue.generateFileDate).getMonth() + 1,
            //   new Date(objValue.generateFileDate).getFullYear()
            // );
          } else if (step === '03') {
            this.returnFileForm.patchValue({
              generateFileNameStep3From: objValue.generateFileName,
              paymentDateStep3From: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep03Parameter.length === 0) {
              this.listGenFileStep03Parameter.push({ from: objValue.generateFileName, to: null, optionExclude: false });
            }
            // this.listGenFileStep03Parameter[0].from = objValue.generateFileName;
            if (this.listPaymentDateStep03.length === 0) {
              this.listPaymentDateStep03.push({
                from: this.utils.parseDate(
                  new Date(objValue.generateFileDate).getDate(),
                  new Date(objValue.generateFileDate).getMonth() + 1,
                  new Date(objValue.generateFileDate).getFullYear()
                ),
                to: null,
                optionExclude: false,
              });
            }
            // this.listPaymentDateStep03[0].from = this.utils.parseDate(
            //   new Date(objValue.generateFileDate).getDate(),
            //   new Date(objValue.generateFileDate).getMonth() + 1,
            //   new Date(objValue.generateFileDate).getFullYear()
            // );
          } else if (step === '04') {
            this.returnFileForm.patchValue({
              generateFileNameStep4From: objValue.generateFileName,
              paymentDateStep4From: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep04Parameter.length === 0) {
              this.listGenFileStep04Parameter.push({ from: objValue.generateFileName, to: null, optionExclude: false });
            }
            // this.listGenFileStep04Parameter[0].from = objValue.generateFileName;
            if (this.listPaymentDateStep04.length === 0) {
              this.listPaymentDateStep04.push({
                from: this.utils.parseDate(
                  new Date(objValue.generateFileDate).getDate(),
                  new Date(objValue.generateFileDate).getMonth() + 1,
                  new Date(objValue.generateFileDate).getFullYear()
                ),
                to: null,
                optionExclude: false,
              });
            }
            // this.listPaymentDateStep04[0].from = new Date(objValue.generateFileDate);
          } else {
          }
        } else if (type === 'to') {
          if (step === '01') {
          } else if (step === '02') {
            this.returnFileForm.patchValue({
              generateFileNameStep2To: objValue.generateFileName,
              paymentDateStep2To: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep02Parameter.length === 0) {
              this.listGenFileStep02Parameter.push({ from: null, to: objValue.generateFileName, optionExclude: false });
            } else {
              this.listGenFileStep02Parameter[0].to = objValue.generateFileName;
            }
            // this.listGenFileStep02Parameter[0].to = objValue.generateFileName;
            if (this.listPaymentDateStep02.length === 0) {
              this.listPaymentDateStep02.push({
                from: null,
                to: this.utils.parseDate(
                  new Date(objValue.generateFileDate).getDate(),
                  new Date(objValue.generateFileDate).getMonth() + 1,
                  new Date(objValue.generateFileDate).getFullYear()
                ),
                optionExclude: false,
              });
            } else {
              this.listPaymentDateStep02[0].to = this.utils.parseDate(
                new Date(objValue.generateFileDate).getDate(),
                new Date(objValue.generateFileDate).getMonth() + 1,
                new Date(objValue.generateFileDate).getFullYear()
              );
            }
            // this.listPaymentDateStep02[0].to = this.utils.parseDate(
            //   new Date(objValue.generateFileDate).getDate(),
            //   new Date(objValue.generateFileDate).getMonth() + 1,
            //   new Date(objValue.generateFileDate).getFullYear()
            // );
          } else if (step === '03') {
            this.returnFileForm.patchValue({
              generateFileNameStep3To: objValue.generateFileName,
              paymentDateStep3To: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep03Parameter.length === 0) {
              this.listGenFileStep03Parameter.push({ from: null, to: objValue.generateFileName, optionExclude: false });
            } else {
              this.listGenFileStep03Parameter[0].to = objValue.generateFileName;
            }
            // this.listGenFileStep03Parameter[0].to = objValue.generateFileName;
            if (this.listPaymentDateStep03.length === 0) {
              this.listPaymentDateStep03.push({
                from: null,
                to: this.utils.parseDate(
                  new Date(objValue.generateFileDate).getDate(),
                  new Date(objValue.generateFileDate).getMonth() + 1,
                  new Date(objValue.generateFileDate).getFullYear()
                ),
                optionExclude: false,
              });
            } else {
              this.listPaymentDateStep03[0].to = this.utils.parseDate(
                new Date(objValue.generateFileDate).getDate(),
                new Date(objValue.generateFileDate).getMonth() + 1,
                new Date(objValue.generateFileDate).getFullYear()
              );
            }
            // this.listPaymentDateStep03[0].to = this.utils.parseDate(
            //   new Date(objValue.generateFileDate).getDate(),
            //   new Date(objValue.generateFileDate).getMonth() + 1,
            //   new Date(objValue.generateFileDate).getFullYear()
            // );
          } else if (step === '04') {
            this.returnFileForm.patchValue({
              generateFileNameStep4To: objValue.generateFileName,
              paymentDateStep4To: new Date(objValue.generateFileDate), // วันที่ประมวลผล
            });
            if (this.listGenFileStep04Parameter.length === 0) {
              this.listGenFileStep04Parameter.push({ from: null, to: objValue.generateFileName, optionExclude: false });
            } else {
              this.listGenFileStep04Parameter[0].to = objValue.generateFileName;
            }
            // this.listGenFileStep04Parameter[0].to = objValue.generateFileName;
            if (this.listPaymentDateStep04.length === 0) {
              this.listPaymentDateStep04.push({
                from: null,
                to: new Date(objValue.generateFileDate),
                optionExclude: false,
              });
            } else {
              this.listPaymentDateStep04[0].to = new Date(objValue.generateFileDate);
            }
            // this.listPaymentDateStep04[0].to = new Date(objValue.generateFileDate);
          } else {
          }
        }
      }
    });
  }

  openDialogParameterGenerateFileRange() {
    // if (this.listGenFileStep02Parameter.length === 0 && this.returnFileForm.controls.generateFileNameStep2From.value) {
    //   this.listGenFileStep02Parameter.push({ from: this.returnFileForm.controls.generateFileNameStep2From.value, to: this.returnFileForm.controls.generateFileNameStep2To.value, optionExclude: false });
    //   this.listGenFileStep02Parameter.push({ from: null, to: null, optionExclude: false });
    //   this.listGenFileStep02Parameter.push({ from: null, to: null, optionExclude: false });
    //   if (this.returnFileForm.controls.paymentDateStep2From.value) {
    //     this.listPaymentDateStep02.push({ from: this.returnFileForm.controls.paymentDateStep2From.value, to: this.returnFileForm.controls.paymentDateStep2To.value, optionExclude: false });
    //     this.listPaymentDateStep02.push({ from: null, to: null, optionExclude: false });
    //     this.listPaymentDateStep02.push({ from: null, to: null, optionExclude: false });
    //   }
    // }
    const dialogRef = this.dialog.open(DialogReturnParameterGenFileComponent, {
      data: {
        listGenFileParameter: this.listGenFileStep02Parameter,
        listGenFilePaymentDate: this.listPaymentDateStep02,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          if (result.value && result.value.length > 0) {
            result.value.forEach((e, i) => {
              this.listGenFileStep02Parameter.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          if (result.listGenFilePaymentDate && result.listGenFilePaymentDate.length > 0) {
            result.listGenFilePaymentDate.forEach((e) => {
              this.listPaymentDateStep02.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          this.returnFileForm.patchValue({
            generateFileNameStep2From: result.value[0].from,
            generateFileNameStep2To: result.value[0].to,
            paymentDateStep2From: result.listGenFilePaymentDate[0].from,
            paymentDateStep2To: result.listGenFilePaymentDate[0].to,
          });
        }
      }
    });
  }

  openDialogParameterGenerateFileRangeStep3() {
    // if (this.listGenFileStep03Parameter.length === 0 && this.returnFileForm.controls.generateFileNameStep3From.value) {
    //   this.listGenFileStep03Parameter.push({ from: this.returnFileForm.controls.generateFileNameStep3From.value, to: this.returnFileForm.controls.generateFileNameStep3To.value, optionExclude: false });
    //   this.listGenFileStep03Parameter.push({ from: null, to: null, optionExclude: false });
    //   this.listGenFileStep03Parameter.push({ from: null, to: null, optionExclude: false });
    //   if (this.returnFileForm.controls.paymentDateStep3From.value) {
    //     this.listPaymentDateStep03.push({
    //       from: this.returnFileForm.controls.paymentDateStep3From.value,
    //       to: this.returnFileForm.controls.paymentDateStep3To.value,
    //       optionExclude: false
    //     });
    //     this.listPaymentDateStep03.push({from: null, to: null, optionExclude: false});
    //     this.listPaymentDateStep03.push({from: null, to: null, optionExclude: false});
    //   }
    // }
    const dialogRef = this.dialog.open(DialogReturnParameterGenFileComponent, {
      data: {
        listGenFileParameter: this.listGenFileStep03Parameter,
        listGenFilePaymentDate: this.listPaymentDateStep03,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          if (result.value && result.value.length > 0) {
            result.value.forEach((e, i) => {
              this.listGenFileStep03Parameter.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          if (result.listGenFilePaymentDate && result.listGenFilePaymentDate.length > 0) {
            result.listGenFilePaymentDate.forEach((e) => {
              this.listPaymentDateStep03.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          this.returnFileForm.patchValue({
            generateFileNameStep3From: result.value[0].from,
            generateFileNameStep3To: result.value[0].to,
            paymentDateStep3From: result.listGenFilePaymentDate[0].from,
            paymentDateStep3To: result.listGenFilePaymentDate[0].to,
          });
        }
      }
    });
  }

  openDialogParameterGenerateFileRangeStep4() {
    // if (this.listGenFileStep04Parameter.length === 0 && this.returnFileForm.controls.generateFileNameStep4From.value) {
    //   this.listGenFileStep04Parameter.push({ from: this.returnFileForm.controls.generateFileNameStep4From.value, to: this.returnFileForm.controls.generateFileNameStep4To.value, optionExclude: false });
    //   this.listGenFileStep04Parameter.push({ from: null, to: null, optionExclude: false });
    //   this.listGenFileStep04Parameter.push({ from: null, to: null, optionExclude: false });
    //   if (this.returnFileForm.controls.paymentDateStep4From.value) {
    //     this.listPaymentDateStep04.push({
    //       from: this.returnFileForm.controls.paymentDateStep4From.value,
    //       to: this.returnFileForm.controls.paymentDateStep4To.value,
    //       optionExclude: false
    //     });
    //     this.listPaymentDateStep04.push({from: null, to: null, optionExclude: false});
    //     this.listPaymentDateStep04.push({from: null, to: null, optionExclude: false});
    //   }
    // }
    const dialogRef = this.dialog.open(DialogReturnParameterGenFileComponent, {
      data: {
        listGenFileParameter: this.listGenFileStep04Parameter,
        listGenFilePaymentDate: this.listPaymentDateStep04,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          if (result.value && result.value.length > 0) {
            result.value.forEach((e, i) => {
              this.listGenFileStep04Parameter.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          if (result.listGenFilePaymentDate && result.listGenFilePaymentDate.length > 0) {
            result.listGenFilePaymentDate.forEach((e) => {
              this.listPaymentDateStep04.push({
                from: e.from,
                to: e.to,
                optionExclude: e.optionExclude,
              });
            });
          }
          this.returnFileForm.patchValue({
            generateFileNameStep4From: result.value[0].from,
            generateFileNameStep4To: result.value[0].to,
            paymentDateStep4From: result.listGenFilePaymentDate[0].from,
            paymentDateStep4To: result.listGenFilePaymentDate[0].to,
          });
        }
      }
    });
  }

  changeStep(step) {
    this.isSubmitedForm = false;
    this.step = step;
    this.returnFileForm.patchValue({
      step,
    });
    if (step === '01') {
      this.setValidateStep1();
    } else if (step === '02') {
      this.setValidateStep2();
    } else if (step === '03') {
      this.setValidateStep3();
    } else if (step === '04') {
      this.setValidateStep4();
    }
  }

  onSearch() {
    this.isSubmitedForm = true;
    const form = this.returnFileForm.getRawValue();
    this.listValidate = [];
    this.validateForm();
    if (this.listValidate.length <= 0) {
      this.isSubmitedForm = false;
      const payload: any = {};
      if (this.step === '01') {
        const objRequest = {
          directory: form.directoryParameter,
          path: form.path,
        };
        this.returnService.getAutoUpdateFileReturn(objRequest).then((result) => {
          if (result && result.status === 200) {
            const items = result.data;
            const { details, errors } = items;
            if (details.length > 0) {
              let logType = 'STATUS_STEP1';
              // if (details[0].id === null && details[0].fileStatus === 'SMART') {
              //   logType = 'NOT_FOUND_COMPLETE';
              // }
              const dialogRef = this.dialog.open(DialogReturnLogComponent, {
                width: '95vw',
                data: {
                  listLog: items,
                  logType,
                },
              });
              dialogRef.afterClosed().subscribe((dialogResult) => {});
            } else {
              const dialogRef = this.dialog.open(DialogReturnLogComponent, {
                width: '95vw',
                data: {
                  textAlert: 'ไม่พบข้อมูลไฟล์ส่งกลับมา',
                  logType: 'ALERT_TEXT',
                },
              });
              dialogRef.afterClosed().subscribe((dialogResult) => {});
            }
          }
        });
      } else if (this.step === '02') {
        payload.fileStatusComplete = form.fileStatusComplete;
        payload.fileStatusNotSet = form.fileStatusNotSet;
        payload.fileStatusReturn = form.fileStatusReturn;
        payload.fileTypeGiro = form.fileTypeGiro;
        payload.fileTypeInHouse = form.fileTypeInHouse;
        payload.fileTypeSmart = form.fileTypeSmart;
        payload.fileTypeSwift = form.fileTypeSwift;
        if (this.listGenFileStep02Parameter.length === 0) {
          this.listGenFileStep02Parameter.push({ from: form.generateFileNameStep2From, to: form.generateFileNameStep2To, optionExclude: false });
        }
        // this.listGenFileStep02Parameter[0].from = form.generateFileNameStep2From;
        // this.listGenFileStep02Parameter[0].to = form.generateFileNameStep2To;
        this.listGenFileStep02Parameter = this.listGenFileStep02Parameter.filter(
          (e) => e.from !== null
        );
        payload.paymentName = this.listGenFileStep02Parameter;
        if (this.listPaymentDateStep02.length === 0) {
          this.listPaymentDateStep02.push({ from: !!form.paymentDateStep2From
              ? this.utils.parseDate(
                form.paymentDateStep2From.getDate(),
                form.paymentDateStep2From.getMonth() + 1,
                form.paymentDateStep2From.getFullYear()
              )
              : null, to: !!form.paymentDateStep2To
              ? this.utils.parseDate(
                form.paymentDateStep2To.getDate(),
                form.paymentDateStep2To.getMonth() + 1,
                form.paymentDateStep2To.getFullYear()
              )
              : null, optionExclude: false });
        }
        payload.paymentDate = this.listPaymentDateStep02;
        if (this.listVendorStep02.length === 0) {
          this.listVendorStep02.push({ from: form.vendorFrom, to: form.vendorTo, optionExclude: false});
        }
        // this.listVendorStep02[0].from = form.vendorFrom;
        // this.listVendorStep02[0].to = form.vendorTo;
        this.listVendorStep02 = this.listVendorStep02.filter((e) => e.from !== null);
        payload.vendor = this.listVendorStep02;
        if (form.transferDateFrom) {
          const transferFromDate = form.transferDateFrom.getDate();
          const transferFromMonth = form.transferDateFrom.getMonth() + 1;
          const transferFromYear = form.transferDateFrom.getFullYear();
          const fromTransfer = this.utils.parseDate(
            transferFromDate,
            transferFromMonth,
            transferFromYear
          );
          if (this.listTransferDate.length === 0) {
            this.listTransferDate.push({ from: fromTransfer, to: null, optionExclude: false });
          }
          // this.listTransferDate[0].from = fromTransfer;
        }
        if (form.transferDateTo) {
          const transferToDate = form.transferDateTo.getDate();
          const transferToMonth = form.transferDateTo.getMonth() + 1;
          const transferToYear = form.transferDateTo.getFullYear();
          const toTransfer = this.utils.parseDate(transferToDate, transferToMonth, transferToYear);
          if (this.listTransferDate.length === 0) {
            this.listTransferDate.push({ from: null, to: toTransfer, optionExclude: false });
          } else {
            this.listTransferDate[0].to = toTransfer;
          }
        }
        payload.transferDate = this.listTransferDate;
        this.currentPageType = this.nameTableReturnUpdateStatus;
      } else if (this.step === '03') {
        if (this.listGenFileStep03Parameter.length === 0) {
          this.listGenFileStep03Parameter.push({ from: form.generateFileNameStep3From, to: form.generateFileNameStep3To, optionExclude: false });
        }
        // this.listGenFileStep03Parameter[0].from = form.generateFileNameStep3From;
        // this.listGenFileStep03Parameter[0].to = form.generateFileNameStep3To;
        this.listGenFileStep03Parameter = this.listGenFileStep03Parameter.filter(
          (e) => e.from !== null
        );
        payload.paymentName = this.listGenFileStep03Parameter;
        if (this.listPaymentDateStep03.length === 0) {
          this.listPaymentDateStep03.push({ from: !!form.paymentDateStep3From
              ? this.utils.parseDate(
                form.paymentDateStep3From.getDate(),
                form.paymentDateStep3From.getMonth() + 1,
                form.paymentDateStep3From.getFullYear()
              )
              : null, to: !!form.paymentDateStep3To
              ? this.utils.parseDate(
                form.paymentDateStep3To.getDate(),
                form.paymentDateStep3To.getMonth() + 1,
                form.paymentDateStep3To.getFullYear()
              )
              : null, optionExclude: false });
        }
        payload.paymentDate = this.listPaymentDateStep03;
        this.currentPageType = this.nameTableReturnReversePayment;
      } else if (this.step === '04') {
        console.log(this.listPaymentDateStep04);
        if (this.listGenFileStep04Parameter.length === 0) {
          this.listGenFileStep04Parameter.push({ from: form.generateFileNameStep4From, to: form.generateFileNameStep4To, optionExclude: false });
        }
        // this.listGenFileStep04Parameter[0].from = form.generateFileNameStep4From;
        // this.listGenFileStep04Parameter[0].to = form.generateFileNameStep4To;
        this.listGenFileStep04Parameter = this.listGenFileStep04Parameter.filter(
          (e) => e.from !== null
        );
        payload.paymentName = this.listGenFileStep04Parameter;
        if (this.listPaymentDateStep04.length === 0) {
          this.listPaymentDateStep04.push({ from: !!form.paymentDateStep4From
              ? this.utils.parseDate(
                form.paymentDateStep4From.getDate(),
                form.paymentDateStep4From.getMonth() + 1,
                form.paymentDateStep4From.getFullYear()
              )
              : null, to: !!form.paymentDateStep4To
              ? this.utils.parseDate(
                form.paymentDateStep4To.getDate(),
                form.paymentDateStep4To.getMonth() + 1,
                form.paymentDateStep4To.getFullYear()
              )
              : null, optionExclude: false });
        }
        // this.listPaymentDateStep04[0].from = !!form.paymentDateStep4From
        //   ? this.utils.parseDate(
        //       form.paymentDateStep4From.getDate(),
        //       form.paymentDateStep4From.getMonth() + 1,
        //       form.paymentDateStep4From.getFullYear()
        //     )
        //   : null;
        // this.listPaymentDateStep04[0].to = !!form.paymentDateStep4To
        //   ? this.utils.parseDate(
        //       form.paymentDateStep4To.getDate(),
        //       form.paymentDateStep4To.getMonth() + 1,
        //       form.paymentDateStep4To.getFullYear()
        //     )
        //   : null;
        payload.paymentDate = this.listPaymentDateStep04;
        this.currentPageType = this.nameTableReturnReverseInvoice;
      } else {
      }
      this.searchCriteria = payload;
    }
  }

  updateSuccess(payBackObj) {
    this.step = payBackObj.step;
    if (payBackObj.type === 'UPDATE_SUCCESS') {
      this.default();
      if (payBackObj.step === '01') {
        this.setValidateStep1();
      } else if (payBackObj.step === '02') {
        this.setValidateStep2();
      } else if (payBackObj.step === '03') {
        this.setValidateStep3();
      } else if (payBackObj.step === '04') {
        this.setValidateStep4();
      }
    } else {
      this.defaultBack();
    }
    this.back();
  }

  back() {
    this.currentPageType = 'CRITERIA';
  }

  openDialogSearchMaster(type, typeObject): void {
    const dialogRef = this.dialog.open(DialogSearchMasterDataComponent, {
      data: { type },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (typeObject === 'from') {
          this.returnFileForm.patchValue({
            vendorFrom: result.value,
          });
          this.listVendorStep02[0].from = result.value;
        } else if (typeObject === 'to') {
          this.returnFileForm.patchValue({
            vendorTo: result.value,
          });
          this.listVendorStep02[0].to = result.value;
        }
      }
    });
  }

  patchInputForm(formName, type) {
    const form = this.returnFileForm.getRawValue();
    let day, month, year;
    let value;
    if (form[formName]) {
      day = form[formName].getDate();
      month = form[formName].getMonth() + 1;
      year = form[formName].getFullYear();
      value = this.utils.parseDate(day, month, year);
    }
    if (formName === 'paymentDateStep2From' || formName === 'paymentDateStep2To') {
      this.listPaymentDateStep02[0][type] = value;
    } else if (formName === 'transferDateFrom' || formName === 'transferDateTo') {
      this.listTransferDate[0][type] = value;
    }
  }

  openDialogVendorRange() {
    // if (this.listVendorStep02.length === 0 && this.returnFileForm.controls.vendorFrom.value) {
    //   this.listVendorStep02.push({ from: this.returnFileForm.controls.vendorFrom.value, to: this.returnFileForm.controls.vendorTo.value, optionExclude: false });
    //   this.listVendorStep02.push({ from: null, to: null, optionExclude: false });
    //   this.listVendorStep02.push({ from: null, to: null, optionExclude: false });
    // }
    const dialogRef = this.dialog.open(DialogOmVendorComponent, {
      data: {
        listVendor: this.listVendorStep02,
      },
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.status === 'save') {
          this.listVendorStep02 = result.value;
          this.returnFileForm.patchValue({
            vendorFrom: this.listVendorStep02[0].from,
            vendorTo: this.listVendorStep02[0].to,
          });
        }
      }
    });
  }
}
