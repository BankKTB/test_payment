import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { Utils } from '@shared/utils/utils';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LoaderService, MasterService, SidebarService } from '@core/services';
import { MatDialog } from '@angular/material/dialog';
import { GenerateFileService } from '@core/services/generate-file/generate-file.service';
import { DialogSearchParameterGenerateFileComponent } from '@shared/component/dialog-search-parameter-generate-file/dialog-search-parameter-generate-file.component';
import { DialogResultComponent } from '@shared/component/dialog-result/dialog-result.component';
import { DialogAuthenticationConfirmComponent } from '@shared/component/dialog-authentication-confirm/dialog-authentication-confirm.component';
import { DialogGenerateFileResultComponent } from '@shared/component/dialog-generate-file-result/dialog-generate-file-result.component';

@Component({
  selector: 'app-generate-file',
  templateUrl: './generate-file-pac.component.html',
  styleUrls: ['./generate-file-pac.component.scss'],
})
export class GenerateFilePacComponent implements OnInit {
  generateFileForm: FormGroup;
  idControl: FormControl;
  paymentAliasIdControl: FormControl;
  generateFileDateControl: FormControl; // วันที่ประมวลผล
  generateFileNameControl: FormControl; // การกำหนด
  fileNameControl: FormControl;

  swiftAmountDayControl: FormControl;
  swiftDateControl: FormControl;
  smartAmountDayControl: FormControl;
  smartDateControl: FormControl;
  giroAmountDayControl: FormControl;
  giroDateControl: FormControl;
  inhouseAmountDayControl: FormControl;
  inhouseDateControl: FormControl;

  createAgainControl: FormControl;
  testRunControl: FormControl;

  isSubmitedForm = false;

  hiddenButtonGenerateFile = true;
  isReadonly = false;
  hiddenTestRun = false;

  generateFileId = '';
  nonBusinessDays = [];
  canCreateAgain = false;
  isOpenGenerateFilePopup = false;
  listValidateFileName = [];
  constructor(
    private dialog: MatDialog,
    private router: Router,
    private utils: Utils,
    private formBuilder: FormBuilder,
    private generateFileService: GenerateFileService,
    private masterService: MasterService,
    private snackBar: MatSnackBar,
    private sidebarService: SidebarService,
    private loaderService: LoaderService
  ) {}

  ngOnInit() {
    this.getNonBusinessDay();
    this.sidebarService.updatePageType('generate');
    this.sidebarService.updateNowPage('generate');
    this.createHomeFormControl();
    this.createHomeFormGroup();
    this.defaultHomeForm();
  }

  calculateBusinessDay(currentDate: Date): number {
    const date = new Date(currentDate);
    let day: number = 0;
    if (this.nonBusinessDays.includes(date.toDateString())) {
      day = this.isWeekend(date) === 0 ? 1 : this.isWeekend(date);
    }
    return day;
  }

  createHomeFormControl() {
    this.idControl = this.formBuilder.control('');
    this.paymentAliasIdControl = this.formBuilder.control('');
    this.generateFileDateControl = this.formBuilder.control(''); // วันที่ประมวลผล
    this.generateFileNameControl = this.formBuilder.control(''); // การกำหนด
    this.fileNameControl = this.formBuilder.control('');

    this.swiftAmountDayControl = this.formBuilder.control('');
    this.swiftDateControl = this.formBuilder.control('');
    this.smartAmountDayControl = this.formBuilder.control('');
    this.smartDateControl = this.formBuilder.control('');
    this.giroAmountDayControl = this.formBuilder.control('');
    this.giroDateControl = this.formBuilder.control('');
    this.inhouseAmountDayControl = this.formBuilder.control('');
    this.inhouseDateControl = this.formBuilder.control('');

    this.createAgainControl = this.formBuilder.control('');
    this.testRunControl = this.formBuilder.control('');
  }

  createHomeFormGroup() {
    this.generateFileForm = this.formBuilder.group({
      id: this.idControl,
      paymentAliasId: this.paymentAliasIdControl,
      generateFileDate: this.generateFileDateControl, // วันที่ประมวลผล
      generateFileName: this.generateFileNameControl, // การกำหนด
      fileName: this.fileNameControl,

      swiftAmountDay: this.swiftAmountDayControl,
      swiftDate: this.swiftDateControl,
      smartAmountDay: this.smartAmountDayControl,
      smartDate: this.smartDateControl,
      giroAmountDay: this.giroAmountDayControl,
      giroDate: this.giroDateControl,
      inhouseAmountDay: this.inhouseAmountDayControl,
      inhouseDate: this.inhouseDateControl,
      createAgain: this.createAgainControl,
      testRun: this.testRunControl,
    });
  }

  defaultHomeForm() {
    this.hiddenButtonGenerateFile = true;
    this.generateFileForm.patchValue({
      id: '',
      paymentAliasId: '',
      generateFileDate: new Date(), // วันที่ประมวลผล
      generateFileName: '', // การกำหนด
      fileName: '',

      swiftAmountDay: '1',
      swiftDate: new Date(),
      smartAmountDay: '2',
      smartDate: new Date(),
      giroAmountDay: '1',
      giroDate: new Date(),
      inhouseAmountDay: '1',
      inhouseDate: new Date(),
      createAgain: false,
      testRun: false,
    });

    this.calculateSwift(new Date(), 1, 0, false);
    this.calculateSmart(new Date(), 2, 0, false);
    this.calculateGiro(new Date(), 1, 0, false);
    this.calculateinhouse(new Date(), 1, 0, false);
  }

  getNonBusinessDay() {
    this.getWeekendDate();
    this.masterService.findAllNonBusinessDay().then((result) => {
      if (result.status === 200) {
        const items = result.data;
        items.forEach((item) => {
          const date = new Date(item.date1);
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

  onGenerateFileDate() {
    const formValue = this.generateFileForm.value;
    if (formValue.generateFileDate) {
      const swiftDateForm = JSON.parse(JSON.stringify(formValue.generateFileDate));
      const swiftDate = new Date(swiftDateForm);
      this.calculateSwift(new Date(swiftDateForm), 1, 0, false);
      const smartDateForm = JSON.parse(JSON.stringify(formValue.generateFileDate));
      const smartDate = new Date(smartDateForm);
      this.calculateSmart(new Date(smartDateForm), 2, 0, false);
      const giroDateForm = JSON.parse(JSON.stringify(formValue.generateFileDate));
      const giroDate = new Date(giroDateForm);
      this.calculateGiro(new Date(giroDateForm), 1, 0, false);
      const inhouseDateForm = JSON.parse(JSON.stringify(formValue.generateFileDate));
      const inhouseDate = new Date(inhouseDateForm);
      this.calculateinhouse(new Date(inhouseDateForm), 1, 0, false);
    }
    if (formValue.generateFileDate && formValue.generateFileName) {
      // this.isDisabledTab = false;
      this.searchGenerateFileDetail();
    } else {
      // this.isDisabledTab = true;
      // this.tabSelectedIndex = 0;
    }
  }

  onBlurGenerateFileName() {
    const formValue = this.generateFileForm.value;
    this.hiddenButtonGenerateFile = false;
    if (formValue.generateFileDate && formValue.generateFileName) {
      // this.isDisabledTab = false;
      console.log('true');
      this.searchGenerateFileDetail();
    } else {
      console.log('false');
      // this.isDisabledTab = true;
      // this.tabSelectedIndex = 0;
    }
  }

  searchGenerateFileDetail() {
    const value = this.generateFileForm.value;
    const date = new Date(value.generateFileDate);
    const dayGenerateFileDate = date.getDate();
    const monthGenerateFileDate = date.getMonth() + 1;
    const yearGenerateFileDate = date.getFullYear();
    const generateFileDate = this.utils.parseDate(
      dayGenerateFileDate,
      monthGenerateFileDate,
      yearGenerateFileDate
    );
    const generateFileName = value.generateFileName;
    this.generateFileService.search(generateFileDate, generateFileName).then((result) => {
      if (result.status === 200) {
        const data = result.data;
        console.log(data);
        if (data.generateFileRunStatus === 'W') {
          this.hiddenButtonGenerateFile = false;
          this.isReadonly = false;
          this.hiddenTestRun = false;
        } else {
          this.hiddenButtonGenerateFile = false;
          this.isReadonly = true;
          if (data.generateFileRunStatus && data.generateFileRunStatus === 'S') {
            this.hiddenTestRun = true;
          }
        }
        this.calNonBusinessDay(result.data);
        this.generateFileId = result.data.generateFileAliasId;
      } else if (result.status === 404) {
        const data = result.error;
        this.hiddenButtonGenerateFile = true;
      }
    });
  }

  isWeekend(d) {
    const day = (d || new Date()).getDay();
    if (day === 0) {
      return 1;
    } else if (day === 6) {
      return 2;
    } else {
      return 0;
    }
  }

  calNonBusinessDay(result) {
    // SWIFT DATE
    this.calculateSwift(
      result.generateFileDate,
      result.swiftAmountDay,
      result.generateFileAliasId,
      false
    );
    // SMART DATE
    this.calculateSmart(
      result.generateFileDate,
      result.smartAmountDay,
      result.generateFileAliasId,
      false
    );
    // GIRO DATE
    this.calculateGiro(
      result.generateFileDate,
      result.giroAmountDay,
      result.generateFileAliasId,
      false
    );
    // IN HOUSE DATE
    this.calculateinhouse(
      result.generateFileDate,
      result.inhouseAmountDay,
      result.generateFileAliasId,
      false
    );

    const genDate = new Date(result.generateFileDate);
    genDate.setDate(genDate.getDate());
    this.generateFileForm.patchValue({
      id: result.generateFileAliasId,
      paymentAliasId: result.paymentAliasId,
      generateFileDate: new Date(result.generateFileDate), // วันที่ประมวลผล
      generateFileName: result.generateFileName, // การกำหนด
      createAgain: result.createAgain,
      testRun: result.testRun,
    });
  }

  openDialogParameterGenerateFile(type): void {
    const dialogRef = this.dialog.open(DialogSearchParameterGenerateFileComponent, {
      data: {
        title: 'รายการการชำระเงินอัตโนมัติ : ภาพรวม',
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.event) {
        if (result.value.generateFileRunStatus === 'W') {
          this.hiddenButtonGenerateFile = false;
          this.isReadonly = false;
          this.hiddenTestRun = false;
        } else {
          this.hiddenButtonGenerateFile = false;
          this.isReadonly = true;
          if (result.value.generateFileRunStatus && result.value.generateFileRunStatus === 'S') {
            this.hiddenTestRun = true;
          }
        }
        this.calNonBusinessDay(result.value);
        this.generateFileId = result.value.generateFileId;
        this.listValidateFileName = [];
      } else {
        // const formValue = this.generateFileForm.value;
        // if (formValue.generateFileDate && formValue.generateFileName) {
        //   this.searchGenerateFileDetail();
        // } else {
        //   this.hiddenButtonGenerateFile = true;
        // }
      }
    });
  }

  clearInputAll() {
    this.defaultHomeForm();
  }

  generateFile() {
    const value = this.generateFileForm.value;
    if (value.createAgain) {
      this.listValidateFileName = [];
      if (!value.fileName) {
        this.listValidateFileName.push('กรุณาระบุชื่อไฟล์ที่ต้องการสร้างใหม่');
        return false;
      }
    }
    this.listValidateFileName = [];
    const payload = {} as any;
    const dayGenerateFileDate = value.generateFileDate.getDate();
    const monthGenerateFileDate = value.generateFileDate.getMonth() + 1;
    const yearGenerateFileDate = value.generateFileDate.getFullYear();
    payload.id = this.generateFileId;
    payload.paymentAliasId = value.paymentAliasId;
    payload.generateFileName = value.generateFileName;
    payload.fileName = value.fileName;
    payload.swiftAmountDay = !!value.swiftAmountDay ? value.swiftAmountDay : 0;
    payload.smartAmountDay = !!value.smartAmountDay ? value.smartAmountDay : 0;
    payload.giroAmountDay = !!value.giroAmountDay ? value.giroAmountDay : 0;
    payload.inhouseAmountDay = !!value.inhouseAmountDay ? value.inhouseAmountDay : 0;
    // payload.smartAmountDay = value.smartAmountDay;
    // payload.giroAmountDay = value.giroAmountDay;
    // payload.inhouseAmountDay = value.inhouseAmountDay;
    payload.createAgain = value.createAgain;
    payload.testRun = value.testRun;
    payload.generateFileDate = this.utils.parseDate(
      dayGenerateFileDate,
      monthGenerateFileDate,
      yearGenerateFileDate
    );

    const daySwiftDate = value.swiftDate.getDate();
    const monthSwiftDate = value.swiftDate.getMonth() + 1;
    const yearSwiftDate = value.swiftDate.getFullYear();
    payload.swiftDate = this.utils.parseDate(daySwiftDate, monthSwiftDate, yearSwiftDate);

    const daySmartDate = value.smartDate.getDate();
    const monthSmartDate = value.smartDate.getMonth() + 1;
    const yearSmartDate = value.smartDate.getFullYear();
    payload.smartDate = this.utils.parseDate(daySmartDate, monthSmartDate, yearSmartDate);

    const dayGiroDate = value.giroDate.getDate();
    const monthGiroDate = value.giroDate.getMonth() + 1;
    const yearGiroDate = value.giroDate.getFullYear();
    payload.giroDate = this.utils.parseDate(dayGiroDate, monthGiroDate, yearGiroDate);

    const dayinhouseDate = value.inhouseDate.getDate();
    const monthinhouseDate = value.inhouseDate.getMonth() + 1;
    const yearinhouseDate = value.inhouseDate.getFullYear();
    payload.inhouseDate = this.utils.parseDate(dayinhouseDate, monthinhouseDate, yearinhouseDate);
    // this.generateFileId = '';
    this.isOpenGenerateFilePopup = false;
    console.log('payload', payload);
    if (value.createAgain) {
      const dialogRef = this.dialog.open(DialogAuthenticationConfirmComponent, {
        width: '50vw',
        data: {},
      });
      dialogRef.afterClosed().subscribe((result) => {
        if (result.event) {
          this.genFile(payload);
        } else {
          return;
        }
      });
    } else {
      this.genFile(payload);
    }
  }

  genFile(payload) {
    const value = this.generateFileForm.value;
    let response = null;
    this.generateFileService
      .create(payload)
      .then((result) => {
        response = result;
        if (result.status === 201) {
          this.searchGenerateFileDetail();
          this.generateFileId = result.data.id;
          if (!value.testRun) {
            this.isReadonly = true;
          }
        } else if (result.status === 202) {
          this.snackBar.open('กำลังประมวลการสร้างไฟล์', '', {
            panelClass: '_warning',
          });
        } else if (result.status === 403) {
          this.snackBar.open('ข้อมูลนี้มีอยู่แล้วในระบบ', '', {
            panelClass: '_warning',
          });
        } else if (result.status === 422) {
          this.isReadonly = true;
          this.snackBar.open(result.error.message, '', {
            panelClass: '_warning',
          });
        }
      })
      .then(() => {
        if (response.status === 201) {
          this.generateFileService.generateFormatPac(this.generateFileId, payload).then((object) => {
            if (object.status === 200) {
              this.searchGenerateFileDetail();
              const url = './generate-result/' + this.generateFileId;
              const dialogRef = this.dialog.open(DialogGenerateFileResultComponent, {
                disableClose: true,
                maxWidth: '100vw',
                maxHeight: '100vh',
                height: '100%',
                width: '100%',
                panelClass: 'generate-file-result-dialog',
                data: {
                  generateFileId: this.generateFileId,
                },
              });

              // let popup = window.open(url, '_blank');
              // if (!!!popup) {
              //   const dialogRef = this.dialog.open(DialogResultComponent, {
              //     data: {
              //       textError: 'Please disable your pop-up block browser.',
              //       type: 'ERROR_DIALOG',
              //     },
              //   });
              //   dialogRef.afterClosed().subscribe((result) => {});
              // }
            }
          });
        }
        // this.searchGenerateFileDetail();
      });
  }

  openDialogResultDetail(referenceDoc) {
    const dialogRef = this.dialog.open(DialogResultComponent, {
      data: {
        type: 'TEXT_RESULT',
        referenceDoc,
      },
    });
    dialogRef.afterClosed().subscribe((result) => {
      if (result && result.value === 'CONFIRM') {
        const url = './generate-result/' + this.generateFileId;
        window.open(url, '_blank');
      } else {
        const url = './generate-result/' + this.generateFileId;
        window.open(url, '_blank');
      }
    });
  }

  deleteGenerateFile() {
    if (this.generateFileId) {
      this.generateFileService.delete(this.generateFileId).then((result) => {
        if (result.status === 200) {
          this.snackBar.open('ลบข้อมูลสำเร็จ   ', '', {
            panelClass: '_success',
          });
        } else if (result.status === 403) {
          this.snackBar.open('ลบข้อมูลไม่สำเร็จ   ', '', {
            panelClass: '_error',
          });
        }
      });
    }
  }

  onChangeDate(type, typeDate) {
    const amountDay = this.generateFileForm.controls[type].value;
    const generateFileDate = new Date(this.generateFileForm.controls.generateFileDate.value);
    generateFileDate.setDate(generateFileDate.getDate() + Number(amountDay));
    const holidays: number = this.calculateBusinessDay(generateFileDate);
    console.log('holidays : ', holidays);
    generateFileDate.setDate(generateFileDate.getDate() + holidays);
    if (type === 'swiftAmountDay') {
      this.generateFileForm.patchValue({
        swiftDate: generateFileDate,
        swiftAmountDay: Number(amountDay) + holidays,
      });
    } else if (type === 'smartAmountDay') {
      this.generateFileForm.patchValue({
        smartDate: generateFileDate,
        smartAmountDay: Number(amountDay) + holidays,
      });
    } else if (type === 'giroAmountDay') {
      this.generateFileForm.patchValue({
        giroDate: generateFileDate,
        giroAmountDay: Number(amountDay) + holidays,
      });
    } else if (type === 'inhouseAmountDay') {
      this.generateFileForm.patchValue({
        inhouseDate: generateFileDate,
        inhouseAmountDay: Number(amountDay) + holidays,
      });
    }
  }

  calculateSwift(generateFileDate, swiftAmountDay, generateFileAliasId, isChangeDate) {
    let swiftResultDate = new Date(generateFileDate);
    console.log('swiftResultDate : ', swiftResultDate);
    const swiftResultDateForCal = new Date(generateFileDate);
    let newAmountDaySwift = generateFileAliasId > 0 ? Number(swiftAmountDay) : 1;
    if (isChangeDate) {
      newAmountDaySwift = Number(swiftAmountDay);
    }
    let swiftDay = 0;
    let amountDay = 0;
    while (newAmountDaySwift > 0) {
      swiftResultDateForCal.setDate(swiftResultDateForCal.getDate() + 1);
      const isNonBusinessDay =
        this.nonBusinessDays &&
        this.nonBusinessDays.some((item) => {
          return item === swiftResultDateForCal.toDateString();
        });
      if (isNonBusinessDay) {
        swiftDay++;
        amountDay++;
      } else {
        swiftDay++;
        newAmountDaySwift--;
      }
    }
    if (swiftDay === 0 && generateFileAliasId > 0) {
      console.log('1');
      swiftResultDate.setDate(swiftResultDate.getDate() + swiftDay);
    } else {
      if (swiftAmountDay === 0) {
        swiftResultDate.setDate(swiftResultDate.getDate() + (1 + Number(amountDay)));
      } else {
        if (generateFileAliasId > 0) {
          console.log('3');
          swiftResultDate.setDate(swiftResultDate.getDate() + Number(swiftAmountDay));
        } else {
          console.log('2');
          swiftResultDate.setDate(swiftResultDate.getDate() + Number(swiftDay));
        }
      }
    }
    this.generateFileForm.patchValue({
      swiftDate: swiftResultDate,
      swiftAmountDay: this.diffDay(swiftResultDate, new Date(generateFileDate)),
    });
  }

  calculateSmart(generateFileDate, smartAmountDay, generateFileAliasId, isChangeDate) {
    const smartResultDate = new Date(generateFileDate);
    const smartResultDateForCal = new Date(generateFileDate);
    let newAmountDaySmart = generateFileAliasId > 0 ? Number(smartAmountDay) : 2;
    if (isChangeDate) {
      newAmountDaySmart = Number(smartAmountDay);
    }
    let smartDay = 0;
    let amountDay = 0;
    while (newAmountDaySmart > 0) {
      smartResultDateForCal.setDate(smartResultDateForCal.getDate() + 1);
      const isNonBusinessDay =
        this.nonBusinessDays &&
        this.nonBusinessDays.some((item) => item === smartResultDateForCal.toDateString());
      if (isNonBusinessDay) {
        smartDay++;
        amountDay++;
      } else {
        smartDay++;
        newAmountDaySmart--;
      }
    }
    if (smartDay === 0 && generateFileAliasId > 0) {
      smartResultDate.setDate(smartResultDate.getDate() + smartDay);
    } else {
      if (smartAmountDay === 0) {
        smartResultDate.setDate(smartResultDate.getDate() + (2 + Number(amountDay)));
      } else {
        if (generateFileAliasId > 0) {
          smartResultDate.setDate(smartResultDate.getDate() + Number(smartAmountDay));
        } else {
          smartResultDate.setDate(smartResultDate.getDate() + Number(smartDay));
        }
      }
    }
    this.generateFileForm.patchValue({
      smartDate: smartResultDate,
      smartAmountDay: this.diffDay(smartResultDate, new Date(generateFileDate)),
    });
  }

  calculateGiro(generateFileDate, giroAmountDay, generateFileAliasId, isChangeDate) {
    const giroResultDate = new Date(generateFileDate);
    const giroResultDateForCal = new Date(generateFileDate);
    let newAmountDayGiro = generateFileAliasId > 0 ? Number(giroAmountDay) : 1;
    if (isChangeDate) {
      newAmountDayGiro = Number(giroAmountDay);
    }
    let giroDay = 0;
    let amountDay = 0;
    while (newAmountDayGiro > 0) {
      giroResultDateForCal.setDate(giroResultDateForCal.getDate() + 1);
      const isNonBusinessDay =
        this.nonBusinessDays &&
        this.nonBusinessDays.some((item) => item === giroResultDateForCal.toDateString());
      if (isNonBusinessDay) {
        giroDay++;
        amountDay++;
      } else {
        giroDay++;
        newAmountDayGiro--;
      }
    }
    if (giroDay === 0 && generateFileAliasId > 0) {
      giroResultDate.setDate(giroResultDate.getDate() + giroDay);
    } else {
      if (giroAmountDay === 0) {
        giroResultDate.setDate(giroResultDate.getDate() + (1 + Number(amountDay)));
      } else {
        if (generateFileAliasId > 0) {
          giroResultDate.setDate(giroResultDate.getDate() + Number(giroAmountDay));
        } else {
          giroResultDate.setDate(giroResultDate.getDate() + Number(giroDay));
        }
      }
    }
    this.generateFileForm.patchValue({
      giroDate: giroResultDate,
      giroAmountDay: this.diffDay(giroResultDate, new Date(generateFileDate)),
    });
  }

  calculateinhouse(generateFileDate, inhouseAmountDay, generateFileAliasId, isChangeDate) {
    const inhouseResultDate = new Date(generateFileDate);
    const inhouseResultDateForCal = new Date(generateFileDate);
    let newAmountDayinhouse = generateFileAliasId > 0 ? Number(inhouseAmountDay) : 1;
    if (isChangeDate) {
      newAmountDayinhouse = Number(inhouseAmountDay);
    }
    let inhouseDay = 0;
    let amountDay = 0;
    while (newAmountDayinhouse > 0) {
      inhouseResultDateForCal.setDate(inhouseResultDateForCal.getDate() + 1);
      const isNonBusinessDay =
        this.nonBusinessDays &&
        this.nonBusinessDays.some((item) => item === inhouseResultDateForCal.toDateString());
      if (isNonBusinessDay) {
        inhouseDay++;
        amountDay++;
      } else {
        inhouseDay++;
        newAmountDayinhouse--;
      }
    }
    if (inhouseDay === 0 && generateFileAliasId > 0) {
      inhouseResultDate.setDate(inhouseResultDate.getDate() + inhouseDay);
    } else {
      if (inhouseAmountDay === 0) {
        inhouseResultDate.setDate(inhouseResultDate.getDate() + (1 + Number(amountDay)));
      } else {
        if (generateFileAliasId > 0) {
          inhouseResultDate.setDate(inhouseResultDate.getDate() + Number(inhouseAmountDay));
        } else {
          inhouseResultDate.setDate(inhouseResultDate.getDate() + Number(inhouseDay));
        }
      }
    }
    this.generateFileForm.patchValue({
      inhouseDate: inhouseResultDate,
      inhouseAmountDay: this.diffDay(inhouseResultDate, new Date(generateFileDate)),
    });
  }

  diffDay(endDate: Date, startDate: Date): number {
    const diffInMs = endDate.getTime() - startDate.getTime();
    return diffInMs / (1000 * 60 * 60 * 24);
  }

  changeCreateFileAgain() {
    this.listValidateFileName = [];
  }
}
